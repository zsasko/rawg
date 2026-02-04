package com.zsasko.rawg.domain.repository

import app.cash.turbine.test
import com.zsasko.rawg.api.DawgApi
import com.zsasko.rawg.data.db.SelectedGenre
import com.zsasko.rawg.data.model.GameDetailsResponse
import com.zsasko.rawg.data.model.NetworkResponse
import com.zsasko.rawg.data.repository.GameRepositoryImpl
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class GameRepositoryImplTest {

    private val apiService: DawgApi = mockk()
    private lateinit var repository: GameRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = GameRepositoryImpl(apiService, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `getGameDetails returns Success when api successful`() = runTest {
        val gameId = 1
        val body = GameDetailsResponse.createMinimalMock(gameId)

        val response = Response.success(body)
        coEvery { apiService.getGameDetails(gameId) } returns response

        val result = repository.getGameDetails(gameId)

        assertTrue(result is NetworkResponse.Success)
        assertEquals(body, (result as NetworkResponse.Success).data)
    }

    @Test
    fun `getGameDetails returns Error when api not successful`() = runTest {
        val gameId = 1
        val response = Response.error<GameDetailsResponse>(
            400,
            "error".toResponseBody("text/plain".toMediaType())
        )

        coEvery { apiService.getGameDetails(gameId) } returns response

        val result = repository.getGameDetails(gameId)

        assertTrue(result is NetworkResponse.Error)
    }

    @Test
    fun `getGamesStream emits paging data when genres change`() = runTest {
        val genresFlow = MutableSharedFlow<List<SelectedGenre>>(replay = 1)

        val flow = repository.getGamesStream(
            selectedGenresFlow = genresFlow,
            pageSize = 20,
            enablePlaceHolders = false,
            prefetchDistance = 5,
            initialLoadSize = 20,
            maxCacheSize = 100
        )

        flow.test {
            genresFlow.emit(listOf(SelectedGenre(1)))
            advanceUntilIdle()

            val pagingData = awaitItem()
            assertNotNull(pagingData)

            cancelAndIgnoreRemainingEvents()
        }
    }
}

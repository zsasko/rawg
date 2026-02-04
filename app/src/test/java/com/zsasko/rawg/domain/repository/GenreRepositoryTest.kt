package com.zsasko.rawg.domain.repository

import app.cash.turbine.test
import com.zsasko.rawg.api.DawgApi
import com.zsasko.rawg.data.db.SelectedGenre
import com.zsasko.rawg.data.db.SelectedGenreDao
import com.zsasko.rawg.data.model.GenreResponse
import com.zsasko.rawg.data.model.NetworkResponse
import com.zsasko.rawg.data.repository.GenreRepositoryImpl
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class GenreRepositoryImplTest {

    private val selectedGenreDao: SelectedGenreDao = mockk()
    private val apiService: DawgApi = mockk()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var repository: GenreRepositoryImpl

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = GenreRepositoryImpl(
            selectedGenreDao = selectedGenreDao,
            apiService = apiService,
            dispatcher = testDispatcher
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `getGenres emits Success when api successful`() = runTest {
        val genreResponse = GenreResponse()
        val response = Response.success(genreResponse)

        coEvery { apiService.getGenres() } returns response

        repository.getGenres().test {
            val item = awaitItem()
            assertTrue(item is NetworkResponse.Success)
            assertEquals(
                genreResponse,
                (item as NetworkResponse.Success).data
            )
            awaitComplete()
        }
    }

    @Test
    fun `getGenres emits Error when api fails`() = runTest {
        val response = Response.error<GenreResponse>(
            400,
            "error".toResponseBody("text/plain".toMediaType())
        )

        coEvery { apiService.getGenres() } returns response

        repository.getGenres().test {
            val item = awaitItem()
            assertTrue(item is NetworkResponse.Error)
            awaitComplete()
        }
    }

    @Test
    fun `getSelectedGenresFlow emits dao flow`() = runTest {
        val genres = listOf(SelectedGenre(1), SelectedGenre(2))
        val flow = flowOf(genres)

        every { selectedGenreDao.getAllFlow() } returns flow

        repository.getSelectedGenresFlow().test {
            val item = awaitItem()
            assertEquals(genres, item)
            awaitComplete()
        }
    }

    @Test
    fun `getSelectedGenres returns dao list`() = runTest {
        val genres = listOf(SelectedGenre(1))

        coEvery { selectedGenreDao.getAll() } returns genres

        val result = repository.getSelectedGenres()

        assertEquals(genres, result)
    }

    @Test
    fun `toggleGenreSelection calls dao toggle`() = runTest {
        val genreId = 5
        coEvery { selectedGenreDao.toggle(any()) } just Runs

        repository.toggleGenreSelection(genreId)
        advanceUntilIdle()

        coVerify {
            selectedGenreDao.toggle(
                match { it.genreId == genreId }
            )
        }
    }

    @Test
    fun `hasSelectedGenres returns true when count bigger than 0`() = runTest {
        coEvery { selectedGenreDao.getCount() } returns 3

        val result = repository.hasSelectedGenres()

        assertTrue(result)
    }

    @Test
    fun `hasSelectedGenres returns false when count is 0`() = runTest {
        coEvery { selectedGenreDao.getCount() } returns 0

        val result = repository.hasSelectedGenres()

        assertFalse(result)
    }
}

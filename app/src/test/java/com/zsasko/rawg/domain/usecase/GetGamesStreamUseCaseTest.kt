package com.zsasko.rawg.domain.usecase

import androidx.paging.PagingData
import com.zsasko.rawg.data.db.SelectedGenre
import com.zsasko.rawg.data.model.GameResponseItem
import com.zsasko.rawg.domain.repository.GameRepository
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetGamesStreamUseCaseTest {

    private lateinit var gameRepository: GameRepository
    private lateinit var getGamesStreamUseCase: GetGamesStreamUseCase

    @Before
    fun setup() {
        gameRepository = mockk()
        getGamesStreamUseCase = GetGamesStreamUseCase(gameRepository)
    }

    @Test
    fun `getGames emits expected PagingData`() = runTest {
        // Arrange
        val selectedGenresFlow = flowOf(listOf(SelectedGenre(1)))
        val pagingData = PagingData.from(
            listOf(
                GameResponseItem.createMockGame(1, "Game 1"),
                GameResponseItem.createMockGame(2, "Game 2")
            )
        )
        every {
            gameRepository.getGamesStream(
                selectedGenresFlow,
                pageSize = 20,
                enablePlaceHolders = false,
                prefetchDistance = 5,
                initialLoadSize = 20,
                maxCacheSize = 2000
            )
        } returns flowOf(pagingData)

        val resultFlow = getGamesStreamUseCase.getGames(selectedGenresFlow)

        val results = resultFlow.toList()

        assertEquals(1, results.size)
    }
}




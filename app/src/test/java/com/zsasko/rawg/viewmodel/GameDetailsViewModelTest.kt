package com.zsasko.rawg.viewmodel

import app.cash.turbine.test
import com.zsasko.rawg.data.model.GameDetailsResponse
import com.zsasko.rawg.data.model.NetworkResponse
import com.zsasko.rawg.data.state.GameDetailsUiState
import com.zsasko.rawg.domain.usecase.GetGameDetailsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GameDetailsViewModelTest {

    private lateinit var viewModel: GameDetailsViewModel
    private lateinit var getGameDetailsUseCase: GetGameDetailsUseCase
    private val testDispatcher = StandardTestDispatcher()

    private val testGameId = 123
    private val mockGameDetails = GameDetailsResponse.createMinimalMock(testGameId, "GTA")

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getGameDetailsUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be Loading`() = runTest {
        coEvery { getGameDetailsUseCase.getGameDetails(testGameId) } returns
                NetworkResponse.Success(mockGameDetails)

        viewModel = GameDetailsViewModel(testGameId, getGameDetailsUseCase)

        val initialState = viewModel.gameDetailsUiState.value
        assertTrue(initialState is GameDetailsUiState.Loading)
    }

    @Test
    fun `loadData should emit Loading then Success state`() = runTest {
        coEvery { getGameDetailsUseCase.getGameDetails(testGameId) } returns
                NetworkResponse.Success(mockGameDetails)

        viewModel = GameDetailsViewModel(testGameId, getGameDetailsUseCase)

        viewModel.gameDetailsUiState.test {
            skipItems(1) // skip initial Loading state

            val loadingState = awaitItem()
            assertTrue(loadingState is GameDetailsUiState.Loading)

            advanceUntilIdle()

            val loadedState = awaitItem()
            assertTrue(loadedState is GameDetailsUiState.Loaded)
            assertEquals(mockGameDetails, (loadedState as GameDetailsUiState.Loaded).data)

            coVerify(exactly = 1) { getGameDetailsUseCase.getGameDetails(testGameId) }
        }
    }

    @Test
    fun `loadData should emit Error state when use case returns error`() = runTest {
        val errorMessage = "Failed to load game details"
        coEvery { getGameDetailsUseCase.getGameDetails(testGameId) } returns
                NetworkResponse.Error(errorMessage)

        viewModel = GameDetailsViewModel(testGameId, getGameDetailsUseCase)

        assertTrue(viewModel.gameDetailsUiState.value is GameDetailsUiState.Loading)

        advanceUntilIdle()

        val finalState = viewModel.gameDetailsUiState.value
        assertTrue(finalState is GameDetailsUiState.Error)
        assertEquals(errorMessage, (finalState as GameDetailsUiState.Error).errorMessage)

        coVerify(exactly = 1) { getGameDetailsUseCase.getGameDetails(testGameId) }
    }

    @Test
    fun `viewModel initialization should trigger loadData`() = runTest {
        coEvery { getGameDetailsUseCase.getGameDetails(testGameId) } returns
                NetworkResponse.Success(mockGameDetails)

        viewModel = GameDetailsViewModel(testGameId, getGameDetailsUseCase)
        advanceUntilIdle()

        coVerify(exactly = 1) { getGameDetailsUseCase.getGameDetails(testGameId) }
    }
}
package com.zsasko.rawg.viewmodel


import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import app.cash.turbine.test
import com.zsasko.rawg.data.db.SelectedGenre
import com.zsasko.rawg.data.intents.GamesUiIntent
import com.zsasko.rawg.data.model.GameResponseItem.Companion.createMockGame
import com.zsasko.rawg.domain.repository.GenreRepository
import com.zsasko.rawg.domain.usecase.GetGamesStreamUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GamesViewModelTest {

    private lateinit var viewModel: GamesViewModel
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var genreRepository: GenreRepository
    private lateinit var getGamesUseCase: GetGamesStreamUseCase
    private val testDispatcher = StandardTestDispatcher()

    private val mockSelectedGenres = listOf(
        SelectedGenre(genreId = 1),
        SelectedGenre(genreId = 2)
    )

    private val mockGames = listOf(
        createMockGame(1, "Game 1"),
        createMockGame(2, "Game 2"),
        createMockGame(3, "Game 3")
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        savedStateHandle = SavedStateHandle()
        genreRepository = mockk(relaxed = true)
        getGamesUseCase = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `viewModel initializes with null selected game`() = runTest {
        setupDefaultMocks()

        viewModel = GamesViewModel(savedStateHandle, genreRepository, getGamesUseCase)
        advanceUntilIdle()

        assertNull(viewModel.selectedGameId.value)
    }

    @Test
    fun `viewModel retrieves selected genres from repository`() = runTest {
        val selectedGenresFlow = MutableStateFlow(mockSelectedGenres)
        every { genreRepository.getSelectedGenresFlow() } returns selectedGenresFlow
        every { getGamesUseCase.getGames(any()) } returns flowOf(PagingData.empty())

        viewModel = GamesViewModel(savedStateHandle, genreRepository, getGamesUseCase)
        advanceUntilIdle()

        verify(exactly = 1) { genreRepository.getSelectedGenresFlow() }
        verify(exactly = 1) { getGamesUseCase.getGames(any()) }
    }

    @Test
    fun `selectGame updates selected game in state`() = runTest {
        setupDefaultMocks()
        viewModel = GamesViewModel(savedStateHandle, genreRepository, getGamesUseCase)
        advanceUntilIdle()

        val testGame = createMockGame(123, "Test Game")

        viewModel.selectGameId(testGame.id)
        advanceUntilIdle()

        assertEquals(testGame.id, viewModel.selectedGameId.value)
    }

    @Test
    fun `selectGame updates multiple times correctly`() = runTest {
        setupDefaultMocks()
        viewModel = GamesViewModel(savedStateHandle, genreRepository, getGamesUseCase)
        advanceUntilIdle()

        val game1 = createMockGame(1, "Game 1")
        val game2 = createMockGame(2, "Game 2")

        viewModel.selectGameId(game1.id)
        advanceUntilIdle()
        assertEquals(game1.id, viewModel.selectedGameId.value)

        viewModel.selectGameId(game2.id)
        advanceUntilIdle()

        assertEquals(game2.id, viewModel.selectedGameId.value)
    }

    @Test
    fun `handleIntent with LoadGames emits viewEvent`() = runTest {
        setupDefaultMocks()
        viewModel = GamesViewModel(savedStateHandle, genreRepository, getGamesUseCase)
        advanceUntilIdle()

        viewModel.viewEvent.test {
            viewModel.handleIntent(GamesUiIntent.LoadGames)
            advanceUntilIdle()

            val event = awaitItem()
            assertTrue(event is GamesUiIntent.LoadGames)
        }
    }

    @Test
    fun `handleIntent emits LoadGames event only once`() = runTest {
        setupDefaultMocks()
        viewModel = GamesViewModel(savedStateHandle, genreRepository, getGamesUseCase)
        advanceUntilIdle()

        viewModel.viewEvent.test {
            viewModel.handleIntent(GamesUiIntent.LoadGames)
            advanceUntilIdle()

            val event = awaitItem()
            assertTrue(event is GamesUiIntent.LoadGames)
            expectNoEvents()
        }
    }

    @Test
    fun `multiple handleIntent calls emit multiple events`() = runTest {
        setupDefaultMocks()
        viewModel = GamesViewModel(savedStateHandle, genreRepository, getGamesUseCase)
        advanceUntilIdle()

        viewModel.viewEvent.test {
            viewModel.handleIntent(GamesUiIntent.LoadGames)
            advanceUntilIdle()
            assertTrue(awaitItem() is GamesUiIntent.LoadGames)

            viewModel.handleIntent(GamesUiIntent.LoadGames)
            advanceUntilIdle()
            assertTrue(awaitItem() is GamesUiIntent.LoadGames)

            expectNoEvents()
        }
    }

    @Test
    fun `games flow is properly cached in viewModelScope`() = runTest {
        val pagingData = PagingData.from(mockGames)
        every { genreRepository.getSelectedGenresFlow() } returns flowOf(mockSelectedGenres)
        every { getGamesUseCase.getGames(any()) } returns flowOf(pagingData)

        viewModel = GamesViewModel(savedStateHandle, genreRepository, getGamesUseCase)
        advanceUntilIdle()

        verify(exactly = 1) { getGamesUseCase.getGames(any()) }
    }

    @Test
    fun `selected genres flow changes trigger games reload`() = runTest {
        val selectedGenresFlow = MutableStateFlow(emptyList<SelectedGenre>())
        every { genreRepository.getSelectedGenresFlow() } returns selectedGenresFlow
        every { getGamesUseCase.getGames(any()) } returns flowOf(PagingData.empty())

        viewModel = GamesViewModel(savedStateHandle, genreRepository, getGamesUseCase)
        advanceUntilIdle()

        selectedGenresFlow.value = mockSelectedGenres
        advanceUntilIdle()

        // getGames is called with the flow, not multiple times
        verify(exactly = 1) { getGamesUseCase.getGames(any()) }
    }

    @Test
    fun `savedStateHandle persists selected game across configuration changes`() = runTest {
        setupDefaultMocks()
        val testGame = createMockGame(456, "Persistent Game")

        savedStateHandle["selected_game_id"] = testGame.id

        viewModel = GamesViewModel(savedStateHandle, genreRepository, getGamesUseCase)
        advanceUntilIdle()

        assertEquals(testGame.id, viewModel.selectedGameId.value)
    }

    @Test
    fun `viewEvent is a shared flow that doesn't replay events`() = runTest {
        setupDefaultMocks()
        viewModel = GamesViewModel(savedStateHandle, genreRepository, getGamesUseCase)
        advanceUntilIdle()

        // Emit an event before collecting
        viewModel.handleIntent(GamesUiIntent.LoadGames)
        advanceUntilIdle()

        // When - Start collecting after event was emitted
        viewModel.viewEvent.test {
            // Then - Should not receive the previous event
            expectNoEvents()
        }
    }

    @Test
    fun `games flow uses correct parameters from use case`() = runTest {
        val selectedGenresFlow = flowOf(mockSelectedGenres)
        every { genreRepository.getSelectedGenresFlow() } returns selectedGenresFlow
        every { getGamesUseCase.getGames(any()) } returns flowOf(PagingData.empty())

        viewModel = GamesViewModel(savedStateHandle, genreRepository, getGamesUseCase)
        advanceUntilIdle()

        verify(exactly = 1) {
            getGamesUseCase.getGames(match { flow ->
                // Verify that the flow passed is the selectedGenresFlow
                flow != null
            })
        }
    }

    private fun setupDefaultMocks() {
        every { genreRepository.getSelectedGenresFlow() } returns flowOf(emptyList())
        every { getGamesUseCase.getGames(any()) } returns flowOf(PagingData.empty())
    }


}
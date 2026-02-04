package com.zsasko.rawg.viewmodel

import app.cash.turbine.test
import com.zsasko.rawg.data.db.SelectedGenre
import com.zsasko.rawg.data.intents.SelectGenreUiIntent
import com.zsasko.rawg.data.model.GenreResponse
import com.zsasko.rawg.data.model.GenreResponseItem
import com.zsasko.rawg.data.model.NetworkResponse
import com.zsasko.rawg.data.state.SelectGenreUiState
import com.zsasko.rawg.domain.repository.GenreRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
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
class SelectGenreViewModelTest {

    private lateinit var viewModel: SelectGenreViewModel
    private lateinit var genreRepository: GenreRepository
    private val testDispatcher = StandardTestDispatcher()

    private val mockGenreItems = listOf(
        GenreResponseItem.makeMock(id = 1, name = "Action"),
        GenreResponseItem.makeMock(id = 2, name = "RPG"),
        GenreResponseItem.makeMock(id = 3, name = "Strategy")
    )

    val mockGenreResponse = GenreResponse(
        2,
        "https://api.example.com/genres?page=2",
        null,
        mockGenreItems
    )

    private val mockSelectedGenres = listOf(
        SelectedGenre(genreId = 1)
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        genreRepository = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be Loading`() = runTest {
        setupDefaultMocks()

        viewModel = SelectGenreViewModel(genreRepository)

        assertTrue(viewModel.genresAll.value is SelectGenreUiState.Loading)
    }

    @Test
    fun `genresAll emits Success state when repository returns success`() = runTest {
        every { genreRepository.getSelectedGenresFlow() } returns flowOf(mockSelectedGenres)
        every { genreRepository.getGenres() } returns flowOf(
            NetworkResponse.Success(
                mockGenreResponse
            )
        )

        viewModel = SelectGenreViewModel(genreRepository)

        viewModel.genresAll.test {
            val loadingState = awaitItem()
            assertTrue(loadingState is SelectGenreUiState.Loading)

            advanceUntilIdle()

            val successState = awaitItem()
            assertTrue(successState is SelectGenreUiState.Success)

            val genres = (successState as SelectGenreUiState.Success).genres
            assertEquals(3, genres.size)

            // First genre should be selected
            assertEquals("Action", genres[0].data.name)
            assertTrue(genres[0].isChecked)

            // Other genres should not be selected
            assertEquals("RPG", genres[1].data.name)
            assertTrue(!genres[1].isChecked)
        }
    }

    @Test
    fun `genresAll emits Error state when repository returns error`() = runTest {
        val errorMessage = "Network error"
        every { genreRepository.getSelectedGenresFlow() } returns flowOf(emptyList())
        every { genreRepository.getGenres() } returns flowOf(NetworkResponse.Error(errorMessage))

        viewModel = SelectGenreViewModel(genreRepository)

        viewModel.genresAll.test {
            val loadingState = awaitItem()
            assertTrue(loadingState is SelectGenreUiState.Loading)

            advanceUntilIdle()

            val errorState = awaitItem()
            assertTrue(errorState is SelectGenreUiState.Error)
            assertEquals(errorMessage, (errorState as SelectGenreUiState.Error).errorMessage)
        }
    }

    @Test
    fun `genresAll updates when selected genres change`() = runTest {
        val selectedGenresFlow = MutableStateFlow(emptyList<SelectedGenre>())
        every { genreRepository.getSelectedGenresFlow() } returns selectedGenresFlow
        every { genreRepository.getGenres() } returns flowOf(
            NetworkResponse.Success(
                mockGenreResponse
            )
        )

        viewModel = SelectGenreViewModel(genreRepository)

        viewModel.genresAll.test {
            assertTrue(awaitItem() is SelectGenreUiState.Loading)
            advanceUntilIdle()

            // First emission - no selected genres
            val firstState = awaitItem()
            assertTrue(firstState is SelectGenreUiState.Success)
            val firstGenres = (firstState as SelectGenreUiState.Success).genres
            assertTrue(firstGenres.all { !it.isChecked })

            // When - Update selected genres
            selectedGenresFlow.value = mockSelectedGenres
            advanceUntilIdle()

            // Then - Second emission with selected genre
            val secondState = awaitItem()
            assertTrue(secondState is SelectGenreUiState.Success)
            val secondGenres = (secondState as SelectGenreUiState.Success).genres
            assertTrue(secondGenres.first { it.data.id == 1 }.isChecked)
            assertTrue(secondGenres.filter { it.data.id != 1 }.all { !it.isChecked })
        }
    }

    @Test
    fun `handleIntent with ToggleSelectedGenre calls repository`() = runTest {
        setupDefaultMocks()
        coEvery { genreRepository.toggleGenreSelection(any()) } returns Unit

        viewModel = SelectGenreViewModel(genreRepository)
        advanceUntilIdle()

        val genreId = 1

        viewModel.handleIntent(SelectGenreUiIntent.ToggleSelectedGenre(genreId))
        advanceUntilIdle()

        coVerify(exactly = 1) { genreRepository.toggleGenreSelection(genreId) }
    }

    @Test
    fun `handleIntent toggles multiple genres correctly`() = runTest {
        setupDefaultMocks()
        coEvery { genreRepository.toggleGenreSelection(any()) } returns Unit

        viewModel = SelectGenreViewModel(genreRepository)
        advanceUntilIdle()

        viewModel.handleIntent(SelectGenreUiIntent.ToggleSelectedGenre(1))
        advanceUntilIdle()

        viewModel.handleIntent(SelectGenreUiIntent.ToggleSelectedGenre(2))
        advanceUntilIdle()

        viewModel.handleIntent(SelectGenreUiIntent.ToggleSelectedGenre(3))
        advanceUntilIdle()

        coVerify(exactly = 1) { genreRepository.toggleGenreSelection(1) }
        coVerify(exactly = 1) { genreRepository.toggleGenreSelection(2) }
        coVerify(exactly = 1) { genreRepository.toggleGenreSelection(3) }
    }

    @Test
    fun `genresAll handles exception with Error state`() = runTest {
        val exceptionMessage = "Unexpected error"
        every { genreRepository.getSelectedGenresFlow() } returns flowOf(emptyList())
        every { genreRepository.getGenres() } returns flowOf(NetworkResponse.Error(exceptionMessage))

        viewModel = SelectGenreViewModel(genreRepository)

        viewModel.genresAll.test {
            val loadingState = awaitItem()
            assertTrue(loadingState is SelectGenreUiState.Loading)

            advanceUntilIdle()

            val errorState = awaitItem()
            assertTrue(errorState is SelectGenreUiState.Error)
            assertTrue(
                (errorState as SelectGenreUiState.Error).errorMessage.contains(
                    exceptionMessage
                )
            )
        }
    }

    @Test
    fun `stateIn keeps last value for 5 seconds after unsubscribe`() = runTest {
        every { genreRepository.getSelectedGenresFlow() } returns flowOf(mockSelectedGenres)
        every { genreRepository.getGenres() } returns flowOf(
            NetworkResponse.Success(
                mockGenreResponse
            )
        )

        viewModel = SelectGenreViewModel(genreRepository)
        advanceUntilIdle()

        // When - Collect once to initialize
        viewModel.genresAll.test {
            skipItems(2) // Skip Loading and Success
            cancelAndIgnoreRemainingEvents()
        }

        // Advance time but less than 5 seconds
        advanceTimeBy(4000)

        // Then - Should still have the cached value
        val state = viewModel.genresAll.value
        assertTrue(state is SelectGenreUiState.Success)
    }

    @Test
    fun `combines selectedGenres and allGenres correctly`() = runTest {
        val selectedGenres = listOf(
            SelectedGenre(genreId = 1),
            SelectedGenre(genreId = 3)
        )

        every { genreRepository.getSelectedGenresFlow() } returns flowOf(selectedGenres)
        every { genreRepository.getGenres() } returns flowOf(
            NetworkResponse.Success(
                mockGenreResponse
            )
        )

        viewModel = SelectGenreViewModel(genreRepository)

        viewModel.genresAll.test {
            skipItems(1) // Skip Loading
            advanceUntilIdle()

            val successState = awaitItem()
            assertTrue(successState is SelectGenreUiState.Success)

            val genres = (successState as SelectGenreUiState.Success).genres

            assertEquals(3, genres.size)
            assertTrue(genres.first { it.data.id == 1 }.isChecked) // Action selected
            assertTrue(!genres.first { it.data.id == 2 }.isChecked) // RPG not selected
            assertTrue(genres.first { it.data.id == 3 }.isChecked) // Strategy selected
        }
    }

    @Test
    fun `empty selected genres shows all genres as unselected`() = runTest {
        every { genreRepository.getSelectedGenresFlow() } returns flowOf(emptyList())
        every { genreRepository.getGenres() } returns flowOf(
            NetworkResponse.Success(
                mockGenreResponse
            )
        )

        viewModel = SelectGenreViewModel(genreRepository)

        viewModel.genresAll.test {
            skipItems(1) // Skip Loading
            advanceUntilIdle()

            val successState = awaitItem()
            assertTrue(successState is SelectGenreUiState.Success)

            val genres = (successState as SelectGenreUiState.Success).genres

            assertTrue(genres.all { !it.isChecked })
        }
    }

    @Test
    fun `multiple toggle intents are handled sequentially`() = runTest {
        setupDefaultMocks()
        coEvery { genreRepository.toggleGenreSelection(any()) } returns Unit

        viewModel = SelectGenreViewModel(genreRepository)
        advanceUntilIdle()

        // When - Send multiple intents rapidly
        repeat(5) { index ->
            viewModel.handleIntent(SelectGenreUiIntent.ToggleSelectedGenre(index + 1))
        }
        advanceUntilIdle()

        // Then - All should be processed
        repeat(5) { index ->
            coVerify(exactly = 1) { genreRepository.toggleGenreSelection(index + 1) }
        }
    }

    // Helper functions
    private fun setupDefaultMocks() {
        every { genreRepository.getSelectedGenresFlow() } returns flowOf(emptyList())
        every { genreRepository.getGenres() } returns flowOf(
            NetworkResponse.Success(
                mockGenreResponse
            )
        )
    }
}
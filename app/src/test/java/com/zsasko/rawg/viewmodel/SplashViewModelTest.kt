package com.zsasko.rawg.viewmodel

import app.cash.turbine.test
import com.zsasko.rawg.data.services.ConfigurationService
import com.zsasko.rawg.domain.repository.GenreRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description


@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var configurationService: ConfigurationService
    private lateinit var genreRepository: GenreRepository

    @Before
    fun setup() {
        configurationService = mockk(relaxed = true)
        genreRepository = mockk()
    }

    @Test
    fun `init calls fetchConfiguration`() = runTest {
        coEvery { configurationService.fetchConfiguration() } returns true
        coEvery { genreRepository.hasSelectedGenres() } returns true

        InitViewModel(configurationService, genreRepository)
        advanceUntilIdle()

        coVerify(exactly = 1) { configurationService.fetchConfiguration() }
    }

    @Test
    fun `isGameCategoriesLoaded emits true`() = runTest {
        coEvery { configurationService.fetchConfiguration() } returns true
        coEvery { genreRepository.hasSelectedGenres() } returns true

        val viewModel = InitViewModel(configurationService, genreRepository)

        viewModel.hasSelectedGenres.test {
            // initial value from stateIn
            assertEquals(null, awaitItem())

            // skip delay(1000)
            advanceTimeBy(1_000)

            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `isGameCategoriesLoaded emits false`() = runTest {
        coEvery { configurationService.fetchConfiguration() } returns true
        coEvery { genreRepository.hasSelectedGenres() } returns false

        val viewModel = InitViewModel(configurationService, genreRepository)

        viewModel.hasSelectedGenres.test {
            assertEquals(null, awaitItem())

            advanceTimeBy(1_000)

            assertEquals(false, awaitItem())
        }
    }
}


@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val dispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description) {
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
package com.zsasko.rawg.viewmodel

import com.zsasko.rawg.data.services.ConfigurationService
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private lateinit var configurationService: ConfigurationService
    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setup() {
        configurationService = mockk()
    }

    @Test
    fun `init sets showAppVersion to true when config is true`() {
        every { configurationService.isShowAppVersionLabelConfig } returns true

        viewModel = SettingsViewModel(configurationService)

        assertTrue(viewModel.showAppVersion.value)
    }

    @Test
    fun `init sets showAppVersion to false when config is false`() {
        every { configurationService.isShowAppVersionLabelConfig } returns false

        viewModel = SettingsViewModel(configurationService)

        assertFalse(viewModel.showAppVersion.value)
    }

    @Test
    fun `loadTaskOptions updates value correctly`() {
        every { configurationService.isShowAppVersionLabelConfig } returns false
        viewModel = SettingsViewModel(configurationService)

        every { configurationService.isShowAppVersionLabelConfig } returns true

        viewModel.loadTaskOptions()

        assertTrue(viewModel.showAppVersion.value)
    }
}

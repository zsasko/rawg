package com.zsasko.rawg.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.zsasko.rawg.data.services.ConfigurationService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val configurationService: ConfigurationService) :
    ViewModel() {

    var showAppVersion = mutableStateOf(false)
        private set

    init {
        loadTaskOptions()
    }

    fun loadTaskOptions() {
        val hasEditOption = configurationService.isShowAppVersionLabelConfig
        showAppVersion.value = hasEditOption
    }

}
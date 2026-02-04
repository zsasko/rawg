package com.zsasko.rawg.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zsasko.rawg.data.intents.GamesDetailsUiIntent
import com.zsasko.rawg.data.model.NetworkResponse
import com.zsasko.rawg.data.state.GameDetailsUiState
import com.zsasko.rawg.domain.usecase.GetGameDetailsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = GameDetailsViewModel.Factory::class)
class GameDetailsViewModel @AssistedInject constructor(
    @Assisted val gameId: Int,
    val getGameDetailsUseCase: GetGameDetailsUseCase
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(gameId: Int): GameDetailsViewModel
    }

    private val _gameDetailsUiState =
        MutableStateFlow<GameDetailsUiState>(GameDetailsUiState.Loading())
    val gameDetailsUiState: StateFlow<GameDetailsUiState> = _gameDetailsUiState

    init {
        loadData(gameId)
    }

    fun handleIntent(intent: GamesDetailsUiIntent) {
        if (intent is GamesDetailsUiIntent.LoadGameDetails) {
            loadData(gameId)
        }
    }

    private fun loadData(gameId: Int) {
        viewModelScope.launch {
            _gameDetailsUiState.value = GameDetailsUiState.Loading()
            when (val gameDetailsResponse = getGameDetailsUseCase.getGameDetails(gameId)) {
                is NetworkResponse.Success -> {
                    _gameDetailsUiState.value =
                        GameDetailsUiState.Loaded(gameDetailsResponse.data)
                }

                is NetworkResponse.Error -> {
                    _gameDetailsUiState.value =
                        GameDetailsUiState.Error(gameDetailsResponse.errorMessage)
                }
            }
        }
    }
}
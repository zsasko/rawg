package com.zsasko.rawg.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.zsasko.rawg.data.intents.GamesUiIntent
import com.zsasko.rawg.domain.repository.GenreRepository
import com.zsasko.rawg.domain.usecase.GetGamesStreamUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GamesViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    genreRepository: GenreRepository,
    getGamesUseCase: GetGamesStreamUseCase
) : ViewModel() {

    private val selectedGenres = genreRepository
        .getSelectedGenresFlow()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )

    val games = getGamesUseCase
        .getGames(selectedGenres)
        .cachedIn(viewModelScope)


    val selectedGameId: StateFlow<Int?> =
        savedStateHandle.getStateFlow("selected_game_id", null)

    fun selectGameId(gameId: Int) {
        savedStateHandle["selected_game_id"] = gameId
    }

    private val _viewEvent = MutableSharedFlow<GamesUiIntent>()
    val viewEvent = _viewEvent.asSharedFlow()


    fun handleIntent(intent: GamesUiIntent) {
        if (intent is GamesUiIntent.LoadGames) {
            reloadGames()
        }
    }

    private fun reloadGames() {
        viewModelScope.launch {
            _viewEvent.emit(GamesUiIntent.LoadGames)
        }
    }
}
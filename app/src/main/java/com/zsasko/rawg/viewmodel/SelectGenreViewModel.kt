package com.zsasko.rawg.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zsasko.rawg.data.intents.SelectGenreUiIntent
import com.zsasko.rawg.data.model.NetworkResponse
import com.zsasko.rawg.data.model.toChecked
import com.zsasko.rawg.data.state.SelectGenreUiState
import com.zsasko.rawg.domain.repository.GenreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectGenreViewModel @Inject constructor(private val genreRepository: GenreRepository) :
    ViewModel() {

    private val updateGenres = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val genresAll = updateGenres.flatMapLatest {
        combine(
            updateGenres, genreRepository.getSelectedGenresFlow(),
            genreRepository.getGenres()
        ) { updateGenres, selectedGenres, allGenres ->
            when (allGenres) {
                is NetworkResponse.Success -> {
                    SelectGenreUiState.Success(
                        allGenres.data.results.toChecked(
                            selectedGenres
                        )
                    )
                }

                is NetworkResponse.Error -> {
                    SelectGenreUiState.Error(allGenres.errorMessage)
                }
            }
        }
    }.onStart { emit(SelectGenreUiState.Loading) }
        .catch {
            emit(SelectGenreUiState.Error(it.message.toString()))
        }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SelectGenreUiState.Loading
        )

    fun handleIntent(intent: SelectGenreUiIntent) {
        when (intent) {
            is SelectGenreUiIntent.ReloadData -> {
                updateGenres.update { !it }
            }

            is SelectGenreUiIntent.ToggleSelectedGenre -> {
                toggleGenreSelection(intent.genreId)
            }
        }
    }

    private fun toggleGenreSelection(genreId: Int) {
        viewModelScope.launch {
            genreRepository.toggleGenreSelection(genreId)
        }
    }
}
package com.zsasko.rawg.data.state

import com.zsasko.rawg.data.model.GenreResponseItemChecked


sealed class SelectGenreUiState {
    object Loading : SelectGenreUiState()
    data class Success(var genres: List<GenreResponseItemChecked>) : SelectGenreUiState()
    data class Error(val errorMessage: String) : SelectGenreUiState()
}
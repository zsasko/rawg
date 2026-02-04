package com.zsasko.rawg.data.intents

sealed class SelectGenreUiIntent {
    data object ReloadData : SelectGenreUiIntent()
    data class ToggleSelectedGenre(val genreId: Int) : SelectGenreUiIntent()
}
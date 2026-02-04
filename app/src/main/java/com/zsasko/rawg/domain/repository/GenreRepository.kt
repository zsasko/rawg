package com.zsasko.rawg.domain.repository

import com.zsasko.rawg.data.db.SelectedGenre
import com.zsasko.rawg.data.model.GenreResponse
import com.zsasko.rawg.data.model.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface GenreRepository {
    fun getGenres(): Flow<NetworkResponse<GenreResponse>>
    fun getSelectedGenresFlow(): Flow<List<SelectedGenre>>
    suspend fun getSelectedGenres(): List<SelectedGenre>
    suspend fun toggleGenreSelection(genreId: Int)
    suspend fun hasSelectedGenres(): Boolean
}
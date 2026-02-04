package com.zsasko.rawg.data.repository

import com.zsasko.rawg.api.DawgApi
import com.zsasko.rawg.data.db.SelectedGenre
import com.zsasko.rawg.data.db.SelectedGenreDao
import com.zsasko.rawg.data.model.GenreResponse
import com.zsasko.rawg.data.model.NetworkResponse
import com.zsasko.rawg.domain.repository.GenreRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.net.UnknownHostException


class GenreRepositoryImpl(
    private val selectedGenreDao: SelectedGenreDao,
    private val apiService: DawgApi,
    private val dispatcher: CoroutineDispatcher
) :
    GenreRepository {

    override fun getGenres(): Flow<NetworkResponse<GenreResponse>> = flow {
        try {
            val genreResponse = apiService.getGenres()
            if (genreResponse.isSuccessful) {
                emit(NetworkResponse.Success(genreResponse.body() ?: GenreResponse()))
            } else {
                emit(NetworkResponse.Error(genreResponse.message()))
            }
        } catch (e: UnknownHostException) {
            emit(NetworkResponse.Error(e.message.toString()))
        } catch (e: Exception) {
            emit(NetworkResponse.Error(e.message.toString()))
        }
    }

    override fun getSelectedGenresFlow(): Flow<List<SelectedGenre>> {
        return selectedGenreDao.getAllFlow()
    }

    override suspend fun getSelectedGenres(): List<SelectedGenre> {
        return selectedGenreDao.getAll()
    }

    override suspend fun toggleGenreSelection(genreId: Int) {
        withContext(dispatcher) {
            selectedGenreDao.toggle(SelectedGenre(genreId))
        }
    }

    override suspend fun hasSelectedGenres(): Boolean {
        return selectedGenreDao.getCount() > 0
    }

}
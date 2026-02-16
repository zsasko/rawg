package com.zsasko.rawg.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.zsasko.rawg.api.DawgApi
import com.zsasko.rawg.data.datasource.GamesPagingSource
import com.zsasko.rawg.data.db.SelectedGenre
import com.zsasko.rawg.data.model.GameDetailsResponse
import com.zsasko.rawg.data.model.GameResponseItem
import com.zsasko.rawg.data.model.NetworkResponse
import com.zsasko.rawg.domain.repository.GameRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.withContext


class GameRepositoryImpl(
    private val apiService: DawgApi,
    private val dispatcher: CoroutineDispatcher
) :
    GameRepository {

    override suspend fun getGameDetails(gameId: Int): NetworkResponse<GameDetailsResponse> {
        return withContext(dispatcher) {
            try {
                val response = apiService.getGameDetails(gameId)
                response.takeIf { it.isSuccessful }?.body()?.let {
                    NetworkResponse.Success(it)
                } ?: run {
                    NetworkResponse.Error(response.toString())
                }
            } catch (e: Exception) {
                e.printStackTrace()
                NetworkResponse.Error(e.toString())
            }
        }
    }

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    override fun getGamesStream(
        selectedGenresFlow: Flow<List<SelectedGenre>>,
        pageSize: Int,
        enablePlaceHolders: Boolean,
        prefetchDistance: Int,
        initialLoadSize: Int,
        maxCacheSize: Int
    ): Flow<PagingData<GameResponseItem>> {
        return selectedGenresFlow
            .distinctUntilChanged()
            .flatMapLatest { genres ->
                Pager(
                    // Configure how data is loaded by passing additional properties to
                    // PagingConfig, such as prefetchDistance.
                    config = PagingConfig(
                        pageSize = pageSize,
                        enablePlaceholders = enablePlaceHolders,
                        prefetchDistance = prefetchDistance,
                        initialLoadSize = initialLoadSize,
                        maxSize = maxCacheSize
                    ),
                    pagingSourceFactory = {
                        GamesPagingSource(apiService, genres)
                    }
                ).flow
            }
    }

}
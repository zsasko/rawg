package com.zsasko.rawg.domain.repository

import androidx.paging.PagingData
import com.zsasko.rawg.data.db.SelectedGenre
import com.zsasko.rawg.data.model.GameDetailsResponse
import com.zsasko.rawg.data.model.GameResponseItem
import com.zsasko.rawg.data.model.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface GameRepository {

    fun getGamesStream(
        selectedGenresFlow: Flow<List<SelectedGenre>>,
        pageSize: Int,
        enablePlaceHolders: Boolean,
        prefetchDistance: Int,
        initialLoadSize: Int,
        maxCacheSize: Int
    ): Flow<PagingData<GameResponseItem>>

    suspend fun getGameDetails(gameId: Int): NetworkResponse<GameDetailsResponse>

}
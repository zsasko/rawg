package com.zsasko.rawg.domain.usecase

import androidx.paging.PagingData
import com.zsasko.rawg.data.db.SelectedGenre
import com.zsasko.rawg.data.model.GameResponseItem
import com.zsasko.rawg.domain.repository.GameRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetGamesStreamUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {

    fun getGames(
        selectedGenresFlow: Flow<List<SelectedGenre>>
    ): Flow<PagingData<GameResponseItem>> {
        return gameRepository.getGamesStream(
            selectedGenresFlow,
            pageSize = 20,
            enablePlaceHolders = false,
            prefetchDistance = 5,
            initialLoadSize = 20,
            maxCacheSize = 2000
        )
    }

}
package com.zsasko.rawg.domain.usecase

import com.zsasko.rawg.data.model.GameDetailsResponse
import com.zsasko.rawg.data.model.NetworkResponse
import com.zsasko.rawg.domain.repository.GameRepository
import javax.inject.Inject


class GetGameDetailsUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {

    suspend fun getGameDetails(
        gameId: Int
    ): NetworkResponse<GameDetailsResponse> {
        return gameRepository.getGameDetails(gameId)
    }

}
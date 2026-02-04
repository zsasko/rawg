package com.zsasko.rawg.api

import com.zsasko.rawg.data.model.GameDetailsResponse
import com.zsasko.rawg.data.model.GameResponse
import com.zsasko.rawg.data.model.GenreResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DawgApi {
    @GET("api/genres")
    suspend fun getGenres(): Response<GenreResponse>

    @GET("api/games")
    suspend fun getGames(
        @Query("genres") genres: String,
        @Query("page") page: String
    ): Response<GameResponse>

    @GET("api/games/{id}")
    suspend fun getGameDetails(
        @Path("id") gameId: Int
    ): Response<GameDetailsResponse>

}
package com.zsasko.rawg.data.model

import com.zsasko.rawg.data.db.SelectedGenre
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenreResponse(
    override val count: Int = 0,
    override val next: String? = null,
    override val previous: String? = null,
    override val results: List<GenreResponseItem> = emptyList()
) : GenericPaginatedResponse<GenreResponseItem>

@Serializable
data class GenreResponseItem(
    val id: Int?,
    val name: String,
    val slug: String?,
    @SerialName("games_count")
    val gamesCount: Int?,
    @SerialName("image_background")
    val imageBackground: String?,
    val games: List<GenreResponseItemGame>
) {
    companion object {
        fun makeMock(id: Int, name: String): GenreResponseItem {
            return GenreResponseItem(
                id = id,
                name = name,
                slug = "action",
                gamesCount = 12345,
                imageBackground = "https://example.com/images/genres/action.jpg",
                games = listOf(
                    GenreResponseItemGame(
                        id = 101,
                        slug = "doom-eternal",
                        name = "DOOM Eternal",
                        added = 5000
                    ),
                    GenreResponseItemGame(
                        id = 102,
                        slug = "god-of-war",
                        name = "God of War",
                        added = 4800
                    ),
                    GenreResponseItemGame(
                        id = 103,
                        slug = "elden-ring",
                        name = "Elden Ring",
                        added = 6200
                    )
                )
            )
        }
    }
}

@Serializable
data class GenreResponseItemGame(
    val id: Int?,
    val slug: String?,
    val name: String,
    val added: Int?
)

fun List<GenreResponseItem>.toChecked(selectedGenres: List<SelectedGenre>): List<GenreResponseItemChecked> {
    val selectedGenreIds = selectedGenres.map { it.genreId }.toSet()
    return this.map {
        GenreResponseItemChecked(it, selectedGenreIds.contains(it.id))
    }
}
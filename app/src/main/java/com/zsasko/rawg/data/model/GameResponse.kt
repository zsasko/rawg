package com.zsasko.rawg.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


data class GameResponse(
    override val count: Int = 0,
    override val next: String? = null,
    override val previous: String? = null,
    override val results: List<GameResponseItem> = emptyList()
) : GenericPaginatedResponse<GameResponseItem>


data class GameResponseItem(
    val id: Int,
    val slug: String?,
    val name: String,
    val released: String? = null,
    val tba: Boolean,
    @SerializedName("background_image")
    val backgroundImage: String,
    val rating: Float?,
    @SerializedName("rating_top")
    val ratingTop: Float?,
    val ratings: List<GameResponseItemRating>,
    @SerializedName("ratings_count")
    val ratingsCount: Int?,
    @SerializedName("reviews_text_count")
    val reviewsTextCount: Int?,
    val added: Int?,
    @SerializedName("added_by_status")
    val addedByStatus: GameResponseItemAddedByStatus?,
    val metacritic: Int?,
    val playtime: Int?,
    @SerializedName("suggestions_count")
    val suggestionsCount: Int?,
    val updated: String,
    @SerializedName("esrb_rating")
    val esrbRating: GameResponseItemEsrbRating?,
    val platforms: List<GameResponseItemPlatform>?,
) {
    companion object {
        fun createMockGame(id: Int, name: String): GameResponseItem {
            return GameResponseItem(
                id = id,
                slug = name.lowercase().replace(" ", "-"),
                name = name,
                released = "2024-01-01",
                tba = false,
                backgroundImage = "https://example.com/image.jpg",
                rating = null,
                ratingTop = null,
                ratings = emptyList(),
                ratingsCount = 100,
                reviewsTextCount = 50,
                added = 1000,
                addedByStatus = null,
                metacritic = 85,
                playtime = 10,
                suggestionsCount = 5,
                updated = "2024-01-01T00:00:00",
                esrbRating = GameResponseItemEsrbRating(2, "test", "test"),
                platforms = emptyList()
            )
        }
    }
}


@Serializable
data class GameResponseItemRating(
    val id: Int,
    val title: String,
    val count: Int,
    val percent: Float
)

@Serializable
data class GameResponseItemAddedByStatus(
    val yet: Int? = null,
    val owned: Int? = null,
    val beaten: Int? = null,
    val toplay: Int? = null,
    val dropped: Int? = null,
    val playing: Int? = null,
)


@Serializable
data class GameResponseItemEsrbRating(
    val id: Int,
    val slug: String,
    val name: String,
)

@Serializable
data class GameResponseItemPlatformPlatform(
    val id: Int? = null,
    val slug: String? = null,
    val name: String? = null
)

@Serializable
data class GameResponseItemRequirements(
    val minimum: String? = null,
    val recommended: String? = null,
)

@Serializable
data class GameResponseItemPlatform(
    val platform: GameResponseItemPlatformPlatform? = null
)
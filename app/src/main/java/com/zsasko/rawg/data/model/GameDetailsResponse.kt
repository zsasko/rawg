package com.zsasko.rawg.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class GameDetailsResponse(
    val id: Int,
    val slug: String,
    val name: String,
    @SerializedName("name_original")
    val nameOriginal: String,
    val description: String,
    val metacritic: Int?,
    @SerializedName("metacritic_platforms")
    val metacriticPlatforms: List<GameDetailsResponseMetacriticPlatform>,
    val released: String,
    val tba: Boolean,
    val updated: String,
    @SerializedName("background_image")
    val backgroundImage: String?,
    @SerializedName("background_image_additional")
    val backgroundImageAdditional: String?,
    val website: String,
    val rating: Float?,
    @SerializedName("rating_top")
    val ratingTop: Float?,
    val ratings: List<GameResponseItemRating>,
    //val reactions: Any,  // TODO fix
    val added: Int?,
    @SerializedName("added_by_status")
    val addedByStatus: GameResponseItemAddedByStatus?,
    val playtime: Int?,
    @SerializedName("screenshots_count")
    val screenshotsCount: Int?,
    @SerializedName("movies_count")
    val moviesCount: Int?,
    @SerializedName("creators_count")
    val creatorsCount: Int?,
    @SerializedName("achievements_count")
    val achievementsCount: Int?,
    @SerializedName("parent_achievements_count")
    val parentAchievementsCount: String?,
    @SerializedName("reddit_url")
    val redditUrl: String,
    @SerializedName("reddit_name")
    val redditName: String,
    @SerializedName("reddit_description")
    val redditDescription: String,
    @SerializedName("reddit_logo")
    val redditLogo: String,
    @SerializedName("reddit_count")
    val redditCount: Int?,
    @SerializedName("twitch_count")
    val twitchCount: Int?,
    @SerializedName("youtube_count")
    val youtubeCount: Int?,
    @SerializedName("reviews_text_count")
    val reviewsTextCount: String?,
    @SerializedName("ratings_count")
    val ratingsCount: Int?,
    @SerializedName("suggestions_count")
    val suggestionsCount: Int?,
    @SerializedName("alternative_names")
    val alternativeNames: List<String>?,
    @SerializedName("metacritic_url")
    val metacriticUrl: String?,
    @SerializedName("parents_count")
    val parentsCount: Int?,
    @SerializedName("additions_count")
    val additionsCount: Int?,
    @SerializedName("game_series_count")
    val gameSeriesCount: Int?,
    @SerializedName("esrb_rating")
    val esrbRating: GameResponseItemEsrbRating?,
    val platforms: List<GameDetailsResponseItemPlatform>?,
) {

    companion object {
        fun createMinimalMock(
            id: Int = 1,
            name: String = "Test Game"
        ): GameDetailsResponse {
            return GameDetailsResponse(
                id = id,
                slug = name.lowercase().replace(" ", "-"),
                name = name,
                nameOriginal = name,
                description = "An example game used for testing and previews.",
                metacritic = 82,
                metacriticPlatforms = listOf(
                    GameDetailsResponseMetacriticPlatform(
                        metascore = 82,
                        url = "https://www.metacritic.com/game/example"
                    )
                ),
                released = "2023-09-15",
                tba = false,
                updated = "2024-01-10T12:00:00Z",
                backgroundImage = "https://example.com/background.jpg",
                backgroundImageAdditional = "https://example.com/background_extra.jpg",
                website = "https://examplegame.com",
                rating = 4.3f,
                ratingTop = 5f,
                ratings = listOf(
                    GameResponseItemRating(
                        id = 5,
                        title = "exceptional",
                        count = 120,
                        percent = 68.5f
                    )
                ),
                //  reactions = null,
                added = 5400,
                addedByStatus = GameResponseItemAddedByStatus(
                    yet = 400,
                    owned = 2500,
                    beaten = 1200,
                    toplay = 800,
                    dropped = 200,
                    playing = 300
                ),
                playtime = 18,
                screenshotsCount = 12,
                moviesCount = 3,
                creatorsCount = 25,
                achievementsCount = 40,
                parentAchievementsCount = "40",
                redditUrl = "https://reddit.com/r/examplegame",
                redditName = "examplegame",
                redditDescription = "Official subreddit for Example Game",
                redditLogo = "https://example.com/reddit_logo.png",
                redditCount = 8700,
                twitchCount = 120,
                youtubeCount = 340,
                reviewsTextCount = "95",
                ratingsCount = 1800,
                suggestionsCount = 420,
                alternativeNames = listOf("Example Game Deluxe", "EG"),
                metacriticUrl = "https://www.metacritic.com/game/example",
                parentsCount = 1,
                additionsCount = 2,
                gameSeriesCount = 1,
                esrbRating = GameResponseItemEsrbRating(
                    id = 4,
                    name = "Mature",
                    slug = "mature"
                ),
                platforms = listOf(
                    GameDetailsResponseItemPlatform(
                        platform = GameDetailsResponseItemPlatformPlatform(
                            id = 1,
                            name = "PC",
                            slug = "pc",
                            image = "https://example.com/platform_pc.png",
                            yearEnd = null,
                            yearStart = 1990,
                            gamesCount = 50000,
                            imageBackground = "https://example.com/platform_pc_bg.jpg"
                        ),
                        releasedAt = "2023-09-15",
                        requirements = GameResponseItemRequirements(
                            minimum = "Intel i5, 8 GB RAM, GTX 1060",
                            recommended = "Intel i7, 16 GB RAM, RTX 2060"
                        )
                    )
                )
            )
        }
    }
}

@Serializable
data class GameDetailsResponseMetacriticPlatform(
    val metascore: Int,
    val url: String
)

@Serializable
data class GameDetailsResponseItemPlatform(
    val platform: GameDetailsResponseItemPlatformPlatform? = null,
    @SerializedName("released_at")
    val releasedAt: String? = null,
    val requirements: GameResponseItemRequirements? = null,
)

@Serializable
data class GameDetailsResponseItemPlatformPlatform(
    val id: Int? = null,
    val name: String? = null,
    val slug: String? = null,
    val image: String? = null,
    @SerializedName("year_end")
    val yearEnd: Int? = null,
    @SerializedName("year_start")
    val yearStart: Int? = null,
    @SerializedName("games_count")
    val gamesCount: Int? = null,
    @SerializedName("image_background")
    val imageBackground: String? = null
)
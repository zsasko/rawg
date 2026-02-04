package com.zsasko.rawg.ui.common.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

object Routes {
    @Serializable
    data object Splash : NavKey

    @Serializable
    data class SelectGenres(val showUpButton: Boolean, val showNextButton: Boolean) : NavKey

    @Serializable
    data object Games : NavKey

    @Serializable
    data class GameDetails(val gameId: Int) : NavKey

    @Serializable
    data object Settings : NavKey
}
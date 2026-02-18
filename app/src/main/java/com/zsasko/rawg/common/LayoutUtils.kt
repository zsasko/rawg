package com.zsasko.rawg.common

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo

@Composable
fun rememberIsTablet(): Boolean {
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current

    // Convert container size from pixels to dp
    val screenWidthDp = with(density) { windowInfo.containerSize.width.toDp() }
    val screenHeightDp = with(density) { windowInfo.containerSize.height.toDp() }
    val smallestWidth = minOf(screenWidthDp.value, screenHeightDp.value)

    // A device is considered a tablet if its smallest width is >= 600dp
    return smallestWidth >= 600
}

@Composable
fun rememberIsLandscape(): Boolean {
    val configuration = LocalConfiguration.current
    return configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}

@Composable
fun rememberIsTabletLandscape(): Boolean {
    return rememberIsTablet() && rememberIsLandscape()
}
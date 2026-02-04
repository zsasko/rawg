/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zsasko.rawg.common

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import com.zsasko.rawg.R
import com.zsasko.rawg.ui.common.navigation.Routes
import com.zsasko.rawg.ui.common.theme.RAWGTheme

@Composable
fun AppNavRail(
    currentRoute: NavKey,
    navigateToHome: () -> Unit,
    navigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val iconSize = dimensionResource(R.dimen.navigation_icon_size)
    NavigationRail(
        header = {
            Image(
                painter = painterResource(R.drawable.ic_rawg_colorful),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(iconSize)
            )
        },
        modifier = modifier,
    ) {
        Spacer(Modifier.weight(1f))
        NavigationRailItem(
            selected = currentRoute is Routes.Games || currentRoute is Routes.GameDetails,
            onClick = navigateToHome,
            icon = {
                Icon(
                    painterResource(id = R.drawable.ic_home),
                    stringResource(R.string.home_title),
                    modifier = Modifier.size(iconSize)
                )
            },
            label = { Text(stringResource(R.string.home_title)) },
        )
        NavigationRailItem(
            selected = currentRoute is Routes.SelectGenres || currentRoute is Routes.Settings,
            onClick = navigateToSettings,
            icon = {
                Icon(
                    painterResource(id = R.drawable.ic_settings),
                    stringResource(R.string.settings_title),
                    modifier = Modifier.size(iconSize)
                )
            },
            label = { Text(stringResource(R.string.settings_title)) },
        )
        Spacer(Modifier.weight(1f))
    }
}

@Preview("Drawer contents")
@Preview("Drawer contents (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewAppNavRail() {
    RAWGTheme {
        AppNavRail(
            currentRoute = Routes.Games,
            navigateToHome = {},
            navigateToSettings = {},
        )
    }
}

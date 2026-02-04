/*
 * Copyright 2020 The Android Open Source Project
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

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.NavKey
import com.zsasko.rawg.R
import com.zsasko.rawg.ui.common.AppDrawerLogo
import com.zsasko.rawg.ui.common.navigation.Routes

@Composable
fun AppDrawer(
    drawerState: DrawerState,
    currentRoute: NavKey,
    navigateToHome: () -> Unit,
    navigateToSettings: () -> Unit,
    closeDrawer: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val iconSize = dimensionResource(R.dimen.navigation_icon_size)
    ModalDrawerSheet(
        drawerState = drawerState,
        modifier = modifier,
    ) {
        AppDrawerLogo()
        NavigationDrawerItem(
            label = { Text(stringResource(id = R.string.home_title)) },
            icon = {
                Icon(
                    painterResource(R.drawable.ic_home),
                    null,
                    modifier = Modifier.size(iconSize)
                )
            },
            selected = currentRoute == Routes.Games,
            onClick = {
                navigateToHome()
                closeDrawer()
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
        )
        NavigationDrawerItem(
            label = { Text(stringResource(id = R.string.settings_title)) },
            icon = {
                Icon(
                    painterResource(R.drawable.ic_settings),
                    null,
                    modifier = Modifier.size(iconSize)
                )
            },
            selected = currentRoute is Routes.SelectGenres || currentRoute is Routes.Settings,
            onClick = {
                navigateToSettings()
                closeDrawer()
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
        )
    }
}
package com.zsasko.rawg.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.zsasko.rawg.R
import com.zsasko.rawg.common.ANALYTICS_SCREEN_SETTINGS
import com.zsasko.rawg.common.analytics.TrackScreenViewEvent
import com.zsasko.rawg.ui.settings.views.SettingsClickableItem
import com.zsasko.rawg.ui.settings.views.SettingsVersionCodeLabel
import com.zsasko.rawg.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onSelectGenres: () -> Unit,
    isExpandedScreen: Boolean,
    openDrawer: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    TrackScreenViewEvent(screenName = ANALYTICS_SCREEN_SETTINGS)

    Scaffold(
        modifier = Modifier.padding(horizontal = 16.dp),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.settings_title)
                    )
                },
                navigationIcon = {
                    if (!isExpandedScreen) {
                        IconButton(onClick = openDrawer) {
                            Icon(
                                painter = painterResource(R.drawable.ic_menu),
                                modifier = Modifier.size(32.dp),
                                contentDescription = stringResource(R.string.general_open_navigation_drawer),
                            )
                        }
                    }
                },

                )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            SettingsClickableItem(
                stringResource(R.string.select_genres_title),
                stringResource(R.string.settings_select_genres_subtitle),
                onSelectGenres
            )

            Spacer(modifier = Modifier.weight(1f))

            if (viewModel.showAppVersion.value) {
                SettingsVersionCodeLabel()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    SettingsScreen(isExpandedScreen = false, openDrawer = {}, onSelectGenres = {})
}
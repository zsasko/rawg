package com.zsasko.rawg.ui.game_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zsasko.rawg.R
import com.zsasko.rawg.common.ANALYTICS_SCREEN_SELECT_GAME_DETAILS
import com.zsasko.rawg.common.analytics.TrackScreenViewEvent
import com.zsasko.rawg.data.intents.GamesDetailsUiIntent
import com.zsasko.rawg.data.model.GameDetailsResponse
import com.zsasko.rawg.data.state.GameDetailsUiState
import com.zsasko.rawg.ui.common.ErrorLayout
import com.zsasko.rawg.ui.common.LoadingLayout
import com.zsasko.rawg.ui.game_details.views.GameDetailsAdditionalInfoSection
import com.zsasko.rawg.ui.game_details.views.GameDetailsDescriptionSection
import com.zsasko.rawg.ui.game_details.views.GameDetailsHeaderImageView
import com.zsasko.rawg.ui.game_details.views.GameDetailsPlatformsSection
import com.zsasko.rawg.ui.game_details.views.GameDetailsRatingMetacriticSection
import com.zsasko.rawg.ui.game_details.views.GameDetailsRatingsDistributionSection
import com.zsasko.rawg.ui.game_details.views.GameDetailsSocialLinksSection
import com.zsasko.rawg.ui.game_details.views.GameDetailsStatsRow
import com.zsasko.rawg.ui.game_details.views.GameDetailsTitleSection
import com.zsasko.rawg.viewmodel.GameDetailsViewModel


@Composable
fun GameDetailsScreen(
    viewModel: GameDetailsViewModel,
    showUpButton: Boolean,
    onUpButtonClicked: () -> Unit,
) {
    TrackScreenViewEvent(screenName = ANALYTICS_SCREEN_SELECT_GAME_DETAILS)

    val state by viewModel.gameDetailsUiState.collectAsStateWithLifecycle()

    GameDetailsScreenLayout(showUpButton, onUpButtonClicked, state) {
        viewModel.handleIntent(GamesDetailsUiIntent.LoadGameDetails)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GameDetailsScreenLayout(
    showUpButton: Boolean,
    onUpButtonClicked: () -> Unit,
    state: GameDetailsUiState,
    onReloadDataInvoked: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val title = (state as? GameDetailsUiState.Loaded)?.data?.name ?: "Game Details"
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Text(title)
                },
                navigationIcon = {
                    if (!showUpButton) {
                        IconButton(onClick = onUpButtonClicked) {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow_back),
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
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            when (state) {

                is GameDetailsUiState.Loaded -> {
                    GameDetailsContent(game = state.data)
                }

                is GameDetailsUiState.Error -> {
                    ErrorLayout(
                        errorText = state.errorMessage ?: "",
                        onReloadDataButtonClicked = {
                            onReloadDataInvoked()
                        })
                }

                is GameDetailsUiState.Loading -> {
                    LoadingLayout()
                }
            }
        }

    }

}

@Composable
fun GameDetailsContent(game: GameDetailsResponse) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            GameDetailsHeaderImageView(imageUrl = game.backgroundImage)
        }

        item {
            GameDetailsTitleSection(
                game = game,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        item {
            GameDetailsRatingMetacriticSection(
                game = game,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        item {
            GameDetailsStatsRow(
                game = game,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        item {
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        }

        if (game.description.isNotEmpty()) {
            item {
                GameDetailsDescriptionSection(
                    description = game.description,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }

        game.platforms?.let {
            item {
                GameDetailsPlatformsSection(
                    platforms = it,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }

        if (game.ratings.isNotEmpty()) {
            item {
                GameDetailsRatingsDistributionSection(
                    ratings = game.ratings,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }

        item {
            GameDetailsAdditionalInfoSection(
                game = game,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        item {
            GameDetailsSocialLinksSection(
                game = game,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

}

@Preview
@Composable
private fun GameDetailsScreenLayoutPReview() {
    GameDetailsScreenLayout(
        showUpButton = false,
        onReloadDataInvoked = {},
        onUpButtonClicked = {},
        state = GameDetailsUiState.Loaded(GameDetailsResponse.createMinimalMock(1, ""))
    )
}
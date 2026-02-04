package com.zsasko.rawg.ui.games

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.zsasko.rawg.R
import com.zsasko.rawg.common.ANALYTICS_SCREEN_SELECT_GAMES
import com.zsasko.rawg.common.analytics.TrackScreenViewEvent
import com.zsasko.rawg.common.rememberIsTabletLandscape
import com.zsasko.rawg.data.intents.GamesUiIntent
import com.zsasko.rawg.data.model.GameResponseItem
import com.zsasko.rawg.ui.common.ErrorLayout
import com.zsasko.rawg.ui.common.LoadingLayout
import com.zsasko.rawg.ui.games.views.GameListItem
import com.zsasko.rawg.ui.games.views.NoGenresSelected
import com.zsasko.rawg.viewmodel.GamesViewModel
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamesScreen(
    isExpandedScreen: Boolean,
    viewModel: GamesViewModel = hiltViewModel(),
    onGameClicked: (Int) -> Unit,
    openDrawer: () -> Unit,
) {
    TrackScreenViewEvent(screenName = ANALYTICS_SCREEN_SELECT_GAMES)

    val games = viewModel.games.collectAsLazyPagingItems()
    val selectedGame = viewModel.selectedGameId.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.viewEvent.collect { action ->
            when (action) {
                GamesUiIntent.LoadGames -> games.refresh()
            }
        }
    }

    val isTablet = rememberIsTabletLandscape()
    if (isTablet) {
        LaunchedEffect(games.loadState, selectedGame) {
            if (games.loadState.refresh is LoadState.NotLoading &&
                games.itemCount > 0 &&
                selectedGame.value == null
            ) {
                // preselecting first game item and displaying it's content
                games[0]?.let { firstGame ->
                    viewModel.selectGameId(firstGame.id)
                    onGameClicked(firstGame.id)
                }
            }
        }

        LaunchedEffect(selectedGame) {
            // if already a game has been selected, display it, it can be triggered when tablet
            // was rotated from portrait to landscape
            selectedGame.let { game ->
                game.value?.let {
                    onGameClicked(it)
                }
            }
        }
    }

    GamesScreenContent(
        isExpandedScreen = isExpandedScreen,
        imageListItems = games,
        onReloadDataButtonClicked = {
            viewModel.handleIntent(GamesUiIntent.LoadGames)
        },
        onGameClicked = {
            viewModel.selectGameId(it)
            onGameClicked(it)
        },
        openDrawer = openDrawer
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GamesScreenContent(
    isExpandedScreen: Boolean,
    imageListItems: LazyPagingItems<GameResponseItem>?,
    onReloadDataButtonClicked: () -> Unit,
    onGameClicked: (Int) -> Unit,
    openDrawer: () -> Unit,
) {

    val isLoading =
        imageListItems?.loadState?.refresh is LoadState.Loading

    val isError =
        (imageListItems?.loadState?.refresh as? LoadState.Error)
            ?: (imageListItems?.loadState?.append as? LoadState.Error)
            ?: (imageListItems?.loadState?.prepend as? LoadState.Error)

    val isEmpty = imageListItems?.loadState?.refresh is LoadState.NotLoading
            && imageListItems.itemCount == 0

    Scaffold(
        modifier = Modifier.padding(horizontal = 16.dp),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.app_name),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
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
        })
    { innerPadding ->
        when {
            isEmpty -> {
                NoGenresSelected()
            }

            isLoading -> {
                LoadingLayout()
            }

            isError != null -> {
                ErrorLayout(
                    errorText = isError.toString(),
                    onReloadDataButtonClicked = onReloadDataButtonClicked
                )
            }

            else -> {
                GamesScreenLayout(imageListItems, innerPadding, onGameClicked)
            }
        }
    }

}

@Composable
private fun GamesScreenLayout(
    games: LazyPagingItems<GameResponseItem>?,
    innerPadding: PaddingValues,
    onGameClicked: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(top = innerPadding.calculateTopPadding())
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(games?.itemCount ?: 0) { position ->
            val game = games?.get(position)
            game?.let { gameObject ->
                GameListItem(
                    gameObject, { onGameClicked.invoke(gameObject.id) },
                    modifier = Modifier
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GamesScreenPreview() {
    val mockedGamesResponse = listOf(
        GameResponseItem.createMockGame(1, "Test 1"),
        GameResponseItem.createMockGame(2, "Test 2"),
        GameResponseItem.createMockGame(3, "Test 3")
    )
    val gamesPagingData = PagingData.from(mockedGamesResponse)
    val fakeGamesFlow = MutableStateFlow(gamesPagingData)
    GamesScreenLayout(
        games = fakeGamesFlow.collectAsLazyPagingItems(),
        innerPadding = PaddingValues(0.dp),
        onGameClicked = {},
    )
}
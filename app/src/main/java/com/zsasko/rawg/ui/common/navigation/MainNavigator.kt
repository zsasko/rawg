package com.zsasko.rawg.ui.common.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.PaneScaffoldDirective
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.zsasko.rawg.common.AppDrawer
import com.zsasko.rawg.common.AppNavRail
import com.zsasko.rawg.common.rememberIsTabletLandscape
import com.zsasko.rawg.ui.common.LoadingLayout
import com.zsasko.rawg.ui.game_details.GameDetailsScreen
import com.zsasko.rawg.ui.games.GamesScreen
import com.zsasko.rawg.ui.select_genre.SelectGenreScreen
import com.zsasko.rawg.ui.settings.SettingsScreen
import com.zsasko.rawg.viewmodel.GameDetailsViewModel
import com.zsasko.rawg.viewmodel.InitViewModel
import kotlinx.coroutines.launch

@Composable
fun MainNavigator() {
    val initViewModel: InitViewModel = hiltViewModel()
    val hasSelectedGenres = initViewModel.hasSelectedGenres.collectAsStateWithLifecycle()
    hasSelectedGenres.value?.let {
        val firstRoute: NavKey = if (it) Routes.Games else Routes.SelectGenres(
            showUpButton = false,
            showNextButton = true
        )
        MainNavigatorWithRoute(firstRoute)
    } ?: run {
        LoadingLayout()
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainNavigatorWithRoute(firstRoute: NavKey) {
    val coroutineScope = rememberCoroutineScope()

    val backStack = rememberNavBackStack(firstRoute)
    val currentRoute = backStack.lastOrNull() ?: firstRoute

    val listDetailsDirective = getListDetailsDirective()
    val listDetailStrategy =
        rememberListDetailSceneStrategy<NavKey>(directive = listDetailsDirective)
    val standaloneSelectGenres = (currentRoute as? Routes.SelectGenres)?.showNextButton ?: false

    val isTabletLayout = rememberIsTabletLandscape()
    val sizeAwareDrawerState = rememberSizeAwareDrawerState(isTabletLayout)
    val gesturesEnabled =
        currentRoute !is Routes.GameDetails && currentRoute !is Routes.SelectGenres



    ModalNavigationDrawer(
        drawerContent = {
            AppDrawer(
                drawerState = sizeAwareDrawerState,
                currentRoute = currentRoute,
                navigateToHome = {
                    navigateAndClearStackUntilRoot(backStack, null)
                },
                navigateToSettings = {
                    navigateAndClearStackUntilRoot(backStack, Routes.Settings)
                },
                closeDrawer = { coroutineScope.launch { sizeAwareDrawerState.close() } },
            )
        },
        drawerState = sizeAwareDrawerState,
        gesturesEnabled = !isTabletLayout && gesturesEnabled,
    ) {
        Row {
            if (isTabletLayout && !standaloneSelectGenres) {
                AppNavRail(
                    currentRoute = currentRoute,
                    navigateToHome = {
                        navigateAndClearStackUntilRoot(backStack, null)
                    },
                    navigateToSettings = {
                        navigateAndClearStackUntilRoot(backStack, Routes.Settings)
                    },
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            NavDisplay(
                sceneStrategy = listDetailStrategy,
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                entryDecorators = listOf(
                    // used in order to inject viewmodel for Routes.GameDetails route
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator()
                ),
                entryProvider = entryProvider {
                    entry<Routes.SelectGenres> {
                        SelectGenreScreen(
                            showUpButton = it.showUpButton,
                            showNextButton = it.showNextButton,
                            onUpButtonClicked = {
                                backStack.removeLastOrNull()
                            },
                            onNextButtonClicked = {
                                backStack.removeLastOrNull()
                                backStack.add(Routes.Games)
                            })
                    }
                    entry<Routes.Games>(
                        metadata = ListDetailSceneStrategy.listPane(
                            detailPlaceholder = {
                                LoadingLayout()
                            }
                        )
                    ) {
                        GamesScreen(
                            isExpandedScreen = isTabletLayout,
                            onGameClicked = { gameId ->
                                backStack.removeAll { it is Routes.GameDetails }
                                backStack.add(Routes.GameDetails(gameId))
                            }, openDrawer = {
                                coroutineScope.launch { sizeAwareDrawerState.open() }
                            })
                    }
                    entry<Routes.GameDetails>(metadata = ListDetailSceneStrategy.detailPane()) {
                        val viewModel =
                            hiltViewModel<GameDetailsViewModel, GameDetailsViewModel.Factory>(
                                creationCallback = { factory ->
                                    factory.create(it.gameId)
                                }
                            )
                        GameDetailsScreen(
                            viewModel = viewModel,
                            showUpButton = isTabletLayout,
                            onUpButtonClicked = {
                                backStack.removeLastOrNull()
                            },
                        )
                    }
                    entry<Routes.Settings> {
                        SettingsScreen(isExpandedScreen = isTabletLayout, openDrawer = {
                            coroutineScope.launch { sizeAwareDrawerState.open() }
                        }, onSelectGenres = {
                            backStack.add(
                                Routes.SelectGenres(
                                    showUpButton = true,
                                    showNextButton = false
                                )
                            )
                        })
                    }
                }
            )
        }
    }
}

private fun navigateAndClearStackUntilRoot(
    backStack: MutableList<NavKey>,
    nextRouteToOpen: NavKey?
) {
    while (backStack.size > 1) {
        backStack.removeLastOrNull()
    }
    nextRouteToOpen?.let {
        backStack.add(it)
    }
}

/**
 * Determine the drawer state to pass to the modal drawer.
 */
@Composable
private fun rememberSizeAwareDrawerState(isExpandedScreen: Boolean): DrawerState {
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    return if (!isExpandedScreen) {
        // If we want to allow showing the drawer, we use a real, remembered drawer
        // state defined above
        drawerState
    } else {
        // If we don't want to allow the drawer to be shown, we provide a drawer state
        // that is locked closed. This is intentionally not remembered, because we
        // don't want to keep track of any changes and always keep it closed
        DrawerState(DrawerValue.Closed)
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun getListDetailsDirective(): PaneScaffoldDirective {
    val listToDetailsRatio = 0.33
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current
    val oneThirdWidth = remember(density, windowInfo) {
        with(density) {
            (windowInfo.containerSize.width * listToDetailsRatio).toInt().toDp()
        }
    }

    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    val directive = remember(windowAdaptiveInfo, oneThirdWidth) {
        calculatePaneScaffoldDirective(windowAdaptiveInfo)
            .copy(
                horizontalPartitionSpacerSize = 0.dp,
                defaultPanePreferredWidth = oneThirdWidth
            )
    }
    return directive
}

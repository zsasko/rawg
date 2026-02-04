package com.zsasko.rawg.ui.select_genre

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zsasko.rawg.R
import com.zsasko.rawg.common.ANALYTICS_SCREEN_SELECT_GENRE
import com.zsasko.rawg.common.analytics.TrackScreenViewEvent
import com.zsasko.rawg.data.intents.SelectGenreUiIntent
import com.zsasko.rawg.data.model.GenreResponseItem
import com.zsasko.rawg.data.model.GenreResponseItemChecked
import com.zsasko.rawg.data.state.SelectGenreUiState
import com.zsasko.rawg.ui.common.ErrorLayout
import com.zsasko.rawg.ui.common.LoadingLayout
import com.zsasko.rawg.ui.select_genre.views.SelectGenreListItem
import com.zsasko.rawg.viewmodel.SelectGenreViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectGenreScreen(
    showUpButton: Boolean,
    showNextButton: Boolean,
    onUpButtonClicked: () -> Unit,
    onNextButtonClicked: () -> Unit,
    viewModel: SelectGenreViewModel = hiltViewModel()
) {

    TrackScreenViewEvent(screenName = ANALYTICS_SCREEN_SELECT_GENRE)

    val genresUiState = viewModel.genresAll.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.select_genres_title),
                    )
                },
                navigationIcon = {
                    if (showUpButton) {
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
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize()
        ) {

            when (genresUiState.value) {
                is SelectGenreUiState.Success -> {
                    SelectGenreScreenLayout(
                        showNextButton = showNextButton,
                        successState = genresUiState.value as SelectGenreUiState.Success,
                        onGenreToggled = {
                            viewModel.handleIntent(
                                SelectGenreUiIntent.ToggleSelectedGenre(
                                    it.data.id ?: 0
                                )
                            )
                        },
                        onNextButtonClicked = onNextButtonClicked
                    )
                }

                is SelectGenreUiState.Error -> {
                    ErrorLayout(
                        errorText = (genresUiState.value as SelectGenreUiState.Error).errorMessage,
                        onReloadDataButtonClicked = {
                            viewModel.handleIntent(SelectGenreUiIntent.ReloadData)
                        })
                }

                else -> {
                    LoadingLayout()
                }
            }
        }
    }
}

@Composable
private fun SelectGenreScreenLayout(
    showNextButton: Boolean,
    successState: SelectGenreUiState.Success,
    onGenreToggled: (GenreResponseItemChecked) -> Unit,
    onNextButtonClicked: () -> Unit,
) {
    val selectedItemCount = successState.genres.count { it.isChecked }
    Column{
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(
                items = successState.genres,
                key = { genre ->
                    "${genre.data.id}${genre.data.name}"
                }
            ) { genre ->
                SelectGenreListItem(genre, onGenreToggled)
            }
        }
        if (showNextButton) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(onClick = onNextButtonClicked, enabled = selectedItemCount > 0) {
                    Text(stringResource(R.string.select_genres_button_next))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectGenreScreenPreview() {
    SelectGenreScreenLayout(
        false, SelectGenreUiState.Success(
            listOf(
                GenreResponseItemChecked(
                    GenreResponseItem.makeMock(1, "Action"),
                    true
                ), GenreResponseItemChecked(GenreResponseItem.makeMock(2, "GPG"), true)
            )
        ), {}, {})
}
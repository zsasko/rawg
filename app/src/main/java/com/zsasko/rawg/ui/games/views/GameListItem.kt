/*
 * Copyright 2022 The Android Open Source Project
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

package com.zsasko.rawg.ui.games.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.zsasko.rawg.R
import com.zsasko.rawg.data.model.GameResponseItem

private const val CROSSFADE_DURATION = 500
private const val IMAGE_HEIGHT = 200

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GameListItem(
    game: GameResponseItem,
    navigateToDetail: (GameResponseItem) -> Unit,
    modifier: Modifier = Modifier,
    isOpened: Boolean = false,
) {
    Card(
        modifier = modifier
            .clip(CardDefaults.shape)
            .combinedClickable(
                onClick = { navigateToDetail(game) },
            )
            .clip(CardDefaults.shape),
        colors = CardDefaults.cardColors(
            containerColor = if (isOpened) MaterialTheme.colorScheme.secondaryContainer
            else MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(game.backgroundImage)
                    .crossfade(CROSSFADE_DURATION)
                    .build(),
                modifier = Modifier.height(IMAGE_HEIGHT.dp),
                contentScale = ContentScale.Crop,
                contentDescription = game.name,
                error = painterResource(R.drawable.ic_placeholder),
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black
                            )
                        )
                    )
                    .padding(20.dp)
            ) {
                Text(
                    text = game.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameListItemPreview() {
    val gameMocked = GameResponseItem.createMockGame(1, "TEST")
    GameListItem(gameMocked, {})
}
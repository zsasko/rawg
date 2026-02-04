package com.zsasko.rawg.ui.game_details.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zsasko.rawg.R
import com.zsasko.rawg.data.model.GameDetailsResponse

@Composable
fun GameDetailsStatsRow(
    game: GameDetailsResponse,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        game.playtime?.takeIf { it > 0 }?.let { playtime ->
            item {
                GameDetailsStatItem(
                    icon = Icons.Default.Schedule,
                    value = stringResource(R.string.game_details_playtime_hours, playtime),
                    label = stringResource(R.string.game_details_playtime)
                )
            }
        }

        game.achievementsCount?.takeIf { it > 0 }?.let { achievements ->
            item {
                GameDetailsStatItem(
                    icon = Icons.Default.EmojiEvents,
                    value = "$achievements",
                    label = stringResource(R.string.game_details_achievements)
                )
            }
        }

        game.screenshotsCount?.takeIf { it > 0 }?.let { screenshots ->
            item {
                GameDetailsStatItem(
                    icon = Icons.Default.Photo,
                    value = "$screenshots",
                    label = stringResource(R.string.game_details_screenshots)
                )
            }
        }

        game.moviesCount?.takeIf { it > 0 }?.let { movies ->
            item {
                GameDetailsStatItem(
                    icon = Icons.Default.Movie,
                    value = "$movies",
                    label = stringResource(R.string.game_details_videos)
                )
            }
        }
    }
}

@Preview
@Composable
private fun GameDetailsStatsRowPreview() {
    GameDetailsStatsRow(GameDetailsResponse.createMinimalMock(1, "test"))
}
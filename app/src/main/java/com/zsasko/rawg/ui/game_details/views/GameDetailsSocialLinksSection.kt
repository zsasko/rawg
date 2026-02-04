package com.zsasko.rawg.ui.game_details.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zsasko.rawg.R
import com.zsasko.rawg.data.model.GameDetailsResponse


@Composable
fun GameDetailsSocialLinksSection(
    game: GameDetailsResponse,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.game_details_community),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            game.redditCount?.let { redditCount ->
                if (redditCount > 0) {
                    GameDetailsSocialButton(
                        icon = Icons.Default.Forum,
                        count = redditCount,
                        label = stringResource(R.string.game_details_social_link_reddit),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            game.twitchCount?.let { twitchCount ->
                if (twitchCount > 0) {
                    GameDetailsSocialButton(
                        icon = Icons.Default.PlayArrow,
                        count = twitchCount,
                        label = stringResource(R.string.game_details_social_link_twitch),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            game.youtubeCount?.let { youtubeCount ->
                if (youtubeCount > 0) {
                    GameDetailsSocialButton(
                        icon = Icons.Default.PlayCircle,
                        count = youtubeCount,
                        label = stringResource(R.string.game_details_social_link_youtube),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun GameDetailsSocialLinksSectionPreview() {
    val mockedGameDetails = GameDetailsResponse.createMinimalMock(1, "test")
    GameDetailsSocialLinksSection(mockedGameDetails)
}
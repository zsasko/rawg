package com.zsasko.rawg.ui.game_details.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
fun GameDetailsAdditionalInfoSection(
    game: GameDetailsResponse,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.game_details_additional_information),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        if (game.website.isNotEmpty()) {
            GameDetailsInfoRow(
                label = stringResource(R.string.game_details_website),
                value = game.website,
                isLink = true
            )
        }

        game.added?.let { added ->
            GameDetailsInfoRow(
                label = stringResource(R.string.game_details_added_by),
                value = stringResource(R.string.game_details_added_users, added)
            )
        }

        game.suggestionsCount?.let { suggestions ->
            GameDetailsInfoRow(
                label = stringResource(R.string.game_details_suggestions),
                value = "$suggestions"
            )
        }

        game.alternativeNames?.let {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = stringResource(R.string.game_details_alternative_names),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = it.joinToString(", "),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview
@Composable
private fun GameDetailsAdditionalInfoSectionPreview() {
    val mockedGameDetails = GameDetailsResponse.createMinimalMock(1, "test")
    GameDetailsAdditionalInfoSection(mockedGameDetails)
}
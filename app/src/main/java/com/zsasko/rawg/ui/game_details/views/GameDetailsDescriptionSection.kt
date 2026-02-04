package com.zsasko.rawg.ui.game_details.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zsasko.rawg.R
import com.zsasko.rawg.data.model.GameDetailsResponse
import com.zsasko.rawg.ui.common.stripHtmlTags

const val GAME_DETAILS_STRIPPED_DESCRIPTION_MAX_LINES = 4
const val GAME_DETAILS_STRIPPED_DESCRIPTION_LENGTH = 200

@Composable
fun GameDetailsDescriptionSection(
    description: String,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    val strippedDescription = description.stripHtmlTags()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.game_details_about),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = strippedDescription,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = if (isExpanded) Int.MAX_VALUE else GAME_DETAILS_STRIPPED_DESCRIPTION_MAX_LINES,
            overflow = TextOverflow.Ellipsis
        )

        if (description.length > GAME_DETAILS_STRIPPED_DESCRIPTION_LENGTH) {
            TextButton(
                onClick = { isExpanded = !isExpanded }
            ) {
                Text(
                    text = if (isExpanded) stringResource(R.string.general_show_less) else stringResource(
                        R.string.general_show_more
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}


@Preview
@Composable
private fun GameDetailsStatsRowPreview() {
    val mockedGameDetails = GameDetailsResponse.createMinimalMock(1, "test")
    GameDetailsDescriptionSection(mockedGameDetails.description)
}

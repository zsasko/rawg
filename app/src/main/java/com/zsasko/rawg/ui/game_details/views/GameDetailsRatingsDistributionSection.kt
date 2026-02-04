package com.zsasko.rawg.ui.game_details.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zsasko.rawg.R
import com.zsasko.rawg.data.model.GameDetailsResponse
import com.zsasko.rawg.data.model.GameResponseItemRating
import com.zsasko.rawg.ui.common.RATING_DISTRIBUTION_EXCEPTIONAL
import com.zsasko.rawg.ui.common.RATING_DISTRIBUTION_MEH
import com.zsasko.rawg.ui.common.RATING_DISTRIBUTION_RECOMMENDED
import com.zsasko.rawg.ui.common.RATING_DISTRIBUTION_SKIP


@Composable
fun GameDetailsRatingsDistributionSection(
    ratings: List<GameResponseItemRating>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.game_details_ratings_distribution),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        ratings.forEach { rating ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = rating.title,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.width(100.dp)
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(rating.percent / 100f)
                            .background(ratingColor(rating.title))
                    )
                }

                Text(
                    text = "${rating.percent.toInt()}%",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.width(40.dp)
                )
            }
        }
    }
}

private fun ratingColor(title: String): Color {
    return when (title.lowercase()) {
        RATING_DISTRIBUTION_EXCEPTIONAL -> Color(0xFF4CAF50)
        RATING_DISTRIBUTION_RECOMMENDED -> Color(0xFF2196F3)
        RATING_DISTRIBUTION_MEH -> Color(0xFFFF9800)
        RATING_DISTRIBUTION_SKIP -> {
            Color(0xFFF44336)
        }

        else -> Color.Gray
    }
}

@Preview
@Composable
private fun GameDetailsRatingsDistributionSectionPreview() {
    val mockedGameDetails = GameDetailsResponse.createMinimalMock(1, "test")
    GameDetailsRatingsDistributionSection(mockedGameDetails.ratings)
}
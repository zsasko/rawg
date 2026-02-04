package com.zsasko.rawg.ui.game_details.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zsasko.rawg.data.model.GameDetailsResponse

private val METACRITIC_COLOR_RANGE_75_100 = Color(0xFF4CAF50)
private val METACRITIC_COLOR_RANGE_50_74 = Color(0xFFFFEB3B)
private val METACRITIC_COLOR_DEFAULT = Color(0xFFF44336)

@Composable
fun GameDetailsRatingMetacriticSection(
    game: GameDetailsResponse,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        game.rating?.let { rating ->
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "%.1f".format(rating),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                game.ratingsCount?.let { count ->
                    Text(
                        text = "$count ratings",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        game.metacritic?.let { metacritic ->
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(metacriticColor(metacritic).copy(alpha = 0.2f))
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "$metacritic",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = metacriticColor(metacritic)
                )
                Text(
                    text = "Metacritic",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun metacriticColor(score: Int?): Color {
    return when (score) {
        in 75..100 -> METACRITIC_COLOR_RANGE_75_100
        in 50..74 -> METACRITIC_COLOR_RANGE_50_74
        else -> METACRITIC_COLOR_DEFAULT
    }
}

@Preview
@Composable
private fun GameDetailsRatingMetacriticSectionPreview() {
    GameDetailsRatingMetacriticSection(GameDetailsResponse.createMinimalMock(1, "test"))
}
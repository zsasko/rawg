package com.zsasko.rawg.ui.game_details.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.zsasko.rawg.R

private val IMAGE_HEIGHT = 250.dp

@Composable
fun GameDetailsHeaderImageView(
    imageUrl: String?,
    modifier: Modifier = Modifier
) {
    SubcomposeAsyncImage(
        model = imageUrl,
        contentDescription = stringResource(R.string.general_game_header_image_content_description),
        modifier = modifier
            .fillMaxWidth()
            .height(IMAGE_HEIGHT),
        contentScale = ContentScale.Crop,
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        },
        error = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_placeholder),
                    contentDescription = stringResource(R.string.general_failed_load_image_content_description),
                    contentScale = ContentScale.Crop,
                )
            }
        }
    )
}

@Preview
@Composable
private fun GameDetailsHeaderImageViewPreview() {
    GameDetailsHeaderImageView("https://placehold.co/600x400/EEE/31343C")
}
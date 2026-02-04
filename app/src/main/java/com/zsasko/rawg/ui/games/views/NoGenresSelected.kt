package com.zsasko.rawg.ui.games.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zsasko.rawg.R

@Composable
fun NoGenresSelected() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            stringResource(R.string.general_no_selected_genres),
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Preview
@Composable
private fun NoGenresSelectedPreview() {
    NoGenresSelected()
}
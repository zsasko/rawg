package com.zsasko.rawg.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zsasko.rawg.R

@Composable
fun ErrorLayout(
    modifier: Modifier = Modifier,
    errorText: String,
    onReloadDataButtonClicked: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                modifier = Modifier
                    .padding(bottom = 10.dp),
                onClick = onReloadDataButtonClicked
            ) {
                Text(
                    stringResource(R.string.error_screen_reload_data_button),
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Text(
                errorText,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
@Preview
fun ErrorLayoutPreview() {
    ErrorLayout(errorText = "Some error occurred", onReloadDataButtonClicked = {})
}

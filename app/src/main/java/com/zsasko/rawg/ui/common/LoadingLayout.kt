package com.zsasko.rawg.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.zsasko.rawg.ui.common.theme.RAWGTheme

@Composable
fun LoadingLayout(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.testTag("loadingIndicator"))
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingLayoutPreview() {
    RAWGTheme {
        LoadingLayout()
    }
}
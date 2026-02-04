package com.zsasko.rawg.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zsasko.rawg.R

@Composable
fun AppDrawerLogo() {
    Box(
        modifier = Modifier
            .height(190.dp)
            .fillMaxWidth(),
    ) {
        Image(
            painter = painterResource(R.drawable.ic_rawg_colorful),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

@Preview
@Composable
private fun AppDrawerLogoPreview() {
    AppDrawerLogo()
}
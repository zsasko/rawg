package com.zsasko.rawg.ui.settings.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zsasko.rawg.BuildConfig
import com.zsasko.rawg.R

@Composable
fun SettingsVersionCodeLabel() {
    Text(
        stringResource(
            R.string.settings_app_version_format,
            BuildConfig.VERSION_NAME
        ),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun SettingsVersionCodeLabelPreview() {
    SettingsVersionCodeLabel()
}
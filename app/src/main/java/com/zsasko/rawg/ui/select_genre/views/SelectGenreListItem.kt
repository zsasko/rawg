package com.zsasko.rawg.ui.select_genre.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zsasko.rawg.R
import com.zsasko.rawg.data.model.GenreResponseItem
import com.zsasko.rawg.data.model.GenreResponseItemChecked


@Composable
fun SelectGenreListItem(
    genre: GenreResponseItemChecked,
    onItemChecked: (GenreResponseItemChecked) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = modifier.toggleable(
                value = genre.isChecked,
                onValueChange = { onItemChecked(genre) },
            ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = genre.data.name,
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f),
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(Modifier.width(16.dp))
            SelectGenreButton(selected = genre.isChecked)
        }
        HorizontalDivider(
            modifier = modifier.padding(top = 8.dp, bottom = 8.dp),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
        )
    }
}

@Composable
fun SelectGenreButton(modifier: Modifier = Modifier, selected: Boolean = false) {
    val icon =
        if (selected) painterResource(R.drawable.ic_check) else painterResource(R.drawable.ic_add)
    val iconColor = if (selected) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.primary
    }
    val borderColor = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
    }
    val backgroundColor = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onPrimary
    }
    Surface(
        color = backgroundColor,
        shape = CircleShape,
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier.size(36.dp, 36.dp),
    ) {
        Image(
            painter = icon,
            colorFilter = ColorFilter.tint(iconColor),
            modifier = Modifier.padding(8.dp),
            contentDescription = stringResource(R.string.select_genres_toggle_selection),
        )
    }
}


@Preview(showBackground = true)
@Composable
fun SelectGenreListItemPreview() {
    val mockedGenreResponseItem = GenreResponseItem.makeMock(1, "Action")
    val mockedGenreChecked =
        GenreResponseItemChecked(data = mockedGenreResponseItem, isChecked = false)
    SelectGenreListItem(mockedGenreChecked, {})
}
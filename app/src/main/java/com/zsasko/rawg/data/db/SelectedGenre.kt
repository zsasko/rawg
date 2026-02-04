package com.zsasko.rawg.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "selected_genre")
data class SelectedGenre(
    // @PrimaryKey val uid: Int,
    @PrimaryKey
    @ColumnInfo(name = "genre_id") val genreId: Int
)
package com.zsasko.rawg.data.db

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [SelectedGenre::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun selectedGenreDao(): SelectedGenreDao
}
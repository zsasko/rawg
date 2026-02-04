package com.zsasko.rawg.di

import android.content.Context
import androidx.room.Room
import com.zsasko.rawg.common.DB_NAME
import com.zsasko.rawg.data.db.AppDatabase
import com.zsasko.rawg.data.db.SelectedGenreDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DB_NAME
        ).build()

    @Provides
    fun provideMovieDao(db: AppDatabase): SelectedGenreDao =
        db.selectedGenreDao()
}
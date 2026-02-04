package com.zsasko.rawg.di

import com.zsasko.rawg.api.DawgApi
import com.zsasko.rawg.data.db.SelectedGenreDao
import com.zsasko.rawg.data.repository.GameRepositoryImpl
import com.zsasko.rawg.data.repository.GenreRepositoryImpl
import com.zsasko.rawg.domain.repository.GameRepository
import com.zsasko.rawg.domain.repository.GenreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideGenreRepository(
        apiService: DawgApi,
        selectedGenreDao: SelectedGenreDao,
        @Named("Dispatcher_IO") dispatcher: CoroutineDispatcher
    ): GenreRepository =
        GenreRepositoryImpl(selectedGenreDao, apiService, dispatcher)

    @Provides
    @Singleton
    fun provideGameRepository(
        apiService: DawgApi,
        @Named("Dispatcher_IO") dispatcher: CoroutineDispatcher
    ): GameRepository =
        GameRepositoryImpl(apiService, dispatcher)

}
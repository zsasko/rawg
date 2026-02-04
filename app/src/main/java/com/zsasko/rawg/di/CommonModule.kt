package com.zsasko.rawg.di

import com.zsasko.rawg.common.analytics.AnalyticsHelper
import com.zsasko.rawg.common.analytics.CustomAnalyticsHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CommonModule {

    @Provides
    @Singleton
    @Named("Dispatcher_IO")
    fun provideDispatcherIO(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideAnalyticsHelper(): AnalyticsHelper =
        CustomAnalyticsHelper()

}
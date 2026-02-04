package com.zsasko.rawg.di

import com.zsasko.rawg.data.services.ConfigurationService
import com.zsasko.rawg.data.services.ConfigurationServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {

    @Binds
    abstract fun provideConfigurationService(impl: ConfigurationServiceImpl): ConfigurationService

}
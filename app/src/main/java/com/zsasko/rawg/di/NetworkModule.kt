package com.zsasko.rawg.di

import com.google.gson.Gson
import com.zsasko.rawg.api.DawgApi
import com.zsasko.rawg.common.RAWG_BASE_URL
import com.zsasko.rawg.common.network.ApiKeyInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory =
        GsonConverterFactory.create(gson)

    @Provides
    @Singleton
    fun provideApiKeyInterceptor(): ApiKeyInterceptor = ApiKeyInterceptor()

    @Provides
    @Singleton
    fun provideOkHttpClient(apiKeyInterceptor: ApiKeyInterceptor): OkHttpClient {
        val okHttpBuilder = OkHttpClient.Builder()
        okHttpBuilder.apply {
            addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            addInterceptor(apiKeyInterceptor)
        }
        return okHttpBuilder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gsonConverter: GsonConverterFactory): Retrofit =
        Retrofit.Builder()
            .baseUrl(RAWG_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverter)
            .build()

    @Provides
    @Singleton
    fun provideDawgApiService(retrofit: Retrofit): DawgApi = retrofit.create<DawgApi>()

}
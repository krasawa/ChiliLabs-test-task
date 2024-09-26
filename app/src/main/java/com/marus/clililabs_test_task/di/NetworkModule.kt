package com.marus.clililabs_test_task.di

import com.marus.clililabs_test_task.data.api.service.GiphyApi
import com.marus.clililabs_test_task.data.repository.GiphyRepository
import com.marus.clililabs_test_task.data.repository.RemoteGiphyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.giphy.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideGiphyApi(retrofit: Retrofit): GiphyApi {
        return retrofit.create(GiphyApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGiphyRepository(api: GiphyApi): GiphyRepository {
        return RemoteGiphyRepository(api)
    }
}
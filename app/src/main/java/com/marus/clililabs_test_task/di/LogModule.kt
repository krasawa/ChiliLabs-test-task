package com.marus.clililabs_test_task.di

import com.marus.clililabs_test_task.util.DefaultLogger
import com.marus.clililabs_test_task.util.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LogModule {
    @Provides
    @Singleton
    fun provideLogger(): Logger {
        return DefaultLogger()
    }
}
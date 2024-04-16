package com.gp.geminitravelplanner.di.module

import com.gp.geminitravelplanner.network.interceptors.ApiKeyInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiKeyInterceptorModule {
    @Provides
    @Singleton
    fun provideApiKeyInterceptor() = ApiKeyInterceptor()
}
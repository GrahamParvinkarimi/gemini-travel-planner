package com.gp.geminitravelplanner.di.module

import com.google.ai.client.generativeai.GenerativeModel
import com.gp.geminitravelplanner.BuildConfig
import com.gp.geminitravelplanner.di.util.NetworkConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class GoogleGeminiNetworkModule {

    @Provides
    @Singleton
    fun provideGenerativeModel(): GenerativeModel {
        return GenerativeModel(
            modelName = NetworkConstants.GEMINI_PRO_MODEL_NAME,
            apiKey = BuildConfig.geminiAPIKey
        )
    }

}
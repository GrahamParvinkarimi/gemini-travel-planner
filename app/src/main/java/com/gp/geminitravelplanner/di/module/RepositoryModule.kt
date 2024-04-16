package com.gp.geminitravelplanner.di.module

import com.gp.itinerary_planner.data.repository.contract.GenerativeAIRepository
import com.gp.itinerary_planner.data.repository.implementation.GeoApifyLocationRepository
import com.gp.itinerary_planner.data.repository.contract.LocationRepository
import com.gp.itinerary_planner.data.repository.implementation.GeminiGenerativeAIRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    internal abstract fun provideLocationRepository(
        geoApifyLocationRepository: GeoApifyLocationRepository
    ): LocationRepository

    @Binds
    internal abstract fun provideGenerativeAIRepository(
        geminiGenerativeAIRepository: GeminiGenerativeAIRepository,
    ): GenerativeAIRepository

}
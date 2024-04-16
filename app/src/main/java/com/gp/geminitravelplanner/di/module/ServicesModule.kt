package com.gp.geminitravelplanner.di.module

import com.gp.itinerary_planner.data.source.network.service.GeoApifyService
import com.gp.itinerary_planner.data.source.network.service.GeoApifyServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServicesModule {
    @Binds
    abstract fun bindsGeoApifyService(geoApifyServiceImpl: GeoApifyServiceImpl): GeoApifyService
}
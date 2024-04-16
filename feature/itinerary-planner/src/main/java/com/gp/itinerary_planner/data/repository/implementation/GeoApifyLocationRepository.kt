package com.gp.itinerary_planner.data.repository.implementation

import com.gp.itinerary_planner.data.repository.contract.LocationRepository
import com.gp.itinerary_planner.domain.model.LocationResult
import com.gp.itinerary_planner.data.source.network.service.GeoApifyService
import com.gp.itinerary_planner.domain.model.NetworkResult
import javax.inject.Inject

class GeoApifyLocationRepository @Inject constructor(private val geoApifyService: GeoApifyService) :
    LocationRepository {
    override suspend fun geoApifySearch(text: String): NetworkResult<LocationResult> {
        return geoApifyService.search(text)
    }
}
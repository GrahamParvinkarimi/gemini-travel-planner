package com.gp.itinerary_planner.data.source.network.service

import com.gp.itinerary_planner.domain.model.LocationResult
import com.gp.itinerary_planner.domain.model.NetworkResult

interface GeoApifyService {
    suspend fun search(text: String): NetworkResult<LocationResult>
}
package com.gp.itinerary_planner.data.repository.contract

import com.gp.itinerary_planner.domain.model.LocationResult
import com.gp.itinerary_planner.domain.model.NetworkResult

interface LocationRepository {
    suspend fun geoApifySearch(text: String): NetworkResult<LocationResult>
}
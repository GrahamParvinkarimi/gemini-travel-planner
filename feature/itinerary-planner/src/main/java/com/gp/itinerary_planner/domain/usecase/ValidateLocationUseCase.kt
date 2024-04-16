package com.gp.itinerary_planner.domain.usecase

import com.gp.itinerary_planner.data.repository.contract.LocationRepository
import com.gp.itinerary_planner.domain.model.LocationResult
import com.gp.itinerary_planner.domain.model.NetworkResult
import javax.inject.Inject

class ValidateLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(enteredLocationText: String): NetworkResult<LocationResult> {
        return locationRepository.geoApifySearch(text = enteredLocationText)
    }
}
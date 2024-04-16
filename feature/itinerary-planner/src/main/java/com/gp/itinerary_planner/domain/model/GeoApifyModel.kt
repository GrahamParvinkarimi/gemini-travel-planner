package com.gp.itinerary_planner.domain.model

data class LocationResult(
    val foundMatch: Boolean,
    val country: String?,
    val city: String?
)
package com.gp.itinerary_planner.util

object ItineraryPlannerScreenUtils {
    @JvmStatic
    fun isPlannerButtonEnabled(location: String, days: String): Boolean {
        return location.isNotEmpty() && days.isNotEmpty()
    }

    @JvmStatic
    fun getGenerativePrompt(location: String, numberOfDays: String): String {
        return "Please provide a travel itinerary for $location for $numberOfDays."
    }
}
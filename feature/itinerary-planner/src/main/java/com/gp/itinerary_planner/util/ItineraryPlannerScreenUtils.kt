package com.gp.itinerary_planner.util

object ItineraryPlannerScreenUtils {
    @JvmStatic
    fun isPlannerButtonEnabled(city: String, country: String, days: String): Boolean {
        return days.isNotEmpty() && city.isNotEmpty() && country.isNotEmpty()
    }

    @JvmStatic
    fun getCombinedCityAndCountryString(city: String, country: String): String {
        return "$country, $city"
    }

    @JvmStatic
    fun getGenerativePrompt(city: String, country: String, numberOfDays: String): String {
        return "Please provide a travel itinerary for $country, $city for $numberOfDays."
    }
}
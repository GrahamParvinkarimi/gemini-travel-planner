package com.gp.itinerary_planner.data.mappers

import com.gp.itinerary_planner.domain.model.LocationResult
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeoApifySearchResponse(
    val results: List<GeoApifySearchLocationResult>
)

@JsonClass(generateAdapter = true)
data class GeoApifySearchLocationResult(
    val name: String?,
    val country: String?,
    @Json(name = "country_code") val countryCode: String?,
    @Json(name = "result_type") val resultType: String?,
    val state: String?,
    val county: String?,
    val city: String?,
    val rank: Rank?
)

@JsonClass(generateAdapter = true)
data class Rank(
    val importance: Double?,
    val popularity: Double?,
    val confidence: Double?,
    @Json(name = "confidence_city_level") val confidenceCityLevel: Double?,
    val match_type: String?
)

enum class MatchType(val value: String) {
    FULL_MATCH("full_match"),
    INNER_PART("inner_part"),
    MATCH_BY_BUILDING("match_by_building"),
    MATCH_BY_STREET("match_by_street"),
    MATCH_BY_POSTCODE("match_by_postcode"),
    MATCH_BY_CITY_OR_DISTRICT("match_by_city_or_district"),
    MATCH_BY_COUNTRY_OR_STATE("match_by_country_or_state")
}

enum class ResultType(val value: String) {
    COUNTRY("country"),
    CITY("city"),
    COUNTY("county"),
    STATE("state")
}

fun GeoApifySearchResponse.mapToDomain(): LocationResult {
    //Check for a match of a count, city, county, or state to avoid false positives (like amenity matches)
    val fullMatch =
        results.firstOrNull { ResultType.entries.any { typeValue -> typeValue.value == it.resultType } }

    return LocationResult(
        foundMatch = fullMatch != null,
        country = fullMatch?.country,
        city = fullMatch?.city
    )
}
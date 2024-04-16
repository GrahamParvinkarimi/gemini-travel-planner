package com.gp.itinerary_planner.data.source.network.service

import com.gp.itinerary_planner.data.mappers.GeoApifySearchResponse
import com.gp.itinerary_planner.data.mappers.mapToDomain
import com.gp.itinerary_planner.domain.model.LocationResult
import com.gp.itinerary_planner.domain.model.NetworkResult
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

class GeoApifyServiceImpl @Inject constructor(
    retrofit: Retrofit
): GeoApifyService {

    private val api = retrofit.create<GeoApifyApi>()

    interface GeoApifyApi {
        @GET("v1/geocode/search")
        suspend fun search(
            @Query("text") text: String,
            @Query("format") format: String = "json"
        ): Response<GeoApifySearchResponse>
    }

    override suspend fun search(text: String): NetworkResult<LocationResult> {
        val response = api.search(text)

        return if (response.isSuccessful && response.body() != null) {
            NetworkResult.Success(data = response.body()!!.mapToDomain())
        } else {
            NetworkResult.Error(errorMessage = response.message())
        }
    }

}
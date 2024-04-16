package com.gp.itinerary_planner.domain.model

/*
    TODO: Extract this to a core/domain module if the project grows
 */
sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error(val errorMessage: String?) : NetworkResult<Nothing>()
}
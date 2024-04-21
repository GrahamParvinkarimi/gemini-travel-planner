@file:OptIn(ExperimentalMaterial3Api::class)

package com.gp.itinerary_planner.util

import androidx.compose.material3.CalendarLocale
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api

object ItineraryPlannerScreenUtils {
    @JvmStatic
    fun isPlannerButtonEnabled(location: String, days: String): Boolean {
        return location.isNotEmpty() && days.isNotEmpty()
    }

    @JvmStatic
    fun getGenerativePrompt(location: String, dateRange: String): String {
        return "Please provide a travel itinerary for $location for the dates of $dateRange"
    }

    @JvmStatic
    fun getFormattedDateString(dateRangePickerState: DateRangePickerState, datePickerFormatter: DatePickerFormatter): String {
        return datePickerFormatter.formatDate(
            dateMillis = dateRangePickerState.selectedStartDateMillis,
            locale = CalendarLocale.getDefault()
        ) + " - " + datePickerFormatter.formatDate(
            dateMillis = dateRangePickerState.selectedEndDateMillis,
            locale = CalendarLocale.getDefault()
        )
    }

    @JvmStatic
    fun startAndEndDateSelected(dateRangePickerState: DateRangePickerState): Boolean {
        return dateRangePickerState.selectedStartDateMillis != null && dateRangePickerState.selectedEndDateMillis != null
    }
}
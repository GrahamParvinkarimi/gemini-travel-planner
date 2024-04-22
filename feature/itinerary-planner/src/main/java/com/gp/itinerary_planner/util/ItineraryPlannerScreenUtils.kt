@file:OptIn(ExperimentalMaterial3Api::class)

package com.gp.itinerary_planner.util

import androidx.compose.material3.CalendarLocale
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight

object ItineraryPlannerScreenUtils {
    private const val geminiItineraryResponseRegex = """\*\*(Day|Hotel|Additional|Accommodation|Transportation|Tips).+\*\*"""

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

    @JvmStatic
    fun buildItineraryAnnotatedString(outputText: String): AnnotatedString {
        val headers = Regex(geminiItineraryResponseRegex).findAll(outputText)

        //Find all of the headers (regex checks for the "**" pattern that Gemini uses & some hardcoded key words) and apply a bold style to each match found
        return buildAnnotatedString {
            append(outputText)
            for (header in headers) {
                val currentMatch = header.groupValues[0]
                val startIndex = outputText.indexOf(header.groupValues[0])
                val endIndex = startIndex + currentMatch.length
                addStyle(style = SpanStyle(fontWeight = FontWeight.Bold), start = startIndex, end = endIndex)
            }
        }
    }
}
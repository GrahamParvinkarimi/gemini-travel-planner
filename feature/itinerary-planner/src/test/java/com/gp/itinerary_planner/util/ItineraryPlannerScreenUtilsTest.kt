@file:OptIn(ExperimentalMaterial3Api::class)

package com.gp.itinerary_planner.util

import androidx.compose.material3.CalendarLocale
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.mock
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.whenever

@RunWith(JUnit4::class)
class ItineraryPlannerScreenUtilsTest {

    @Test
    fun `isPlannerButtonEnabled returns true when location and days are not empty`() {
        val location = "New York City"
        val days = "5"

        val result = ItineraryPlannerScreenUtils.isPlannerButtonEnabled(location, days)

        Assert.assertTrue(result)
    }

    @Test
    fun `isPlannerButtonEnabled returns false when location is empty`() {
        val location = ""
        val days = "5"

        val result = ItineraryPlannerScreenUtils.isPlannerButtonEnabled(location, days)

        Assert.assertFalse(result)
    }

    @Test
    fun `isPlannerButtonEnabled returns false when days are empty`() {
        val location = "New York City"
        val days = ""

        val result = ItineraryPlannerScreenUtils.isPlannerButtonEnabled(location, days)

        Assert.assertFalse(result)
    }

    @Test
    fun `getGenerativePrompt returns the correct prompt`() {
        val location = "New York City"
        val dateRange = "July 10th - July 15th"

        val result = ItineraryPlannerScreenUtils.getGenerativePrompt(location, dateRange)

        Assert.assertEquals(
            result,
            "Please provide a travel itinerary for $location for the dates of $dateRange"
        )
    }

    @Test
    fun `getFormattedDateString returns the correct formatted date string`() {
        val dateRangePickerState = mock<DateRangePickerState>()
        whenever(dateRangePickerState.selectedStartDateMillis).thenReturn(1657516800000)
        whenever(dateRangePickerState.selectedEndDateMillis).thenReturn(1658121600000)

        val datePickerFormatter = mock<DatePickerFormatter>()

        whenever(
            datePickerFormatter.formatDate(
                eq(1657516800000),
                anyOrNull(),
                anyOrNull()
            )
        ).thenReturn("July 10th")
        whenever(
            datePickerFormatter.formatDate(
                eq(1658121600000),
                anyOrNull(),
                anyOrNull()
            )
        ).thenReturn("July 15th")

        val result = ItineraryPlannerScreenUtils.getFormattedDateString(
            dateRangePickerState,
            datePickerFormatter
        )

        Assert.assertEquals(result, "July 10th - July 15th")
    }

    @Test
    fun `startAndEndDateSelected returns true when both start and end dates are selected`() {
        val dateRangePickerState = DateRangePickerState(
            initialSelectedStartDateMillis = 1657516800000,
            initialSelectedEndDateMillis = 1658121600000,
            locale = CalendarLocale.getDefault()
        )

        val result = ItineraryPlannerScreenUtils.startAndEndDateSelected(dateRangePickerState)

        Assert.assertTrue(result)
    }

    @Test
    fun `startAndEndDateSelected returns false when start date is not selected`() {
        val dateRangePickerState = mock<DateRangePickerState>()
        whenever(dateRangePickerState.selectedStartDateMillis).thenReturn(null)
        whenever(dateRangePickerState.selectedEndDateMillis).thenReturn(1658121600000)

        val result = ItineraryPlannerScreenUtils.startAndEndDateSelected(dateRangePickerState)

        Assert.assertFalse(result)
    }

    @Test
    fun `startAndEndDateSelected returns false when end date is not selected`() {
        val dateRangePickerState = mock<DateRangePickerState>()
        whenever(dateRangePickerState.selectedStartDateMillis).thenReturn(1657516800000)
        whenever(dateRangePickerState.selectedEndDateMillis).thenReturn(null)

        val result = ItineraryPlannerScreenUtils.startAndEndDateSelected(dateRangePickerState)

        Assert.assertFalse(result)
    }

    @Test
    fun `buildItineraryAnnotatedString returns an AnnotatedString with bold headers`() {
        val outputText = """
            **Day 1:**
            Visit the Statue of Liberty and Ellis Island.
            **Hotel:** The Ritz-Carlton NewYork, Central Park.
            **Additional:**
            Take a walk through Central Park.
        """.trimIndent()

        val result = ItineraryPlannerScreenUtils.buildItineraryAnnotatedString(outputText)

        Assert.assertEquals(result.toString(), outputText)

        val spans = result.spanStyles

        Assert.assertEquals(spans.size, 3)
    }
}
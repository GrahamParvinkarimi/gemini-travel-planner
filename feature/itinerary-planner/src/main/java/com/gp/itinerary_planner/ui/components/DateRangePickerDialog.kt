@file:OptIn(ExperimentalMaterial3Api::class)

package com.gp.itinerary_planner.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CalendarLocale
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gp.itinerary_planner.R
import com.gp.itinerary_planner.ui.constants.Dimens
import com.gp.itinerary_planner.util.ItineraryPlannerScreenUtils

@Composable
fun DateRangePickerDialog(
    showDatePicker: MutableState<Boolean>,
    dateRangeString: MutableState<String>,
    dateRangePickerState: DateRangePickerState
) {
    val dateFormatter = remember { DatePickerDefaults.dateFormatter() }

    DatePickerDialog(
        onDismissRequest = { showDatePicker.value = false },
        confirmButton = {
            TextButton(
                onClick = {
                    showDatePicker.value = false
                    dateRangeString.value = ItineraryPlannerScreenUtils.getFormattedDateString(
                        dateRangePickerState = dateRangePickerState,
                        datePickerFormatter = dateFormatter
                    )
                },
                enabled = ItineraryPlannerScreenUtils.startAndEndDateSelected(
                    dateRangePickerState
                )
            ) {
                Text(stringResource(id = R.string.ok_modal_text))
            }
        },
        dismissButton = {
            TextButton(
                onClick = { showDatePicker.value = false }
            ) {
                Text(stringResource(id = R.string.cancel_modal_text))
            }
        }
    ) {
        Spacer(modifier = Modifier.padding(10.dp))

        DateRangePicker(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            state = dateRangePickerState,
            title = {
                Text(
                    text = stringResource(id = R.string.date_picker_title_text),
                    modifier = Modifier
                        .padding(Dimens.padding_16)
                )
            },
            headline = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = Dimens.padding_16,
                            bottom = Dimens.padding_8
                        )
                ) {
                    HeadlineDateSection(
                        dateValue = dateRangePickerState.selectedStartDateMillis,
                        placeholderText = stringResource(id = R.string.date_picker_start_date_text),
                        dateFormatter = dateFormatter
                    )
                    HeadlineDateSection(
                        dateValue = dateRangePickerState.selectedEndDateMillis,
                        placeholderText = stringResource(id = R.string.date_picker_end_date_text),
                        dateFormatter = dateFormatter
                    )
                }
            },
        )
    }
}

@Composable
fun RowScope.HeadlineDateSection(
    dateValue: Long?,
    placeholderText: String,
    dateFormatter: DatePickerFormatter
) {
    Box(Modifier.weight(1f)) {
        val dateText = if (dateValue != null) {
            val formattedDate = dateFormatter.formatDate(
                dateMillis = dateValue,
                locale = CalendarLocale.getDefault()
            )
            "$formattedDate"
        } else {
            placeholderText
        }

        Text(text = dateText, style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp))
    }
}
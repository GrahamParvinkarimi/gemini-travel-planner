@file:OptIn(ExperimentalMaterial3Api::class)

package com.gp.itinerary_planner.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.gp.itinerary_planner.R
import com.gp.itinerary_planner.ui.constants.Dimens
import com.gp.itinerary_planner.util.ItineraryPlannerScreenUtils
import com.gp.itinerary_planner.viewstate.UiState

@Composable
fun PlanYourTripCard(
    uiState: UiState,
    location: MutableState<String>,
    dateRangeString: MutableState<String>,
    showBottomSheet: MutableState<Boolean>,
    sendPrompt: (String, String) -> Unit
) {
    val showDatePicker = remember { mutableStateOf(false) }
    val dateRangePickerState = rememberDateRangePickerState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = Dimens.tripCardHeight,
                start = Dimens.standardPadding,
                end = Dimens.standardPadding
            ),
        verticalArrangement = Arrangement.spacedBy(Dimens.standardPadding),
    ) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = Dimens.elevatedCardElevation
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFFBFE)
            ),
        ) {
            Column(
                modifier = Modifier
                    .padding(Dimens.standardPadding)
                    .fillMaxWidth(),
            ) {
                //Plan Your Trip title
                Text(
                    text = stringResource(R.string.plan_your_trip_title),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(Dimens.standardPadding))

                //Row for location
                TextFieldRow(label = stringResource(R.string.label_destination),
                    value = location.value,
                    onValueChange = { location.value = it },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = stringResource(id = R.string.search_icon_content_description)
                        )
                    })
                Spacer(modifier = Modifier.height(Dimens.standardPadding))

                //Row for to select start & end date of the trip
                DateTextFieldRow(
                    label = stringResource(R.string.label_date_range),
                    placeholder = stringResource(id = R.string.placeholder_date_range),
                    value = dateRangeString.value,
                    onValueChange = { },
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker.value = true }) {
                            Icon(
                                imageVector = Icons.Filled.CalendarMonth,
                                contentDescription = stringResource(id = R.string.date_button_content_description)
                            )
                        }
                    },
                )

                if (showDatePicker.value) {
                    DateRangePickerDialog(
                        showDatePicker = showDatePicker,
                        dateRangeString = dateRangeString,
                        dateRangePickerState = dateRangePickerState
                    )
                }

                Spacer(modifier = Modifier.height(Dimens.standardPadding))

                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    if (uiState is UiState.Loading) {
                        CircularProgressIndicator()
                    } else {
                        //Button to create itinerary
                        Button(
                            onClick = {
                                showBottomSheet.value = true
                                sendPrompt(
                                    location.value, dateRangeString.value
                                )
                            }, enabled = ItineraryPlannerScreenUtils.isPlannerButtonEnabled(
                                location = location.value, days = dateRangeString.value
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = stringResource(R.string.create_itinerary_button_text))
                        }
                    }
                }
            }
        }
    }
}
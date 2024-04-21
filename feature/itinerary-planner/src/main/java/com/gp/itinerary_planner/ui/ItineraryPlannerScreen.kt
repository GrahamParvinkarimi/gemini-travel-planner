@file:OptIn(ExperimentalMaterial3Api::class)

package com.gp.itinerary_planner.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CalendarLocale
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gp.itinerary_planner.R
import com.gp.itinerary_planner.ui.constants.Dimens
import com.gp.itinerary_planner.util.ItineraryPlannerScreenUtils
import com.gp.itinerary_planner.viewstate.UiState
import com.gp.itinerary_planner.vm.ItineraryPlannerViewModel
import kotlinx.coroutines.launch

@Composable
fun ItineraryPlannerRoute(
    itineraryPlannerViewModel: ItineraryPlannerViewModel = viewModel()
) {
    val uiState by itineraryPlannerViewModel.uiState.collectAsState()

    ItineraryPlannerScreen(
        uiState = uiState, sendPrompt = itineraryPlannerViewModel::sendPrompt
    )
}

@Composable
fun ItineraryPlannerScreen(
    uiState: UiState, sendPrompt: (String, String) -> Unit
) {
    val location = remember { mutableStateOf("") }
    val dateRangeString = remember { mutableStateOf("") }

    val showBottomSheet = remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.plane_travel_header_artwork),
            contentDescription = null
        )

        PlanYourTripCard(
            uiState = uiState,
            location = location,
            dateRangeString = dateRangeString,
            showBottomSheet = showBottomSheet,
            sendPrompt = sendPrompt
        )

        if (uiState is UiState.Success) {
            ItineraryBottomSheet(
                outputText = uiState.outputText,
                showBottomSheet = showBottomSheet,
                dateRangeString = dateRangeString.value
            )
        }
    }

}

@Composable
fun ItineraryBottomSheet(
    outputText: String,
    showBottomSheet: MutableState<Boolean>,
    dateRangeString: String
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    if (outputText.isNotEmpty() && showBottomSheet.value) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet.value = false
            },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        start = Dimens.standardPadding,
                        end = Dimens.standardPadding,
                        bottom = Dimens.standardPadding
                    )
                    .align(Alignment.CenterHorizontally)
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.align(Alignment.End)
                ) {
                    IconButton(onClick = {
                        clipboardManager.setText(AnnotatedString((outputText)))
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ContentCopy,
                            contentDescription = null,
                        )
                    }
                    IconButton(onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet.value = false
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = null,
                        )
                    }
                }
                Text(
                    text = stringResource(id = R.string.itinerary_title, dateRangeString),
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(Dimens.smallPadding))
                Text(
                    text = outputText, color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

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
                    label = stringResource(R.string.label_date),
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
                        .padding(Dimens.standardPadding)
                )
            },
            headline = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = Dimens.standardPadding,
                            bottom = Dimens.smallPadding
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

@Composable
fun TextFieldRow(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable (() -> Unit)
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            //Indent text field below header
            .padding(horizontal = Dimens.indentPadding)
    ) {
        BasicTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            modifier = Modifier
                .height(Dimens.textFieldHeight)
                .fillMaxWidth(),
            singleLine = true,
            interactionSource = interactionSource,
            textStyle = MaterialTheme.typography.bodySmall.copy(
                textAlign = TextAlign.Start
            ),
        ) { innerTextField ->
            OutlinedTextFieldDefaults.DecorationBox(
                value = value,
                label = {
                    OutlinedTextFieldLabel(label = label)
                },
                innerTextField = innerTextField,
                singleLine = true,
                leadingIcon = if (!interactionSource.collectIsFocusedAsState().value && value.isBlank()) {
                    leadingIcon
                } else {
                    null
                },
                enabled = true,
                interactionSource = interactionSource,
                visualTransformation = VisualTransformation.None,
                container = { OutlinedTextFieldContainerBox(interactionSource) },
                contentPadding = TextFieldDefaults.contentPaddingWithLabel(
                    top = Dimens.zeroPadding,
                    bottom = Dimens.zeroPadding,
                    start = if (!interactionSource.collectIsFocusedAsState().value && value.isBlank()) {
                        Dimens.smallPadding
                    } else {
                        Dimens.standardPadding
                    }
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTextFieldRow(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    trailingIcon: @Composable (() -> Unit)
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            //Indent text field below header
            .padding(horizontal = Dimens.indentPadding)
    ) {
        BasicTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            modifier = Modifier
                .height(Dimens.textFieldHeight)
                .fillMaxWidth(),
            singleLine = true,
            readOnly = true,
            interactionSource = interactionSource,
            textStyle = MaterialTheme.typography.bodySmall.copy(
                textAlign = TextAlign.Start
            ),
        ) { innerTextField ->
            OutlinedTextFieldDefaults.DecorationBox(
                value = value,
                label = {
                    OutlinedTextFieldLabel(label = label)
                },
                innerTextField = innerTextField,
                singleLine = true,
                trailingIcon = trailingIcon,
                enabled = true,
                interactionSource = interactionSource,
                visualTransformation = VisualTransformation.None,
                container = { OutlinedTextFieldContainerBox(interactionSource) },
                contentPadding = TextFieldDefaults.contentPaddingWithLabel(
                    top = Dimens.zeroPadding,
                    bottom = Dimens.zeroPadding,
                    start = Dimens.standardPadding
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedTextFieldContainerBox(interactionSource: MutableInteractionSource) {
    OutlinedTextFieldDefaults.ContainerBox(
        enabled = true,
        isError = false,
        interactionSource,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.LightGray,
            focusedBorderColor = Color.DarkGray
        ),
        shape = RoundedCornerShape(Dimens.textFieldRoundedCornerSize)
    )
}

@Composable
fun OutlinedTextFieldLabel(label: String) {
    Text(
        text = label,
        style = MaterialTheme.typography.bodySmall
    )
}

@Preview
@Composable
fun ItineraryPlannerScreenPreview() {
    ItineraryPlannerScreen(UiState.Success(
        outputText = "Success"
    ), sendPrompt = { _, _ -> })
}
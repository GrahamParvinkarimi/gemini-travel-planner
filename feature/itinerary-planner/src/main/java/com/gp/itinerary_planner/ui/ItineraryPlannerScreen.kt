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
import androidx.compose.runtime.setValue
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
    val days = remember { mutableStateOf("") }

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
            days = days,
            showBottomSheet = showBottomSheet,
            sendPrompt = sendPrompt
        )

        if (uiState is UiState.Success) {
            ItineraryBottomSheet(
                outputText = uiState.outputText,
                showBottomSheet = showBottomSheet
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItineraryBottomSheet(
    outputText: String,
    showBottomSheet: MutableState<Boolean>
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
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
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
                    text = "Itinerary for [dates]",
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = outputText, color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanYourTripCard(
    uiState: UiState,
    location: MutableState<String>,
    days: MutableState<String>,
    showBottomSheet: MutableState<Boolean>,
    sendPrompt: (String, String) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val dateRangePickerState = rememberDateRangePickerState()
    val dateFormatter = remember { DatePickerDefaults.dateFormatter() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 54.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFFBFE)
            ),
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
            ) {
                //Plan Your Trip title
                Text(
                    text = stringResource(R.string.plan_your_trip_title),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(16.dp))

                //Row for location
                TextFieldRow(label = stringResource(R.string.label_destination),
                    value = location.value,
                    onValueChange = { location.value = it },
                    leadingIcon = {
                        // Show the leading icon if the value is empty
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = null
                        )
                    })
                Spacer(modifier = Modifier.height(16.dp))

                //Row for to select start & end date of the trip
                DateTextFieldRow(
                    label = stringResource(R.string.label_date),
                    value = dateFormatter.formatDate(
                        dateMillis = dateRangePickerState.selectedStartDateMillis,
                        locale = CalendarLocale.getDefault()
                    ) + " - " + dateFormatter.formatDate(
                        dateMillis = dateRangePickerState.selectedEndDateMillis,
                        locale = CalendarLocale.getDefault()
                    ),
                    onValueChange = { },
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(
                                imageVector = Icons.Filled.CalendarMonth,
                                contentDescription = "Open date picker"
                            )
                        }
                    },
                )

                if (showDatePicker) {
                    DatePickerDialog(
                        onDismissRequest = { showDatePicker = false },
                        confirmButton = {
                            TextButton(
                                onClick = { showDatePicker = false },
                                enabled = dateRangePickerState.selectedStartDateMillis != null && dateRangePickerState.selectedEndDateMillis != null
                            ) {
                                Text("OK")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = { showDatePicker = false }
                            ) {
                                Text("CANCEL")
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
                                    text = "Please choose the start and end dates for your trip",
                                    modifier = Modifier
                                        .padding(16.dp)
                                )
                            },
                            headline = {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 16.dp, bottom = 8.dp)
                                ) {
                                    HeadlineDateSection(
                                        dateValue = dateRangePickerState.selectedStartDateMillis,
                                        placeholderText = "Start Date",
                                        dateFormatter = dateFormatter
                                    )
                                    HeadlineDateSection(
                                        dateValue = dateRangePickerState.selectedEndDateMillis,
                                        placeholderText = "End Date",
                                        dateFormatter = dateFormatter
                                    )
                                }
                            },
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

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
                                    location.value, days.value
                                )
                            }, enabled = ItineraryPlannerScreenUtils.isPlannerButtonEnabled(
                                location = location.value, days = days.value
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

@OptIn(ExperimentalMaterial3Api::class)
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

@OptIn(ExperimentalMaterial3Api::class)
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
            .padding(start = 1.dp, end = 1.dp)
    ) {
        BasicTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            modifier = Modifier
                .height(42.dp)
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
                    top = 0.dp,
                    bottom = 0.dp,
                    start = if (!interactionSource.collectIsFocusedAsState().value && value.isBlank()) {
                        8.dp
                    } else {
                        16.dp
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
            .padding(start = 1.dp, end = 1.dp)
    ) {
        BasicTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            modifier = Modifier
                .height(42.dp)
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
                    top = 0.dp,
                    bottom = 0.dp,
                    start = 16.dp
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
        shape = RoundedCornerShape(24.dp)
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
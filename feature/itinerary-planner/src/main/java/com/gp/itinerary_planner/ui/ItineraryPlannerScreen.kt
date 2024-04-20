package com.gp.itinerary_planner.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gp.itinerary_planner.R
import com.gp.itinerary_planner.util.ItineraryPlannerScreenUtils
import com.gp.itinerary_planner.viewstate.UiState
import com.gp.itinerary_planner.vm.ItineraryPlannerViewModel

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
    var result by rememberSaveable { mutableStateOf("") }

    val location = remember { mutableStateOf("") }
    val days = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        Image(
            modifier = Modifier
                .height(204.dp)
                .fillMaxWidth(),
            painter = painterResource(id = R.drawable.plane_travel_header_artwork),
            contentDescription = null
        )

        PlanYourTripCard(location = location, days = days, sendPrompt = sendPrompt)

        if (uiState is UiState.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            var textColor = MaterialTheme.colorScheme.onSurface
            if (uiState is UiState.Error) {
                textColor = MaterialTheme.colorScheme.error
                result = uiState.errorMessage
            } else if (uiState is UiState.Success) {
                textColor = MaterialTheme.colorScheme.onSurface
                result = uiState.outputText
            }
            if (result.isNotEmpty())
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
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Elevated Card Title",
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = result, color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
        }

    }
}

@Composable
fun PlanYourTripCard(
    location: MutableState<String>,
    days: MutableState<String>,
    sendPrompt: (String, String) -> Unit
) {
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
                    onValueChange = { location.value = it })
                Spacer(modifier = Modifier.height(24.dp))

                //Row for number of days
                TextFieldRow(
                    label = stringResource(R.string.label_days),
                    value = days.value,
                    onValueChange = {
                        days.value = it
                    })
                Spacer(modifier = Modifier.height(16.dp))

                //Button to create itinerary
                Row {
                    Button(
                        onClick = {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldRow(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val colors = OutlinedTextFieldDefaults.colors(
        unfocusedBorderColor = Color.LightGray,
        focusedBorderColor = Color.DarkGray
    )
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
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                },
                innerTextField = innerTextField,
                singleLine = true,
                leadingIcon = if (!interactionSource.collectIsFocusedAsState().value && value.isBlank()) {
                    {
                        // Show the leading icon if the value is empty
                        Icon(
                            painter = painterResource(id = R.drawable.search_icon),
                            contentDescription = null
                        )
                    }
                } else {
                    null
                },
                enabled = true,
                interactionSource = interactionSource,
                visualTransformation = VisualTransformation.None,
                container = {
                    OutlinedTextFieldDefaults.ContainerBox(
                        enabled = true,
                        isError = false,
                        interactionSource,
                        colors = colors,
                        shape = RoundedCornerShape(24.dp)
                    )
                },
                contentPadding = TextFieldDefaults.contentPaddingWithLabel(
                    top = 0.dp,
                    bottom = 0.dp,
                    start = if (!interactionSource.collectIsFocusedAsState().value && value.isBlank()) {
                        2.dp
                    } else {
                        16.dp
                    }
                )
            )
        }
    }
}

@Preview
@Composable
fun ItineraryPlannerScreenPreview() {
    ItineraryPlannerScreen(UiState.Success(
        outputText = "Success"
    ), sendPrompt = { _, _ -> })
}
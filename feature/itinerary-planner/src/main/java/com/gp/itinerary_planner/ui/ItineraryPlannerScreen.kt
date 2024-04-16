package com.gp.itinerary_planner.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gp.itinerary_planner.R
import com.gp.itinerary_planner.util.ItineraryPlannerScreenUtils
import com.gp.itinerary_planner.viewstate.UiState
import com.gp.itinerary_planner.vm.ItineraryPlannerViewModel

@Composable
fun ItineraryPlannerScreen(
    itineraryPlannerViewModel: ItineraryPlannerViewModel = viewModel()
) {
    val placeholderResult = stringResource(R.string.results_placeholder)
    var result by rememberSaveable { mutableStateOf(placeholderResult) }
    val uiState by itineraryPlannerViewModel.uiState.collectAsState()

    var country by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var days by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.travel_planning_title),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )

        //Row for country
        TextFieldRow(
            label = stringResource(R.string.label_country),
            value = country,
            onValueChange = { country = it }
        )

        //Row for city
        TextFieldRow(
            label = stringResource(R.string.label_city),
            value = city,
            onValueChange = { city = it }

        )

        //Row for number of days
        TextFieldRow(
            label = stringResource(R.string.label_days),
            value = days,
            onValueChange = {
                days = it
            }
        )

        Row(
            modifier = Modifier.padding(all = 16.dp)
        ) {
            Button(
                onClick = {
                    itineraryPlannerViewModel.sendPrompt(
                        city = city,
                        country = country,
                        numberOfDays = days
                    )
                },
                enabled = ItineraryPlannerScreenUtils.isPlannerButtonEnabled(
                    city = city,
                    country = country,
                    days = days
                ),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            ) {
                Text(text = stringResource(R.string.action_go))
            }
        }

        if (uiState is UiState.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            var textColor = MaterialTheme.colorScheme.onSurface
            if (uiState is UiState.Error) {
                textColor = MaterialTheme.colorScheme.error
                result = (uiState as UiState.Error).errorMessage
            } else if (uiState is UiState.Success) {
                textColor = MaterialTheme.colorScheme.onSurface
                result = (uiState as UiState.Success).outputText
            }
            val scrollState = rememberScrollState()
            Text(
                text = result,
                textAlign = TextAlign.Start,
                color = textColor,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            )
        }
    }
}

@Composable
fun TextFieldRow(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
) {
    Row(
        modifier = Modifier.padding(all = 16.dp)
    ) {
        OutlinedTextField(
            value = value,
            label = { Text(label) },
            onValueChange = { onValueChange(it) },
            isError = value.isEmpty(),
            modifier = Modifier
                .align(Alignment.CenterVertically)
        )
    }
}
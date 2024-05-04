package com.gp.itinerary_planner.ui.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.gp.itinerary_planner.R
import com.gp.itinerary_planner.ui.components.ItineraryBottomSheet
import com.gp.itinerary_planner.ui.components.PlanYourTripCard
import com.gp.itinerary_planner.ui.components.QuickSelectCardSection
import com.gp.itinerary_planner.viewstate.UiState
import com.gp.itinerary_planner.vm.ItineraryPlannerViewModel

@Composable
fun ItineraryPlannerRoute(
    itineraryPlannerViewModel: ItineraryPlannerViewModel = hiltViewModel()
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
    val locationString = remember { mutableStateOf("") }
    val dateRangeString = remember { mutableStateOf("") }

    val showBottomSheet = remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.plane_travel_header_artwork),
            contentDescription = stringResource(id = R.string.travel_header_image_content_description)
        )

        PlanYourTripCard(
            uiState = uiState,
            location = locationString,
            dateRangeString = dateRangeString,
            showBottomSheet = showBottomSheet,
            sendPrompt = sendPrompt
        )

        QuickSelectCardSection(
            onQuickSelectCardClicked = { selectedCardLocationString ->
                locationString.value = selectedCardLocationString
            }
        )

        if (uiState is UiState.Success) {
            ItineraryBottomSheet(
                outputText = uiState.outputText,
                showBottomSheet = showBottomSheet,
                dateRangeString = dateRangeString.value,
                locationString = locationString.value
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
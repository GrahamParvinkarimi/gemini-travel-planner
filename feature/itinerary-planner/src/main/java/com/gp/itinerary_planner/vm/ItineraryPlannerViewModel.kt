package com.gp.itinerary_planner.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gp.itinerary_planner.domain.model.NetworkResult
import com.gp.itinerary_planner.domain.usecase.GenerateItineraryUseCase
import com.gp.itinerary_planner.domain.usecase.ValidateLocationUseCase
import com.gp.itinerary_planner.util.ItineraryPlannerScreenUtils
import com.gp.itinerary_planner.viewstate.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItineraryPlannerViewModel @Inject constructor(
    private val generateItineraryUseCase: GenerateItineraryUseCase,
    private val validateLocationUseCase: ValidateLocationUseCase
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(UiState.Initial)
    val uiState: StateFlow<UiState> =
        _uiState.asStateFlow()

    fun sendPrompt(
        location: String,
        dateRange: String
    ) {
        _uiState.value = UiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            val locationResult = validateLocationUseCase(
                location
            )

            if (locationResult is NetworkResult.Success) {
                if (locationResult.data.foundMatch) {
                    when (val itineraryResult = generateItineraryUseCase(
                        prompt = ItineraryPlannerScreenUtils.getGenerativePrompt(
                            location = location,
                            dateRange = dateRange
                        )
                    )) {
                        is NetworkResult.Success -> {
                            _uiState.value = UiState.Success(itineraryResult.data.text ?: "")
                        }

                        is NetworkResult.Error -> {
                            _uiState.value = UiState.Error(itineraryResult.errorMessage ?: "")
                        }
                    }
                } else {
                    //Update inline error fields based on city/state returned
                    _uiState.value = UiState.Error("Validation Error")
                }
            } else {
                _uiState.value =
                    UiState.Error((locationResult as NetworkResult.Error).errorMessage ?: "")
            }
        }
    }
}
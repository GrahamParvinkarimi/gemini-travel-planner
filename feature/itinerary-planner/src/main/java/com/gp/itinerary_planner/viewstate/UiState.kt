package com.gp.itinerary_planner.viewstate

/**
 * A sealed hierarchy describing the state of the text generation.
 * Simple UiState provided OOTB, can expand this and add a ViewState if needed.
 * This can be moved to a `core` module to share across features if the project expands.
 */
sealed interface UiState {

    /**
     * Empty state when the screen is first shown
     */
    object Initial : UiState

    /**
     * Still loading
     */
    object Loading : UiState

    /**
     * Text has been generated
     */
    data class Success(val outputText: String) : UiState

    /**
     * There was an error generating text
     */
    data class Error(val errorMessage: String? = null) : UiState
}
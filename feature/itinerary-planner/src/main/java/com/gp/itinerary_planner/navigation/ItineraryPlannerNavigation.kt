package com.gp.itinerary_planner.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gp.itinerary_planner.ui.ItineraryPlannerRoute

const val ITINERARY_LANDING_SCREEN_ROUTE = "itinerary_planner_landing_screen_route"

fun NavGraphBuilder.itineraryPlannerLandingScreen() {
    composable(
        route = ITINERARY_LANDING_SCREEN_ROUTE,
    ) {
        ItineraryPlannerRoute()
    }
}
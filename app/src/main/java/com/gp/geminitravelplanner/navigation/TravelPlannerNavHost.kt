package com.gp.geminitravelplanner.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.gp.geminitravelplanner.TravelPlannerAppState
import com.gp.itinerary_planner.navigation.ITINERARY_LANDING_SCREEN_ROUTE
import com.gp.itinerary_planner.navigation.itineraryPlannerLandingScreen

/**
 * Top-level navigation graph
 */
@Composable
fun TravelPlannerNavHost(
    appState: TravelPlannerAppState,
    startDestination: String = ITINERARY_LANDING_SCREEN_ROUTE,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        itineraryPlannerLandingScreen()
    }
}
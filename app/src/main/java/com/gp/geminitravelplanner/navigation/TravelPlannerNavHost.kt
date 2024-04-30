package com.gp.geminitravelplanner.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.gp.geminitravelplanner.TravelPlannerAppState
import com.gp.itinerary_planner.navigation.TravelPlannerScreen
import com.gp.itinerary_planner.navigation.travelPlannerNavGraph

/**
 * Top-level navigation graph
 */
@Composable
fun TravelPlannerNavHost(
    appState: TravelPlannerAppState,
    startDestination: String = TravelPlannerScreen.Landing.route,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        travelPlannerNavGraph(navController)
    }
}
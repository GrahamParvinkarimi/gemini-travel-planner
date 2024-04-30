package com.gp.itinerary_planner.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gp.itinerary_planner.ui.content.ItineraryPlannerRoute
import com.gp.itinerary_planner.ui.landing.TravelPlannerLandingRoute

sealed interface TravelPlannerScreen {
    val route: String

    data object Landing : TravelPlannerScreen {
        override val route = "/landing"
    }

    data object ItineraryPlanner : TravelPlannerScreen {
        override val route = "/itinerary-planner"
    }
}

fun NavGraphBuilder.travelPlannerNavGraph(navController: NavController) {
    composable(
        route = TravelPlannerScreen.Landing.route,
    ) {
        TravelPlannerLandingRoute(navController = navController)
    }

    composable(
        route = TravelPlannerScreen.ItineraryPlanner.route,
    ) {
        ItineraryPlannerRoute()
    }
}
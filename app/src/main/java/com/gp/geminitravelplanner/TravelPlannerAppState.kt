package com.gp.geminitravelplanner

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun rememberTravelPlannerAppState(
    navController: NavHostController = rememberNavController()
): TravelPlannerAppState {
    return remember(
        navController,
    ) {
        TravelPlannerAppState(
            navController = navController,
        )
    }
}

@Stable
class TravelPlannerAppState(
    val navController: NavHostController
)
package com.gp.geminitravelplanner.ui

import androidx.compose.runtime.Composable
import com.gp.geminitravelplanner.TravelPlannerAppState
import com.gp.geminitravelplanner.navigation.TravelPlannerNavHost

@Composable
fun TravelPlannerApp(
    appState: TravelPlannerAppState,
) {
    /*
       Scaffold isn't needed for now since a `topBar` / `bottomBar` aren't needed
       at the moment
     */
    TravelPlannerNavHost(
        appState = appState
    )
}
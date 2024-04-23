package com.gp.geminitravelplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.gp.geminitravelplanner.designsystem.components.TravelPlannerBackground
import com.gp.geminitravelplanner.ui.TravelPlannerApp
import com.gp.geminitravelplanner.ui.theme.GeminiTravelPlannerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Enable full screen layout and make the status bar transparent
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )
        setContent {
            val appState = rememberTravelPlannerAppState()

            GeminiTravelPlannerTheme {
                // A surface container using the 'background' color from the theme
                TravelPlannerBackground {
                    TravelPlannerApp(appState = appState)
                }
            }
        }
    }
}
package com.gp.itinerary_planner.ui.landing

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.gp.itinerary_planner.R
import com.gp.itinerary_planner.navigation.TravelPlannerScreen
import com.gp.itinerary_planner.ui.constants.Dimens

@Composable
fun TravelPlannerLandingRoute(navController: NavController) {
    TravelPlannerLandingScreen(
        onPlanItineraryClicked = { navController.navigate(TravelPlannerScreen.ItineraryPlanner.route) }
    )
}

@Composable
fun TravelPlannerLandingScreen(
    onPlanItineraryClicked: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Image(
            painter = painterResource(id = R.drawable.landing_screen_background),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize(),
            colorFilter = ColorFilter.tint(
                Color.Black.copy(alpha = 0.2f),
                blendMode = BlendMode.Darken
            )
        )
    }
    Column {
        Text(
            text = stringResource(id = R.string.landing_screen_title_text),
            style = MaterialTheme.typography.displayLarge,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = Dimens.padding_48,
                    start = Dimens.padding_58,
                    end = Dimens.padding_48
                )
        )

        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                onPlanItineraryClicked()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    bottom = Dimens.padding_70,
                    start = Dimens.padding_32,
                    end = Dimens.padding_32
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(text = stringResource(id = R.string.landing_screen_button_text))
        }
    }
}

@Preview
@Composable
fun TravelPlannerScreenPreview() {
    TravelPlannerLandingScreen(onPlanItineraryClicked = { })
}
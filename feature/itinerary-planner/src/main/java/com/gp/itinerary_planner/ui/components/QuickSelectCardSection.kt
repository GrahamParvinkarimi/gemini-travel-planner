package com.gp.itinerary_planner.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.gp.itinerary_planner.R
import com.gp.itinerary_planner.ui.constants.Dimens

@Composable
fun QuickSelectCardSection(
    onQuickSelectCardClicked: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(
                top = Dimens.quickSelectCardTopPadding,
                start = Dimens.padding_16,
                end = Dimens.padding_16
            ),
        verticalArrangement = Arrangement.spacedBy(Dimens.padding_16),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens.padding_16),
            modifier = Modifier.fillMaxSize()
        ) {
            DestinationQuickSelectCard(
                onQuickSelectCardClicked = onQuickSelectCardClicked,
                cardData = DestinationQuickSelectCardData(
                    title = stringResource(id = R.string.madrid_card_text),
                    prefilledText = stringResource(id = R.string.madrid_prefilled_text),
                    imageResource = R.drawable.card_image_madrid
                )
            )
            DestinationQuickSelectCard(
                onQuickSelectCardClicked = onQuickSelectCardClicked,
                cardData = DestinationQuickSelectCardData(
                    title = stringResource(id = R.string.london_card_text),
                    prefilledText = stringResource(id = R.string.london_prefilled_text),
                    imageResource = R.drawable.card_image_london
                )
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens.padding_16),
            modifier = Modifier.fillMaxSize()
        ) {
            DestinationQuickSelectCard(
                onQuickSelectCardClicked = onQuickSelectCardClicked,
                cardData = DestinationQuickSelectCardData(
                    title = stringResource(id = R.string.tokyo_card_text),
                    prefilledText = stringResource(id = R.string.tokyo_prefilled_text),
                    imageResource = R.drawable.card_image_tokyo
                )
            )
            DestinationQuickSelectCard(
                onQuickSelectCardClicked = onQuickSelectCardClicked,
                cardData = DestinationQuickSelectCardData(
                    title = stringResource(id = R.string.paris_card_text),
                    prefilledText = stringResource(id = R.string.paris_prefilled_text),
                    imageResource = R.drawable.card_image_paris
                )
            )
        }
    }
}

@Composable
fun RowScope.DestinationQuickSelectCard(
    onQuickSelectCardClicked: (String) -> Unit,
    cardData: DestinationQuickSelectCardData
) {
    Column(
        modifier = Modifier.weight(1f)
    ) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = Dimens.elevatedCardElevation
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            modifier = Modifier.clickable {
                onQuickSelectCardClicked(cardData.prefilledText)
            }
        ) {
            Column {
                Image(
                    painter = painterResource(id = cardData.imageResource),
                    contentDescription = stringResource(id = R.string.image_content_description, cardData.title),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens.cardImageHeight)
                )
                Row(modifier = Modifier.padding(Dimens.padding_16)) {
                    Text(
                        text = cardData.title,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(end = Dimens.padding_8)
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        modifier = Modifier.size(Dimens.cardArrowIconSize),
                        contentDescription = stringResource(id = R.string.arrow_content_description)
                    )
                }
            }
        }
    }
}

data class DestinationQuickSelectCardData(
    val title: String,
    val prefilledText: String,
    val imageResource: Int
)
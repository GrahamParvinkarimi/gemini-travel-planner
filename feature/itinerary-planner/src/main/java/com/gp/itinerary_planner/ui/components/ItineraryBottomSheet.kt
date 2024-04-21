@file:OptIn(ExperimentalMaterial3Api::class)

package com.gp.itinerary_planner.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import com.gp.itinerary_planner.R
import com.gp.itinerary_planner.ui.constants.Dimens
import kotlinx.coroutines.launch

@Composable
fun ItineraryBottomSheet(
    outputText: String,
    showBottomSheet: MutableState<Boolean>,
    dateRangeString: String
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    if (outputText.isNotEmpty() && showBottomSheet.value) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet.value = false
            },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        start = Dimens.standardPadding,
                        end = Dimens.standardPadding,
                        bottom = Dimens.standardPadding
                    )
                    .align(Alignment.CenterHorizontally)
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.align(Alignment.End)
                ) {
                    IconButton(onClick = {
                        clipboardManager.setText(AnnotatedString((outputText)))
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ContentCopy,
                            contentDescription = null,
                        )
                    }
                    IconButton(onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet.value = false
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = null,
                        )
                    }
                }
                Text(
                    text = stringResource(id = R.string.itinerary_title, dateRangeString),
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(Dimens.smallPadding))
                Text(
                    text = outputText, color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
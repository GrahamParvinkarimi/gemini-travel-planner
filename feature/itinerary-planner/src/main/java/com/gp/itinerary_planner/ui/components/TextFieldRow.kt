@file:OptIn(ExperimentalMaterial3Api::class)

package com.gp.itinerary_planner.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.gp.itinerary_planner.ui.constants.Dimens

@Composable
fun TextFieldRow(
    label: String,
    value: String,
    errorMessage: String?,
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable (() -> Unit)
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            //Indent text field below header
            .padding(horizontal = Dimens.indentPadding)
    ) {
        BasicTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            modifier = Modifier
                .height(Dimens.textFieldHeight)
                .fillMaxWidth(),
            singleLine = true,
            interactionSource = interactionSource,
            textStyle = MaterialTheme.typography.bodySmall.copy(
                textAlign = TextAlign.Start
            ),
        ) { innerTextField ->
            OutlinedTextFieldDefaults.DecorationBox(
                value = value,
                label = {
                    OutlinedTextFieldLabel(label = label)
                },
                innerTextField = innerTextField,
                singleLine = true,
                leadingIcon = if (!interactionSource.collectIsFocusedAsState().value && value.isBlank()) {
                    leadingIcon
                } else {
                    null
                },
                enabled = true,
                interactionSource = interactionSource,
                visualTransformation = VisualTransformation.None,
                container = { OutlinedTextFieldContainerBox(interactionSource) },
                contentPadding = TextFieldDefaults.contentPaddingWithLabel(
                    top = Dimens.zeroPadding,
                    bottom = Dimens.zeroPadding,
                    start = if (!interactionSource.collectIsFocusedAsState().value && value.isBlank()) {
                        Dimens.padding_8
                    } else {
                        Dimens.padding_16
                    }
                )
            )
        }
    }

    /**
     * Displaying an error message as a row of text below the Destination text field for now
     * (spacing works better OOTB with the custom-styled M3 text fields)
     */
    Row {
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Red,
                modifier = Modifier.padding(start = Dimens.padding_18, top = Dimens.padding_4)
            )
        }
    }
}

@Composable
fun OutlinedTextFieldContainerBox(interactionSource: MutableInteractionSource) {
    OutlinedTextFieldDefaults.ContainerBox(
        enabled = true,
        isError = false,
        interactionSource,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.LightGray,
            focusedBorderColor = Color.DarkGray
        ),
        shape = RoundedCornerShape(Dimens.textFieldRoundedCornerSize)
    )
}

@Composable
fun OutlinedTextFieldLabel(label: String) {
    Text(
        text = label,
        style = MaterialTheme.typography.bodySmall
    )
}
@file:OptIn(ExperimentalMaterial3Api::class)

package com.gp.itinerary_planner.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.gp.itinerary_planner.ui.constants.Dimens

@Composable
fun DateTextFieldRow(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    trailingIcon: @Composable (() -> Unit)
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
            readOnly = true,
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
                placeholder = {
                    OutlinedTextFieldLabel(label = placeholder)
                },
                innerTextField = innerTextField,
                singleLine = true,
                trailingIcon = trailingIcon,
                enabled = true,
                interactionSource = interactionSource,
                visualTransformation = VisualTransformation.None,
                container = { OutlinedTextFieldContainerBox(interactionSource) },
                contentPadding = TextFieldDefaults.contentPaddingWithLabel(
                    top = Dimens.zeroPadding,
                    bottom = Dimens.zeroPadding,
                    start = Dimens.standardPadding
                )
            )
        }
    }
}
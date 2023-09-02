package com.iideprived.jetpackcomposecomponents.ui.inputs.forms

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.iideprived.jetpackcomposecomponents.ui.inputs.AnimatedTextField
import com.iideprived.jetpackcomposecomponents.utils.modifier.AutofillData

@Composable
fun Field(
    modifier: Modifier = Modifier,
    name: String,
    value: String,
    autofillData: AutofillData? = null,
    onFocusChanged: (Boolean) -> Unit = {},
    onValueChanged: (String) -> Unit
) {
    var focused by remember { mutableStateOf(false) }
    AnimatedTextField(
        modifier = modifier
            .padding(vertical = 12.dp)
            .shadow(
                animateDpAsState(if (focused) 5.dp else 0.dp).value,
                MaterialTheme.shapes.small
            )
            .background(
                color = MaterialTheme.colors.surface,
                shape = MaterialTheme.shapes.small
            )
        ,
        onFocusChanged = { focused = it; onFocusChanged(it) },
        value = value,
        autofillData = autofillData,
        textStyle = MaterialTheme.typography.h6.copy(color = MaterialTheme.colors.onSurface),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        cursorBrush = SolidColor(MaterialTheme.colors.primary),
        onValueChange = onValueChanged,
        label = {
            val alpha by animateFloatAsState(if (value.isNotEmpty()) 0.5f else 0.3f)
            Text(name,
                modifier = Modifier
                    .alpha(alpha),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface,
            )
        }
    )
}
package com.iideprived.jetpackcomposecomponents.ui.inputs

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iideprived.jetpackcomposecomponents.data.PhoneNumberVisualTransformation


@Composable
fun PhoneInputField(
    text: String,
    onValueChange: (String) -> Unit,
    onFocusChange: (Boolean) -> Unit = {},
    placeholder: String = "+1 000-000-0000",
    textStyle: TextStyle = LocalTextStyle.current,
    accepted: Boolean? = null,
    onSendCode: (String) -> Unit,
){
    var fc1 by remember { mutableStateOf(false) }

    AnimatedTextField(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(
                    color = MaterialTheme.colors.onBackground,
                    width = 1.dp
                ),
                shape = CircleShape
            )
            .background(
                color = Color.Transparent,
                shape = CircleShape
            )
            .clip(CircleShape)
        ,
        textStyle = textStyle.copy(color = MaterialTheme.colors.onBackground),
        value = text,
        onValueChange = onValueChange,
        onFocusChanged = { fc1 = it; onFocusChange(it)},
        label = { Text(
            text = "Phone",
            style = textStyle,
            color = MaterialTheme.colors.onBackground) },
        placeholder = {
            Text(
                modifier = Modifier.alpha(0.5f),
                text = placeholder,
                style = textStyle,
                color = MaterialTheme.colors.onBackground) },
        leadingIcon = {
            Icon(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(16.dp),
                imageVector = Icons.Rounded.Phone,
                contentDescription = "Phone",
                tint = MaterialTheme.colors.onBackground)
         },
        trailingIcon = {
            AnimatedVisibility(visible = accepted != null && fc1 || text.isNotEmpty()) {
                AnimatedContent(targetState = accepted) {
                    Icon(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(24.dp)
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = rememberRipple(bounded = false)
                            ) { if (accepted == true) onSendCode(text) }
                        ,
                        imageVector = if (accepted == true) Icons.Rounded.CheckCircle else Icons.Rounded.Cancel,
                        tint = if (accepted != true) Color.Red else Color.Green,
                        contentDescription = if (accepted == true) "Accepted Phone Number" else "Invalid Phone Number"
                    )
                }
            }
        },
        cursorBrush = SolidColor(MaterialTheme.colors.onBackground),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        keyboardActions = KeyboardActions(onDone = { onSendCode(text) }),
        visualTransformation = PhoneNumberVisualTransformation(),
        letterEnterTransition = fadeIn(tween(500)).plus(slideInVertically(tween(500)) { it }),
        letterExitTransition = fadeOut(tween(100)).plus(slideOutVertically(tween(100)) { it })
    )
}


@Composable
@Preview
private fun PhoneNumberAnimatedTextPreview(){
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Gray)) {
        MaterialTheme() {
            var phoneNumber by remember { mutableStateOf("") }
            PhoneInputField(text = phoneNumber, onValueChange = { phoneNumber = it }) {}
        }
    }
}
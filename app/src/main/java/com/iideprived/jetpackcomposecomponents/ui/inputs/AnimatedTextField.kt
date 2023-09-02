package com.iideprived.jetpackcomposecomponents.ui.inputs

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iideprived.jetpackcomposecomponents.data.PhoneNumberVisualTransformation
import com.iideprived.jetpackcomposecomponents.ui.inputs.AnimatedBasicTextField
import com.iideprived.jetpackcomposecomponents.utils.modifier.AutofillData

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AnimatedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onFocusChanged: (Boolean) -> Unit = {},
    textStyle: TextStyle = LocalTextStyle.current,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource = MutableInteractionSource(),
    cursorBrush: Brush = SolidColor(Color.Black),
    letterEnterTransition: EnterTransition = fadeIn(),
    letterExitTransition: ExitTransition = fadeOut(),
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    autofillData: AutofillData? = null,
    isError: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(12.dp),
    visualTransformation: VisualTransformation = VisualTransformation.None
){

    val keyboard = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    var hasFocus by remember { mutableStateOf(false) }
    var triedToFocus by remember(hasFocus) { mutableStateOf(hasFocus) }
    Row(modifier
        .clickable {
            try {
                focusRequester.requestFocus()
                keyboard?.show()
            } catch (_: Exception) {
                triedToFocus = true
            }
        }
        .padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        AnimatedVisibility(visible = leadingIcon != null){
            leadingIcon?.invoke()
        }
        Column(
            Modifier
                .weight(1f)
                .animateContentSize()) {
            AnimatedVisibility(label != null){ label?.invoke() }
            Box {
                val showTextField = label == null || hasFocus || triedToFocus || value.isNotEmpty()
                AnimatedBasicTextField(
                    value = value,
                    focusRequester = focusRequester,
                    onFocusChanged = { hasFocus = it; onFocusChanged(it) },
                    onValueChange = onValueChange,
                    onTextLayout = onTextLayout,
                    interactionSource = interactionSource,
                    cursorBrush = if (hasFocus) cursorBrush else SolidColor(Color.Transparent),
                    letterEnterTransition = letterEnterTransition,
                    letterExitTransition = letterExitTransition,
                    textStyle = textStyle,
                    enabled = enabled,
                    readOnly = readOnly,
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions,
                    visualTransformation = visualTransformation,
                    autofillData = autofillData,
                    modifier = if (showTextField) Modifier else Modifier.size(1.dp),
                    singleLine = false,
                    maxLines = 1,
                )
                if(value.isEmpty() && placeholder != null) placeholder.invoke()
                if (hasFocus) {
                    try {
                        focusRequester.requestFocus()
                    } catch (_ : Exception){

                    }
                } else {
                    try {
                        focusRequester.freeFocus()
                    } catch ( _ : Exception){

                    }
                }
            }
//            AnimatedVisibility(
//                visible = label == null || hasFocus || triedToFocus || value.isNotEmpty()) {
//                Box {
//                    AnimatedBasicTextField(
//                        value = value,
//                        focusRequester = focusRequester,
//                        onFocusChanged = { hasFocus = it; onFocusChanged(it) },
//                        onValueChange = onValueChange,
//                        onTextLayout = onTextLayout,
//                        interactionSource = interactionSource,
//                        cursorBrush = if (hasFocus) cursorBrush else SolidColor(Color.Transparent),
//                        letterEnterTransition = letterEnterTransition,
//                        letterExitTransition = letterExitTransition,
//                        textStyle = textStyle,
//                        enabled = enabled,
//                        readOnly = readOnly,
//                        keyboardOptions = keyboardOptions,
//                        keyboardActions = keyboardActions,
//                        visualTransformation = visualTransformation,
//                        autofillData = autofillData,
//                    )
//                    if(value.isEmpty() && placeholder != null) placeholder.invoke()
//                }
//
//            }
        }
        AnimatedVisibility(visible = trailingIcon != null) { trailingIcon?.invoke() }
    }
}

@Composable
@Preview
private fun AnimatedTextFields(){
    MaterialTheme {
        var text by remember { mutableStateOf("") }
        Column(
            Modifier
                .padding(32.dp)
                .shadow(5.dp)
                .background(color = Color.White, RoundedCornerShape(18.dp))
        ) {
            var fc1 by remember { mutableStateOf(false) }
            val bgcolor by animateColorAsState(if (fc1) MaterialTheme.colors.secondary else Color.DarkGray)

            AnimatedTextField(
                modifier = Modifier
                    .padding(8.dp)
                    .width(300.dp)
                    .border(
                        border = BorderStroke(
                            color = Color.Black,
                            width = 1.dp
                        ),
                        shape = CircleShape
                    )
                    .background(
                        color = bgcolor,
                        shape = CircleShape
                    )
                    .clip(CircleShape)
                ,
                value = text,
                onValueChange = { text = it },
                onFocusChanged = { fc1 = it; Log.d("AnimatedTextField", "hasFocus: $fc1" ) },
                label = { Text(
                    text = "Phone",
                    style = MaterialTheme.typography.body1,
                    color = Color.White) },
                placeholder = {
                    Text(
                        modifier = Modifier.alpha(0.5f),
                        text = "Placeholder Text",
                        style = MaterialTheme.typography.body1,
                        color = Color.White) },
                leadingIcon = {
                    Icon(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(16.dp),
                        imageVector = Icons.Rounded.Phone,
                        contentDescription = "Phone",
                        tint = Color.White) },
                cursorBrush = SolidColor(Color.White),
                textStyle = MaterialTheme.typography.body1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = PhoneNumberVisualTransformation(),
                letterEnterTransition = fadeIn(tween(500)).plus(slideInVertically(tween(500)) { it }),
                letterExitTransition = fadeOut(tween(100)).plus(slideOutVertically(tween(100)) { it })
            )
            OutlinedTextField(
                value = text,
                onValueChange = { text = it }
            )
            TextField(
                modifier = Modifier
                    .padding(8.dp)
                    .width(300.dp)
                    .border(
                        border = BorderStroke(
                            color = Color.Black,
                            width = 1.dp
                        ),
                        shape = CircleShape
                    )
                    .background(
                        brush = Brush.horizontalGradient(
                            if (fc1) {
                                listOf(bgcolor.copy(0.9f), bgcolor)
                            } else {
                                listOf(Color.Transparent, Color.Transparent)
                            }
                        ),
                        shape = CircleShape
                    )
                    .clip(CircleShape),
                isError = true,
                value = text,
                onValueChange = { text = it },
                label = { Text(
                    text = "Phone",
                    style = MaterialTheme.typography.body1,
                    color = Color.White) },
                placeholder = {
                    Text(
                        modifier = Modifier.alpha(0.5f),
                        text = "Placeholder Text",
                        style = MaterialTheme.typography.body1,
                        color = Color.White) },
                leadingIcon = {
                    Icon(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(16.dp),
                        imageVector = Icons.Rounded.Phone,
                        contentDescription = "Phone",
                        tint = Color.White) },
                textStyle = MaterialTheme.typography.body1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = PhoneNumberVisualTransformation()
            )
        }
    }
}
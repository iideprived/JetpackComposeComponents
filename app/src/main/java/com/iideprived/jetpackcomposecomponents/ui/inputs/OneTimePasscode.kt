package com.iideprived.jetpackcomposecomponents.ui.inputs

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.iideprived.jetpackcomposecomponents.utils.keyevent.digit
import com.iideprived.jetpackcomposecomponents.utils.modifier.autofill

@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun OneTimePasscode(
    modifier: Modifier = Modifier,
    text: String = "",
    isReadOnly: Boolean  = false,
    length : Int = 6,
    boxShape: Shape = MaterialTheme.shapes.medium,
    boxPadding: PaddingValues = PaddingValues(4.dp),
    boxSelectedColor: Color = MaterialTheme.colors.primary,
    boxEnabledColor: Color = MaterialTheme.colors.surface,
    boxDisabledColor: Color = Color.DarkGray,
    boxWidth: Dp = 40.dp,
    boxHeight: Dp = 40.dp,
    textEnter: EnterTransition = fadeIn(),
    textExit: ExitTransition = fadeOut(),
    textStyle: TextStyle = MaterialTheme.typography.button,
    textColor: Color? = null,
    textSize: TextUnit? = null,
    enabled: Boolean = true,
    onValueChange: (input: String) -> Unit = {}
){
    val focusManager = LocalFocusManager.current
    if (isReadOnly){
        ReadOnlyOneTimePasscode(
            text, modifier, length,
            boxShape, boxPadding,
            boxSelectedColor, boxEnabledColor, boxDisabledColor,
            boxWidth, boxHeight,
            textEnter, textExit,
            textStyle, textColor, textSize,
            enabled
        )
        return
    }

    val focusRequesters = remember {
        val list = mutableListOf<FocusRequester>()
        repeat(length) { list.add(FocusRequester())}
        list
    }
    val hasFocus = remember {
         val list = mutableStateListOf<Boolean>()
        repeat(length) { list.add(false) }
        list
    }
    var inBoxChar by remember { mutableStateOf( text.padEnd(length, ' ')) }
    val boxColor by animateColorAsState(if (enabled) boxEnabledColor else boxDisabledColor)
    Row(modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ){
        repeat(length){index ->
            val interactionSource = MutableInteractionSource()
            val displayedChar = if(inBoxChar[index] == ' ' || !inBoxChar[index].isDigit()) "" else inBoxChar[index].toString()
            val elevation by animateDpAsState(if (hasFocus[index]) 20.dp else 0.dp)
            val boxColor2 by animateColorAsState(if(hasFocus[index]) boxSelectedColor else boxColor)
            val scale by animateFloatAsState(if(hasFocus[index]) 1.05f else 1f)
            BasicTextField(
                textStyle = TextStyle(
                    color = Color.Transparent
                ),
                cursorBrush = Brush.horizontalGradient(listOf(Color.Transparent, Color.Transparent)),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = if (index < length - 1) ImeAction.Next else ImeAction.Go
                ),
                value = displayedChar,
                interactionSource = interactionSource,
                modifier = Modifier
                    .autofill(
                        listOf(AutofillType.SmsOtpCode),
                        onFill = { autoCode ->
                            if (autoCode.isDigitsOnly() && autoCode.length == length){
                                inBoxChar = autoCode
                                focusRequesters[index].freeFocus()
                                focusManager.clearFocus(true)
                                onValueChange(inBoxChar)
                                return@autofill
                            }
                        }
                    )
                    .focusRequester(focusRequesters[index])
                    .onFocusChanged {
                        hasFocus[index] = it.hasFocus
                    }
                    .onKeyEvent { event ->
                        when {
                            event.key == Key.Backspace && displayedChar.isNotBlank() -> {
                                Log.d(
                                    "OneTimePasscode",
                                    "deleted via onKeyEvent: displayedChar = '$displayedChar'"
                                )
                                inBoxChar = inBoxChar.substring(
                                    0,
                                    index
                                ) + " " + inBoxChar.substring(index + 1, length)
                                onValueChange(inBoxChar.replace(" ", ""))
                            }
                            event.key == Key.Backspace -> {
                                if (index > 0) {
                                    focusRequesters[index - 1].requestFocus(); hasFocus[index - 1] =
                                        true
                                } else {
                                    focusRequesters[0].freeFocus(); hasFocus[0] =
                                        false; focusManager.clearFocus(true)
                                }
                            }
                            event.digit() != null -> {
                                inBoxChar = inBoxChar.substring(
                                    0,
                                    index
                                ) + event.digit() + inBoxChar.substring(index + 1, length)
                                if (index < length - 1) {
                                    focusRequesters[index + 1].requestFocus(); hasFocus[index + 1] =
                                        true
                                } else {
                                    focusRequesters
                                        .last()
                                        .freeFocus(); hasFocus[index] =
                                        false; focusManager.clearFocus(true)
                                }
                                onValueChange(inBoxChar.replace(" ", ""))
                            }
                        }
                        false
                    }
                    .scale(scale)
                    .padding(boxPadding)
                    .height(boxHeight)
                    .width(boxWidth)
                    .shadow(elevation, boxShape)
                    .background(boxColor2, boxShape)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = rememberRipple(
                            color = Color.Black,
                            bounded = false
                        )
                    )
                    { hasFocus[index] = true }
                ,
                onValueChange = { newValue ->
                    if (newValue.isDigitsOnly() && newValue.length == length){
                        inBoxChar = newValue
                        focusRequesters[index].freeFocus()
                        focusManager.clearFocus(true)
                        onValueChange(inBoxChar)
                        return@BasicTextField
                    }
                }
            ) {
                Box(
                    Modifier
                        .width(boxWidth)
                        .height(boxHeight)){
                    AnimatedContent(
                        modifier = Modifier.align(Alignment.Center),
                        targetState = displayedChar,
                        transitionSpec = {
                            textEnter.with(textExit)
                        }
                    ) {
                        Text(
                            text = it,
                            style = textStyle,
                            color = textColor ?: textStyle.color,
                            textAlign = TextAlign.Center,
                            fontSize = textSize ?: textStyle.fontSize,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ReadOnlyOneTimePasscode(
    text: String,
    modifier: Modifier = Modifier,
    length : Int = 6,
    boxShape: Shape = MaterialTheme.shapes.medium,
    boxPadding: PaddingValues = PaddingValues(4.dp),
    boxSelectedColor: Color = MaterialTheme.colors.primary,
    boxEnabledColor: Color = MaterialTheme.colors.surface,
    boxDisabledColor: Color = Color.DarkGray,
    boxWidth: Dp = 40.dp,
    boxHeight: Dp = 40.dp,
    textEnter: EnterTransition = fadeIn(),
    textExit: ExitTransition = fadeOut(),
    textStyle: TextStyle = MaterialTheme.typography.button,
    textColor: Color? = null,
    textSize: TextUnit? = null,
    enabled: Boolean = true,
){
    val boxColor by animateColorAsState(if (enabled) boxEnabledColor else boxDisabledColor)
    Row(modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(length) { index ->
            val elevation by animateDpAsState(if (text.length == index) 20.dp else 0.dp)
            val boxColor2 by animateColorAsState(if (text.length == index) boxSelectedColor else boxColor)
            val scale by animateFloatAsState(if (text.length == index) 1.05f else 1f)
            Box(
                Modifier
                    .scale(scale)
                    .padding(boxPadding)
                    .height(boxHeight)
                    .width(boxWidth)
                    .shadow(elevation, boxShape)
                    .background(boxColor2, boxShape)
            ) {
            AnimatedContent(
                modifier = Modifier.align(Alignment.Center),
                targetState = text.getOrElse(index) { ' ' },
                transitionSpec = {
                    textEnter.with(textExit)
                }
            ) {
                Text(
                    text = it.toString(),
                    style = textStyle,
                    color = textColor ?: textStyle.color,
                    textAlign = TextAlign.Center,
                    fontSize = textSize ?: textStyle.fontSize,
                )
            }
        }
        }
    }
}

@Composable
@Preview
private fun OneTimePasscodePreview(){
    MaterialTheme {

        var text by remember {
            mutableStateOf("")
        }
        Column {
            OneTimePasscode(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Gray, CircleShape)
            ) {
                text = it
            }
            OneTimePasscode(
                text = text,
                isReadOnly = true,
                modifier = Modifier.fillMaxWidth()
            )
            Button(onClick = { }) {
                Text(text = text)
            }
        }
        
    }
}
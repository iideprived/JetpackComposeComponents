package com.iideprived.jetpackcomposecomponents.ui.inputs

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iideprived.jetpackcomposecomponents.data.PhoneNumberVisualTransformation
import com.iideprived.jetpackcomposecomponents.utils.modifier.AutofillData
import com.iideprived.jetpackcomposecomponents.utils.modifier.autofill
import kotlinx.coroutines.delay

@Composable
fun AnimatedBasicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester? = null,
    onFocusChanged: (Boolean) -> Unit = {},
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = TextStyle.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    cursorBrush: Brush = SolidColor(Color.Black),
    letterEnterTransition: EnterTransition = fadeIn(),
    letterExitTransition: ExitTransition = fadeOut(),
    autofillData: AutofillData? = null,
    decorationBox: @Composable (innerTextField: @Composable () -> Unit) -> Unit =
        @Composable { innerTextField -> innerTextField() }
){
    var cursorFade by remember { mutableStateOf(true) }
    var oldText by remember { mutableStateOf(value) }
    var newText by remember { mutableStateOf(value) }
    val transformedOldText = remember(oldText) {
        visualTransformation.filter(AnnotatedString(oldText))
    }
    val transformedNewText = remember(newText) {
        visualTransformation.filter(AnnotatedString(newText))
    }
    var textfieldvalue by remember { mutableStateOf(TextFieldValue(newText)) }

    LaunchedEffect(key1 = newText){
        delay(50)
        oldText = newText
    }

    val newModifier = modifier
        .let { mod -> if (autofillData != null) mod.autofill(autofillData) else mod }
        .let { mod -> if (focusRequester != null) mod.focusRequester(focusRequester) else mod}
        .let { mod ->
            if (focusRequester != null)
                mod.onFocusChanged {
                    textfieldvalue = TextFieldValue(newText, TextRange(value.length))
                    onFocusChanged(it.hasFocus)
                    cursorFade = !it.hasFocus
                }
            else mod }


    BasicTextField(
        textfieldvalue,
        onValueChange = {
            newText = it.text
            cursorFade = false
            onValueChange(it.text)
            textfieldvalue = it
        },
        newModifier,
        enabled,
        readOnly,
        textStyle.copy(color = Color.Transparent),
        keyboardOptions,
        keyboardActions,
        singleLine,
        maxLines,
        minLines,
        visualTransformation,
        onTextLayout,
        interactionSource,
        SolidColor(Color.Transparent),
    ) {
        it()
        Row(
            Modifier.height(IntrinsicSize.Min)
        )
        {
            repeat(transformedOldText.text.length + 10){
                val letter =
                    transformedNewText.text.getOrNull(it)?.toString() ?:
                    transformedOldText.text.getOrNull(it)?.toString() ?:
                    ""

                AnimatedVisibility(
                    visible = transformedNewText.text.length > it,
                    enter = letterEnterTransition,
                    exit = letterExitTransition
                ) {
                    Text(
                        text = letter,
                        maxLines = 1,
                        onTextLayout = onTextLayout,
                        style = textStyle
                    )
                }
            }
            // TODO: Figure out how to get textfield position using textfieldvalue
            // TODO: Fade this shit with launched effect and focusRequester
            Spacer(modifier = Modifier
                .alpha(
                    animateFloatAsState(
                        if (cursorFade) 0f else 1f,
                        tween(1000),
                        finishedListener = { cursorFade = !cursorFade }
                    ).value
                )
                .width(2.dp)
                .fillMaxHeight()
                .background(cursorBrush)
            )
        }
    }
}

@Composable
@Preview
private fun AnimatedTextPreview(){
    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()){
            var text by remember { mutableStateOf("") }
            AnimatedBasicTextField(
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(200.dp)
                    .background(color = Color.Gray),
                value = text,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                visualTransformation = PhoneNumberVisualTransformation(),
                onValueChange = {text = it})
        }
    }
}
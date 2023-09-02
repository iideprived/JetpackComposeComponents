@file:OptIn(ExperimentalComposeUiApi::class)

package com.iideprived.jetpackcomposecomponents.ui.inputs

import android.util.Size
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iideprived.jetpackcomposecomponents.utils.string.getWordAt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomTextField3(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = TextStyle.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    selectionColor: Color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
    cursorBrush: Brush = SolidColor(Color.Black),
    cursorSize: Size = Size(1, 12),
){
    val keyboard = LocalSoftwareKeyboardController.current
    var showCursor by remember { mutableStateOf(false) }
    val focusRequester by remember { mutableStateOf(FocusRequester()) }
    var lastActionIsTyping by remember { mutableStateOf(false) }

    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = value,
            )
        )
    }
    BasicTextField(
        modifier = modifier
            .focusRequester(focusRequester)
            .onFocusChanged {
                lastActionIsTyping = false
                showCursor = it.hasFocus
            }
        ,
        value = textFieldValue,
        onValueChange = {
            lastActionIsTyping = true
            textFieldValue = it
            onValueChange(it.text)
        },
        textStyle = textStyle.copy(color = Color.Transparent),
        enabled = enabled,
        readOnly = readOnly,
        singleLine = true,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        cursorBrush = cursorBrush,
        decorationBox = {
            var fadeCursor by remember { mutableStateOf(false) }
            val cursorAlpha by animateFloatAsState(
                if (fadeCursor) 0.0f else 1f,
                tween(500, easing = LinearEasing),
                finishedListener = { fadeCursor = !fadeCursor }
            )
            LaunchedEffect(value){
                fadeCursor = !fadeCursor
            }
            Row(modifier
                .height(IntrinsicSize.Min)
                .combinedClickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = {
                        lastActionIsTyping = false
                        textFieldValue = TextFieldValue(
                            text = value,
                            selection = TextRange(value.length)
                        )
                        focusRequester.requestFocus()
                        keyboard?.show()
                    },
                    onDoubleClick = {
                        lastActionIsTyping = false
                        textFieldValue = TextFieldValue(
                            text = value,
                            selection = TextRange(0, value.length)
                        )
                        focusRequester.requestFocus()
                        keyboard?.show()
                    },
                    onLongClick = {
                        lastActionIsTyping = false
                        textFieldValue = TextFieldValue(
                            text = value,
                            selection = TextRange(0, value.length)
                        )
                        focusRequester.requestFocus()
                        keyboard?.show()
                    }

                )
            ) {
                if ((value.isEmpty() || textFieldValue.selection.max == 0) && showCursor){
                    Box {
                        Spacer(modifier = Modifier
                            .alpha(cursorAlpha)
                            .width(cursorSize.width.dp)
                            .height(cursorSize.height.dp)
                            .align(Alignment.TopEnd)
                            .background(cursorBrush))
                    }
                }

                repeat(value.length + 1){
                    Box {
                        AnimatedContent(
                            value.getOrNull(it) != null,
                        ) {showLetter ->
                            if (!showLetter) return@AnimatedContent
                            Text(
                                modifier = Modifier
                                    .height(IntrinsicSize.Min)
                                    .background(
                                        color = if (it in textFieldValue.selection) selectionColor else Color.Transparent
                                    )
                                    .combinedClickable(
                                        interactionSource = interactionSource,
                                        indication = null,
                                        onClick = {
                                            lastActionIsTyping = false
                                            textFieldValue = TextFieldValue(
                                                text = value,
                                                selection = TextRange(it + 1)
                                            )
                                            focusRequester.requestFocus()
                                            keyboard?.show()
                                        },
                                        onDoubleClick = {
                                            val (_, range) = value.getWordAt(it)
                                            lastActionIsTyping = false
                                            textFieldValue = TextFieldValue(
                                                text = value,
                                                selection = range
                                            )
                                            focusRequester.requestFocus()
                                            keyboard?.show()
                                        },
                                        onLongClick = {
                                            lastActionIsTyping = false
                                            textFieldValue = TextFieldValue(
                                                text = value,
                                                selection = TextRange(0, value.length)
                                            )
                                            focusRequester.requestFocus()
                                            keyboard?.show()
                                        }
                                    ),
                                text = (value.getOrNull(it) ?: "").toString(),
                                style = textStyle,
                            )
                        }
                        if (textFieldValue.selection.max == it + 1 && showCursor){
                            Spacer(
                                Modifier
                                    .alpha(cursorAlpha)
                                    .width(cursorSize.width.dp)
                                    .height(cursorSize.height.dp)
                                    .background(cursorBrush)
                                    .align(Alignment.TopEnd),
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
@Preview
private fun CustomTextFieldPreview(){
    MaterialTheme {
        var value by remember { mutableStateOf("") }
        val columnModifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFFEFEFEF), shape = RoundedCornerShape(18.dp))
            .padding(12.dp)
        Column(
            Modifier
                .padding(8.dp)
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column(columnModifier) {
                Text(text = "CustomTextField with Decoration Box", style = MaterialTheme.typography.h4)
                CustomTextField3(value, { value = it}, textStyle = MaterialTheme.typography.caption )
            }
        }
    }
}
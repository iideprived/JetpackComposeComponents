package com.iideprived.jetpackcomposecomponents.ui.text

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
import com.iideprived.jetpackcomposecomponents.ui.Styled
import kotlin.math.max

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun StepCounter(
    count: Int,
    modifier: Modifier = Modifier,
    style : TextStyle = MaterialTheme.typography.body1,
    baseNumberChangeDurationMs: Long = 500L,
    durationBasedOnPlace: (place: Int) -> Float = { it.toFloat() / 2},
    representCountAsString: (count: Int) -> String = { it.toString() },
) {
    val stringRepresentation = representCountAsString(count)
    val useStringRepresentation = stringRepresentation == count.toString()

    var oldCount by remember { mutableStateOf(count) }

    SideEffect {
        oldCount = count
    }

    val numTexts = max((count - 1).toString().length, (count + 1).toString().length )

    Row(modifier.animateContentSize(),
        horizontalArrangement = Arrangement.Center
    ) {
        val newDigits = count.toString().padStart(numTexts, ' ')
        val oldDigits = oldCount.toString().padStart(numTexts, ' ')

        for (i in 0 until  numTexts) {
            val oldDigit = oldDigits[i]
            val newDigit = newDigits[i]
            val displayedDigit = if (oldDigit == newDigit) oldDigit else newDigit

            val animationSpec = tween<IntOffset>(
                durationMillis = (baseNumberChangeDurationMs * durationBasedOnPlace(numTexts - i)).toInt()
            )
            AnimatedContent(
                targetState =  if (useStringRepresentation) displayedDigit else '`',
                transitionSpec = {
                    if (targetState == '`' || initialState == '`') { fadeIn() with fadeOut()}
                    else if (oldCount < count) {
                        slideInVertically(animationSpec)  { it } + fadeIn() with slideOutVertically(animationSpec) { -it } + fadeOut()
                    } else if (oldCount > count) {
                        slideInVertically(animationSpec) { -it } + fadeIn() with slideOutVertically(animationSpec) { it } + fadeOut()
                    } else {
                        fadeIn() with fadeOut()
                    }
                }
            ) {
                when {
                    it == '`' && i == numTexts - 1 -> stringRepresentation.Styled(with = style)
                    it != ' ' -> it.toString().Styled(with = style)
                    else -> "".Styled(with = style)
                }
            }
        }
    }
}
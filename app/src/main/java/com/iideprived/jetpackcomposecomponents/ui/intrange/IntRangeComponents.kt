package com.iideprived.jetpackcomposecomponents.ui

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import com.iideprived.jetpackcomposecomponents.ui.Styled
import kotlin.math.max

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Counter(
    count: Int,
    range: IntRange,
    modifier: Modifier = Modifier,
    style : TextStyle = MaterialTheme.typography.body1,
    baseNumberChangeDurationMs: Long = 500L,
    durationBasedOnPlace: (place: Int) -> Float = { it.toFloat() / 2},
    representCountAsString: (count: Int) -> String = { it.toString() },
    numTexts: Int = max(range.first.toString().length, range.last.toString().length)
) {
    val clampedCount = if (count < range.first) range.first else if (count > range.last) range.last else count
    val stringRepresentation = representCountAsString(clampedCount)
    val useStringRepresentation = stringRepresentation == clampedCount.toString()
    
    var oldCount by remember { mutableStateOf(clampedCount) }

    SideEffect {
        oldCount = clampedCount
    }

    Row(modifier.animateContentSize(),
        horizontalArrangement = Arrangement.Center
    ) {
        val newDigits = clampedCount.toString().padStart(numTexts, ' ')
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
                    if (targetState == '`' || initialState == '`') { EnterTransition.None with ExitTransition.None}
                    else if (oldCount < clampedCount) {
                        slideInVertically(animationSpec)  { it } + fadeIn() with slideOutVertically(animationSpec) { -it } + fadeOut()
                    } else if (oldCount > clampedCount) {
                        slideInVertically(animationSpec) { -it } + fadeIn() with slideOutVertically(animationSpec) { it } + fadeOut()
                    } else {
                        EnterTransition.None with ExitTransition.None
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

@Composable
@Preview
private fun CounterPreview(){
    MaterialTheme{
        var count by remember { mutableStateOf(0) }
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Counter(count = count, range = (1..11))
            Counter(count = count, range = (1..11),
                representCountAsString = {
                    when {
                        it < 2 -> "Solo"
                        it > 10 -> "10+"
                        else -> it.toString()
                    }
                }
            )
            Button(onClick = { count += 1 }) {
                Text("Increment")
            }
            Button(onClick = { count -= 1 }) {
                Text("Decrement")
            }
        }
    }
}
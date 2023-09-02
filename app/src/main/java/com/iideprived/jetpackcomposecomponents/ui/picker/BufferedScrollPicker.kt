package com.iideprived.jetpackcomposecomponents.ui.picker

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.iideprived.jetpackcomposecomponents.ui.Counter
import com.iideprived.jetpackcomposecomponents.ui.time.Month
import com.iideprived.jetpackcomposecomponents.utils.transitionspec.TransitionSpecs
import kotlinx.coroutines.delay

@Composable
fun <T> BufferedScrollPicker(
    modifier: Modifier = Modifier,
    items: List<T>,
    selectedItem: Int,
    numBuffers: Int = 2,
    indexOfSelectedContent: Int = 1,
    toText: (T) -> String = { it.toString() },
    scrollLength: Dp = 20.dp,
    onItemSelected: (item: T) -> Unit,
    scrollDirection: Orientation = Orientation.Vertical,
    content: @Composable (item: T?, isSelected: Boolean) -> Unit = { item, isSelected ->
        val alpha = if( isSelected ) 1f else 0.3f
        val scale = if(isSelected) 1f else 0.5f
        Text(
            modifier = Modifier
                .alpha(alpha)
                .scale(scale),
            text = if (item != null) toText(item) else "",
            style = MaterialTheme.typography.h5,
            textAlign = TextAlign.Center,
        )
    },
){
    ScrollPicker(
        modifier = modifier,
        selectedItem = selectedItem,
        scrollLength = scrollLength,
        scrollDirection = scrollDirection,
        items = items,
        onItemSelected = onItemSelected){
        if (scrollDirection == Orientation.Vertical){
            Column(
                modifier = Modifier.width(IntrinsicSize.Min),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                repeat(numBuffers + 1){ indexOfBuffer ->
                    val offsetOfSelected = indexOfBuffer - indexOfSelectedContent
                    val bufferItem = items.getOrNull(selectedItem + offsetOfSelected)
                    if (offsetOfSelected == 0){
                        content(bufferItem, true)
                    } else {
                        Box(
                            modifier = Modifier.clickable(
                                indication = null,
                                interactionSource = MutableInteractionSource(),
                            ) { bufferItem?.let(onItemSelected) }
                        ) {
                            content(bufferItem, false)
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun BufferedScrollPickerPreview(){
    MaterialTheme {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            val range = remember { (1..31) }
            val months = remember { Month.values() }
            val years = remember { (1900..2023) }
            var x by remember { mutableStateOf(3) }
            var y by remember { mutableStateOf(Month.April) }
            var z by remember { mutableStateOf(2023) }
            Row {
                var lastMonth by remember { mutableStateOf(y) }
                LaunchedEffect(y){
                    delay(1000/120)
                    lastMonth = y
                }
                BufferedScrollPicker(
                    modifier = Modifier.padding(8.dp),
                    items = months.toList(),
                    selectedItem = y.ordinal,
                    onItemSelected = { y = it },
                    numBuffers = 2,
                    indexOfSelectedContent = 1,
                ) { month , isSelected ->
                    val (alpha, scale) = if (isSelected) 1f to 1f else 0.3f to (if (month == null) 0f else 0.5f)
                    AnimatedContent(targetState = month,
                        transitionSpec = TransitionSpecs.fadingSlideInAndOut(lastMonth.ordinal < y.ordinal)
                    ) {
                        Text(
                            modifier = Modifier.alpha(alpha).scale(scale),
                            text = it?.name ?: "",
                            style = MaterialTheme.typography.h5
                        )
                    }
                }
                BufferedScrollPicker(
                    modifier = Modifier.padding(8.dp),
                    items = range.toList(),
                    selectedItem = range.indexOf(x),
                    onItemSelected = { x = it},
                ) { day , isSelected ->
                    val (alpha, scale) = if (isSelected) 1f to 1f else 0.3f to (if (day == null) 0f else 0.5f)
                    Counter(
                        modifier = Modifier
                            .alpha(alpha)
                            .scale(scale),
                        count = day ?: x,
                        range = (3..11),
                        style = MaterialTheme.typography.h5
                    )
                }
                BufferedScrollPicker(
                    modifier = Modifier.padding(8.dp),
                    items = years.toList(),
                    selectedItem = years.indexOf(z),
                    onItemSelected = { z = it },
                ) { year , isSelected ->
                    val (alpha, scale) = if (isSelected) 1f to 1f else 0.3f to (if (year == null) 0f else 0.5f)
                    Counter(
                        modifier = Modifier
                            .alpha(alpha)
                            .scale(scale),
                        count = year ?: z,
                        range = years,
                        style = MaterialTheme.typography.h5
                    )
                }
            }
        }
    }
}
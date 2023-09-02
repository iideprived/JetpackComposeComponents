package com.iideprived.jetpackcomposecomponents.ui.picker

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.iideprived.jetpackcomposecomponents.ui.Counter
import com.iideprived.jetpackcomposecomponents.ui.time.Month
import com.iideprived.jetpackcomposecomponents.utils.time.DateUtils
import com.iideprived.jetpackcomposecomponents.utils.time.DateUtils.Companion.monthsForYear
import com.iideprived.jetpackcomposecomponents.utils.time.DateUtils.Companion.numDays
import kotlinx.coroutines.delay

@Composable
fun <T> ScrollPicker(
    modifier: Modifier = Modifier,
    items: List<T>,
    selectedItem: Int = 0,
    toText: (T) -> String = { it.toString() },
    scrollLength: Dp = 20.dp,
    onItemSelected: (item: T) -> Unit,
    scrollDirection: Orientation = Orientation.Vertical,
    content: @Composable (T) -> Unit = {
        Text(
            text = toText(it),
            style = MaterialTheme.typography.h5,
            textAlign = TextAlign.Center,
        )
    },
){
    val scrollState = rememberLazyListState(selectedItem)
    val selectedIndex by remember { derivedStateOf { scrollState.firstVisibleItemIndex } }
    var enableScroll by remember { mutableStateOf(true) }
    LaunchedEffect(key1 = items){
        if (selectedIndex >= items.size) scrollState.scrollToItem(items.size - 1)
    }

    LaunchedEffect(key1 = true){
        scrollState.scrollToItem(selectedItem)
    }

    LaunchedEffect(key1 = selectedIndex) {
        onItemSelected(items[selectedIndex])
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ){
        val isColumn = scrollDirection == Orientation.Vertical
        val listModifier = with(Modifier) {
            if (isColumn) height(scrollLength) else width(scrollLength)
        }
        val listContent: LazyListScope.() -> Unit = {
            repeat(items.size){
                item {
                    Spacer(modifier = listModifier)
                }
            }
        }

        content(items[if (selectedIndex >= items.size) items.lastIndex else selectedIndex])
        if (isColumn)
            LazyColumn(listModifier, scrollState, userScrollEnabled = enableScroll, content = listContent)
        else
            LazyRow(listModifier, scrollState, content = listContent, userScrollEnabled = enableScroll)
    }

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
@Preview
private fun ScrollPickerPreview(){
    MaterialTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Row() {
                var month by remember { mutableStateOf(Month.values()[DateUtils.month]) }
                var lastMonth by remember { mutableStateOf(month) }
                var day   by remember { mutableStateOf(DateUtils.day) }
                var year  by remember { mutableStateOf(DateUtils.year) }

                val months = remember { mutableStateListOf(*monthsForYear(year).toTypedArray()) }
                val days = remember { mutableStateListOf(*(1..month.numDays).toList().toTypedArray()) }
                val years = remember { (1900..DateUtils.year) }

                LaunchedEffect(key1 = year){
                    val monthsForYear = monthsForYear(year)
                    Log.d("ScrollPicker", "monthsForYear($year) = $monthsForYear")
                    if (monthsForYear != months) months.apply {
                        clear()
                        addAll(monthsForYear)
                        if (!months.any { m -> m == month }) month = months.last()
                    }
                }

                LaunchedEffect(month){
                    val daysForMonth = month.numDays(year)
                    if (daysForMonth != days.last()) days.apply {
                        clear()
                        addAll( 1..daysForMonth)
                        if (day > daysForMonth) day = daysForMonth
                    }
                    delay(1000/120)
                    lastMonth = month
                }

                ScrollPicker(
                    modifier = Modifier.padding(8.dp),
                    items = months,
                    onItemSelected = {
                        month = it
                    },
                    scrollLength = 200.dp
                ) {
                    AnimatedContent(
                        targetState = month,
                        transitionSpec = {
                            if (lastMonth.ordinal < month.ordinal){
                                slideInVertically { it }.plus(fadeIn()) . with( slideOutVertically { -it }.plus(fadeOut()) )
                            } else {
                                slideInVertically { -it }.plus(fadeIn()).with( slideOutVertically { it }.plus(fadeOut()) )
                            }
                        }
                    ) {
                        when (month) {
                            lastMonth -> {
                                Text(
                                    text = month.name,
                                    style = MaterialTheme.typography.h5,
                                )
                            }
                            else -> {
                                Text(
                                    text = lastMonth.name,
                                    style = MaterialTheme.typography.h5
                                )
                            }
                        }
                    }
                }
                ScrollPicker(
                    modifier = Modifier.padding(8.dp),
                    items = days,
                    onItemSelected = {
                        day = it
                    }
                ) {
                    Counter(
                        count = day,
                        range = (days.first() .. days.last()),
                        style = MaterialTheme.typography.h5
                    )
                }
                ScrollPicker(
                    selectedItem = years.indexOf(year),
                    modifier = Modifier.padding(8.dp),
                    items = years.toList(),
                    onItemSelected = {
                        year = it
                    }
                ){
                    Counter(
                        count = year,
                        range = years,
                        style = MaterialTheme.typography.h5
                    )
                }
            }
        }
    }
}
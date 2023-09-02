package com.iideprived.jetpackcomposecomponents.ui.picker

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iideprived.jetpackcomposecomponents.ui.text.StepCounter
import com.iideprived.jetpackcomposecomponents.ui.time.Month
import com.iideprived.jetpackcomposecomponents.utils.time.DateUtils
import com.iideprived.jetpackcomposecomponents.utils.time.DateUtils.Companion.numDays
import com.iideprived.jetpackcomposecomponents.utils.transitionspec.TransitionSpecs

@Composable
fun BirthdayPicker(
    modifier: Modifier = Modifier,
    defaultMonth: Month = Month.values()[DateUtils.month],
    defaultDay: Int = DateUtils.day,
    defaultYear: Int = DateUtils.year,
    availableYears: IntRange = (1900..DateUtils.year),
    onDateChange: (String) -> Unit
){
    var month by remember { mutableStateOf(defaultMonth) }
    var lastMonth by remember { mutableStateOf(month) }
    var day   by remember { mutableStateOf(defaultDay) }
    var year  by remember { mutableStateOf(defaultYear) }

    val months = remember { mutableStateListOf(*DateUtils.monthsForYear(year).toTypedArray()) }
    var days by remember { mutableStateOf((1..month.numDays)) }

    var mmddyyyy by remember {
        mutableStateOf(
            buildString {
                append(month.ordinal + 1)
                append('/')
                append(day)
                append('/')
                append(year)
        })
    }

    fun resetMonths(){
        val monthsForYear = DateUtils.monthsForYear(year)
        if (monthsForYear != months) months.apply {
            if (monthsForYear.size > months.size){
                addAll((monthsForYear.subList(months.size, monthsForYear.size)))
            } else {
                removeAll((months.subList(monthsForYear.size, months.size)))
            }
            if (month.ordinal >= months.size) month = months.last()
        }
    }

    fun resetDays(){
        days = (1..month.numDays(year))
        if (day > days.last) day = days.last
    }

    fun resetMMDDYYYY(){
        mmddyyyy = buildString {
            append(month.ordinal + 1)
            append('/')
            append(day)
            append('/')
            append(year)
        }
    }

    LaunchedEffect(year){
        resetMonths()
        resetDays()
        resetMMDDYYYY()
    }

    LaunchedEffect(month){
        resetDays()
        resetMMDDYYYY()
    }

    LaunchedEffect(day){
        resetMMDDYYYY()
    }

    LaunchedEffect(mmddyyyy){
        onDateChange(mmddyyyy)
    }


    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        Column(
            modifier = Modifier.weight(6f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Header("Month")
            MonthScroller(
                months = months,
                selectedMonth = month,
                lastMonth = lastMonth,
                onMonthChanged = { lastMonth = month; month = it})
        }
        Column(Modifier.weight(3f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Header("Day")
            CounterScroller(
                range = days,
                current = day,
                onItemChange = {day = it})
        }
        Column(Modifier.weight(4f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Header("Year")
            CounterScroller(
                range = availableYears,
                current = year,
                onItemChange = { year = it })
        }
    }

}


@Composable
@Preview
private fun BirthdayPickerPreview(){
    MaterialTheme {
        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center){
            var birthday by remember { mutableStateOf("11/20/1998") }
            Text(
                text = "What's your birthday?",
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.padding(8.dp)
            )
            Card(
                modifier = Modifier.padding(8.dp)
            ) {
                BirthdayPicker(
                    modifier = Modifier.padding(8.dp),
                    defaultMonth = Month.values()[Integer.parseInt(birthday.split('/')[0]) - 1],
                    defaultDay = Integer.parseInt(birthday.split('/')[1]),
                    defaultYear = Integer.parseInt(birthday.split('/')[2]),
                ) { birthday = it }
            }
            Spacer(Modifier.height(40.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text("Your birthday is $birthday?")
                Button(onClick = { /*TODO*/ }) {
                    Text("Confirm")
                }
            }
        }
    }
}


@Composable
private fun Header(
    header: String,
    isSelected: Boolean = true
) {
    val alpha by animateFloatAsState(if (isSelected) 1f else 0.5f)
    Text(
        modifier = Modifier.alpha(alpha),
        text = header,
        color = Color.Transparent,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun MonthScroller(
    months: List<Month>,
    selectedMonth: Month,
    lastMonth: Month,
    onMonthChanged: (Month) -> Unit,
) {
    BufferedScrollPicker(
        modifier = Modifier.padding(8.dp),
        items = months,
        selectedItem = selectedMonth.ordinal,
        onItemSelected = onMonthChanged,
        numBuffers = 2,
        indexOfSelectedContent = 1,
    ) { month , isSelected ->
        val (alpha, scale) = if (isSelected) 1f to 1f else 0.3f to (if (month == null) 0f else 0.5f)
        AnimatedContent(
            modifier = Modifier.fillMaxWidth(),
            targetState = month,
            transitionSpec = TransitionSpecs.fadingSlideInAndOut(lastMonth.ordinal < selectedMonth.ordinal)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(alpha)
                    .scale(scale),
                text = it?.name ?: "",
                style = MaterialTheme.typography.h5,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun CounterScroller(
    range: IntRange,
    current: Int,
    onItemChange: (Int) -> Unit
){
    BufferedScrollPicker(
        modifier = Modifier.padding(8.dp),
        items = range.toList(),
        selectedItem = range.indexOf(current),
        onItemSelected = onItemChange,
    ) { day , isSelected ->
        val (alpha, scale) = if (isSelected) 1f to 1f else 0.3f to (if (day == null) 0f else 0.5f)
        StepCounter(
            modifier = Modifier
                .alpha(alpha)
                .scale(scale),
            count = day ?: current,
            representCountAsString = {
                when (day) {
                    null -> ""
                    else -> day.toString()
                }
            },
            style = MaterialTheme.typography.h5.copy(textAlign = TextAlign.Center, color = Color.White),
        )
    }
}


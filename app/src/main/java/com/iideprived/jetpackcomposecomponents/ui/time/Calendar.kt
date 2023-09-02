package com.iideprived.jetpackcomposecomponents.ui.time

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.iideprived.jetpackcomposecomponents.utils.time.DateUtils
import com.iideprived.jetpackcomposecomponents.utils.time.DateUtils.Companion.monthsForYear
import com.iideprived.jetpackcomposecomponents.utils.time.DateUtils.Companion.numDays
import kotlinx.coroutines.delay

@Composable
fun Calendar(
    modifier: Modifier = Modifier,
    mmddyyyy: String? = null,
    onDateChange: (String) -> Unit,
    textStyle: TextStyle = MaterialTheme.typography.h5,
    textColor: Color = MaterialTheme.colors.onSurface
){

    var month by remember { mutableStateOf(Month.values()[DateUtils.month])}
    var day by remember { mutableStateOf(DateUtils.day)}
    var year by remember { mutableStateOf(DateUtils.year)}

    var _month by remember { mutableStateOf(Month.values()[DateUtils.month])}
    var _day by remember { mutableStateOf(DateUtils.day)}
    var _year by remember { mutableStateOf(DateUtils.year)}

    var expanded by remember { mutableStateOf(0) }

    LaunchedEffect(key1 = "$month/$day/$year"){
        expanded = 0
        if (year == DateUtils.year){
            month = if (DateUtils.month < month.ordinal) Month.values()[DateUtils.month] else month
            val dayMax = month.numDays(year)
            day = if (day > dayMax) dayMax else day
        }
        onDateChange("$month/$day/$year")
        delay(50)
        _month = month
        _day = day
        _year = year
    }

    Row(modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ){
        repeat(3){
            val (weight, text) = when(it){
                0 -> 0.2f to month.name
                1 -> 0.2f to day.toString()
                else -> 0.2f to year.toString()
            }
            Box(
                modifier = Modifier.clip(CircleShape).animateContentSize(),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    modifier = Modifier
                        .padding(8.dp)
                        .alpha(animateFloatAsState(if(expanded == it + 1) 0f else 1f).value)
                        .background(
                            color = MaterialTheme.colors.surface,
                            shape = CircleShape
                        )
                        .clip(CircleShape)
                        .clickable { expanded = it + 1 }
                        .padding(16.dp)
                    ,
                    targetState = when (it){
                        0 -> month
                        1 -> day
                        else -> year
                    },
                ) {_ ->
                    when (month) {
                        _month -> Text(
                            text = text,
                            style = textStyle,
                            color = textColor,
                            textAlign = TextAlign.Center)
                        else -> Text(
                            text = when(it){
                                0 -> _month.name
                                1 -> _day.toString()
                                else -> _year.toString()
                            },
                            style = textStyle,
                            color = textColor,
                            textAlign = TextAlign.Center)
                    }
                }
                DropdownMenu(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .height(80.dp),
                    expanded = expanded == it + 1, onDismissRequest = { expanded = 0 },
                    offset = DpOffset(x = 8.dp, y = (-70).dp)
                ) {

                    val (range, alpha, onClick: (Any) -> Unit) = when (it){
                        0 -> Triple( monthsForYear(year), 0.5f) { item: Any ->
                            if (item == month) month = item as Month else expanded = 0
                        }
                        1 -> Triple( (1..month.numDays(year)), 0.2f) { item: Any ->
                            if (item == day) day = item as Int else expanded = 0
                        }
                        else -> Triple((DateUtils.year downTo 1900), 0.2f) { item: Any ->
                            if (item == year) year = item as Int else expanded = 0
                        }
                    }

                    range.forEach { item ->
                        Text(
                            modifier = Modifier
                                .alpha(alpha)
                                .clickable { onClick(item) },
                            text = item.toString(),
                            style = textStyle,
                            color = textColor
                        )
                    }

                }
            }
        }
    }
}

enum class Month(
    val numDays: Int
) {
    January(31),
    February(28),
    March(31),
    April(30),
    May(31),
    June(30),
    July(31),
    August(31),
    September(30),
    October(31),
    November(30),
    December(31)
}


@Composable
@Preview
private fun CalendarPreview(){
    MaterialTheme {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            Calendar(onDateChange = {} )
        }
    }
}

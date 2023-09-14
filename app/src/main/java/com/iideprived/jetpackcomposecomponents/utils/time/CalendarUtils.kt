package com.iideprived.jetpackcomposecomponents.utils.time

import java.util.Calendar
import kotlin.math.abs

fun millisToCalendar(millis: Long): Calendar = Calendar.getInstance().apply { timeInMillis = millis }

fun Calendar.isSameDayAs(other: Calendar) =
    this.get(Calendar.YEAR) == other.get(Calendar.YEAR) &&
    this.get(Calendar.DAY_OF_YEAR) == other.get(Calendar.DAY_OF_YEAR)

fun Calendar.formatToHourMinuteAmPm(): String {
    val hour = this.get(Calendar.HOUR_OF_DAY)
    val minute = this.get(Calendar.MINUTE)
    val amPm = if (hour < 12) "AM" else "PM"
    val formattedHour = if (hour % 12 == 0) 12 else hour % 12
    return String.format("%02d:%02d %s", formattedHour, minute, amPm)
}

fun Calendar.formatToDDDAtHourMinuteAmPm(): String {
    val ddd = when (this.get(Calendar.DAY_OF_WEEK)) {
        Calendar.SUNDAY -> "Sun"
        Calendar.MONDAY -> "Mon"
        Calendar.TUESDAY -> "Tue"
        Calendar.WEDNESDAY -> "Wed"
        Calendar.THURSDAY -> "Thu"
        Calendar.FRIDAY -> "Fri"
        Calendar.SATURDAY -> "Sat"
        else -> ""
    }
    return "$ddd At ${this.formatToHourMinuteAmPm()}"
}

fun Calendar.formatToMMMddAtHourMinuteAmPm(): String {
    val month = when (this.get(Calendar.MONTH)) {
        Calendar.JANUARY -> "Jan"
        Calendar.FEBRUARY -> "Feb"
        Calendar.MARCH -> "Mar"
        Calendar.APRIL -> "Apr"
        Calendar.MAY -> "May"
        Calendar.JUNE -> "Jun"
        Calendar.JULY -> "Jul"
        Calendar.AUGUST -> "Aug"
        Calendar.SEPTEMBER -> "Sep"
        Calendar.OCTOBER -> "Oct"
        Calendar.NOVEMBER -> "Nov"
        Calendar.DECEMBER -> "Dec"
        else -> ""
    }
    return "$month ${this.get(Calendar.DAY_OF_MONTH)} At ${this.formatToHourMinuteAmPm()}"
}

fun Calendar.formatToMMMddyyyyAtHourMinuteAmPm(): String {
    val month = when (this.get(Calendar.MONTH)) {
        Calendar.JANUARY -> "Jan"
        Calendar.FEBRUARY -> "Feb"
        Calendar.MARCH -> "Mar"
        Calendar.APRIL -> "Apr"
        Calendar.MAY -> "May"
        Calendar.JUNE -> "Jun"
        Calendar.JULY -> "Jul"
        Calendar.AUGUST -> "Aug"
        Calendar.SEPTEMBER -> "Sep"
        Calendar.OCTOBER -> "Oct"
        Calendar.NOVEMBER -> "Nov"
        Calendar.DECEMBER -> "Dec"
        else -> ""
    }
    return "$month ${this.get(Calendar.DAY_OF_MONTH)}, ${this.get(Calendar.YEAR)} At ${this.formatToHourMinuteAmPm()}"
}

fun Calendar.compareAndFormatTimestamp(other: Calendar? = null) : String {
    val now: Calendar = Calendar.getInstance()
    if (other == null) {
        return when {
            now[Calendar.YEAR] != this[Calendar.YEAR] -> this.formatToMMMddyyyyAtHourMinuteAmPm()
            now[Calendar.WEEK_OF_MONTH] != this[Calendar.WEEK_OF_MONTH] -> this.formatToMMMddAtHourMinuteAmPm()
            now[Calendar.DAY_OF_YEAR] != this[Calendar.DAY_OF_YEAR] -> this.formatToDDDAtHourMinuteAmPm()
            else -> this.formatToHourMinuteAmPm()
        }
    }

    return when {
        now[Calendar.YEAR] != other[Calendar.YEAR] -> this.formatToMMMddyyyyAtHourMinuteAmPm()
        now[Calendar.WEEK_OF_YEAR] != other[Calendar.WEEK_OF_YEAR] -> this.formatToMMMddAtHourMinuteAmPm()
        now[Calendar.DAY_OF_YEAR] != other[Calendar.DAY_OF_YEAR] -> this.formatToDDDAtHourMinuteAmPm()
        else -> this.formatToHourMinuteAmPm()
    }
}
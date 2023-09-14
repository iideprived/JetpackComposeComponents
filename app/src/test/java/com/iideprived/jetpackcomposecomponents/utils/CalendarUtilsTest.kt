package com.iideprived.jetpackcomposecomponents.utils

import com.iideprived.jetpackcomposecomponents.utils.time.compareAndFormatTimestamp
import com.iideprived.jetpackcomposecomponents.utils.time.formatToHourMinuteAmPm
import com.iideprived.jetpackcomposecomponents.utils.time.formatToMMMddyyyyAtHourMinuteAmPm
import org.junit.Test
import java.util.Calendar

class CalendarUtilsTest {


    @Test
    fun test1(){
        val time = System.currentTimeMillis()
        val now = Calendar.getInstance().apply { timeInMillis = time }
        val hour = Calendar.getInstance().apply { timeInMillis = time - 3600000 }
        val tenHours = Calendar.getInstance().apply { timeInMillis = time - 3600000 * 10 }
        val oneDay = Calendar.getInstance().apply { timeInMillis = time - 3600000 * 24 }
        val oneWeek = Calendar.getInstance().apply { timeInMillis = time - 3600000 * 24 * 7 }
        val oneYear = Calendar.getInstance().apply { timeInMillis = time - 3600000L * 24 * 7 * 53 }

        println(now.formatToMMMddyyyyAtHourMinuteAmPm())
        println(hour.formatToMMMddyyyyAtHourMinuteAmPm())
        println(tenHours.formatToMMMddyyyyAtHourMinuteAmPm())
        println(oneDay.formatToMMMddyyyyAtHourMinuteAmPm())
        println(oneWeek.formatToMMMddyyyyAtHourMinuteAmPm())
        println(oneYear.formatToMMMddyyyyAtHourMinuteAmPm())



        println(now.compareAndFormatTimestamp(hour))
        println(now.compareAndFormatTimestamp(hour))
        println(now.compareAndFormatTimestamp(hour))
        println(now.compareAndFormatTimestamp(hour))
    }
}
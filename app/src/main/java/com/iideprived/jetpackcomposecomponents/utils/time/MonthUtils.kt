package com.iideprived.jetpackcomposecomponents.utils.time

import android.icu.util.Calendar
import com.iideprived.jetpackcomposecomponents.ui.time.Month

class DateUtils{
    companion object {
        val today by lazy { Calendar.getInstance() }
        val year by lazy { today[Calendar.YEAR] }
        val month by lazy { today[Calendar.MONTH] }
        val day by lazy { today[Calendar.DAY_OF_MONTH] }
        val isLeapYear by lazy { today[Calendar.IS_LEAP_MONTH] == 1 }

        fun  getTodaysDate() : String = "${month + 1}/$day/${ year - 21 }"

        fun Month.isLeapYear(year: Int) : Boolean = this == Month.February && ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0);
        fun Month.numDays(year: Int) : Int = if (month == ordinal && Companion.year == year) day else (numDays + if (isLeapYear(year)) 1 else 0)
        fun monthsForYear(year: Int) : List<Month> = if (Companion.year == year) Month.values().toList().subList(0, month + 1) else Month.values().toList()

        fun is13(mmddyyyy: String) : Boolean {
            val (mm, dd, yyyy) = mmddyyyy.split('/').map { Integer.parseInt(it) }

            if (year < yyyy + 13) return false
            if (year > yyyy + 13) return true
            if (month + 1 > mm) return false
            if (month + 1 < mm) return true
            return day <= dd
        }

        fun is18(mmddyyyy: String) : Boolean {
            val (mm, dd, yyyy) = mmddyyyy.split('/').map { Integer.parseInt(it) }

            if (year < yyyy + 18) return false
            if (year > yyyy + 18) return true
            if (month + 1 > mm) return false
            if (month + 1 < mm) return true
            return day <= dd
        }

        fun is21(mmddyyyy: String) : Boolean {
            val (mm, dd, yyyy) = mmddyyyy.split('/').map { Integer.parseInt(it) }

            if (year < yyyy + 21) return false
            if (year > yyyy + 21) return true
            if (month + 1 > mm) return false
            if (month + 1 < mm) return true
            return day <= dd
        }
    }
}


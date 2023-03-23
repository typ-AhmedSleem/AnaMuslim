/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */
package com.typ.muslim.features.calendar

import android.content.Context
import androidx.annotation.IntRange
import com.typ.muslim.R
import com.typ.muslim.core.ummelqura.UmmalquraCalendar
import com.typ.muslim.core.ummelqura.UmmalquraGregorianConverter
import com.typ.muslim.managers.ResMan
import com.typ.muslim.features.calendar.models.HijriDate
import com.typ.muslim.models.Timestamp
import java.util.*

/**
 * Contains necessary code that works with Hijri Dates
 */
object HijriCalendar {

    @JvmStatic
    fun getHijriMonthName(context: Context?, @IntRange(from = 0, to = 11) monthNumber: Int): String {
        return ResMan.getStringArray(context, R.array.HijriMonthsNames)[monthNumber]
    }

    fun getHijriDate(year: Int, month: Int, day: Int): HijriDate {
        return HijriDate(year, month, day)
    }

    @JvmStatic
    val today: HijriDate
        get() = Timestamp.NOW().toHijri()

    @JvmStatic
    fun getHijriDates(dates: List<Date?>): List<HijriDate?> {
        val hijriDates: MutableList<HijriDate?> = ArrayList()
        for (date in dates) if (date != null) hijriDates.add(toHijri(Timestamp(date)))
        return hijriDates
    }

    @JvmStatic
    fun toHijri(date: Timestamp?): HijriDate? {
        if (date != null) {
            val hDate = UmmalquraGregorianConverter.toHijri(date)
            return HijriDate(hDate[0], hDate[1], hDate[2])
        }
        return null
    }

    @JvmStatic
    fun toGregorian(hijriDate: HijriDate?): Timestamp? {
        if (hijriDate != null) {
            val gDate = UmmalquraGregorianConverter.toGregorian(hijriDate.year, hijriDate.month, hijriDate.day)
            return Timestamp(gDate[0], gDate[1], gDate[2])
        }
        return null
    }

    /**
     * @param hMonth Zero-based month number
     */
    @JvmStatic
    fun lengthOfMonth(hYear: Int, @IntRange(from = 0, to = 11) hMonth: Int): Int {
        return UmmalquraCalendar.lengthOfMonth(hYear, hMonth)
    }
}
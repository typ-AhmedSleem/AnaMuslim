/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */
package com.typ.muslim.features.calendar.models

import android.content.Context
import com.typ.muslim.app.AnaMuslimApp
import com.typ.muslim.features.calendar.HijriCalendar.getHijriMonthName
import com.typ.muslim.features.calendar.HijriCalendar.toGregorian
import com.typ.muslim.models.Timestamp
import java.io.Serializable
import java.util.*

/**
 * Model class representing HijriDate
 */
open class HijriDate(
    val year: Int,
    /**
     * Zero-based Hijri Month index. 0 to 11
     */
    val month: Int,
    /**
     * Hijri Day of this month
     */
    val day: Int
) : Serializable {

    val monthName: String
        get() = getMonthName(AnaMuslimApp.getContext().get())

    fun getMonthName(context: Context?): String {
        return getHijriMonthName(context, month)
    }

    fun toGregorian(): Timestamp? {
        return toGregorian(this)
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (this === other) return true
        if (other !is HijriDate) return false
        return day == other.day && month == other.month && year == other.year
    }

    fun isAfter(another: HijriDate?): Boolean {
        return if (another == null) false else day + month + year - (another.day + another.month + another.year) > 0
    }

    fun isBefore(another: HijriDate?): Boolean {
        return if (another == null) false else day + month + year - (another.day + another.month + another.year) < 0
    }

    override fun hashCode(): Int {
        return Objects.hash(day, month, year)
    }

    override fun toString(): String {
        return "HijriDate{" +
                "day=" + day +
                ", month=(" + monthName + " | " + month +
                "), year=" + year +
                '}'
    }
}
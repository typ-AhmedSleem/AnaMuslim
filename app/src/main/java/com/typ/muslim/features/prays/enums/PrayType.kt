package com.typ.muslim.features.prays.enums

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.typ.muslim.R
import com.typ.muslim.models.Timestamp
import java.util.Calendar

enum class PrayType {
    FAJR,
    SUNRISE,
    DHUHR,
    ASR,
    MAGHRIB,
    ISHA;

    @get:StringRes
    val prayNameRes: Int
        get() = if (ordinal == 1) {
            R.string.sunrise
        } else if (ordinal == 2) {
            if (Timestamp.NOW().dayOfWeek == Calendar.FRIDAY) R.string.jumaa_pray else R.string.dhuhr_pray
        } else if (ordinal == 3) {
            R.string.asr_pray
        } else if (ordinal == 4) {
            R.string.maghrib_pray
        } else if (ordinal == 5) {
            R.string.isha_pray
        } else {
            R.string.fajr_pray
        }

    @get:ColorRes
    val surfaceColorRes: Int
        get() = when (this) {
            FAJR -> if (Timestamp.NOW().hour < 23) R.color.color_isha_bg else R.color.color_fajr_header
            SUNRISE -> R.color.color_dhuhr_sunrise_highlight
            DHUHR -> R.color.color_dhuhr_sunrise_bg
            ASR -> R.color.color_dhuhr_sunrise_header
            MAGHRIB -> R.color.color_maghrib_isha_header
            else -> R.color.color_isha_bg
        }

    @get:ColorRes
    val onSurfaceColorRes: Int
        get() = when (this) {
            MAGHRIB, ISHA, FAJR, DHUHR -> R.color.yellow
            ASR, SUNRISE -> R.color.color_maghrib_isha_highlight
            else -> R.color.nextPrayCardSurfaceStartColor
        }

    @get:DrawableRes
    val iconRes: Int
        get() = when (this) {
            FAJR -> R.drawable.ic_sun
            SUNRISE -> R.drawable.ic_sun
            DHUHR -> R.drawable.ic_sun
            ASR -> R.drawable.ic_sun
            MAGHRIB -> R.drawable.ic_ramadan_moon
            else -> R.drawable.ic_moon
        }

    fun ordinalWithoutSunrise(): Int {
        return if (this == FAJR) 0 else ordinal - 1
    }

    companion object {
        @JvmStatic
        fun valueOf(ordinal: Int): PrayType {
            return if (ordinal == 1) SUNRISE else if (ordinal == 2) DHUHR else if (ordinal == 3) ASR else if (ordinal == 4) MAGHRIB else if (ordinal == 5) ISHA else FAJR
        }
    }
}

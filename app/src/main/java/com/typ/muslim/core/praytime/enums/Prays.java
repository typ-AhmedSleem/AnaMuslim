package com.typ.muslim.core.praytime.enums;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import com.typ.muslim.R;
import com.typ.muslim.models.Timestamp;

import java.util.Calendar;

public enum Prays {
    FAJR,
    SUNRISE,
    DHUHR,
    ASR,
    MAGHRIB,
    ISHA;

    public static Prays valueOf(int ordinal) {
        if (ordinal == 1) return SUNRISE;
        else if (ordinal == 2) return DHUHR;
        else if (ordinal == 3) return ASR;
        else if (ordinal == 4) return MAGHRIB;
        else if (ordinal == 5) return ISHA;
        else return FAJR;
    }

    @StringRes
    public int getPrayNameRes() {
        if (ordinal() == 1) return R.string.sunrise;
        else if (ordinal() == 2) return Timestamp.NOW().getDayOfWeek() == Calendar.FRIDAY ? R.string.jumaa : R.string.dhuhr;
        else if (ordinal() == 3) return R.string.asr;
        else if (ordinal() == 4) return R.string.maghrib;
        else if (ordinal() == 5) return R.string.isha;
        else return R.string.fajr;
    }

    @ColorRes
    public int getSurfaceColorRes() {
        switch (this) {
            case FAJR:
                return Timestamp.NOW().getHour() < 23 ? R.color.color_isha_bg : R.color.color_fajr_header;
            case SUNRISE:
                return R.color.color_dhuhr_sunrise_highlight;
            case DHUHR:
                return R.color.color_dhuhr_sunrise_bg;
            case ASR:
                return R.color.color_dhuhr_sunrise_header;
            case MAGHRIB:
                return R.color.color_maghrib_isha_header;
            default:
                return R.color.color_isha_bg;
        }
    }

    @ColorRes
    public int getOnSurfaceColorRes() {
        switch (this) {
            case MAGHRIB:
            case ISHA:
            case FAJR:
            case DHUHR:
                return R.color.yellow;
            case ASR:
            case SUNRISE:
                return R.color.color_maghrib_isha_highlight;
            default:
                return R.color.nextPrayCardSurfaceStartColor;
        }
    }

    @DrawableRes
    public int getIconRes() {
        switch (this) {
            case FAJR:
                return R.drawable.ic_sun;
            case SUNRISE:
                return R.drawable.ic_sun;
            case DHUHR:
                return R.drawable.ic_sun;
            case ASR:
                return R.drawable.ic_sun;
            case MAGHRIB:
                return R.drawable.ic_ramadan_moon;
            default:
                return R.drawable.ic_moon;
        }
    }

    public int ordinalWithoutSunrise() {
        if (this == FAJR) return 0;
        else return ordinal() - 1;
    }

}

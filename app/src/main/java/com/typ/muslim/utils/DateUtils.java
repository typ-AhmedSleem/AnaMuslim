/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.utils;

import android.content.Context;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.typ.muslim.R;
import com.typ.muslim.app.AnaMuslimApp;
import com.typ.muslim.managers.ResMan;
import com.typ.muslim.models.Timestamp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Util Code that works with Date and Time
 */
public class DateUtils {

    /**
     * @return DateFormat with pattern [hh:mm aa]. eg: 3:25 pm
     */
    public static DateFormat dfTime12SX() {
        return new SimpleDateFormat("hh:mm aa", Locale.getDefault());
    }

    /**
     * @return DateFormat with pattern [hh:mm]. eg: 3:25
     */
    public static DateFormat dfTime12NSX() {
        return new SimpleDateFormat("hh:mm", Locale.getDefault());
    }

    /**
     * @return DateFormat with pattern [HH:mm]. eg: 15:30
     */
    public static DateFormat dfTime24() {
        return new SimpleDateFormat("HH:mm", Locale.getDefault());
    }

    /**
     * @return DateFormat with pattern [hh:mm aa dd/MM/yyyy]. eg: 01 Aug 2001
     */
    public static DateFormat dfDateNormal() {
        return new SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault());
    }

    /**
     * @return DateFormat with pattern [hh:mm aa dd/MMM/yyyy]. eg: 01 Aug 2001
     */
    public static DateFormat dfDateShortMonth() {
        return new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    }

    /**
     * @return DateFormat with pattern [hh:mm aa ddd/MMM/yyyy]. eg: Sat Aug 2001
     */
    public static DateFormat dfDateShortDayMonth() {
        return new SimpleDateFormat("ddd MMM yyyy", Locale.getDefault());
    }

    /**
     * @return DateFormat with pattern [hh:mm aa dd/MM/yyyy] if with suffix. [hh:mm dd/MM/yyyy] otherwise. eg: WithSuffix[3:25 pm 01/08/2001] - WithoutSuffix[3:25 01/08/2001]
     */
    public static DateFormat dfDateTimeSNX(boolean withSuffix) {
        return withSuffix ? new SimpleDateFormat("hh:mm aa dd/MM/yyyy", Locale.getDefault()) : new SimpleDateFormat("hh:mm dd/MM/yyyy ", Locale.getDefault());
    }

    /**
     * @param pattern The Pattern to use in DateFormat instance
     * @return DateFormat with custom pattern in params
     */
    public static DateFormat dfCustomPattern(@NonNull String pattern) {
        return new SimpleDateFormat(pattern, Locale.getDefault());
    }

    /**
     * Gets the length of this month in days.
     * <p>
     * This takes a flag to determine whether to return the length for a leap year or not.
     * <p>
     * February has 28 days in a standard year and 29 days in a leap year.
     * April, June, September and November have 30 days.
     * All other months have 31 days.
     *
     * @param leapYear true if the length is required for a leap year
     * @return the length of this month in days, from 28 to 31
     */
    public static int getMonthLength(int month, boolean leapYear) {
        switch (month) {
            case 2:
                return leapYear ? 29 : 28;
            case 3:
            case 6:
            case 9:
            case 11:
                return 30;
            default:
                return 31;
        }
    }

    public static int getWeeksOfMonth(@IntRange(from = 1, to = 12) int monthNumber) {
        return monthNumber == 2 ? 3 : 4;
    }

    public static int getWeekLength(int weekNumber, int monthNumber) {
        // TODO: 6/19/2021 to be completed
        return 7;
    }

    public static String toDuration(int minutes) {
        int hours = 0;
        while (minutes >= 60) {
            hours++;
            minutes -= 60;
        }
        if (hours == 0) return String.format(Locale.ENGLISH, "%d Minute", minutes);
        else if (minutes == 0) return String.format(Locale.ENGLISH, "%d Hour%s", hours, hours > 1 ? "s" : "");
        else return String.format(Locale.ENGLISH, "%dH %dMin", hours, minutes);
    }

    public static boolean isToday(Date date) {
        if (date == null) return false;
        Calendar cal = Calendar.getInstance();
        Calendar thatDate = Calendar.getInstance();
        thatDate.setTime(date);
        return cal.get(Calendar.YEAR) == thatDate.get(Calendar.YEAR) &&
                cal.get(Calendar.MONTH) == thatDate.get(Calendar.MONTH) &&
                cal.get(Calendar.DATE) == thatDate.get(Calendar.DATE);
    }

    public static boolean isFriday(Timestamp when) {
        return when.getDayOfWeek() == Calendar.FRIDAY;
    }

    public static boolean isTodayFriday() {
        return isFriday(Timestamp.NOW());
    }

    /**
     * @param format "F" -> Full | "3" -> 3 Letters | "1" -> 1 Letter
     */
    public static String getTodayName(String format) {
        return getDayName(Timestamp.NOW(), format);
    }

    /**
     * @param format "F" -> Full | "3" -> 3 Letters | "1" -> 1 Letter
     */
    public static String getDayName(Timestamp when, String format) {
        if (when.getDayOfWeek() == Calendar.FRIDAY && format.equals("F")) {
            final Context c = AnaMuslimApp.getContext().get();
            // Today is Jum'a
            return ResMan.getString(c, R.string.jumaa);
        } else {
            final String pattern = format == "3" ? "EE" : format == "1" ? "EEEEE" : "EEEE";
            return new SimpleDateFormat(pattern, Locale.getDefault()).format(when.asDate());
        }
    }

}


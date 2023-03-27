/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.models;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.typ.muslim.enums.FormatPatterns;
import com.typ.muslim.features.calendar.HijriCalendar;
import com.typ.muslim.features.calendar.models.HijriDate;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * Model class that holds and does all operations that has time and date on it.
 * It can...
 * 1.Get or Set a field.
 * 2.Get timestamp as Date object.
 * 3.Get timestamp as Calendar instance.
 * 4.Get formatted timestamp according to given pattern.
 * 5.Compare to another timestamp in date or time or both
 */
public class Timestamp implements Serializable {

    private final Calendar cal;

    Timestamp() {
        this.cal = Calendar.getInstance();
    }

    public Timestamp(Date date) {
        this();
        this.cal.setTime(date);
    }

    public Timestamp(long timestampInMillis) {
        this();
        this.cal.setTimeInMillis(timestampInMillis);
    }

    public Timestamp(int year, int month, int day) {
        this();
        this.cal.set(year, month, day);
    }

    public Timestamp(int year, int month, int day, int hour, int minutes, int seconds) {
        this(year, month, day);
        this.cal.set(Calendar.HOUR_OF_DAY, hour);
        this.cal.set(Calendar.MINUTE, minutes);
        this.cal.set(Calendar.SECOND, seconds);
    }

    public static Timestamp YESTERDAY() {
        return Timestamp.NOW().roll(Calendar.DATE, -1);
    }

    public static Timestamp NOW() {
        return new Timestamp();
    }

    public static Timestamp TOMORROW() {
        return Timestamp.NOW().roll(Calendar.DATE, 1);
    }

    @IntRange(from = 0, to = 60)
    public int getSecond() {
        return cal.get(Calendar.SECOND);
    }

    @IntRange(from = 0, to = 59)
    public int getMinute() {
        return cal.get(Calendar.MINUTE);
    }

    @IntRange(from = 0, to = 23)
    public int getHour() {
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    @IntRange(from = 1, to = 31)
    public int getDay() {
        return cal.get(Calendar.DATE);
    }

    @IntRange(from = 1, to = 12)
    public int getMonth() {
        return cal.get(Calendar.MONTH) + 1;
    }

    public int getYear() {
        return cal.get(Calendar.YEAR);
    }

    public long toMillis() {
        return cal.getTimeInMillis();
    }

    public Calendar asCalendar() {
        return this.cal;
    }

    public Date asDate() {
        return asCalendar().getTime();
    }

    /**
     * @apiNote First day of the week is SUNDAY by default.
     */
    @IntRange(from = 1, to = 7)
    public int getDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(toMillis());
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public boolean hasSameDayOf(Timestamp another) {
        if (another == null) return false;
        return getDay() == another.getDay();
    }

    public boolean hasSameMonthOf(Timestamp another) {
        if (another == null) return false;
        return getMonth() == another.getMonth();
    }

    public boolean hasSameYearOf(Timestamp another) {
        if (another == null) return false;
        return getYear() == another.getYear();
    }

    public boolean matches(Timestamp another) {
        if (another == null) return false;
        return dateMatches(another) && timeMatches(another);
    }

    public boolean dateMatches(Timestamp another) {
        if (another == null) return false;
        return getDay() == another.getDay() &&
                getMonth() == another.getMonth() &&
                getYear() == another.getYear();
    }

    public boolean timeMatches(Timestamp another) {
        if (another == null) return false;
        return getMinute() == another.getMinute() &&
                getHour() == another.getHour();
    }

    public boolean isAfter(Timestamp another) {
        if (another == null) return false;
        return isAfter(another.toMillis());
    }

    public boolean isAfter(long timestamp) {
        return this.toMillis() > timestamp;
    }

    public boolean isBefore(Timestamp another) {
        if (another == null) return false;
        return isBefore(another.toMillis());
    }

    public boolean isBefore(long timestamp) {
        return this.toMillis() < timestamp;
    }

    public String getFormatted(FormatPatterns pattern) {
        return pattern.format(this);
    }

    public Timestamp roll(int field, int amount) {
        this.cal.add(field, amount);
        return this;
    }

    public Timestamp set(int field, int newValue) {
        this.cal.set(field, newValue);
        return this;
    }

    public boolean isLastMonth() {
        final Timestamp lastMonthTimestamp = Timestamp.NOW().roll(Calendar.MONTH, -1);
        return hasSameYearOf(lastMonthTimestamp) && hasSameMonthOf(lastMonthTimestamp);
    }

    public boolean isInThisMonth() {
        return hasSameYearOf(Timestamp.NOW()) && hasSameMonthOf(Timestamp.NOW());
    }

    public boolean isNextMonth() {
        final Timestamp nextMonthTimestamp = Timestamp.NOW().roll(Calendar.MONTH, 1);
        return hasSameYearOf(nextMonthTimestamp) && hasSameMonthOf(nextMonthTimestamp);
    }

    public boolean isYesterday() {
        return dateMatches(Timestamp.YESTERDAY());
    }

    public boolean isToday() {
        return dateMatches(Timestamp.NOW());
    }

    public boolean isTomorrow() {
        return dateMatches(Timestamp.TOMORROW());
    }

    @Override
    public boolean equals(Object another) {
        if (this == another) return true;
        if (another == null || getClass() != another.getClass()) return false;
        return matches((Timestamp) another);
    }

    public int get(int field) {
        return cal.get(field);
    }

    public HijriDate toHijri() {
        return HijriCalendar.toHijri(this);
    }

    @NonNull
    public Timestamp clone() {
        return new Timestamp(this.toMillis());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMinute(), getHour(), getDay(), getMonth(), getYear());
    }

    @NonNull
    @Override
    public String toString() {
        return "Timestamp{ " + getFormatted(FormatPatterns.DATETIME_FULL) + " }";
    }
}

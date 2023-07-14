/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.features.ramadan.models;

import android.content.Context;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.typ.muslim.enums.FormatPattern;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.features.calendar.HijriCalendar;
import com.typ.muslim.features.prays.PrayerManager;
import com.typ.muslim.models.Period;
import com.typ.muslim.features.prays.models.PrayTimes;
import com.typ.muslim.models.Timestamp;

import java.util.concurrent.TimeUnit;

public class Ramadan {

    private final Timestamp startsIn;
    private final Timestamp endsIn;

    public Ramadan(Timestamp startsIn, Timestamp endsIn) {
        this.startsIn = startsIn;
        this.endsIn = endsIn;
        AManager.log("Ramadan", "%s | %s", startsIn.getFormatted(FormatPattern.DATE_NORMAL),
                endsIn.getFormatted(FormatPattern.DATE_NORMAL));
    }

    public Timestamp getStartsIn() {
        return startsIn;
    }

    public Timestamp getEndsIn() {
        return endsIn;
    }

    public Imsakeya getImsakeya(Context context) {
        return new Imsakeya();
    }

    public Period getPeriodTillStart() {
        return Period.between(Timestamp.NOW().toMillis(), startsIn.toMillis());
    }

    public Period getPeriodTillEnd() {
        return Period.between(Timestamp.NOW().toMillis(), endsIn.toMillis());
    }

    public int getRemainingDays() {
        return (int) TimeUnit.MILLISECONDS.toDays(this.endsIn.toMillis() - Timestamp.NOW().toMillis());
    }

    /**
     * Returns the day of ramadan number representing today
     *
     * @apiNote This method always returns an int (from 1 to 31)) so developer should always care to call this method when he is sure curr month is Ramadan
     */
    @IntRange(from = 1, to = 30)
    public int getTodayNumber() {
        return HijriCalendar.getToday().getDay();
    }

    public boolean isPassed() {
        return endsIn.isBefore(Timestamp.NOW());
    }

    /**
     * Constructs a {@link RamadanDay} model which representing today in ramadan
     *
     * @apiNote This method never returns null result because as developer should always care to call this method when he is sure curr month is Ramadan
     */
    @NonNull
    public RamadanDay getToday(Context context) {
        // fixme: Get day times from getImsakeya not direct getPrays
        final int todayNum = getTodayNumber();
//		final Timestamp todayTimestamp = todayNum == 1 ? startsIn : startsIn.clone().roll(DATE, todayNum - 1);
        PrayTimes todayTimes = PrayerManager.getTodayPrays(context);
        return new RamadanDay(todayNum,
                todayTimes.getFajr().time,
                todayTimes.getMaghrib().time);
    }

    @Override
    public String toString() {
        return "Ramadan{" +
                "startsIn=" + startsIn.getFormatted(FormatPattern.DATE_SHORT) +
                "endsIn=" + endsIn.getFormatted(FormatPattern.DATE_SHORT) +
                '}';
    }

}

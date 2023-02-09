/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.enums;

import android.content.Context;
import android.graphics.Typeface;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.mpt.android.stv.Slice;
import com.typ.muslim.R;
import com.typ.muslim.features.quran.Quran;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.managers.ResMan;
import com.typ.muslim.models.Timestamp;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public enum KhatmaPlans {

    // todo: add more plans like [15,20,40,60,CUSTOM] days

    PLAN_10_DAYS(10), // 3 Parts a day
    PLAN_15_DAYS(15), // 2 Parts a day
    PLAN_30_DAYS(30), // 1 Part a day
    PLAN_60_DAYS(60); // 0.5 Part a day

    /**
     * Duration in Days
     */
    final int duration;

    KhatmaPlans(int duration) {
        this.duration = duration;
    }

    @NonNull
    public static KhatmaPlans valueOf(int ordinal) {
        for (KhatmaPlans plan : values()) if (plan.ordinal() == ordinal) return plan;
        return PLAN_30_DAYS; // Default is 30 days plan.
    }

    /**
     * Khatma duration in days
     */
    public int getDuration() {
        return duration;
    }

    public int getTodayNumber(Timestamp startedIn) {
        final Timestamp now = Timestamp.NOW();
        final Timestamp expectedPlanEnd = getExpectedEnd(startedIn);
        // Log operation
        AManager.log("Khatma", "getTodayNumber: START[%s] | DAYS[%d] | END[%s]",
                startedIn.getFormatted(FormatPatterns.DATE_NORMAL),
                TimeUnit.MILLISECONDS.toDays(expectedPlanEnd.toMillis() - now.toMillis()),
                expectedPlanEnd.getFormatted(FormatPatterns.DATE_NORMAL));
        // Return today number
        if (now.isAfter(expectedPlanEnd)) return -1;
        return duration - (int) TimeUnit.MILLISECONDS.toDays(expectedPlanEnd.toMillis() - now.toMillis()) + 1;
    }

    /**
     * Gets the expected end timestamp for this khatma plan
     */
    public Timestamp getExpectedEnd(Timestamp startedIn) {
        final Timestamp startDate = startedIn == null ? Timestamp.NOW() : new Timestamp(startedIn.toMillis());
        AManager.log("Khatma", "getExpectedEndDate: INPUT[%s]", startDate);
        return startDate.roll(Calendar.DATE, duration);
    }

    /**
     * Get the number of parts this plan provides for every single day
     */
    public int getWerdPartsNumberPerDay() {
        return Quran.QURAN_JUZ2S_COUNT / duration;
    }

    public Slice buildPlanCurrStepTextSlices(Context c, int dayNumber, @ColorInt int textColor, int textSize) {
        return new Slice.Builder(ResMan.getString(c, R.string.day) + " " + dayNumber)
                .textColor(textColor)
                .textSize(textSize)
                .style(Typeface.BOLD)
                .build();
    }

    public Slice buildPlanTargetTextSlice(@ColorInt int textColor, int textSize) {
        return new Slice.Builder(" /" + this.duration)
                .textColor(textColor)
                .textSize(textSize)
                .build();
    }

}

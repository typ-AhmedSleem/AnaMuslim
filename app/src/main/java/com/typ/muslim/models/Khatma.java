/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.models;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.typ.muslim.R;
import com.typ.muslim.app.Consumers;
import com.typ.muslim.enums.FormatPatterns;
import com.typ.muslim.enums.KhatmaPlans;
import com.typ.muslim.features.quran.Quran;
import com.typ.muslim.features.quran.models.QuranAyah;
import com.typ.muslim.features.quran.models.QuranPart;
import com.typ.muslim.interfaces.Refreshable;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.managers.ResMan;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Model class representing Khatma
 */
public class Khatma implements Serializable, Refreshable {

    /**
     * Auto Increased number in database
     */
    private final int number;
    private final int werdLength;
    private final String name;
    private final KhatmaPlans plan;
    private final Timestamp startedIn;
    private final Timestamp expectedEnd;
    private final Timestamp remindIn;
    private @IntRange(from = 0, to = 60)
    int completedWerds;
    private Werd currentWerd;
    private int todayNumber = -2; // -1 means out of plan | -2 means not set yet.

    /**
     * Default constructor for creating a new Khatma model without reminder
     */
    public Khatma(KhatmaPlans plan, String name) {
        this(plan, name, null);
    }

    /**
     * Default constructor for creating a new Khatma model with reminder
     */
    public Khatma(KhatmaPlans plan, String name, @Nullable Timestamp remindIn) {
        this.number = 0;
        this.name = name;
        this.plan = plan;
        this.completedWerds = 0;
        this.remindIn = remindIn;
        this.werdLength = plan.getWerdPartsNumberPerDay();
        this.startedIn = Timestamp.NOW();
        this.todayNumber = 1;
        this.expectedEnd = getExpectedEnd();
        this.currentWerd = getCurrentWerd();
    }

    /**
     * Constructor used while constructing an existing Khatma model received from database
     */
    public Khatma(int number, String name, KhatmaPlans plan, int completedWerds, Timestamp startedIn, Timestamp remindIn) {
        this.number = number;
        this.name = name;
        this.plan = plan;
        this.remindIn = remindIn;
        this.startedIn = startedIn;
        this.werdLength = plan.getWerdPartsNumberPerDay();
        this.completedWerds = completedWerds;
        this.todayNumber = getTodayNumber();
        this.expectedEnd = getExpectedEnd();
        if (hasWerdsRemaining()) this.currentWerd = getRefreshedCurrentWerd();
        AManager.log("Khatma", toString());
    }

    @Nullable
    public String getName() {
        return name;
    }

    public String getName(Context context) {
        if (number == 0 && TextUtils.isEmpty(name)) return ResMan.getString(context, R.string.untitled_khatma);
        return Consumers.returnWhen(TextUtils.isEmpty(name), String.format(Locale.getDefault(), "%s %d", ResMan.getString(context, R.string.khatma_number), number + 1), name);
    }

    @Deprecated
    public int getTodayNumber() {
        if (todayNumber >= -1) return todayNumber;
        final Timestamp now = Timestamp.NOW();
        final Timestamp expectedPlanEnd = getExpectedEnd();
        // Log operation
        AManager.log("Khatma", "getTodayNumber: START[%s] | DAYS[%d] | END[%s]",
                startedIn.getFormatted(FormatPatterns.DATE_NORMAL),
                TimeUnit.MILLISECONDS.toDays(expectedPlanEnd.toMillis() - now.toMillis()),
                expectedPlanEnd.getFormatted(FormatPatterns.DATE_NORMAL));
        // Return today number
        if (now.isAfter(expectedPlanEnd)) return -1;
        return plan.getDuration() - (int) TimeUnit.MILLISECONDS.toDays(expectedPlanEnd.toMillis() - now.toMillis()) + 1;
    }

    public int getWerdLength() {
        return werdLength;
    }

    /**
     * Gets the expected end timestamp for this khatma plan
     */
    public Timestamp getExpectedEnd() {
        if (expectedEnd != null) return expectedEnd;
        final Timestamp startDate = startedIn == null ? Timestamp.NOW() : new Timestamp(startedIn.toMillis());
        return startDate.roll(Calendar.DATE, plan.getDuration());
    }

    public int getNumber() {
        return number;
    }

    public Timestamp getStartedIn() {
        return startedIn;
    }

    /**
     * 0 <= Completed progress <= 100
     */
    public int getProgressPercentage() {
        return Math.min(Math.round((completedWerds * 100f) / plan.getDuration()), 100); // <= 100
    }

    public int getCompletedWerds() {
        return completedWerds;
    }

    public boolean isActive() {
        return getExpectedEnd().isAfter(Timestamp.NOW()) && completedWerds < plan.getDuration();
    }

    public KhatmaPlans getPlan() {
        return plan;
    }

    public Werd getCurrentWerd() {
        return this.currentWerd;
    }

    private Werd getRefreshedCurrentWerd() {
        final QuranPart startPart = Quran.getJuz2((completedWerds * werdLength) + 1);
        final QuranPart endPart = Quran.getJuz2((completedWerds * werdLength) + werdLength);
        return new Werd(startPart.getStart(), endPart.getEnd());
    }

    public boolean hasWerdsRemaining() {
        return plan.getDuration() > completedWerds;
    }

    public Werd getNextWerd() {
        if (!hasWerdsRemaining()) return null;
        final QuranPart startPart = Quran.getJuz2(((completedWerds + 1) * werdLength) + 1);
        final QuranPart endPart = Quran.getJuz2(((completedWerds + 1) * werdLength) + werdLength);
        return new Werd(startPart.getStart(), endPart.getEnd());
    }

    public Timestamp getRemindIn() {
        return remindIn;
    }

    public boolean hasReminder() {
        return this.remindIn != null;
    }

    public void saveProgress() {
        this.completedWerds++;
        if (hasWerdsRemaining()) this.currentWerd = getRefreshedCurrentWerd();
    }

    @Override
    public void refresh() {
    }

    public String toString(Context context) {
        return "Khatma{" +
                "khatmaNumber=" + number +
                ", name=" + name +
                ", completedDays=" + completedWerds +
                ", completedPercentage=" + getProgressPercentage() +
                ", plan=" + plan +
                ", startedIn=" + startedIn.getFormatted(FormatPatterns.DATE_NORMAL) +
                ", endsIn=" + expectedEnd.getFormatted(FormatPatterns.DATE_NORMAL) +
                ", currStep=" + (getCurrentWerd() != null ? getCurrentWerd().start.toString(context) : null) +
                ", nextStep=" + (getCurrentWerd() != null ? getCurrentWerd().end.toString(context) : null) +
                ", remindIn=" + remindIn.getFormatted(FormatPatterns.DATETIME_FULL) +
                ", hasReminder=" + hasReminder() +
                ", isActive=" + isActive() +
                ", todayNumber=" + todayNumber +
                '}';
    }

    @NonNull
    @Override
    public String toString() {
        return "Khatma{" +
                "khatmaNumber=" + number +
                ", name=" + name +
                ", completedDays=" + completedWerds +
                ", completedPercentage=" + getProgressPercentage() +
                ", plan=" + plan +
                ", startedIn=" + startedIn.getFormatted(FormatPatterns.DATE_NORMAL) +
                ", endsIn=" + expectedEnd.getFormatted(FormatPatterns.DATE_NORMAL) +
                ", remindIn=" + (hasReminder() ? remindIn.getFormatted(FormatPatterns.DATETIME_FULL) : "NO_REMINDER") +
                ", isActive=" + isActive() +
                ", todayNumber=" + todayNumber +
                ", step=" + werdLength +
                '}';
    }

    public static class Werd implements Serializable {

        private final QuranAyah start;
        private final QuranAyah end;

        public Werd(QuranAyah start, QuranAyah end) {
            this.start = start;
            this.end = end;
        }

        public Werd(int fromAyah, int fromSurah, int toAyah, int toSurah) {
            this.start = new QuranAyah(fromAyah, fromSurah, "");
            this.end = new QuranAyah(toAyah, toSurah, "");
        }

        public QuranAyah getStart() {
            return start;
        }

        public QuranAyah getEnd() {
            return end;
        }

        @NotNull
        @Override
        public String toString() {
            return "Step{" +
                    "start=" + start +
                    ", end=" + end +
                    '}';
        }
    }

}

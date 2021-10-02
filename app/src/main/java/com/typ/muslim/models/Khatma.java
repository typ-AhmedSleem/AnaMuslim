/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.models;

import android.content.Context;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.typ.muslim.Constants;
import com.typ.muslim.enums.FormatPatterns;
import com.typ.muslim.enums.KhatmaPlans;
import com.typ.muslim.interfaces.Refreshable;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.models.quran.QuranAyah;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Model class representing Khatma
 */
public class Khatma implements Serializable, Refreshable {

	/**
	 * Auto Increased number in database
	 */
	private final int number;
	private final float step;
	private final String name;
	private final KhatmaPlans plan;
	private final Timestamp startedIn;
	private final Timestamp expectedEnd;
	private final Timestamp remindIn;
	private @IntRange(from = 0, to = 60) int completedDays;
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
		this.completedDays = 0;
		this.remindIn = remindIn;
		this.step = plan.stepPerDay();
		this.startedIn = Timestamp.NOW();
		this.todayNumber = 1;
		this.expectedEnd = getExpectedEnd();
	}

	/**
	 * Constructor used while constructing an existing Khatma model received from database
	 */
	public Khatma(int number, String name, KhatmaPlans plan, int completedDays, Timestamp startedIn, Timestamp remindIn) {
		this.number = number;
		this.name = name;
		this.plan = plan;
		this.remindIn = remindIn;
		this.startedIn = startedIn;
		this.step = plan.stepPerDay();
		this.completedDays = completedDays;
		this.todayNumber = getTodayNumber();
		this.expectedEnd = getExpectedEnd();
		AManager.log("Khatma", toString());
	}

	public String getName() {
		return name;
	}

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

	/**
	 * Gets the expected end timestamp for this khatma plan
	 */
	public Timestamp getExpectedEnd() {
		if (expectedEnd != null) return expectedEnd;
		final Timestamp startDate = startedIn == null ? Timestamp.NOW() : new Timestamp(startedIn.toMillis());
		AManager.log("Khatma", "getExpectedEndDate: INPUT[%s]", startDate);
		return startDate.roll(Calendar.DATE, plan.getDuration());
	}

	/**
	 * Get the number of Juz's this plan provides for every single day
	 */
	public float getStep() {
		return step;
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
		return Math.round((completedDays * 100f) / plan.getDuration());
	}

	/**
	 * Calculate the percentage of a single step
	 */
	public float getStepPercentage() {
		return (step * 100f) / ((float) Constants.QURAN_JUZ2S_COUNT);
	}

	public int getCompletedDays() {
		return completedDays;
	}

	public boolean isActive() {
		return getExpectedEnd().isAfter(Timestamp.YESTERDAY()) && completedDays >= plan.getDuration();
	}

	public KhatmaPlans getPlan() {
		return plan;
	}

	public Step getCurrentStep() {
		// fixme: not yet completed
		if (getProgressPercentage() >= 100) {
			// Get daily step from
			final int fromAyah = 1;
			final int fromSurah = 1;
			final int toAyah = 100;
			final int toSurah = 2;
			return new Step(fromAyah,
					fromSurah,
					toAyah,
					toSurah);
		}
		return null;
	}

	private Step getNextStep() {
		// todo: Get the next step according to progress, plan target and currStep
		return null;
	}

	public Timestamp getRemindIn() {
		return remindIn;
	}

	public boolean hasReminder() {
		return this.remindIn != null;
	}

	public void saveProgress() {
		this.completedDays++;
	}

	@Override
	public void refresh() {
		this.todayNumber = getTodayNumber();
	}

	public String toString(Context context) {
		return "Khatma{" +
				"khatmaNumber=" + number +
				", name=" + name +
				", completedDays=" + completedDays +
				", completedPercentage=" + getProgressPercentage() +
				", plan=" + plan +
				", startedIn=" + startedIn.getFormatted(FormatPatterns.DATE_NORMAL) +
				", endsIn=" + expectedEnd.getFormatted(FormatPatterns.DATE_NORMAL) +
				", currStep=" + (getCurrentStep() != null ? getCurrentStep().start.toString(context) : null) +
				", nextStep=" + (getCurrentStep() != null ? getCurrentStep().end.toString(context) : null) +
				", remindIn=" + remindIn.getFormatted(FormatPatterns.DATETIME_FULL) +
				", hasReminder=" + hasReminder() +
				", todayNumber=" + todayNumber +
				'}';
	}

	@NonNull
	@Override
	public String toString() {
		return "Khatma{" +
				"khatmaNumber=" + number +
				", name=" + name +
				", completedDays=" + completedDays +
				", completedPercentage=" + getProgressPercentage() +
				", plan=" + plan +
				", startedIn=" + startedIn.getFormatted(FormatPatterns.DATE_NORMAL) +
				", endsIn=" + expectedEnd.getFormatted(FormatPatterns.DATE_NORMAL) +
				", remindIn=" + (hasReminder() ? remindIn.getFormatted(FormatPatterns.DATETIME_FULL) : "NO_REMINDER") +
				", todayNumber=" + todayNumber +
				", step=" + step +
				'}';
	}

	public static class Step implements Serializable {

		private final QuranAyah start;
		private final QuranAyah end;

		public Step(int fromAyah, int fromSurah, int toAyah, int toSurah) {
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

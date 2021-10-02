/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.models;

import com.typ.muslim.managers.AManager;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class Period {

	private final int years;
	private final int months;
	private final int days;

	Period(int years, int months, int days) {
		this.years = Math.max(years, 0);
		this.months = Math.max(months, 0);
		this.days = Math.max(days, 0);
		AManager.log("Period", "Created " + toString());
	}

	@NotNull
	@Contract("_, _, _ -> new")
	public static Period of(int years, int months, int days) {
		return new Period(years, months, days);
	}

	@NotNull
	@Contract("_ -> new")
	public static Period ofYears(int years) {
		return new Period(years, 0, 0);
	}

	@NotNull
	@Contract("_ -> new")
	public static Period ofMonths(int months) {
		return new Period(0, months, 0);
	}

	@NotNull
	@Contract("_ -> new")
	public static Period ofDays(int days) {
		return new Period(0, 0, days);
	}

	@NotNull
	@Contract("_ -> new")
	public static Period from(long millis) {
		if (millis <= 0) return new Period(0, 0, -1);
		int years, months, days = (int) TimeUnit.MILLISECONDS.toDays(millis);
		// Get months count
		months = days / 30;
		days -= (months * 30);
		// Get years count
		years = months / 12;
		months -= years * 12;
		// Return the period
		AManager.log("Period", "INPUT[%d] -> Y[%d] M[%d] D[%d]",millis, years, months, days);
		return new Period(years, months, days);
	}

	@NotNull
	@Contract("_, _ -> new")
	public static Period between(long start, long end) {
		return from(end - start);
	}

	public int getYears() {
		return years;
	}

	public int getMonths() {
		return months;
	}

	public int getDays() {
		return days;
	}

	@Override
	public String toString() {
		return "Period{" +
		       "years=" + years +
		       ", months=" + months +
		       ", days=" + days +
		       '}';
	}
}

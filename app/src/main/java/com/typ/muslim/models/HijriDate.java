/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.models;

import android.content.Context;

import com.typ.muslim.app.AnaMuslimApp;
import com.typ.muslim.managers.HijriCalendar;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * Model class representing HijriDate
 */
public class HijriDate implements Serializable {

	/**
	 * Hijri Day of this month
	 */
	private final int day;
	/**
	 * Zero-based Hijri Month index. 0 to 11
	 */
	private final int month;
	/**
	 * Hijri year
	 */
	private final int year;

	public HijriDate(int year, int month, int day) {
		this.day = day;
		this.month = month;
		this.year = year;
	}

	public int getDay() {
		return day;
	}

	public int getMonth() {
		return month;
	}

	public int getYear() {
		return year;
	}

	public String getMonthName() {
		return getMonthName(AnaMuslimApp.getContext().get());
	}

	public String getMonthName(Context context) {
		return HijriCalendar.getHijriMonthName(context, this.month);
	}

	public Timestamp toGregorian() {
		return HijriCalendar.toGregorian(this);
	}

	@Override
	public boolean equals(Object another) {
		if (another == null) return false;
		if (this == another) return true;
		if (!(another instanceof HijriDate)) return false;
		HijriDate hijriDate = (HijriDate) another;
		return day == hijriDate.day &&
		       month == hijriDate.month &&
		       year == hijriDate.year;
	}

	public boolean isAfter(HijriDate another) {
		if (another == null) return false;
		return (day + month + year) - (another.day + another.month + another.year) > 0;
	}

	public boolean isBefore(HijriDate another) {
		if (another == null) return false;
		return (day + month + year) - (another.day + another.month + another.year) < 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(day, month, year);
	}

	@NotNull
	@Override
	public String toString() {
		return "HijriDate{" +
		       "day=" + day +
		       ", month=(" + getMonthName() + " | " + month +
		       "), year=" + year +
		       '}';
	}


}

/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.models;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.typ.muslim.enums.FormatPattern;
import com.typ.muslim.features.calendar.HijriCalendar;
import com.typ.muslim.features.calendar.models.HijriDate;

import java.io.Serializable;
import java.util.Objects;

public class IslamicEvent extends HijriDate implements Serializable {

	private final @StringRes int titleStringResId;
	private final @StringRes int descStringResId;
	private final @ColorRes int colorResId;

	public IslamicEvent(@StringRes int titleStringResId, @StringRes int descStringResId, HijriDate hijriDate, @ColorRes int colorResId) {
		super(hijriDate.getYear(), hijriDate.getMonth(), hijriDate.getDay());
		this.titleStringResId = titleStringResId;
		this.descStringResId = descStringResId;
		this.colorResId = colorResId;
	}

	public IslamicEvent(int hYear, int hMonth, int hDay, @StringRes int titleStringResId, @StringRes int descStringResId, @ColorRes int colorResId) {
		super(hYear, hMonth, hDay);
		this.titleStringResId = titleStringResId;
		this.descStringResId = descStringResId;
		this.colorResId = colorResId;
	}

	public @StringRes
	int getTitleStringResId() {
		return titleStringResId;
	}

	public int getDescStringResId() {
		return descStringResId;
	}

	@ColorRes
	public int getColorResId() {
		return colorResId;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if (this == o) return true;
		if (!(o instanceof IslamicEvent)) return false;
		return super.equals(o);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this);
	}

	@NonNull
	@Override
	public String toString() {
		return "IslamicEvent{" +
		       "eventHijriDate=" + super.toString() +
		       ", eventGregDate=" + HijriCalendar.toGregorian(this).getFormatted(FormatPattern.DATE_NORMAL) +
		       '}';
	}

}

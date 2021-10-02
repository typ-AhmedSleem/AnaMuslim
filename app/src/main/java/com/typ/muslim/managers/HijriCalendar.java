/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.managers;

import android.content.Context;

import androidx.annotation.IntRange;

import com.typ.muslim.R;
import com.typ.muslim.core.ummelqura.UmmalquraCalendar;
import com.typ.muslim.core.ummelqura.UmmalquraGregorianConverter;
import com.typ.muslim.models.HijriDate;
import com.typ.muslim.models.Timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Contains necessary code that works with Hijri Dates
 */
public class HijriCalendar {

	public static String getHijriMonthName(Context context, @IntRange(from = 0, to = 11) int monthNumber) {
		return AMRes.getStringArray(context, R.array.HijriMonthsNames)[monthNumber];
	}

	public static HijriDate getHijriDate(int year, int month, int day) {
		return new HijriDate(year, month, day);
	}

	public static HijriDate getToday() {
//		int[] rawHijri = UmmalquraGregorianConverter.toHijri(Timestamp.NOW());
//		return new HijriDate(rawHijri[0], rawHijri[1], rawHijri[2]);
		return Timestamp.NOW().toHijri();
	}

	public static List<HijriDate> getHijriDates(List<Date> dates) {
		List<HijriDate> hijriDates = new ArrayList<>();
		for (Date date : dates) if (date != null) hijriDates.add(toHijri(new Timestamp(date)));
		return hijriDates;
	}

	public static HijriDate toHijri(Timestamp date) {
		if (date != null) {
			int[] hDate = UmmalquraGregorianConverter.toHijri(date);
			HijriDate hijriDate = new HijriDate(hDate[0], hDate[1], hDate[2]);
			return hijriDate;
		}
		return null;
	}

	public static Timestamp toGregorian(HijriDate hijriDate) {
		if (hijriDate != null) {
			int[] gDate = UmmalquraGregorianConverter.toGregorian(hijriDate.getYear(), hijriDate.getMonth(), hijriDate.getDay());
			return new Timestamp(gDate[0], gDate[1], gDate[2]);
		}
		return null;
	}

	/**
	 * @param hMonth Zero-based month number
	 */
	public static int lengthOfMonth(int hYear, @IntRange(from = 0, to = 11) int hMonth) {
		return UmmalquraCalendar.lengthOfMonth(hYear, hMonth);
	}
}

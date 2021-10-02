/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ramadan;

import static com.typ.muslim.core.ummelqura.UmmalquraCalendar.RAMADAN;

import com.typ.muslim.managers.HijriCalendar;
import com.typ.muslim.models.HijriDate;
import com.typ.muslim.ramadan.models.Ramadan;

public class RamadanManager {

	/**
	 * Checks whether current hijri month is Ramadan or not
	 *
	 * @return {@code true} if only current hijri month is Ramadan. {@code false} if not.
	 */
	public static boolean isInRamadan() {
		return HijriCalendar.getToday().getMonth() == RAMADAN;
	}

	public static Ramadan getThisYearRamadan() {
		HijriDate todayHijri = HijriCalendar.getToday();
		final int ramadanLength = HijriCalendar.lengthOfMonth(todayHijri.getYear(), RAMADAN);
		return new Ramadan(new HijriDate(todayHijri.getYear(), RAMADAN, 1).toGregorian(),
				new HijriDate(todayHijri.getYear(), RAMADAN, ramadanLength).toGregorian());
	}

	public static Ramadan getNextRamadan() {
		if (isRamadanThisYearPassed()) {
			HijriDate todayHijri = HijriCalendar.getToday();
			final int ramadanLength = HijriCalendar.lengthOfMonth(todayHijri.getYear() + 1, RAMADAN);
			return new Ramadan(new HijriDate(todayHijri.getYear() + 1, RAMADAN, 1).toGregorian(),
					new HijriDate(todayHijri.getYear() + 1, RAMADAN, ramadanLength).toGregorian());
		}
		return getThisYearRamadan(); // return this year ramadan.
	}

	public static boolean isRamadanThisYearPassed() {
		return !isInRamadan();
	}

}

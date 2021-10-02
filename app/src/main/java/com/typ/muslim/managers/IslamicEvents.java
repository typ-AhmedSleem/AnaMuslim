/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.managers;

import static com.typ.muslim.core.ummelqura.UmmalquraCalendar.MUHARRAM;
import static com.typ.muslim.core.ummelqura.UmmalquraCalendar.RABI_THANI;
import static com.typ.muslim.core.ummelqura.UmmalquraCalendar.RAMADAN;
import static com.typ.muslim.core.ummelqura.UmmalquraCalendar.THUL_HIJJAH;
import static java.util.Calendar.YEAR;

import androidx.annotation.IntRange;

import com.typ.muslim.R;
import com.typ.muslim.core.ummelqura.UmmalquraCalendar;
import com.typ.muslim.libs.easyjava.interfaces.EasyListLooper;
import com.typ.muslim.models.HijriDate;
import com.typ.muslim.models.IslamicEvent;
import com.typ.muslim.models.Timestamp;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Contains all code that works with Islamic Events
 */
public class IslamicEvents {

	// Islamic Events of whole year
	private static List<IslamicEvent> islamicEvents;

	static {
		islamicEvents = getEventsThisYear();
	}

	static List<IslamicEvent> getEvents() {
		if (islamicEvents == null) islamicEvents = getEventsThisYear();
		return islamicEvents;
	}

	public static IslamicEvent getEventIn(HijriDate hijriDate) {
		if (hijriDate == null) return null;
		for (IslamicEvent event : islamicEvents) if (hijriDate.equals(event)) return event;
		return null;
	}

	public static IslamicEvent getNearestEventInEvents(List<IslamicEvent> events) {
		// Necessary check
		List<IslamicEvent> islamicEventList = events;
		if (islamicEventList == null) islamicEventList = getEventsThisYear();
		// Find nearest event to today
		HijriDate hijriToday = HijriCalendar.getToday();
		IslamicEvent event = null;
		for (IslamicEvent islamicEvent : islamicEventList) {
			if (islamicEvent.isAfter(hijriToday)) {
				event = islamicEvent;
				break;
			}
		}
		return event;
	}

	public static IslamicEvent getNearestEvent() {
		IslamicEvent nearestEventThisYear = getNearestEventInEvents(getEventsThisYear());
		// Return the very first event in the new hijri year if event this year is null
		return nearestEventThisYear != null ? nearestEventThisYear : getNearestEventInEvents(getEventsInYear(HijriCalendar.getToday().getYear() + 1, false)); // Return the nearest event.
	}

	public static List<IslamicEvent> getEventsThisYear() {
		return getEventsInYear(Timestamp.NOW().getYear(), false);
	}

	public static List<IslamicEvent> getEventsInYear(int year, boolean isHijriYear) {
		// TODO: 3/4/21 complete this by adding all islamic events of the year
		int hijriYear = isHijriYear ? year : HijriCalendar.toHijri(Timestamp.NOW().set(YEAR, year)).getYear();
		return Arrays.asList(
				new IslamicEvent(R.string.new_hijri_year, R.string.new_hijri_year, new HijriDate(hijriYear, MUHARRAM, 1), R.color.green),
				new IslamicEvent(R.string.birth_of_prophet_mohamed, R.string.birth_of_prophet_mohamed, new HijriDate(hijriYear, RABI_THANI, 21), R.color.cardBackground3),
				new IslamicEvent(R.string.eventRamadanStart, R.string.eventRamadanStart, new HijriDate(hijriYear, RAMADAN, 1), R.color.nextPrayCardSurfaceStartColor),
				new IslamicEvent(R.string.eventRamadanEnd, R.string.eventRamadanEnd, new HijriDate(hijriYear, RAMADAN, UmmalquraCalendar.lengthOfMonth(hijriYear, RAMADAN)), R.color.nextPrayCardSurfaceStartColor),
				new IslamicEvent(R.string.arafat_day, R.string.arafat_day, new HijriDate(hijriYear, THUL_HIJJAH, 9), R.color.yellow),
				new IslamicEvent(R.string.eid_al_adha_1st_day, R.string.eid_al_adha_1st_day, new HijriDate(hijriYear, THUL_HIJJAH, 10), R.color.orange),
				new IslamicEvent(R.string.eid_al_adha_2nd_day, R.string.eid_al_adha_2nd_day, new HijriDate(hijriYear, THUL_HIJJAH, 11), R.color.orange),
				new IslamicEvent(R.string.eid_al_adha_3rd_day, R.string.eid_al_adha_3rd_day, new HijriDate(hijriYear, THUL_HIJJAH, 12), R.color.orange),
				new IslamicEvent(R.string.eid_al_adha_last_day, R.string.eid_al_adha_last_day, new HijriDate(hijriYear, THUL_HIJJAH, 13), R.color.orange));
	}

	public static List<IslamicEvent> getEventsIn(List<HijriDate> hijriDates) {
		List<IslamicEvent> islamicEvents = new ArrayList<>();
		for (HijriDate hijriDate : hijriDates) islamicEvents.add(getEventIn(hijriDate));
		return islamicEvents;
	}

	@NotNull
	public static List<IslamicEvent> getEventsInMonth(List<IslamicEvent> events, @IntRange(from = 1, to = 12) int month) {
		if (events == null || events.isEmpty()) events = getEventsThisYear();
		List<IslamicEvent> monthEvents = new ArrayList<>();
		for (IslamicEvent event : events) if (event.getMonth() == month - 1) events.add(event);
		return monthEvents;
	}

	public static IslamicEvent searchForEventInMonth(List<IslamicEvent> events, EasyListLooper<IslamicEvent> condition) {
		if (events == null || events.isEmpty()) events = getEventsThisYear(); // fixme: check this line again
//		for ()
		return null;
	}

	public static IslamicEvent searchForEventInYear(List<IslamicEvent> events, EasyListLooper<IslamicEvent> condition) {
		if (events == null || events.isEmpty()) events = getEventsThisYear();
		IslamicEvent result = null;
		for (IslamicEvent event : events) {
			if (condition.validate(0, event)) {
				result = event;
				break;
			}
		}
		return result;
	}

	public static IslamicEvent searchForEventInYear(int year, boolean isHijriYear, EasyListLooper<IslamicEvent> condition) {
		return searchForEventInYear(getEventsInYear(isHijriYear ? year : Timestamp.NOW().set(YEAR, year).toHijri().getYear(), isHijriYear), condition);
	}

}

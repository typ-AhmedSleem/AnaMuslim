/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.calendar.dashboard;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;

import com.google.android.material.textview.MaterialTextView;
import com.typ.muslim.R;
import com.typ.muslim.features.calendar.HijriCalendar;
import com.typ.muslim.features.calendar.models.HijriDate;
import com.typ.muslim.managers.IslamicEvents;
import com.typ.muslim.managers.LocaleManager;
import com.typ.muslim.managers.ResMan;
import com.typ.muslim.models.IslamicEvent;
import com.typ.muslim.ui.calendar.HijriCalendarActivity;
import com.typ.muslim.ui.home.DashboardCard;
import com.typ.muslim.utils.DateUtils;

import org.jetbrains.annotations.NotNull;

public class HijriDashboardCard extends DashboardCard {

    // todo: Add textview to display today name e.g: Sunday, Jum3a
    // todo: Don't forget to replace Friday with Jum3a and Dhuhr pray with Jum3a Pray

    // Statics
    private static final String TAG = "HijriDashboardCard";
    // Runtime
    private HijriDate hijriDateToday;
    private IslamicEvent todayEvent;
    // Views
    private MaterialTextView tvDayName, tvHijriDay, tvHijriMonthYear, tvEventTitle;

    public HijriDashboardCard(Context context) {
        super(context);
    }

    public HijriDashboardCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HijriDashboardCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void prepareRuntime(Context context) {
        this.hijriDateToday = HijriCalendar.getToday();
        this.todayEvent = IslamicEvents.getEventIn(this.hijriDateToday);
    }

    @Override
    public void prepareCardView(Context context) {
        inflate(getContext(), R.layout.layout_hijri_card, this);
        setRippleColorResource(R.color.transparent);
        // Init content views
        tvDayName = $(R.id.tv_day_name);
        tvHijriDay = $(R.id.tv_hijri_day);
        tvHijriMonthYear = $(R.id.tv_hijri_month_year);
        tvEventTitle = findViewById(R.id.tv_hijri_event_title);
        // Refresh UI
        if (!isInEditMode()) this.refreshUI();
    }

    @Override
    public void refreshUI() {
        // Refresh runtime
        this.prepareRuntime(getContext());
        // Refresh views
        tvDayName.setText(DateUtils.getTodayName("F")); // Day name.
        tvHijriDay.setText(String.format(LocaleManager.getCurrLocale(getContext()), "%d", this.hijriDateToday.getDay())); // Hijri day number.
        tvHijriMonthYear.setText(String.format(LocaleManager.getCurrLocale(getContext()), "%s, %d %s",
                this.hijriDateToday.getMonthName(),
                this.hijriDateToday.getYear(), ResMan.getString(getContext(), R.string.H))); // Hijri month name + Hijri year.
        if (todayEvent != null) {
            // There's an event today
            tvEventTitle.setText(todayEvent.getTitleStringResId());
            tvEventTitle.setTextColor(ResMan.getColor(getContext(), R.color.yellow));
        } else tvEventTitle.setText(R.string.no_hijri_events_today); // No events today.
    }

    @Override
    public void onClick(View v) {
        if (isBottomSheetShown) return;
        startActivity(new Intent(getContext(), HijriCalendarActivity.class));
//		BottomSheets.newHijriCalendar(getContext(),
//				view -> getContext().startActivity(new Intent(getContext(), view.getId() == R.id.btn_show_events ? IslamicEventsActivity.class : HijriCalendarActivity.class)),
//				islamicEvent -> BottomSheets.newIslamicEventDetails(getContext(), islamicEvent).show(),
//				isShown -> isBottomSheetShown = isShown);
    }

    @Override
    public boolean onLongClick(View v) {
//		final int ACTION_OPEN_HIJRI_CALENDAR = 1;
//		final int ACTION_SHOW_ISLAMIC_EVENTS = 2;
//		final int ACTION_OPEN_RAMADAN_PAGE = 3;
//		BottomSheets.newActionsBottomSheet(getContext(),
//				R.string.hijri_calendar,
//				R.string.quick_actions,
//				(actionId -> {
//					if (actionId == ACTION_OPEN_MINIMIZED_SHEET) this.onClick(this);
//					else if (actionId == ACTION_OPEN_HIJRI_CALENDAR) {
//						// todo: Open HijriCalendarActivity
//					} else if (actionId == ACTION_SHOW_ISLAMIC_EVENTS) {
//						// todo: Open HijriCalendarActivity
//					} else if (actionId == ACTION_OPEN_RAMADAN_PAGE) {
//						// todo: Open RamadanActivity
//					}
//				}),
//				new ActionItem(ACTION_OPEN_MINIMIZED_SHEET, R.drawable.ic_open_minimized, R.string.open_minimized_sheet),
//				new ActionItem(ACTION_OPEN_HIJRI_CALENDAR, R.drawable.ic_calendar, R.string.open_hijri_calendar),
//				new ActionItem(ACTION_SHOW_ISLAMIC_EVENTS, R.drawable.ic_quran_star, R.string.show_islamic_events),
//				new ActionItem(ACTION_OPEN_RAMADAN_PAGE, R.drawable.ic_ramadan_moon_outline, R.string.open_ramadan_page)).show();
        return true;
    }

    @NotNull
    @Override
    public String toString() {
        return "HijriDashboardCard";
    }

}
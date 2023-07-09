/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.calendar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.android.material.transition.platform.MaterialArcMotion;
import com.google.android.material.transition.platform.MaterialContainerTransform;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.typ.muslim.R;
import com.typ.muslim.enums.ExpansionState;
import com.typ.muslim.features.calendar.models.HijriDate;
import com.typ.muslim.interfaces.ExpansionListener;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.managers.IslamicEvents;
import com.typ.muslim.features.prays.PrayerManager;
import com.typ.muslim.managers.ResMan;
import com.typ.muslim.models.DayTrackerRecords;
import com.typ.muslim.models.IslamicEvent;
import com.typ.muslim.models.PrayTimes;
import com.typ.muslim.models.Timestamp;
import com.typ.muslim.ui.ExpandableContainer;
import com.typ.muslim.ui.prays.views.SquarePrayView;
import com.typ.muslim.ui.TrackerPrayRecordView;
import com.typ.muslim.ui.activities.IslamicEventsActivity;
import com.typ.muslim.ui.calendar.views.IslamicEventView;
import com.typ.muslim.ui.calendar.views.SelectedDateViewCard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HijriCalendarActivity extends AppCompatActivity implements
        CalendarView.OnCalendarSelectListener,
        CalendarView.OnMonthChangeListener,
        CalendarView.OnWeekChangeListener,
        CalendarView.OnYearChangeListener {
    // Statics
    private static final String TAG = "HijriCalendarActivity";
    // Runtime
    private Timestamp todayGeorg;
    private HijriDate todayHijri;
    // Views
    private CalendarView hijriCalendar;
    private CalendarLayout calendarLayout;
    private ExpandableContainer excIEvents, excPrayTimes, excTracker;
    private SelectedDateViewCard sdc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        findViewById(android.R.id.content).setTransitionName("transition_card_to_activity");
        // Setup transitions
        final MaterialContainerTransform transition = new MaterialContainerTransform();
        transition.setPathMotion(new MaterialArcMotion());
        transition.addTarget(android.R.id.content);
        transition.setScrimColor(Color.WHITE);
        transition.setDrawingViewId(android.R.id.content);
        // Setup the activity
        getWindow().setSharedElementEnterTransition(transition);
        getWindow().setSharedElementReenterTransition(transition);
        setEnterSharedElementCallback(new MaterialContainerTransformSharedElementCallback());
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        // Set content view
        setContentView(R.layout.activity_hijri_calendar);
        // Prepare runtime
        prepareRuntime();
        // Setup views
        calendarLayout = findViewById(R.id.cal_layout_hijri);
        hijriCalendar = findViewById(R.id.cal_hijri);
        excIEvents = findViewById(R.id.exc_islamic_events);
        excPrayTimes = findViewById(R.id.exc_pray_times);
        excTracker = findViewById(R.id.exc_tracker);
        sdc = findViewById(R.id.sdv_calendar_card);
        // Init views
        excTracker.setVisibility(View.GONE);
        calendarLayout.setModeBothMonthWeekView();
        hijriCalendar.scrollToCalendar(todayGeorg.getYear(), todayGeorg.getMonth(), todayGeorg.getDay(), true);
        hijriCalendar.setSchemeDate(buildSchemesMap(hijriCalendar.getCurYear()));
        hijriCalendar.update();
        // Setup listeners
        hijriCalendar.setOnWeekChangeListener(this);
        hijriCalendar.setOnYearChangeListener(this);
        hijriCalendar.setOnMonthChangeListener(this);
        hijriCalendar.setOnCalendarSelectListener(this);
        ((MaterialToolbar) findViewById(R.id.toolbar)).setNavigationOnClickListener(v -> finishAfterTransition());
        excIEvents.setExpansionListener(new ExpansionListener() {
            @Override
            public void onStateChanged(ExpansionState newState) {
                if (newState == ExpansionState.EXPANDED) {
                    // Collapse others
                    excPrayTimes.collapse();
                    excTracker.collapse();
                    calendarLayout.expand();
                }
            }

            @Override
            public void onStateUpdate(ExpansionState state, float progress) {
            }
        });
        excPrayTimes.setExpansionListener(new ExpansionListener() {
            @Override
            public void onStateChanged(ExpansionState newState) {
                if (newState == ExpansionState.EXPANDED) {
                    // Collapse others
                    excIEvents.collapse();
                    excTracker.collapse();
                    calendarLayout.shrink();
                }
            }

            @Override
            public void onStateUpdate(ExpansionState state, float progress) {
            }
        });
        excTracker.setExpansionListener(new ExpansionListener() {
            @Override
            public void onStateChanged(ExpansionState newState) {
                if (newState == ExpansionState.EXPANDED) {
                    // Collapse others
                    excIEvents.collapse();
                    excPrayTimes.collapse();
                }
            }

            @Override
            public void onStateUpdate(ExpansionState state, float progress) {
            }
        });
        sdc.setClickListener(v -> hijriCalendar.scrollToCalendar(todayGeorg.getYear(), todayGeorg.getMonth(), todayGeorg.getDay(), true, true));
        // Bind data to views
        refreshViews(todayGeorg);
    }

    private Map<String, Calendar> buildSchemesMap(int year) {
        final Map<String, Calendar> schemes = new HashMap<>();
        final List<IslamicEvent> yearEvents = IslamicEvents.getEventsInYear(year, false);
        for (IslamicEvent event : yearEvents) schemes.put(event.toString(), buildScheme(event));
        return schemes;
    }

    private Calendar buildScheme(IslamicEvent event) {
        final Calendar cal = new Calendar();
        final Timestamp georg = event.toGregorian();
        cal.setDay(georg.getDay());
        cal.setMonth(georg.getMonth());
        cal.setYear(georg.getYear());
        cal.setScheme(getString(event.getTitleStringResId()));
        cal.setSchemeColor(ResMan.getColor(this, event.getColorResId()));
        AManager.log(TAG, "buildScheme: %s", event);
        return cal;
    }

    private void prepareRuntime() {
        todayGeorg = Timestamp.NOW();
        todayHijri = todayGeorg.toHijri();
    }

    private void refreshViews(Timestamp selectedDate) {
        final PrayTimes prays = PrayerManager.getPrays(this, selectedDate);
        // IslamicEvents
        if (excIEvents.hasContent()) {
            final IslamicEvent event = IslamicEvents.getEventIn(selectedDate.toHijri());
            final MaterialTextView tvNoEvents = excIEvents.getContentView().getChildAt(0).findViewById(R.id.tv_no_events);
            final IslamicEventView iev = excIEvents.getContentView().getChildAt(0).findViewById(R.id.iev_exc_events);
            if (event == null) {
                // No events in selected day
                iev.setVisibility(View.INVISIBLE);
                tvNoEvents.setVisibility(View.VISIBLE);
            } else {
                // There's an event in selected day
                iev.setEvent(event);
                iev.setVisibility(View.VISIBLE);
                tvNoEvents.setVisibility(View.INVISIBLE);
            }
            excIEvents.getContentView().getChildAt(0).findViewById(R.id.btn_view_all_events)
                    .setOnClickListener(v -> startActivity(new Intent(this, IslamicEventsActivity.class)));
        }
        // PrayTimes
        if (excPrayTimes.hasContent()) {
            final View container = excPrayTimes.getContentView().getChildAt(0);
            ((SquarePrayView) container.findViewById(R.id.fajr_spv)).setPray(prays.getFajr());
            ((SquarePrayView) container.findViewById(R.id.sunrise_spv)).setPray(prays.getSunrise());
            ((SquarePrayView) container.findViewById(R.id.dhuhr_spv)).setPray(prays.getDhuhr());
            ((SquarePrayView) container.findViewById(R.id.asr_spv)).setPray(prays.getAsr());
            ((SquarePrayView) container.findViewById(R.id.maghrib_spv)).setPray(prays.getMaghrib());
            ((SquarePrayView) container.findViewById(R.id.isha_spv)).setPray(prays.getIsha());
        }
        // Tracker
        if (!excTracker.hasContent()) {
            final View container = excTracker.getContentView().getChildAt(0);
            final DayTrackerRecords records = new DayTrackerRecords(this, selectedDate);
            ((TrackerPrayRecordView) container.findViewById(R.id.tprv_fajr)).holdRecord(prays.getFajr(), records.getFajrRecord());
            ((TrackerPrayRecordView) container.findViewById(R.id.tprv_dhuhr)).holdRecord(prays.getDhuhr(), records.getDhuhrRecord());
            ((TrackerPrayRecordView) container.findViewById(R.id.tprv_asr)).holdRecord(prays.getAsr(), records.getAsrRecord());
            ((TrackerPrayRecordView) container.findViewById(R.id.tprv_maghrib)).holdRecord(prays.getMaghrib(), records.getMaghribRecord());
            ((TrackerPrayRecordView) container.findViewById(R.id.tprv_isha)).holdRecord(prays.getIsha(), records.getIshaRecord());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (todayGeorg == null || !todayGeorg.dateMatches(Timestamp.NOW())) {
            // User has changed date in phone settings
            prepareRuntime();
            sdc.select(todayGeorg);
            refreshViews(todayGeorg);
        }
    }

    @Override
    public void onCalendarSelect(Calendar cal, boolean isClick) {
        final Timestamp selectedDate = new Timestamp(cal.getYear(), cal.getMonth() - 1, cal.getDay());
        sdc.select(selectedDate);
        refreshViews(selectedDate);
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {
    }

    @Override
    public void onMonthChange(int year, int month) {
    }

    @Override
    public void onWeekChange(List<Calendar> weekCalendars) {
    }

    @Override
    public void onYearChange(int year) {
    }

}

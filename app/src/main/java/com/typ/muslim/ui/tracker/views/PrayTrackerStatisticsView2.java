/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.tracker.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;

import com.mpt.android.stv.Slice;
import com.mpt.android.stv.SpannableTextView;
import com.typ.muslim.R;
import com.typ.muslim.features.prays.enums.Prays;
import com.typ.muslim.enums.TrackerRange;
import com.typ.muslim.managers.AMSettings;
import com.typ.muslim.managers.LocaleManager;
import com.typ.muslim.managers.PrayTrackerManager;
import com.typ.muslim.managers.ResMan;
import com.typ.muslim.models.PrayTrackerRecord;
import com.typ.muslim.models.ProgressStep;
import com.typ.muslim.ui.StepsProgressWheel;
import com.typ.muslim.ui.ViewContainer;
import com.typ.muslim.utils.Counter;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

public class PrayTrackerStatisticsView2 extends ViewContainer {

    // Statics
    private static final String TAG = "SimpleTrackerStatisticsView";
    // Runtime
    private TrackerRange trackerRange;
    // Views
    private SpannableTextView stvPrayStats;
    private StepsProgressWheel spwTracker;

    public PrayTrackerStatisticsView2(@NonNull @NotNull Context context) {
        super(context);
    }

    public PrayTrackerStatisticsView2(@NonNull @NotNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PrayTrackerStatisticsView2(@NonNull @NotNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void prepareRuntime(Context context) {
        this.trackerRange = AMSettings.getTrackerRange(context);
    }

    @Override
    public void prepareView(Context context) {
        // Inflate view
        inflate(context, R.layout.layout_pray_tracker_stats_card2, this);
        // Setup views
        stvPrayStats = $(R.id.tv_pray_stats_value);
        spwTracker = $(R.id.spw_pray_tracker);
        spwTracker.setSteps(
                new ProgressStep(false, Prays.FAJR.getSurfaceColorRes()),
                new ProgressStep(false, Prays.DHUHR.getSurfaceColorRes()),
                new ProgressStep(false, Prays.ASR.getSurfaceColorRes()),
                new ProgressStep(false, Prays.MAGHRIB.getSurfaceColorRes()),
                new ProgressStep(false, Prays.ISHA.getSurfaceColorRes()));
        spwTracker.setValueText(String.valueOf(0));
        // Refresh view
        if (!isInEditMode()) refreshUI();
    }

    @Override
    public void refreshUI() {
        // Clear prev text first
        this.stvPrayStats.reset();
        // Get total prayed prays today from local database
        final List<PrayTrackerRecord> records = PrayTrackerManager.getTodayRecords(getContext());
        final int todayPrayedPrays = Counter.countValues(records, rec -> rec.wasPrayed() ? 1 : 0);
        // Build formatted duration to be displayed
        final Locale locale = LocaleManager.getCurrLocale(getContext());
        this.stvPrayStats.addSlice(new Slice(
                new Slice.Builder(String.format(locale, "%d", Math.min(todayPrayedPrays, 5)))
                        .textColor(ResMan.getColor(getContext(), R.color.cardBackground3))
                        .style(Typeface.BOLD)
                        .textSize(dp2px(40f))));
        this.stvPrayStats.addSlice(new Slice(
                new Slice.Builder(String.format(locale, " %s 5", getString(R.string.of)))
                        .textColor(ResMan.getColor(getContext(), R.color.darkAdaptiveColor))
                        .textSize(dp2px(30f))));
        this.stvPrayStats.display();
        // Tracker SPW
        spwTracker.setValueText(String.valueOf(5 - todayPrayedPrays));
        for (PrayTrackerRecord record : records) {
            spwTracker.updateStep(record.getPray().ordinalWithoutSunrise(),
                    record.wasPrayed(), record.wasPrayed() ? R.color.green : R.color.bg_input_box);
        }
        spwTracker.drawSteps();
    }

    @Override
    public Pair[] getViewsTransitionPairs() {
        return new Pair[]{
                new Pair<>(spwTracker, "transition_progress_bar"),
                new Pair<>(stvPrayStats, "transition_tv")
        };
    }
}

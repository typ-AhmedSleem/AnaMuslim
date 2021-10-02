/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.dashboard.tracker.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mpt.android.stv.Slice;
import com.mpt.android.stv.SpannableTextView;
import com.typ.muslim.R;
import com.typ.muslim.enums.TrackerRange;
import com.typ.muslim.managers.AMRes;
import com.typ.muslim.managers.AMSettings;
import com.typ.muslim.managers.PrayTrackerManager;
import com.typ.muslim.managers.QuranTrackerManager;
import com.typ.muslim.models.Duration;
import com.typ.muslim.models.ReadQuranRecord;
import com.typ.muslim.ui.ViewContainer;
import com.typ.muslim.utils.Counter;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class TrackerStatisticsView extends ViewContainer {

	// Statics
	private static final String TAG = TrackerStatisticsView.class.getSimpleName();
	// Runtime
	private TrackerRange trackerRange;
	// Views
	private SpannableTextView tvPrayStats, tvQuranStats, tvQuranTarget;

	public TrackerStatisticsView(@NonNull @NotNull Context context) {
		super(context);
	}

	public TrackerStatisticsView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public TrackerStatisticsView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public void prepareRuntime(Context context) {
		this.trackerRange = AMSettings.getTrackerRange(context);
	}

	@Override
	public void prepareView(Context context) {
		// Inflate view
		inflate(context, R.layout.layout_tracker_stats_card, this);
		// Setup views
		this.tvPrayStats = $(R.id.tv_pray_stats_value);
		this.tvQuranStats = $(R.id.tv_quran_stats_value);
		this.tvQuranTarget = $(R.id.tv_quran_stats_helper);
		// Refresh view
		if (!isInEditMode()) refreshUI();
	}

	@Override
	public void refreshUI() {
		this.showPrayTrackerStats();
		this.showQuranTrackerStats();
	}

	private void showPrayTrackerStats() {
		// Clear prev text first
		this.tvPrayStats.reset();
		// Get total prayed prays today from local database
		final int todayPrayedPrays = Counter.countValues(PrayTrackerManager.getTodayRecords(getContext()), rec -> rec.wasPrayed() ? 1 : 0);
		// Build formatted duration to be displayed
		this.tvPrayStats.addSlice(new Slice(
				new Slice.Builder(String.valueOf(Math.min(todayPrayedPrays, 5)))
						.textColor(AMRes.getColor(getContext(), R.color.cardBackground3))
						.style(Typeface.BOLD)
						.textSize(70)));
		this.tvPrayStats.addSlice(new Slice(
				new Slice.Builder(" of 5")
						.textColor(AMRes.getColor(getContext(), R.color.darkAdaptiveColor))
						.textSize(45)));
		this.tvPrayStats.display();
	}

	private void showQuranTrackerStats() {
		// Clear prev text first
		this.tvQuranStats.reset();
		this.tvQuranTarget.reset();
		// Get duration from local database
		final Duration qd = new Duration(Counter.countValues(QuranTrackerManager.getTodayRecords(getContext()), ReadQuranRecord::getDuration));
		// Build formatted duration to be displayed
		if (qd.getHours() < 1) {
			// {n} Minute(s)
			this.tvQuranStats.addSlice(new Slice.Builder(qd.getMinutes() + " ")
					.textSize(65)
					.style(Typeface.BOLD)
					.textColor(AMRes.getColor(getContext(), R.color.cardBackground1))
					.build());
			this.tvQuranStats.addSlice(new Slice.Builder(AMRes.getString(getContext(), qd.getMinutes() == 1 ? R.string.minute : R.string.minutes))
					.textSize(45)
					.build());
		} else if (qd.getHours() > 0 && qd.getMinutes() < 1) {
			// {n} Hour(s)
			this.tvQuranStats.addSlice(new Slice.Builder(qd.getHours() + " ")
					.textSize(65)
					.style(Typeface.BOLD)
					.textColor(AMRes.getColor(getContext(), R.color.cardBackground1))
					.build());
			this.tvQuranStats.addSlice(new Slice.Builder(AMRes.getString(getContext(), qd.getHours() == 1 ? R.string.hour : R.string.hours))
					.textSize(45)
					.build());
		} else if (qd.getHours() > 0 && qd.getMinutes() > 0) {
			// {n} hr(s) {n} min(s)
			this.tvQuranStats.addSlice(new Slice.Builder(qd.getHours() + " ")
					.textSize(65)
					.style(Typeface.BOLD)
					.textColor(AMRes.getColor(getContext(), R.color.cardBackground1))
					.build());
			this.tvQuranStats.addSlice(new Slice.Builder(AMRes.getString(getContext(), qd.getHours() == 1 ? R.string.hr : R.string.hrs))
					.textSize(45)
					.build());
			this.tvQuranStats.addSlice(new Slice.Builder(" " + qd.getMinutes() + " ")
					.textSize(65)
					.style(Typeface.BOLD)
					.textColor(AMRes.getColor(getContext(), R.color.cardBackground1))
					.build());
			this.tvQuranStats.addSlice(new Slice.Builder(AMRes.getString(getContext(), qd.getMinutes() == 1 ? R.string.min : R.string.mins))
					.textSize(45)
					.build());
		} else {
			// 0 Minute
			this.tvQuranStats.addSlice(new Slice.Builder(0 + " ")
					.style(Typeface.BOLD)
					.textSize(65)
					.textColor(AMRes.getColor(getContext(), R.color.cardBackground1))
					.build());
			this.tvQuranStats.addSlice(new Slice.Builder(AMRes.getString(getContext(), R.string.minute))
					.textSize(45)
					.build());
		}
		this.tvQuranTarget.addSlice(new Slice(new Slice.Builder(String.format(Locale.getDefault(), "%s: ", AMRes.getString(getContext(), R.string.daily_target)))
				.textSize(30)
				.textColor(getColor(R.color.darkAdaptiveColor))));
		this.tvQuranTarget.addSlice(new Slice(new Slice.Builder(String.format(Locale.getDefault(), "%d %s", QuranTrackerManager.getTargetMinutes(getContext()), AMRes.getString(getContext(), R.string.minutes)))
				.textSize(30)
				.style(Typeface.BOLD)
				.textColor(AMRes.getColor(getContext(), R.color.cardBackground1))));
		// Display stats in views
		this.tvQuranStats.display();
		this.tvQuranTarget.display();
	}

}

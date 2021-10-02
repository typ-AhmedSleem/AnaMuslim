/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.dashboard.tracker;

import static com.typ.muslim.enums.TrackerRange.THIS_MONTH;
import static com.typ.muslim.enums.TrackerRange.THIS_WEEK;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.db.williamchart.view.BarChartView;
import com.google.android.material.textview.MaterialTextView;
import com.typ.muslim.Keys;
import com.typ.muslim.R;
import com.typ.muslim.core.praytime.enums.Prays;
import com.typ.muslim.enums.TrackerRange;
import com.typ.muslim.libs.easyjava.data.EasyList;
import com.typ.muslim.managers.AMRes;
import com.typ.muslim.managers.AMSettings;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.managers.PrayTrackerManager;
import com.typ.muslim.models.ActionItem;
import com.typ.muslim.models.PrayTrackerRecord;
import com.typ.muslim.ui.BottomSheets;
import com.typ.muslim.ui.ViewContainer;
import com.typ.muslim.utils.Counter;
import com.typ.muslim.utils.DateUtils;
import com.typ.muslim.utils.ListUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import kotlin.Pair;

public class PrayTrackerCard extends ViewContainer {

	// Statics
	private static final String TAG = PrayTrackerCard.class.getSimpleName();
	// Runtime
	private TrackerRange trackerRange;
	// Views
	private MaterialTextView tvTrackerRange, tvPrayedPraysCount;
	private BarChartView chartPrayTracker;


	public PrayTrackerCard(@NonNull @NotNull Context context) {
		super(context);
	}

	@Override
	public void prepareRuntime(Context context) {
		this.trackerRange = AMSettings.getTrackerRange(context);
	}

	@Override
	public void prepareView(Context context) {
		inflate(context, R.layout.layout_pray_tracker_card, this);
		// Init views
		tvTrackerRange = $(R.id.tv_tracker_range);
		tvPrayedPraysCount = $(R.id.tv_prayed_prays_count);
		chartPrayTracker = $(R.id.bar_chart_pray_tracker);
		// Perform refresh to display data in views
		if (!isInEditMode()) this.refreshUI();
	}

	@Override
	public void refreshUI() {
		// Prepare necessary vars used during calculations
		Calendar calendar = Calendar.getInstance();
		int monthNumber = calendar.get(Calendar.MONTH) + 1;
		int weekNumber = calendar.get(Calendar.WEEK_OF_MONTH); // todo Get the actual week number of this month
		int weekLength = DateUtils.getWeekLength(weekNumber, monthNumber); // todo Get the number of days within this week
		int monthLength = DateUtils.getMonthLength(monthNumber, false); // todo Get the actual this month length
		// Get tracker records for Today, ThisWeek, ThisMonth from local database
		EasyList<PrayTrackerRecord> todayRecords = EasyList.createList(PrayTrackerManager.getTodayRecords(getContext()));
		EasyList<PrayTrackerRecord> thisWeekRecords = EasyList.createList(PrayTrackerManager.getWeekRecords(getContext(), weekNumber));
		EasyList<PrayTrackerRecord> thisMonthRecords = EasyList.createList(PrayTrackerManager.getMonthRecords(getContext(), monthNumber));
		// Calculate percentages and count
		int todayPrayedPrays = todayRecords.countValues(rec -> rec.wasPrayed() ? 1 : 0);
		int thisWeekPrayedPrays = thisWeekRecords.countValues(rec -> rec.wasPrayed() ? 1 : 0);
		int thisMonthPrayedPrays = thisMonthRecords.countValues(rec -> rec.wasPrayed() ? 1 : 0);
		float thisWeekPercentage = thisWeekPrayedPrays / (weekLength * 5f) * 100;
		float thisMonthPercentage = thisMonthPrayedPrays / (monthLength * 7 * 5f) * 100;
		// Refresh content in views
		tvTrackerRange.setText(this.trackerRange.getName());
		if (trackerRange == TrackerRange.THIS_WEEK) this.tvPrayedPraysCount.setText(String.format(Locale.getDefault(), "%.1f%s", thisWeekPercentage, "%"));
		else if (trackerRange == TrackerRange.THIS_MONTH) this.tvPrayedPraysCount.setText(String.format(Locale.getDefault(), "%.1f%s", thisMonthPercentage, "%"));
		else this.tvPrayedPraysCount.setText(String.valueOf(todayPrayedPrays));
		// Show Graph data
		chartPrayTracker.animate(getChartDataOfRange(todayRecords, thisWeekRecords, thisMonthRecords, monthLength, weekLength));
		// fixme Dummy
		dummy(weekLength, thisWeekPrayedPrays, thisMonthPrayedPrays, todayPrayedPrays, monthLength, monthNumber, weekNumber);
		// Log results
		AManager.log(TAG, "TrackerRecords: Today[%d] | Week[%d|%.1f] | Month[%d|%.1f]", todayRecords.size(), thisWeekPrayedPrays, thisWeekPercentage, thisMonthPrayedPrays, thisMonthPercentage);
	}

	@NonNull
	private List<Pair<String, Float>> getChartDataOfRange(List<PrayTrackerRecord> todayRecords, List<PrayTrackerRecord> thisWeekRecords, List<PrayTrackerRecord> thisMonthRecords, int monthLength, int weekLength) {
		// Create chart data list
		List<Pair<String, Float>> chartData = new ArrayList<>();
		// Get chart data according to current tracker range
		if (trackerRange == TrackerRange.THIS_WEEK) {
			// Calculate chart value for each pray
			int totalPraysInWeek = weekLength * 5;
			float[] prayingPercentage = new float[]{0f, 0f, 0f, 0f, 0f, 0f};
			for (int i = 0; i < thisWeekRecords.size(); i++) {
				PrayTrackerRecord record = thisWeekRecords.get(i);
				if (record.wasPrayed()) prayingPercentage[record.getPray().ordinal()] = prayingPercentage[record.getPray().ordinal()] + 1.0f;
			}
			// Set chart data
			// todo: Display 7 chart bars for week days (Sat,Sun,...) instead of 5
			chartData.add(new Pair<>(AMRes.getString(getContext(), R.string.fajr), prayingPercentage[0] / totalPraysInWeek)); // Fajr data.
			chartData.add(new Pair<>(AMRes.getString(getContext(), R.string.dhuhr), prayingPercentage[2] / totalPraysInWeek)); // Dhuhr data.
			chartData.add(new Pair<>(AMRes.getString(getContext(), R.string.asr), prayingPercentage[3] / totalPraysInWeek)); // Asr data.
			chartData.add(new Pair<>(AMRes.getString(getContext(), R.string.magh), prayingPercentage[4] / totalPraysInWeek)); // Maghrib data.
			chartData.add(new Pair<>(AMRes.getString(getContext(), R.string.isha), prayingPercentage[5] / totalPraysInWeek)); // Isha data.
			AManager.log(TAG, "getChartDataOfRange: ThisWeek[%s]", ListUtils.toString(chartData));
		} else if (trackerRange == TrackerRange.THIS_MONTH) {
			// Calculate chart value for each pray
			int totalPraysInMonth = monthLength * 5;
			float[] prayingPercentage = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
			for (int i = 0; i < thisMonthRecords.size(); i++) {
				PrayTrackerRecord record = thisMonthRecords.get(i);
				if (record.wasPrayed()) prayingPercentage[record.getPray().ordinal()] = prayingPercentage[record.getPray().ordinal()] + 1.0f;
			}
			// Set chart data
			chartData.add(new Pair<>(AMRes.getString(getContext(), R.string.fajr), prayingPercentage[0] / totalPraysInMonth)); // Fajr data.
			chartData.add(new Pair<>(AMRes.getString(getContext(), R.string.dhuhr), prayingPercentage[2] / totalPraysInMonth)); // Dhuhr data.
			chartData.add(new Pair<>(AMRes.getString(getContext(), R.string.asr), prayingPercentage[3] / totalPraysInMonth)); // Asr data.
			chartData.add(new Pair<>(AMRes.getString(getContext(), R.string.magh), prayingPercentage[4] / totalPraysInMonth)); // Maghrib data.
			chartData.add(new Pair<>(AMRes.getString(getContext(), R.string.isha), prayingPercentage[5] / totalPraysInMonth)); // Isha data.
			AManager.log(TAG, "getChartDataOfRange: ThisMonth[%s]", ListUtils.toString(chartData));
		} else {
			// Calculate chart value for each pray
			boolean[] prayedPrays = new boolean[]{false, false, false, false, false, false};
			for (int i = 0; i < todayRecords.size(); i++) {
				PrayTrackerRecord record = todayRecords.get(i);
				Prays pray = record.getPray();
				if (record.wasPrayed()) prayedPrays[pray.ordinal()] = true;
			}
			// Set chart data
			chartData.add(new Pair<>(AMRes.getString(getContext(), R.string.fajr), prayedPrays[0] ? 1f : 0f)); // Fajr data.
			chartData.add(new Pair<>(AMRes.getString(getContext(), R.string.dhuhr), prayedPrays[2] ? 1f : 0f)); // Dhuhr data.
			chartData.add(new Pair<>(AMRes.getString(getContext(), R.string.asr), prayedPrays[3] ? 1f : 0f)); // Asr data.
			chartData.add(new Pair<>(AMRes.getString(getContext(), R.string.magh), prayedPrays[4] ? 1f : 0f)); // Maghrib data.
			chartData.add(new Pair<>(AMRes.getString(getContext(), R.string.isha), prayedPrays[5] ? 1f : 0f)); // Isha data.
			AManager.log(TAG, "getChartDataOfRange: Today[%s]", ListUtils.toString(chartData));
		}
		// Return chart data
		return chartData;
	}

	private void dummy(int weekLength, int totalMinsThisWeek, int totalMinsThisMonth, int totalMinsToday, int monthLength, int monthNumber, int weekNumber) {
		if (trackerRange == THIS_WEEK || trackerRange == THIS_MONTH) {
			// Calculate percentage diff between last and this period
			int totalTarget = trackerRange == THIS_WEEK ? weekLength * 5 : monthLength * 5;
			float thisPercentage = trackerRange == THIS_WEEK ? totalMinsThisWeek / (float) totalTarget : totalMinsThisMonth / (float) totalTarget;
			float lastPercentage = trackerRange == THIS_WEEK ?
					Counter.countValues(PrayTrackerManager.getWeekRecords(getContext(), weekNumber - 1), item -> item.wasPrayed() ? 1 : 0) / (float) totalTarget :
					Counter.countValues(PrayTrackerManager.getMonthRecords(getContext(), monthNumber - 1), item -> item.wasPrayed() ? 1 : 0) / (float) totalTarget;
			AManager.log(TAG, "PrayLastStats: Total[%d] | ThisPercentage[%.2f] | LastPercentage[%.2f] | Diff[%.2f %s]", totalTarget, thisPercentage, lastPercentage,
					Math.abs(thisPercentage - lastPercentage), thisPercentage > lastPercentage ? "up" : "down");
		}
		// Log results
		AManager.log(TAG, "PrayTrackerRecords: Today[%d Min] | Week[%d|%d Min] | Month[%d|%d Min]", totalMinsToday, weekLength, totalMinsThisWeek, monthLength, totalMinsThisMonth);
	}

	@Override
	public void onClick(View v) {
		// todo: Open TrackerActivity and go to PrayTracker section
	}

	@Override
	public void onLongClick(View v) {
		BottomSheets.newActions(getContext(),
				R.string.pray_tracker,
				R.string.change_display_range,
				(id -> {
					this.trackerRange = AMSettings.save(getContext(), Keys.TRACKER_RANGE, TrackerRange.valueOf(id));
					this.refreshUI();
				}),null,
				new ActionItem(TrackerRange.TODAY.ordinal(), R.drawable.ic_calendar, R.string.today),
				new ActionItem(TrackerRange.THIS_WEEK.ordinal(), R.drawable.ic_calendar, R.string.this_week),
				new ActionItem(TrackerRange.THIS_MONTH.ordinal(), R.drawable.ic_calendar, R.string.this_month)).show();
	}
}

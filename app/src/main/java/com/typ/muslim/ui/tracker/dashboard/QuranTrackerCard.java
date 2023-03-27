/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.tracker.dashboard;

import static com.typ.muslim.enums.TrackerRange.THIS_MONTH;
import static com.typ.muslim.enums.TrackerRange.THIS_WEEK;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.textview.MaterialTextView;
import com.typ.muslim.R;
import com.typ.muslim.app.Keys;
import com.typ.muslim.enums.TrackerRange;
import com.typ.muslim.libs.easyjava.data.EasyList;
import com.typ.muslim.managers.AMSettings;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.managers.QuranTrackerManager;
import com.typ.muslim.models.ActionItem;
import com.typ.muslim.models.ReadQuranRecord;
import com.typ.muslim.ui.BottomSheets;
import com.typ.muslim.ui.ViewContainer;
import com.typ.muslim.utils.Counter;
import com.typ.muslim.utils.DateUtils;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Locale;

public class QuranTrackerCard extends ViewContainer {

	// Statics
	private static final String TAG = PrayTrackerCard.class.getSimpleName();
	// Runtime
	private int targetMinutes;
	private TrackerRange trackerRange;
	// Views
	private MaterialTextView tvTrackerRange, tvQuranReadingTime;


	public QuranTrackerCard(@NonNull @NotNull Context context) {
		super(context);
	}

	@Override
	public void prepareRuntime(Context context) {
		this.targetMinutes = QuranTrackerManager.getTargetMinutes(getContext());
		this.trackerRange = AMSettings.getTrackerRange(getContext());
	}

	@Override
	public void prepareView(Context context) {
		inflate(context, R.layout.layout_quran_tracker_card, this);
		// Init views
		tvTrackerRange = $(R.id.tv_tracker_range);
		tvQuranReadingTime = $(R.id.tv_total_reading_time);
		// Perform refresh to display data in views
		if (!isInEditMode()) this.refreshUI();
	}

	@Override
	public void refreshUI() {
		// Prepare necessary vars used during calculations
		Calendar cal = Calendar.getInstance();
		int monthNumber = cal.get(Calendar.MONTH) + 1;
		int weekNumber = cal.get(Calendar.WEEK_OF_MONTH); // todo Get the actual week number of this month
		int weekLength = DateUtils.getWeekLength(weekNumber, monthNumber); // todo Get the number of days within this week
		int monthLength = DateUtils.getMonthLength(monthNumber, false); // todo Get the actual this month length
		// Get tracker records for Today, ThisWeek, ThisMonth from local database
		EasyList<ReadQuranRecord> todayRecords = EasyList.createList(QuranTrackerManager.getTodayRecords(getContext()));
		EasyList<ReadQuranRecord> thisWeekRecords = EasyList.createList(QuranTrackerManager.getWeekRecords(getContext(), weekNumber));
		EasyList<ReadQuranRecord> thisMonthRecords = EasyList.createList(QuranTrackerManager.getMonthRecords(getContext(), monthNumber));
		// Calculate total minutes of reading Quran in all 3 ranges
		int totalMinsToday = todayRecords.countValues(ReadQuranRecord::getDuration);
		int totalMinsThisWeek = thisWeekRecords.countValues(ReadQuranRecord::getDuration);
		int totalMinsThisMonth = thisMonthRecords.countValues(ReadQuranRecord::getDuration);
		// Refresh content in views
		tvTrackerRange.setText(this.trackerRange.getName());
		if (trackerRange == TrackerRange.THIS_WEEK) this.tvQuranReadingTime.setText(DateUtils.toDuration(totalMinsThisWeek));
		else if (trackerRange == TrackerRange.THIS_MONTH) this.tvQuranReadingTime.setText(DateUtils.toDuration(totalMinsThisMonth));
		else this.tvQuranReadingTime.setText(DateUtils.toDuration(totalMinsToday));
		// Show Graph data
		if (trackerRange == THIS_WEEK || trackerRange == THIS_MONTH) {
			// Calculate percentage diff between last and this period
			int totalTarget = (trackerRange == THIS_WEEK ? weekLength : DateUtils.getMonthLength(monthLength - 1, false)) * targetMinutes;
			float thisPercentage = (trackerRange == THIS_WEEK ? totalMinsThisWeek : totalMinsThisMonth) / (float) totalTarget;
			float lastPercentage = (trackerRange == THIS_WEEK ? Counter.countValues(QuranTrackerManager.getWeekRecords(getContext(), weekNumber - 1), ReadQuranRecord::getDuration) :
			                        Counter.countValues(QuranTrackerManager.getMonthRecords(getContext(), monthNumber - 1), ReadQuranRecord::getDuration)) / (float) totalTarget;
			// todo: Show percentage
			// todo: Display up or down indicator arrow
			tvQuranReadingTime.setText(String.format(Locale.ENGLISH, "%.1f%s || %.1f%s", thisPercentage, "%", lastPercentage, "%"));
			AManager.log(TAG, "LastStats: TotalMinutes[%d] | ThisPercentage[%.2f] | LastPercentage[%.2f] | DayOfWeek[%d] | Diff[%.2f %s]", totalTarget, thisPercentage, lastPercentage, cal.get(Calendar.WEEK_OF_MONTH),
					thisPercentage > lastPercentage ? lastPercentage / thisPercentage : thisPercentage / lastPercentage, thisPercentage > lastPercentage ? "up" : "down");
		}
		// Log results
		AManager.log(TAG, "QuranTrackerRecords: Today[%d Min] | Week[%d|%d Min] | Month[%d|%d Min]", totalMinsToday, weekLength, totalMinsThisWeek, monthLength, totalMinsThisMonth);
	}

	private void changeIndicator(boolean isUp) {
	}

	@Override
	public void onClick(View v) {
		// todo: Open TrackerActivity and go to PrayTracker section
	}

	@Override
	public void onLongClick(View v) {
		BottomSheets.newActions(getContext(),
				R.string.quran_tracker,
				R.string.change_display_range,
				(id -> {
					this.trackerRange = AMSettings.save(getContext(), Keys.TRACKER_RANGE, TrackerRange.valueOf(id));
					this.refreshUI();
				}), null,
				new ActionItem(TrackerRange.TODAY.ordinal(), R.drawable.ic_calendar, R.string.today),
				new ActionItem(TrackerRange.THIS_WEEK.ordinal(), R.drawable.ic_calendar, R.string.this_week),
				new ActionItem(TrackerRange.THIS_MONTH.ordinal(), R.drawable.ic_calendar, R.string.this_month)).show();
	}

}

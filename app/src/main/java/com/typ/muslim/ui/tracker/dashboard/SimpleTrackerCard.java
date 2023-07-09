/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.tracker.dashboard;

import static java.util.Calendar.MINUTE;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.mpt.android.stv.Slice;
import com.mpt.android.stv.SpannableTextView;
import com.typ.muslim.R;
import com.typ.muslim.app.Consumers;
import com.typ.muslim.features.prays.enums.Prays;
import com.typ.muslim.enums.PrayStatus;
import com.typ.muslim.enums.TrackerRange;
import com.typ.muslim.managers.AMSettings;
import com.typ.muslim.managers.PrayTrackerManager;
import com.typ.muslim.features.prays.PrayerManager;
import com.typ.muslim.managers.ResMan;
import com.typ.muslim.models.Pray;
import com.typ.muslim.models.PrayTrackerRecord;
import com.typ.muslim.models.ProgressStep;
import com.typ.muslim.models.Timestamp;
import com.typ.muslim.ui.BottomSheets;
import com.typ.muslim.ui.StepsProgressWheel;
import com.typ.muslim.ui.home.DashboardCard;
import com.typ.muslim.utils.Counter;

import java.util.List;
import java.util.Locale;

import kotlin.Pair;

public class SimpleTrackerCard extends DashboardCard {

	// todo: Care of the trackerRange after creating TrackerCardOptionsBS

	// Statics
	private static final String TAG = "SimpleTrackerCard";
	// Runtime
	private Pray currentPray;
	private TrackerRange trackerRange;
	// Views
	private SpannableTextView stvTracker;
	private StepsProgressWheel spwTracker;

	public SimpleTrackerCard(Context context) {
		super(context);
	}

	public SimpleTrackerCard(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SimpleTrackerCard(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public void prepareRuntime(Context context) {
		if (isInEditMode()) return;
		trackerRange = AMSettings.getTrackerRange(context);
	}

	@Override
	public void prepareCardView(Context context) {
		inflate(R.layout.layout_simple_tracker_card);
		setCardBackgroundColor(getColor(R.color.adaptiveBackgroundColor));
		// Setup views
		stvTracker = $(R.id.stv_tracker_data);
		spwTracker = $(R.id.spw_tracker);
		spwTracker.setSteps(
				new ProgressStep(true, Prays.FAJR.getSurfaceColorRes()),
				new ProgressStep(false, Prays.DHUHR.getSurfaceColorRes()),
				new ProgressStep(true, Prays.ASR.getSurfaceColorRes()),
				new ProgressStep(false, Prays.MAGHRIB.getSurfaceColorRes()),
				new ProgressStep(true, Prays.ISHA.getSurfaceColorRes()));
		spwTracker.setValueText("");
		stvTracker.addSlice(new Slice(
                new Slice.Builder(getString(R.string.you_prayed) + "\n")
                        .textColor(ResMan.getColor(getContext(), R.color.cardBackground3))
						.superscript()
						.textSize(dp2px(14f))));
		stvTracker.addSlice(new Slice(
                new Slice.Builder(String.valueOf(Math.min(2, 5)))
                        .textColor(ResMan.getColor(getContext(), R.color.cardBackground3))
						.style(Typeface.BOLD)
						.textSize(dp2px(40f))));
		stvTracker.addSlice(new Slice(
                new Slice.Builder(" of 5")
                        .textColor(ResMan.getColor(getContext(), R.color.darkAdaptiveColor))
						.textSize(dp2px(20f))));
		stvTracker.display();
		// Refresh view
		if (!isInEditMode()) refreshUI();
	}

	@Override
	public void refreshRuntime() {
		currentPray = PrayerManager.getCurrentPray(getContext());
	}

	@Override
	public void refreshUI() {
		// Clear prev text first
		stvTracker.reset();
		// Get total prayed prays today from local database
		final boolean didHePray = PrayerManager.didHePray(getContext(), currentPray, true);
		final List<PrayTrackerRecord> records = PrayTrackerManager.getTodayRecords(getContext());
		final int todayPrayedPrays = Counter.countValues(records, rec -> rec.wasPrayed() ? 1 : 0);
		Consumers.doWhen(didHePray, () -> {
			// User has prayed
			stvTracker.addSlice(new Slice(
                    new Slice.Builder(String.valueOf(getString(R.string.you_prayed)))
                            .textColor(ResMan.getColor(getContext(), R.color.cardBackground3))
							.style(Typeface.BOLD)
							.superscript()
							.textSize(dp2px(20f))));
			stvTracker.addSlice(new Slice(
                    new Slice.Builder(String.valueOf(Math.min(todayPrayedPrays, 5)))
                            .textColor(ResMan.getColor(getContext(), R.color.cardBackground3))
							.style(Typeface.BOLD)
							.textSize(dp2px(40f))));
			stvTracker.addSlice(new Slice(
                    new Slice.Builder(" of 5")
                            .textColor(ResMan.getColor(getContext(), R.color.darkAdaptiveColor))
							.textSize(dp2px(25f))));
			stvTracker.display();
			// Clear texts of SPW
			spwTracker.setValueText("");
			spwTracker.setInfoText("");
		}, () -> {
			// User hasn't prayed yet
            stvTracker.reset();
            stvTracker.addSlice(new Slice(
                    new Slice.Builder(String.format(Locale.getDefault(), "%s,  ", ResMan.getString(getContext(), R.string.have_you_prayed)))
                            .textSize(sp2px(14f))
                            .textColor(ResMan.getColor(getContext(), R.color.darkAdaptiveColor))));
            stvTracker.addSlice(new Slice(
                    new Slice.Builder(ResMan.getString(getContext(), currentPray.getPrayNameRes()))
                            .textSize(sp2px(16f))
                            .style(Typeface.BOLD)
                            .textColor(ResMan.getColor(getContext(), R.color.green))));
            stvTracker.display();
			// Show texts on SPW
			spwTracker.setValueText(String.valueOf(todayPrayedPrays));
			spwTracker.setInfoText(getString(R.string.you_prayed));
		});
		for (PrayTrackerRecord record : records) {
			Consumers.doIf(() -> spwTracker.updateStep(record.getPray().ordinalWithoutSunrise(), true),
					record.wasPrayed());
		}
		spwTracker.drawSteps();
	}

	@Override
	public void onClick(View v) {
		final Pray currPray = PrayerManager.getCurrentPray(getContext());
		Consumers.doIf(() -> BottomSheets.newAnswerPrayed(getContext(), currPray,
				result -> submitRecord(currPray, result),
				isShown -> isBottomSheetShown = isShown).show(), currPray != null);
	}

	private void submitRecord(Pray currentPray, Pair<PrayStatus, Boolean> result) {
		if (result == null) return;
		long prayedIn = 0L; // Defaults to missed to pray.
		if (result.getFirst() == PrayStatus.ON_TIME) {
			// Add 15 minutes to PrayTime as he prayed at mosque or use now if not in mosque.
			prayedIn = result.getSecond() ? Timestamp.NOW().roll(MINUTE, 15).toMillis() : Timestamp.NOW().toMillis();
		} else if (result.getFirst() == PrayStatus.DELAYED) prayedIn = Timestamp.NOW().toMillis();
		// Build record then persist it
		if (PrayTrackerManager.record(getContext(),
				currentPray.getType(),
				result.getFirst(),
				result.getSecond(),
				currentPray.getIn().toMillis(),
				prayedIn,
				Timestamp.NOW().toMillis())) {
			// Show statistics
			performRefresh();
		}
	}

	@Override
	public boolean onLongClick(View v) {
		// todo: Create TrackerCardOptionsBS that makes user able to change trackerTarget(Prays, Quran, Azkar) and trackerRange
		return true;
	}
}

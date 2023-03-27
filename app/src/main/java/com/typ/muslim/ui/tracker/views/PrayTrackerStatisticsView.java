/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.tracker.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.typ.muslim.ui.tracker.dashboard.AzkarTrackerCard;
import com.typ.muslim.ui.tracker.dashboard.PrayTrackerCard;
import com.typ.muslim.ui.tracker.dashboard.QuranTrackerCard;

public final class PrayTrackerStatisticsView extends HorizontalScrollView {

	// Statics
	private static final String TAG = "PrayerTrackerStatisticsView";
	// Views
	private PrayTrackerCard prayTrackerCard;
	private QuranTrackerCard quranTrackerCard;
	private AzkarTrackerCard azkarTrackerCard;

	public PrayTrackerStatisticsView(Context context) {
		super(context);
		this.prepareView(context);
	}

	public PrayTrackerStatisticsView(Context context, @Nullable AttributeSet attrs) {
		this(context);
	}

	public PrayTrackerStatisticsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		this(context);
	}

	public void prepareView(Context context) {
		setBackgroundColor(Color.WHITE);
		// Create and init cards container
		LinearLayout llCardsContainer = new LinearLayout(context);
		llCardsContainer.setOrientation(LinearLayout.HORIZONTAL);
		this.addView(llCardsContainer);
		// Create cards
		prayTrackerCard = new PrayTrackerCard(context);
		quranTrackerCard = new QuranTrackerCard(context);
		azkarTrackerCard = new AzkarTrackerCard(context);
		// Add cards to cards container
		llCardsContainer.addView(prayTrackerCard);
		llCardsContainer.addView(quranTrackerCard);
		llCardsContainer.addView(azkarTrackerCard);
	}

	public void refresh() {
		this.prayTrackerCard.refreshUI();
		this.quranTrackerCard.refreshUI();
		this.azkarTrackerCard.refreshUI();
	}

}

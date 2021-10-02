/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.app.progresviews.ProgressWheel;
import com.google.android.material.textview.MaterialTextView;
import com.typ.muslim.R;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.models.Khatma;
import com.typ.muslim.ui.dashboard.DashboardCard;

import java.util.Locale;

public class ActiveKhatmaCard extends DashboardCard {

	// Runtime
	private Khatma khatma;
	// Views
	private MaterialTextView tvDayNumber, tvMsg;
	private ProgressWheel progressKhatma;

	public ActiveKhatmaCard(Context context) {
		super(context);
	}

	public ActiveKhatmaCard(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ActiveKhatmaCard(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public void prepareCardView(Context context) {
		// Inflate card view and setup it
		inflate(R.layout.layout_active_khatma_card);
		setRippleColorResource(R.color.ripple_white);
		setCardBackgroundColor(getColor(R.color.green));
		// Init views
		tvDayNumber = $(R.id.tv_khatma_day_number);
		tvMsg = $(R.id.tv_msg);
		progressKhatma = $(R.id.prw_khatma_percentage);
		// Reset views
		reset();
	}

	@Override
	public void reset() {
		progressKhatma.setPercentage(0);
		progressKhatma.setStepCountText("0");
		progressKhatma.setVisibility(INVISIBLE);
		tvDayNumber.setText(R.string.no_active_khatma);
		tvMsg.setText(R.string.click_to_create_or_join_khatma);
	}

	@Override
	public void refreshUI() {
		if (khatma == null) {
			reset();
			return;
		}
		final int todayNumber = khatma.getTodayNumber();
		progressKhatma.setVisibility(VISIBLE);
		progressKhatma.setStepCountText(String.valueOf(khatma.getProgressPercentage()));
		progressKhatma.setPercentage((int) ((khatma.getProgressPercentage() / 100f) * 360));
		tvMsg.setText(R.string.click_to_view_khatma_details);
		tvDayNumber.setText(String.format(Locale.getDefault(), "%s %d", getString(R.string.day), todayNumber - 1));
		AManager.log("AKC", khatma);
	}

	@Override
	public void onClick(View v) {
		// todo: Open KhatmaDetails (Activity or BottomSheet) and give it current khatma
	}

	public void setKhatma(Khatma khatma) {
		this.khatma = khatma;
		if (this.khatma == null) reset();
		else refreshUI();

	}
}

/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.home;

import android.content.Context;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.View;

import com.mpt.android.stv.Slice;
import com.mpt.android.stv.SpannableTextView;
import com.typ.muslim.R;
import com.typ.muslim.enums.SoMNotifyMethod;
import com.typ.muslim.enums.SoMReminderFreq;
import com.typ.muslim.managers.AMSettings;

public class SalatOnMohamedDashboardCard extends DashboardCard {


	// todo: Make the BottomSheet of reminder settings
	// In new versions -> todo: Add counter and record how many times user clicked then store it as a session model.

	// Statics
	private static final String TAG = "SalatOnMohamedDashboardCard";
	// Runtime
	private boolean isReminderEnabled;
	private SoMNotifyMethod notifyMethod;
	private SoMReminderFreq reminderFreq;
	// Views
	private SpannableTextView stvSoM;

	public SalatOnMohamedDashboardCard(Context context) {
		super(context);
	}

	public SalatOnMohamedDashboardCard(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SalatOnMohamedDashboardCard(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public void prepareRuntime(Context c) {
		this.isReminderEnabled = AMSettings.isSOMEnabled(c);
		this.notifyMethod = AMSettings.getSoMNotifyMethod(c);
		this.reminderFreq = AMSettings.getSoMReminderFreq(c);
	}

	@Override
	public void prepareCardView(Context context) {
		// Inflate view and init base card
		inflate(context, R.layout.layout_som_card, this);
		setRippleColorResource(R.color.ripple_green);
		// Init views
		this.stvSoM = $(R.id.stv_som);
		// Refresh UI
		this.refreshUI();
	}

	@Override
	public void refreshUI() {
		// Reset the TextViews at first
		stvSoM.reset();
		// Add pieces of SoM
		stvSoM.addSlice(new Slice.Builder(getString(R.string.som))
				.style(Typeface.BOLD)
				.textSize(sp2px(16f))
				.superscript()
				.textColor(getColor(R.color.green))
				.build());
		stvSoM.addSlice(new Slice.Builder("\n").build());
		stvSoM.addSlice(new Slice.Builder(getString(R.string.som_sub))
				.style(Typeface.BOLD)
				.textSize(sp2px(14f))
				.textColor(getColor(R.color.darkAdaptiveColor))
				.build());
		// Display pieces on views
		stvSoM.display();
	}

	@Override
	public void onClick(View v) {
		MediaPlayer.create(getContext(), R.raw.som).start();
	}

	@Override
	public boolean onLongClick(View v) {
//		if (!isBottomSheetShown) {
//			BottomSheets.newSoMReminder(getContext(), reminderFreq,
//					newFreq -> {
//						reminderFreq = newFreq;
//						refreshUI();
//					},
//					isShown -> isBottomSheetShown = isShown).show();
//		}
		return true;
	}
}

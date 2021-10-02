/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.dashboard;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.mpt.android.stv.Slice;
import com.mpt.android.stv.SpannableTextView;
import com.typ.muslim.R;
import com.typ.muslim.enums.SoMNotifyMethod;
import com.typ.muslim.enums.SoMReminderFreq;
import com.typ.muslim.managers.AMSettings;
import com.typ.muslim.ui.BottomSheets;

import java.util.Locale;

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
	private SpannableTextView stvReminderDetails, stvSoM;

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
		setRippleColorResource(R.color.ripple_som_card);
		// Init views
		this.stvReminderDetails = $(R.id.stv_som_reminder_details);
		this.stvSoM = $(R.id.stv_som);
		// Refresh UI
		this.refreshUI();
	}

	@Override
	public void refreshUI() {
		// Reset the TextViews at first
		this.stvReminderDetails.reset();
		this.stvSoM.reset();
		// Display reminder details
		if (isReminderEnabled) {
			// A reminder was found
			final int mins = reminderFreq.getFrequency();
			String reminderDetailsText = String.format(Locale.getDefault(), "%s\n%s %d %s",
					getString(R.string.reminder_enabled),
					getString(R.string.every),
					mins < 60 ? reminderFreq.getFrequency() : mins / 60,
					mins < 60 ? getString(R.string.mins) : getString(mins == 60 ? R.string.hr : R.string.hrs));
			this.stvReminderDetails.setText(reminderDetailsText);
			this.stvReminderDetails.setTextColor(getColor(R.color.darkAdaptiveColor));
		} else {
			this.stvReminderDetails.setText(R.string.reminder_disabled);
			this.stvReminderDetails.setTextColor(getColor(R.color.red));
		}
		// Add pieces of SoM
		this.stvSoM.addSlice(new Slice.Builder(getString(R.string.som))
				.style(Typeface.BOLD)
				.textSize(30)
				.superscript()
				.textColor(getColor(R.color.green))
				.build());
		this.stvSoM.addSlice(new Slice.Builder("\n").build());
		this.stvSoM.addSlice(new Slice.Builder(getString(R.string.som_sub))
				.style(Typeface.BOLD)
				.textSize(25)
				.textColor(getColor(R.color.darkAdaptiveColor))
				.build());
		// Display pieces on views
		this.stvSoM.display();
	}

	@Override
	public void onClick(View v) {
//		MediaPlayer.create(getContext(), R.raw.som).start();
	}

	@Override
	public boolean onLongClick(View v) {
		if (!isBottomSheetShown) {
			BottomSheets.newSoMReminder(getContext(), reminderFreq,
					newFreq -> {
						reminderFreq = newFreq;
						refreshUI();
					},
					isShown -> isBottomSheetShown = isShown).show();
		}
		return true;
	}
}

/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.ramadan.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.typ.muslim.R;
import com.typ.muslim.enums.FormatPatterns;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.models.Timestamp;
import com.typ.muslim.features.ramadan.RamadanManager;
import com.typ.muslim.features.ramadan.models.RamadanDay;
import com.typ.muslim.ui.ViewContainer;
import com.typ.muslim.ui.ramadan.RamadanDashboardCard;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class InRamadanView extends ViewContainer {

	// Views
	private MaterialTextView tvDayNumber, tvSuhurIn, tvIftarIn;
	private MaterialButton btnReadQuran, btnViewDetails;

	public InRamadanView(@NonNull @NotNull Context context) {
		super(context);
	}

	public InRamadanView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public InRamadanView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public void prepareView(Context context) {
		// Inflate view
		inflate(context, R.layout.layout_in_ramadan_view, this);
		// Init views
		tvDayNumber = $(R.id.tv_ramadan_day_num);
		tvSuhurIn = $(R.id.tv_today_suhur_in);
		tvIftarIn = $(R.id.tv_today_iftar_in);
		btnReadQuran = $(R.id.btn_read_quran);
		btnViewDetails = $(R.id.btn_view_details);
		// Listeners
		btnReadQuran.setOnClickListener(this::onClick);
		btnViewDetails.setOnClickListener(this::onClick);
		// Refresh UI
		if (!isInEditMode()) refreshUI();
	}

	@Override
	public void refreshUI() {
		// Prepare necessary data
		final RamadanDay today = RamadanManager.getNextRamadan().getToday(getContext());
		final FormatPatterns timePattern = AManager.getSelectedTimeFormat(getContext());
		// Display data in views
		tvDayNumber.setText(String.format(
				Locale.getDefault(), "%s %s %s",
				getString(R.string._Day),
				today.getNum(),
				getString(R.string.of_ramadan)));
		tvSuhurIn.setText(today.getSuhurTime().getFormatted(timePattern));
		tvIftarIn.setText(today.getIftarTime().getFormatted(timePattern));
		// Change Suhur and Iftar textColors according to it has passed or not
		tvSuhurIn.setTextColor(getColor(today.getSuhurTime().isBefore(Timestamp.NOW()) ? R.color.color_maghrib_isha_highlight : R.color.green));
		tvIftarIn.setTextColor(getColor(today.getIftarTime().isBefore(Timestamp.NOW()) ? R.color.color_maghrib_isha_highlight : R.color.green));
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_read_quran) {
			// Open RamadanKhatmaBottomSheet or RamadanKhatmaActivity
		} else {
			// Open InRamadanBottomSheet
			((RamadanDashboardCard) getParent().getParent()).performClick();
		}
	}
}

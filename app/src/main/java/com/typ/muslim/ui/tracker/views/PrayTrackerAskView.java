/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.tracker.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textview.MaterialTextView;
import com.mpt.android.stv.Slice;
import com.mpt.android.stv.SpannableTextView;
import com.typ.muslim.R;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.managers.ResMan;
import com.typ.muslim.models.Pray;
import com.typ.muslim.ui.ViewContainer;
import com.typ.muslim.ui.tracker.dashboard.TrackerDashboardCard;

import java.util.Locale;

public final class PrayTrackerAskView extends ViewContainer implements View.OnClickListener {

	// TODO: 7/14/2021 Replace done & forgot buttons with answer button that opens a bottom sheet contains many answer options

	// Statics
	private static final String TAG = "PrayTrackerAskView";
	// Views
	private SpannableTextView tvPrayName;
	private MaterialTextView tvPrayTime;

	public PrayTrackerAskView(@NonNull Context context) {
		super(context);
	}

	public PrayTrackerAskView(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void prepareView(Context context) {
		// Inflate content view
		inflate(context, R.layout.layout_pray_tracker_card_ask_prayed, this);
		// Init content views
		this.tvPrayName = findViewById(R.id.tv_pray_name);
		this.tvPrayTime = findViewById(R.id.tv_pray_time);
	}

	public void askIfPrayed(Pray whichPray) {
		if (whichPray == null) {
			((TrackerDashboardCard) getParent().getParent()).showStatistics();
			return;
		}
		// Show data in views
        this.tvPrayName.reset();
        this.tvPrayName.addSlice(new Slice(
                new Slice.Builder(String.format(Locale.getDefault(), "%s,  ", ResMan.getString(getContext(), R.string.have_you_prayed)))
                        .textSize(sp2px(14f))
                        .textColor(ResMan.getColor(getContext(), R.color.darkAdaptiveColor))));
        this.tvPrayName.addSlice(new Slice(
                new Slice.Builder(ResMan.getString(getContext(), whichPray.getPrayNameRes()))
                        .textSize(sp2px(16f))
                        .style(Typeface.BOLD)
                        .textColor(ResMan.getColor(getContext(), R.color.green))));
        this.tvPrayName.display();
		this.tvPrayTime.setText(whichPray.getFormattedTime(getContext()));
		// Log
		AManager.log(TAG, "askIfPrayed: " + whichPray);
	}

}

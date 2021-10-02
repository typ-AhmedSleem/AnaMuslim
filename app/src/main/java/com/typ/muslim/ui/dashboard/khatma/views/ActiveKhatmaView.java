/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.dashboard.khatma.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.mpt.android.stv.Slice;
import com.mpt.android.stv.SpannableTextView;
import com.typ.muslim.R;
import com.typ.muslim.managers.KhatmaManager;
import com.typ.muslim.ui.ViewContainer;

import org.jetbrains.annotations.NotNull;

public class ActiveKhatmaView extends ViewContainer {

	// Statics
	private static final String TAG = ActiveKhatmaView.class.getSimpleName();
	// Runtime
	private KhatmaManager manager;
	// Views
	private SpannableTextView stvMyProgress,
			stvTodayFrom,
			stvTodayTo;
	private LinearProgressIndicator progress;
	private MaterialCardView cardViewDetails, cardReadNow;

	public ActiveKhatmaView(@NonNull @NotNull Context context) {
		super(context);
	}

	public ActiveKhatmaView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public ActiveKhatmaView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public void prepareView(Context context) {
		// Inflate view
		inflate(context, R.layout.layout_active_khatma_card, this);
//		// Setup views
//		this.progress = $(R.id.progress_khatma);
//		this.stvMyProgress = $(R.id.stv_khatma_my_progress);
//		this.stvTodayFrom = $(R.id.stv_khatma_from);
//		this.stvTodayTo = $(R.id.stv_khatma_to);
//		this.cardViewDetails = $(R.id.card_view_details);
//		this.cardReadNow = $(R.id.card_read_now);
//		// Setup listeners and callbacks
//		this.cardViewDetails.setOnClickListener(v -> {});
//		this.cardReadNow.setOnClickListener(v -> {});
//		// Perform UI refresh
//		this.refreshUI();
	}

	@Override
	public void refreshUI() {
//		// Show my progress in ProgressBar
//		this.progress.setProgressCompat(38, true);
//		// Reset TextViews at first
//		this.stvMyProgress.reset();
//		this.stvTodayFrom.reset();
//		this.stvTodayTo.reset();
//		// My Progress
//		this.stvMyProgress.addSlice(new Slice.Builder(getString(R.string.my_progress) + "  ")
//				                            .textColor(getColor(R.color.darkAdaptiveColor))
//				                            .textSize(30)
//				                            .build());
//		this.stvMyProgress.addSlice(new Slice.Builder("38%")
//				                            .textColor(getColor(R.color.green))
//				                            .style(Typeface.BOLD)
//				                            .textSize(33)
//				                            .build());
//		// From
//		this.stvTodayFrom.addSlice(new Slice.Builder(getString(R.string.from) + "\n")
//				                           .textColor(getColor(R.color.darkAdaptiveColor))
//				                           .textSize(40)
//				                           .subscript()
//				                           .build());
//		this.stvTodayFrom.addSlice(new Slice.Builder("Al-Kahf | 30")
//				                           .textColor(getColor(R.color.green))
//				                           .style(Typeface.BOLD)
//				                           .superscript()
//				                           .textSizeRelative(1.5f)
//				                           .build());
//		// To
//		this.stvTodayTo.addSlice(new Slice.Builder(getString(R.string.to) + "\n")
//				                         .textColor(getColor(R.color.darkAdaptiveColor))
//				                         .textSize(40)
//				                         .subscript()
//				                         .build());
//		this.stvTodayTo.addSlice(new Slice.Builder("Al-Kahf | 125")
//				                         .textColor(getColor(R.color.green))
//				                         .style(Typeface.BOLD)
//				                         .superscript()
//				                         .textSizeRelative(1.5f)
//				                         .build());
//		// Display
//		this.stvMyProgress.display();
//		this.stvTodayFrom.display();
//		this.stvTodayTo.display();
	}

	public void workWithManager(KhatmaManager manager) {
		this.manager = manager;
		// Refresh view UI
		this.refreshUI();
	}

}

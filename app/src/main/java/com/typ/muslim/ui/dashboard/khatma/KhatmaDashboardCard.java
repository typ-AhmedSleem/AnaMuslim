/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.dashboard.khatma;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;

import com.typ.muslim.R;
import com.typ.muslim.interfaces.KhatmaManagerCallback;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.managers.KhatmaManager;
import com.typ.muslim.models.Khatma;
import com.typ.muslim.ui.dashboard.DashboardCard;
import com.typ.muslim.ui.dashboard.khatma.views.ActiveKhatmaView;
import com.typ.muslim.ui.dashboard.khatma.views.NoActiveKhatmaView;

import org.jetbrains.annotations.NotNull;

public class KhatmaDashboardCard extends DashboardCard implements KhatmaManagerCallback {

	// Statics
	private final String TAG = "KhatmaDashboardCard";
	// Runtime
	private KhatmaManager manager;
	// Views
	private ViewSwitcher switcher;

	public KhatmaDashboardCard(Context context) {
		super(context);
	}

	public KhatmaDashboardCard(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public KhatmaDashboardCard(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public void prepareRuntime(Context context) {
	}

	@Override
	public void prepareCardView(Context context) {
		// Inflate card view and init switcher
		inflate(context, R.layout.layout_khatma_card, this);
		setRippleColor(ColorStateList.valueOf(Color.TRANSPARENT));
		this.switcher = $(R.id.vs_khatma_card);
		this.switcher.setInAnimation(context, R.anim.fade_in_slide_in);
		this.switcher.setOutAnimation(context, R.anim.fade_out_slide_out);
		// Create a new ready KhatmaManager instance
		if (!isInEditMode()) refreshRuntime();
	}

	@Override
	public void refreshRuntime() {
		this.manager = KhatmaManager.getInstance(getContext(), this);
	}

	@Override
	public void refreshUI() {

	}

	public void flipToActiveKhatma() {
		// Flip card to active khatma view
		if (switcher.getCurrentView() instanceof NoActiveKhatmaView) switcher.showNext();
		// Update active khatma view
		((ActiveKhatmaView) switcher.getCurrentView()).workWithManager(manager);
	}

	public void flipToNoKhatma() {
		// Flip card to active khatma view
		if (switcher.getCurrentView() instanceof ActiveKhatmaView) switcher.showPrevious();
	}

	@Override
	public void onPrepareManager() {
		AManager.log(TAG, "onPrepareManager: Preparing Khatma Manager");
	}

	@Override
	public void onPutUnderManagement(@NonNull @NotNull Khatma underManagementKhatma) {
		this.flipToActiveKhatma();
		AManager.log(TAG, "onPutUnderManagement: Put [%s] under management", underManagementKhatma.toString(getContext()));
	}

	@Override
	public void onNoActiveKhatmaFound() {
		this.flipToNoKhatma();
		AManager.log(TAG, "onNoActiveKhatmaFound: No active khatma was found");
	}

	@Override
	public void onKhatmaProgressUpdated() {
		AManager.log(TAG, "onKhatmaProgressUpdated");
	}

	@Override
	public void onKhatmaReleased() {
		AManager.log(TAG, "onKhatmaReleased");
	}

}

/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.dashboard.prays;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.constraintlayout.motion.widget.MotionLayout;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.typ.muslim.R;
import com.typ.muslim.interfaces.PrayTimeCameListener;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.managers.PrayerManager;
import com.typ.muslim.models.Pray;
import com.typ.muslim.systems.ColorSystem;

import cn.iwgang.countdownview.CountdownView;

@SuppressLint("ViewConstructor")
public class PrayCard extends FrameLayout {

	// Statics
	private static final String TAG = PrayCard.class.getSimpleName();
	// Runtime
	private Pray nextPray;
	// Listeners
	private PrayTimeCameListener listener;
	private Pray pray;
	// Views
	private MaterialCardView cardBase;
	private MotionLayout motionContainer;
	private ImageView imgPraySymbol;
	private MaterialTextView tvPrayName, tvPrayTime;
	private CountdownView cdvRemainingTime;

	public PrayCard(Context context, Pray pray, PrayTimeCameListener listener) {
		super(context);
		this.pray = pray;
		this.listener = listener;
		this.nextPray = PrayerManager.getNextPray(context);
		this.prepareView(context);
	}

	public void prepareView(Context context) {
		inflate(context, R.layout.item_pray_card, this);
		// Init views
		this.cardBase = findViewById(R.id.card_item_pray);
		this.motionContainer = findViewById(R.id.motion_pray_card);
		this.imgPraySymbol = findViewById(R.id.img_pray_symbol);
		this.tvPrayName = findViewById(R.id.tv_pray_name);
		this.tvPrayTime = findViewById(R.id.tv_pray_time);
		this.cdvRemainingTime = findViewById(R.id.cdv_pray_time_remaining);
		// Show info in views
		tvPrayName.setText(pray.getName());
		setScaleX(pray.equals(nextPray) ? 1f : 0.95f);
		setScaleY(pray.equals(nextPray) ? 1f : 0.95f);
		tvPrayTime.setText(pray.getFormattedTime(getContext()));
		cdvRemainingTime.setOnCountdownEndListener(cv -> { if (listener != null) listener.onPrayTimeCame(pray); });
		// Change colors of views by pray
		tvPrayTime.setTextColor(ColorSystem.Colors.getPrayCardSubTextColor(getContext(), pray));
		tvPrayName.setTextColor(ColorSystem.Colors.getPrayCardHeadTextColor(getContext(), pray));
		imgPraySymbol.setColorFilter(ColorSystem.Colors.getPrayCardHeadTextColor(getContext(), pray));
		cardBase.setCardBackgroundColor(ColorSystem.Colors.getPrayCardSurfaceColor(getContext(), pray));
		AManager.log(TAG, "Card prepared: HOLDING[%s] || NEXT[%s] || IsHoldingNextPray[%b] || Passed[%b]", pray, nextPray, pray.equals(nextPray), pray.hasPassed());
	}

	public void activate() {
		this.cdvRemainingTime.start(5000);
		this.motionContainer.transitionToEnd();
	}

	public void suspend() {
		this.motionContainer.transitionToStart();
	}

	public boolean isHoldingPray(Pray pray) {
		return pray != null && pray.equals(this.pray);
	}

}

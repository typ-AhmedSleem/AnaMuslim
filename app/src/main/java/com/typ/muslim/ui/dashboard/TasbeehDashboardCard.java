/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.dashboard;

import static android.os.Build.VERSION.SDK_INT;
import static com.typ.muslim.enums.TasbeehatTimesTarget.TIMES_INFINITE;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;

import com.app.progresviews.ProgressWheel;
import com.google.android.material.textview.MaterialTextView;
import com.typ.muslim.R;
import com.typ.muslim.app.Keys;
import com.typ.muslim.enums.TasbeehatTimesTarget;
import com.typ.muslim.managers.AMSettings;
import com.typ.muslim.managers.TasbeehManager;
import com.typ.muslim.models.TasbeehModel;
import com.typ.muslim.ui.BottomSheets;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TasbeehDashboardCard extends DashboardCard {

	// todo: Add next and previous buttons when target set to INFINITE.
	// todo: Replace wheel progress with small width line progress and add spannable tv as counter to show ({made} /{target}).
	// todo: Use Sib7ah in card using a circular dial layout manager like MuslimPro.

	// Statics
	private static final String TAG = TasbeehDashboardCard.class.getSimpleName();
	// Runtime
	private Vibrator vibrator;
	private TasbeehatTimesTarget target;
	private int currentTasbeeh = 0;
	private long madeTasbeehCount = 0L;
	private boolean isUsingVolumeButtons;
	private List<TasbeehModel> tasbeehat;
	// Views
	private MaterialTextView tvContent; // todo: replace with TextSwitcher.
	private ImageButton fabPlusOne;
	//	private AnimatedNumberTextView tvCounter; // todo: create this class.
	private ProgressWheel progressCounter;

	public TasbeehDashboardCard(Context context) {
		super(context);
	}

	public TasbeehDashboardCard(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TasbeehDashboardCard(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public void prepareRuntime(Context context) {
		this.isUsingVolumeButtons = AMSettings.isUsingVolumeButtonsInTasbeeh(context);
		this.target = TasbeehManager.howManyTimesForEachTasbeeh(getContext());
		this.tasbeehat = TasbeehManager.getDailyTasbeehat(context, this.target);
	}

	@Override
	public void prepareCardView(Context context) {
		// Inflate view
		inflate(context, R.layout.layout_tasbeeh_card, this);
		// Init views
		this.tvContent = $(R.id.tv_tasbeeh_content);
		this.fabPlusOne = $(R.id.ibtn_plus_one);
		this.progressCounter = $(R.id.progress_tasbeeh_count);
		// Create listeners
		this.fabPlusOne.setOnClickListener(this);
		// Perform card ui refresh
		refreshUI();
		// Get vibrator instance
		if (!isInEditMode()) this.vibrator = getVibrator();
	}

	@Override
	public void refreshUI() {
		// todo: Showing or hiding some views depends on target
		if (target == TIMES_INFINITE) {
			// todo: Show next and prev buttons
			this.progressCounter.setPercentage(0);
		} else {
			// todo: Hide next and prev buttons
			int howManyTimes = getCurrentTasbeeh().howManyTimes();
			if (madeTasbeehCount >= howManyTimes) {
				this.madeTasbeehCount = -1;
				this.currentTasbeeh++;
				this.plusOne();
			}
			this.progressCounter.setPercentage(Math.round(madeTasbeehCount * calculateStep(target.howMany())));
			this.progressCounter.setStepCountText(String.valueOf(madeTasbeehCount));
		}
		// Show tasbeeh item its index is in runtime
		this.tvContent.setText(this.tasbeehat.get(this.currentTasbeeh).getContent());
	}

	public void plusOne() {
		int tasbeehItemTimes = getCurrentTasbeeh().howManyTimes();
		this.madeTasbeehCount++;
		if (target.howMany() == -1) {
			// Increase counter only
			this.progressCounter.setPercentage(0);
			this.progressCounter.setStepCountText(String.valueOf(this.madeTasbeehCount));
		} else if (madeTasbeehCount > tasbeehItemTimes) {
			// Show next tasbeeh ==then=> Reset counter and progress
			this.madeTasbeehCount = 0;
			this.progressCounter.setPercentage(0);
			this.progressCounter.setStepCountText(String.valueOf(0));
			this.tvContent.setText(tasbeehat.get(++currentTasbeeh >= tasbeehat.size() ? 0 : currentTasbeeh).getContent());
			// Play the sound of next tasbeeh name and do a long haptic feedback
			MediaPlayer.create(getContext(), R.raw.sound_flip).start(); // fixme: replace with next tasbeeh name sound file resId
			if (SDK_INT >= Build.VERSION_CODES.O) vibrator.vibrate(VibrationEffect.createWaveform(new long[]{50, 50, 50, 50}, -1));
			else vibrator.vibrate(new long[]{50, 50, 50, 50}, -1);
		} else {
			// Increase counter and progress
			this.progressCounter.setStepCountText(String.valueOf(madeTasbeehCount));
			this.progressCounter.setPercentage(Math.round(madeTasbeehCount * calculateStep(tasbeehItemTimes)));
			// Play tick sound and perform a tap haptic feedback
			MediaPlayer.create(getContext(), R.raw.sound_tick).start();
			if (SDK_INT >= Build.VERSION_CODES.Q) getVibrator().vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK));
			else vibrator.vibrate(25);
		}
	}

	private TasbeehModel getCurrentTasbeeh() {
		if (currentTasbeeh >= tasbeehat.size()) currentTasbeeh = 0;
		return tasbeehat.get(currentTasbeeh);
	}

	public void reset() {
		// Reset counter and current tasbeeh in runtime
		this.currentTasbeeh = 0;
		this.madeTasbeehCount = 0L;
		// Preform UI refresh
		this.refreshUI();
		// Perform long haptic feedback
		if (SDK_INT >= Build.VERSION_CODES.Q) getVibrator().vibrate(VibrationEffect.createOneShot(250,125));
		else vibrator.vibrate(250);
	}

	private float calculateStep(int howMany) {
		return target != TIMES_INFINITE ? 360f / howMany : 0f;
	}

	public boolean handleKeyEvent(int keyCode, KeyEvent event) {
		// RightNow, all volume buttons makes plus one
		if (isUsingVolumeButtons && (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {
			this.plusOne();
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		this.plusOne();
	}

	@Override
	public boolean onLongClick(View v) {
		if (!isBottomSheetShown) {
			BottomSheets.newTasbeehOptions(
					getContext(),
					this.target,
					newTarget -> {
						if (this.target == newTarget) return;
						// Update times of each tasbeeh that holds old times
						for (int i = 0; i < tasbeehat.size(); i++) {
							TasbeehModel tasbeeh = tasbeehat.get(i);
							if (tasbeeh.howManyTimes() == this.target.howMany()) tasbeehat.set(i, tasbeeh.setHowMany(newTarget.howMany()));
						}
						this.target = newTarget;
						// Refresh UI
						this.refreshUI();
					}, actionId -> {
						if (actionId == R.id.btn_reset_tasbeeh) this.reset();
						else if (actionId == R.id.btn_open_tasbeeh_activity) {
							// todo: Open TasbeehActivity and pass card runtime as bundle
						}
					}, isVolumeButtonsEnabled -> {
						this.isUsingVolumeButtons = isVolumeButtonsEnabled;
						AMSettings.save(getContext(), Keys.USE_VOLUME_BUTTONS_IN_TASBEEH, isUsingVolumeButtons);
					}, isShown -> isBottomSheetShown = isShown).show();
		}
		return true;
	}

	@NotNull
	@Override
	public String toString() {
		return "TasbeehDashboardCard";
	}
}

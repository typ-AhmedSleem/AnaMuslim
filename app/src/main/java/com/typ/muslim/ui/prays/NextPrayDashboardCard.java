/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.prays;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.typ.muslim.R;
import com.typ.muslim.core.praytime.enums.Prays;
import com.typ.muslim.managers.AMSettings;
import com.typ.muslim.managers.PrayerManager;
import com.typ.muslim.managers.ResMan;
import com.typ.muslim.models.Pray;
import com.typ.muslim.systems.ColorSystem;
import com.typ.muslim.ui.AMTextSwitcher;
import com.typ.muslim.ui.home.DashboardCard;

import cn.iwgang.countdownview.CountdownView;

public class NextPrayDashboardCard extends DashboardCard {

	// TODO: Pick best color combinations for each Pray

	// Statics
	private static final String TAG = "NextPrayDashboardCard";
	// Runtime
	private Pray nextPray;
	// Views
	private PopupMenu optionsMenu;
	private AMTextSwitcher nextPrayNameTS, nextPrayTimeTS, notifyMethodTS;
	private ImageView notifyMethodIV;
	private CountdownView remainingTimeCD;

	public NextPrayDashboardCard(Context context) {
		super(context);
	}

	@Override
	public void prepareRuntime(Context context) {
		this.nextPray = PrayerManager.getNextPray(context);
	}

	@Override
	public void prepareCardView(Context context) {
		// Inflate view
		inflate(context, R.layout.layout_next_pray_card, this);
		// Setup popup options menu
		optionsMenu = new PopupMenu(context, this, Gravity.CENTER);
		// TODO: 6/17/2021 create options menu res and inflate it
		// Setup content views
		this.nextPrayNameTS = findViewById(R.id.tv_next_pray_name);
		this.nextPrayTimeTS = findViewById(R.id.tv_next_pray_time);
		this.notifyMethodTS = findViewById(R.id.next_pray_notify_method_tv);
		this.notifyMethodIV = findViewById(R.id.next_pray_notify_method_iv);
		this.remainingTimeCD = findViewById(R.id.cdv_next_pray_remaining);
		// Init content views
		this.nextPrayNameTS.setTextSize(18);
		this.nextPrayTimeTS.setTextSize(14);
		this.notifyMethodTS.setTextSize(8);
		// Callbacks
		this.remainingTimeCD.setOnCountdownEndListener(cv -> {
			// Set the past pray before refreshing card runtime
			final Pray pastPray = this.nextPray;
			// Update runtime and views
//			if (this.nextPray.getType() == Prays.ISHA) this.nextPray.setType(Prays.Fajr).setName("Fajr");
//			else this.nextPray.setType(Prays.valueOf(this.nextPray.getType().ordinal() + 1)).setName(this.nextPray.getType().name());
			this.prepareRuntime(context);
			this.setNextPray(this.nextPray);
			// Add not prayed record if he didn't pray the past one.
//			if (pastPray != null && !PrayerManager.didHePray(context, pastPray)) PrayTrackerManager.record(context, pastPray.getType(),
//					pastPray.getIn(), 0, System.currentTimeMillis());
			// Call any listeners or callbacks registered to this card
		});
		// Start the card
		if (!this.isInEditMode()) {
			// Update views
			this.setNextPray(this.nextPray);
		}
	}

	public NextPrayDashboardCard setNextPray(Pray nextPray) {
        // Necessary check
        if (nextPray == null) return this;
        this.nextPray = nextPray;
        // Determine colors based on next pray
        int targetSurfaceColor = ResMan.getColor(getContext(), R.color.nextPrayCardSurfaceEndColor);
        int targetNameTextColor = ResMan.getColor(getContext(), R.color.yellow);
        int targetTimeTextColor = ResMan.getColor(getContext(), R.color.whiteOpacity80);
        switch (nextPray.getType()) {
            case FAJR:
                targetSurfaceColor = ResMan.getColor(getContext(), R.color.color_fajr_header);
                break;
            case SUNRISE:
                targetSurfaceColor = ResMan.getColor(getContext(), R.color.color_dhuhr_sunrise_bg);
                targetNameTextColor = ResMan.getColor(getContext(), R.color.red);
                targetTimeTextColor = ResMan.getColor(getContext(), R.color.whiteOpacity80);
                break;
            case DHUHR:
                targetSurfaceColor = ResMan.getColor(getContext(), R.color.color_dhuhr_sunrise_header);
                targetNameTextColor = ResMan.getColor(getContext(), R.color.red);
                targetTimeTextColor = ResMan.getColor(getContext(), R.color.blackOpacity60);
                break;
            case ASR:
                targetSurfaceColor = ResMan.getColor(getContext(), R.color.color_asr_header);
                targetNameTextColor = ResMan.getColor(getContext(), R.color.yellow);
                targetTimeTextColor = ResMan.getColor(getContext(), R.color.blackOpacity60);
                break;
            case MAGHRIB:
                targetSurfaceColor = ResMan.getColor(getContext(), R.color.color_maghrib_isha_header);
                targetNameTextColor = ResMan.getColor(getContext(), R.color.yellow);
                targetTimeTextColor = ResMan.getColor(getContext(), R.color.whiteOpacity80);
                break;
            case ISHA:
                targetSurfaceColor = ResMan.getColor(getContext(), R.color.isha_bg);
                targetNameTextColor = ResMan.getColor(getContext(), R.color.yellow);
                targetTimeTextColor = ResMan.getColor(getContext(), R.color.whiteOpacity80);
                break;
        }
		// Update content views
		this.nextPrayNameTS.setTextColor(targetNameTextColor).displayText(this.nextPray.getName());
		this.nextPrayTimeTS.setTextColor(targetTimeTextColor).displayText(this.nextPray.getFormattedTime(getContext()));
		this.remainingTimeCD.start(this.nextPray.getIn().toMillis() - System.currentTimeMillis());
		switch (AMSettings.getPrayNotifyMethod(getContext(), this.nextPray.getType())) {
			case AZAN:
				if (this.nextPray.getType() != Prays.SUNRISE) {
					this.notifyMethodIV.setImageResource(R.drawable.ic_notify_with_sound);
					this.notifyMethodIV.setColorFilter(targetTimeTextColor);
					this.notifyMethodTS.setTextColor(targetTimeTextColor).displayText(R.string.azan_enabled);
				}
				break;
			case NOTIFICATION_ONLY:
				this.notifyMethodIV.setImageResource(R.drawable.ic_notify_without_sound);
				this.notifyMethodIV.setColorFilter(targetTimeTextColor);
				this.notifyMethodTS.setTextColor(targetTimeTextColor).displayText(R.string.notifications_only);
				break;
			case OFF:
				this.notifyMethodIV.setImageResource(R.drawable.ic_notify_off);
				this.notifyMethodIV.setColorFilter(targetTimeTextColor);
				this.notifyMethodTS.setTextColor(targetTimeTextColor).displayText(R.string.muted);
				break;
		}
		// Animate card surface color with determined target color
		this.animateCardColor(targetSurfaceColor);
		return this;
	}

	@Override
	public void refreshUI() {
		this.prepareRuntime(getContext());
	}

	@Override
	public void onClick(View v) {
	}

	@Override
	public boolean onLongClick(View v) {
		optionsMenu.show();
		return true;
	}

	@Override
	public void onFirstTheme(ColorSystem.GlobalTheme globalTheme, ColorSystem.ColorTheme colorTheme) {
		super.onFirstTheme(globalTheme, colorTheme);
		// TODO: Choose the current card bg color based on global & color theme to avoid contrast near
	}

	@Override
	public void onThemeChanged(View madeCallView, ColorSystem.GlobalTheme globalTheme, ColorSystem.ColorTheme colorTheme) {
		super.onThemeChanged(madeCallView, globalTheme, colorTheme);
		// TODO: Animate card bg color if needed to avoid colors contrast near
	}

}

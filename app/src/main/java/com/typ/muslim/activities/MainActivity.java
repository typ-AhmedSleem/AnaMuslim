/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityOptionsCompat;

import com.google.android.material.transition.platform.MaterialArcMotion;
import com.google.android.material.transition.platform.MaterialContainerTransform;
import com.typ.muslim.R;
import com.typ.muslim.activities.khatma.KhatmaActivity;
import com.typ.muslim.interfaces.PrayTimeCameListener;
import com.typ.muslim.interfaces.TimeChangedListener;
import com.typ.muslim.libs.easyjava.data.EasyList;
import com.typ.muslim.managers.KhatmaManager;
import com.typ.muslim.managers.PrayerManager;
import com.typ.muslim.managers.QuranTrackerManager;
import com.typ.muslim.models.Pray;
import com.typ.muslim.models.ReadQuranRecord;
import com.typ.muslim.models.Timestamp;
import com.typ.muslim.ui.ActiveKhatmaCard;
import com.typ.muslim.ui.dashboard.DashboardCard;
import com.typ.muslim.ui.dashboard.TasbeehDashboardCard;
import com.typ.muslim.ui.dashboard.prays.MiniNextPrayDashboardCard;
import com.typ.muslim.ui.dashboard.tracker.TrackerDashboardCard;

import org.jetbrains.annotations.NotNull;

import kotlin.random.Random;

public class MainActivity extends AppCompatActivity implements PrayTimeCameListener, TimeChangedListener {

	// todo: Ask user in wizard if he want to increase tasbeeh by pressing volume buttons and if it's enabled, use it here with tasbeeh card.

	// Statics
	private static final String TAG = "MainActivity";
	private final EasyList<DashboardCard> cards = new EasyList<>();
	// Views
	private ImageButton ibtnMainMenu;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Setup transitions
		getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
		// Set theme and content view
		AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
		setContentView(R.layout.activity_main);
		// Hide default ActionBar
		if (getSupportActionBar() != null) getSupportActionBar().hide();
		// Init views
		ibtnMainMenu = findViewById(R.id.ibtn_main_menu);
		cards.add(findViewById(R.id.mini_next_pray_dashboard_card)); // MiniNextPray card.
		cards.add(findViewById(R.id.tracker_dashboard_card)); // Tracker card.
		cards.add(findViewById(R.id.hijri_dashboard_card)); // Hijri card.
		cards.add(findViewById(R.id.tasbeeh_dashboard_card)); // Tasbeeh card.
		cards.add(findViewById(R.id.khatma_card)); // Khatma card.
		cards.add(findViewById(R.id.som_dashboard_card)); // SalatOnMohamed card.
		cards.add(findViewById(R.id.ramadan_dashboard_card)); // Ramadan card.
		cards.add(findViewById(R.id.telegram_bots_dashboard_card)); // TelegramBots card.
		cards.add(findViewById(R.id.profile_dashboard_card)); // Profile card.
		// Prepare cards runtime
		( (ActiveKhatmaCard)cards.get(4)).setKhatma(KhatmaManager.getLastActiveKhatma(this));
		// Click listeners
		ibtnMainMenu.setOnClickListener(v -> {
			QuranTrackerManager.record(this, new ReadQuranRecord(1, 1, 1, 1, Random.Default.nextInt(0, 5), System.currentTimeMillis()));
			((TrackerDashboardCard) cards.getIf((i, card) -> card instanceof TrackerDashboardCard)).showStatistics();
		});
		cards.get(2).setOnClickListener(v -> {
			final ActivityOptionsCompat op = ActivityOptionsCompat.makeSceneTransitionAnimation(this, v, "transition_card_to_activity");
			startActivity(new Intent(this, HijriCalendarActivity.class), op.toBundle());
		});
		cards.get(4).setOnClickListener(v-> {
			final ActivityOptionsCompat op = ActivityOptionsCompat.makeSceneTransitionAnimation(this, v, "transition_card_to_card");
			startActivity(new Intent(this, KhatmaActivity.class), op.toBundle());
		});
		// Callbacks
		((MiniNextPrayDashboardCard) cards.getIf((i, card) -> card instanceof MiniNextPrayDashboardCard)).setPrayTimeCameListener(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean isHandled = ((TasbeehDashboardCard) cards.getIf((i, card) -> card instanceof TasbeehDashboardCard)).handleKeyEvent(keyCode, event);
		return isHandled || super.onKeyDown(keyCode, event);
	}

	@Override
	public Pray onPrayTimeCame(Pray pray) {
		// Notify cards that have this callback
		cards.iterate(card -> { if (card instanceof PrayTimeCameListener) ((PrayTimeCameListener) card).onPrayTimeCame(pray); });
		return PrayerManager.getNextPray(this);
	}

	@Override
	public void onTimeChanged(Timestamp now) {
		// Notify all cards to refresh its runtime and its UI
		cards.iterate(card -> card.onTimeChanged(now));
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Refresh cards
		this.cards.iterate(DashboardCard::performRefresh);
	}

	@Override
	protected void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
		super.onSaveInstanceState(outState);
		// Save cards instances
		this.cards.iterate(card -> card.saveInstanceState(outState));
	}

	@Override
	protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		// Restore cards instances
		this.cards.iterate(card -> card.restoreInstance(savedInstanceState));
	}


}

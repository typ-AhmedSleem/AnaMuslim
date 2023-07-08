/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityOptionsCompat;

import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;
import com.typ.muslim.R;
import com.typ.muslim.app.Keys;
import com.typ.muslim.features.qibla.ui.QiblaDashboardCard;
import com.typ.muslim.interfaces.PrayTimeCameListener;
import com.typ.muslim.interfaces.TimeChangedListener;
import com.typ.muslim.libs.easyjava.data.EasyList;
import com.typ.muslim.managers.PrayerManager;
import com.typ.muslim.models.Pray;
import com.typ.muslim.models.Timestamp;
import com.typ.muslim.ui.calendar.HijriCalendarActivity;
import com.typ.muslim.ui.khatma.dashboard.KhatmaDashboardCard;
import com.typ.muslim.ui.names.AllahNamesDashboardCard;
import com.typ.muslim.ui.names.HolyNameOfAllahDescActivity;
import com.typ.muslim.ui.prays.MiniNextPrayDashboardCard;
import com.typ.muslim.ui.tasbeeh.TasbeehDashboardCard;
import com.typ.muslim.ui.tracker.dashboard.TrackerDashboardCard;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements PrayTimeCameListener, TimeChangedListener {

    // Statics
    private static final String TAG = "MainActivity";
    private final EasyList<DashboardCard> cards = new EasyList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Setup transitions
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setSharedElementsUseOverlay(false);
        setExitSharedElementCallback(new MaterialContainerTransformSharedElementCallback());
        // Set theme and content view
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);
        // Hide default ActionBar
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        // Init views
        cards.add(findViewById(R.id.mini_next_pray_dashboard_card)); // 0 -> MiniNextPray card.
        cards.add(findViewById(R.id.tracker_dashboard_card)); // 1 -> Tracker card.
        cards.add(findViewById(R.id.hijri_dashboard_card)); // 2 -> Hijri card.
        cards.add(findViewById(R.id.tasbeeh_dashboard_card)); // 3 -> Tasbeeh card.
        cards.add(findViewById(R.id.allah_names_dashboard_card)); // 4 -> AllahNames card.
        cards.add(findViewById(R.id.khatma_card)); // 5 -> Khatma card.
        cards.add(findViewById(R.id.qibla_dashboard_card)); // 6 -> Qibla card.
        cards.add(findViewById(R.id.ramadan_dashboard_card)); // 7 -> Ramadan card.
        cards.add(findViewById(R.id.telegram_bots_dashboard_card)); // 8 -> TelegramBots card.
        cards.add(findViewById(R.id.profile_dashboard_card)); // 9 -> Profile card.
        /* Click listeners */
        cards.get(1).setOnClickListener(v -> ((TrackerDashboardCard) cards.get(1)).onClick(this));
        cards.get(2).setOnClickListener(v -> {
            Toast.makeText(this, "Not yet done and planned to be implemented in v1.2", Toast.LENGTH_SHORT).show();
            final ActivityOptionsCompat op = ActivityOptionsCompat.makeSceneTransitionAnimation(this, v, "transition_card_to_activity");
            startActivity(new Intent(this, HijriCalendarActivity.class), op.toBundle());
        });
        cards.get(4).setOnClickListener(v -> {
            Toast.makeText(this, "Not yet done and planned to be implemented in v1.2", Toast.LENGTH_SHORT).show();
            final ActivityOptionsCompat op = ActivityOptionsCompat.makeSceneTransitionAnimation(this, v, "transition_card_to_activity");
            final Intent intent = new Intent(this, HolyNameOfAllahDescActivity.class);
            intent.putExtra(Keys.NAME_OF_ALLAH, ((AllahNamesDashboardCard) cards.get(4)).getHoldingName());
            startActivity(intent, op.toBundle());
        });
        cards.get(5).setOnClickListener(v -> ((KhatmaDashboardCard) v).handleClick(this));
        cards.get(6).setOnClickListener(v -> {
            final ActivityOptionsCompat op = ActivityOptionsCompat.makeSceneTransitionAnimation(this, v, "transition_card_to_activity");
//            startActivity(new Intent(this, QiblaActivity.class), op.toBundle());
            Toast.makeText(this, "Has bugs and not stable yet.. Planned to be fixed at v1.1.10 and stable at v1.2", Toast.LENGTH_SHORT).show();
        });
        cards.get(7).setOnClickListener(v -> Toast.makeText(this, R.string.feature_under_dev, Toast.LENGTH_SHORT).show());
        cards.get(9).setOnClickListener(v -> Toast.makeText(this, R.string.feature_under_dev, Toast.LENGTH_LONG).show());
        // Register callbacks
        ((MiniNextPrayDashboardCard) cards.get(0)).setPrayTimeCameListener(this);
        ((QiblaDashboardCard) cards.get(6)).register();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean isHandled = ((TasbeehDashboardCard) cards.get(3)).handleKeyEvent(keyCode);
        return isHandled || super.onKeyDown(keyCode, event);
    }

    @Override
    public Pray onPrayTimeCame(Pray pray) {
        // Notify cards that have this callback
        cards.iterate(card -> {
            if (card instanceof PrayTimeCameListener) ((PrayTimeCameListener) card).onPrayTimeCame(pray);
        });
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
        ((QiblaDashboardCard) cards.get(6)).unregister();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh cards
        cards.iterate(DashboardCard::performRefresh);
        ((QiblaDashboardCard) cards.get(6)).register();
    }

    @Override
    protected void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save cards instances
        cards.iterate(card -> card.saveInstanceState(outState));
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore cards instances
        cards.iterate(card -> card.restoreInstance(savedInstanceState));
    }
}

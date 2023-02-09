/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.dashboard.ramadan;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ViewSwitcher;

import com.typ.muslim.R;
import com.typ.muslim.core.praytime.enums.Prays;
import com.typ.muslim.interfaces.PrayTimeCameListener;
import com.typ.muslim.models.Pray;
import com.typ.muslim.models.Timestamp;
import com.typ.muslim.ramadan.RamadanManager;
import com.typ.muslim.ui.dashboard.DashboardCard;
import com.typ.muslim.ui.dashboard.ramadan.views.InRamadanView;
import com.typ.muslim.ui.dashboard.ramadan.views.RamadanRemainingView;

public class RamadanDashboardCard extends DashboardCard implements PrayTimeCameListener {

    // Statics
    private static final String TAG = "RamadanDashboardCard";
    // Views
    private ViewSwitcher switcher;

    public RamadanDashboardCard(Context context) {
        super(context);
    }

    public RamadanDashboardCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RamadanDashboardCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void prepareCardView(Context context) {
        // Inflate card view and change card bg color
        inflate(context, R.layout.layout_ramadan_card, this);
        setCardBackgroundColor(getColor(R.color.isha_bg));
        setOnClickListener(this);
        setOnLongClickListener(this);
        // Init view switcher
        this.switcher = $(R.id.vs_ramadan);
        // Perform UI refresh
        if (!isInEditMode()) refreshUI();
    }

    @Override
    public void refreshUI() {
        if (RamadanManager.isInRamadan()) showInRamadanView();
        else showRemainingView();
    }

    public void showRemainingView() {
        if (switcher.getCurrentView() instanceof InRamadanView) switcher.showPrevious();
    }

    public void showInRamadanView() {
        if (switcher.getCurrentView() instanceof RamadanRemainingView) switcher.showNext();
    }

    @Override
    public void onClick(View v) {
        if (switcher.getCurrentView() instanceof RamadanRemainingView) {
            // todo: Show RamadanRemainingActivity if this month isn't ramadan.
        } else {
            // todo: Show InRamadanActivity if this month is ramadan.
        }
    }

    @Override
    public Pray onPrayTimeCame(Pray pray) {
        if (switcher.getCurrentView() instanceof InRamadanView && (pray.getType() == Prays.FAJR || pray.getType() == Prays.MAGHRIB)) {
            // Refresh InRamadanView
            ((InRamadanView) switcher.getCurrentView()).refreshUI();
        }

        return null; // Do nothing.
    }

    @Override
    public void onTimeChanged(Timestamp now) {
        this.refreshUI();
    }
}

/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.app.progresviews.ProgressWheel;
import com.google.android.material.textview.MaterialTextView;
import com.typ.muslim.R;
import com.typ.muslim.managers.KhatmaManager;
import com.typ.muslim.models.Khatma;
import com.typ.muslim.ui.dashboard.DashboardCard;

public class KhatmaDashboardCard extends DashboardCard {

    // Runtime
    private Khatma khatma;
    // Views
    private MaterialTextView tvDayNumber, tvMsg;
    private ProgressWheel progressKhatma;

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
        if (!isInEditMode()) this.khatma = KhatmaManager.getLastActiveKhatma(context);
    }

    @Override
    public void prepareCardView(Context context) {
        // Inflate card view and setup it
        inflate(R.layout.layout_active_khatma_card);
        setRippleColorResource(R.color.ripple_white);
        setCardBackgroundColor(getColor(R.color.green));
        // Init views
        tvDayNumber = $(R.id.tv_khatma_day_number);
        tvMsg = $(R.id.tv_msg);
        progressKhatma = $(R.id.prw_khatma_percentage);
        // Reset views
        reset();
    }

    @Override
    public void reset() {
        progressKhatma.setPercentage(0);
        progressKhatma.setStepCountText("0");
        progressKhatma.setVisibility(INVISIBLE);
        tvDayNumber.setText(R.string.no_active_khatma);
        tvMsg.setText(R.string.click_to_create_or_join_khatma);
        setCardBackgroundColor(getColor(R.color.white));
        tvDayNumber.setTextColor(getColor(R.color.darkAdaptiveColor));
        tvMsg.setTextColor(getColor(R.color.subtitleTextColor));
    }

    @Override
    public void refreshUI() {
        if (this.khatma == null) reset();
        else {
            if (khatma.isActive()) {
                final int todayNumber = khatma.getTodayNumber();
                progressKhatma.setVisibility(VISIBLE);
                progressKhatma.setStepCountText(Math.min(khatma.getProgressPercentage(), 100) + "%");
                progressKhatma.setPercentage(Math.min((int) ((khatma.getProgressPercentage() / 100f) * 360), 360));
                tvMsg.setText(R.string.click_to_view_khatma_details);
                tvDayNumber.setText(khatma.getName(getContext()));
                setCardBackgroundColor(getColor(R.color.green));
                tvDayNumber.setTextColor(getColor(R.color.adaptiveTextColor));
                tvMsg.setTextColor(getColor(R.color.brightSubtitleTextColor));
            } else reset();
        }
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void refreshRuntime() {
        prepareRuntime(getContext());
    }
}

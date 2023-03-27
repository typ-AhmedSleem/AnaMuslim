/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.prays;

import static com.typ.muslim.core.praytime.enums.Prays.FAJR;
import static com.typ.muslim.core.praytime.enums.Prays.ISHA;
import static com.typ.muslim.core.praytime.enums.Prays.MAGHRIB;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.textview.MaterialTextView;
import com.mpt.android.stv.Slice;
import com.mpt.android.stv.SpannableTextView;
import com.typ.muslim.R;
import com.typ.muslim.core.praytime.enums.Prays;
import com.typ.muslim.enums.PrayNotifyMethod;
import com.typ.muslim.features.ramadan.RamadanManager;
import com.typ.muslim.interfaces.PrayTimeCameListener;
import com.typ.muslim.managers.AMSettings;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.managers.PrayerManager;
import com.typ.muslim.managers.ResMan;
import com.typ.muslim.models.Pray;
import com.typ.muslim.models.Timestamp;
import com.typ.muslim.ui.BottomSheets;
import com.typ.muslim.ui.home.DashboardCard;
import com.typ.muslim.utils.DisplayUtils;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import cn.iwgang.countdownview.CountdownView;

public class MiniNextPrayDashboardCard extends DashboardCard {

    // Statics
    private static final String TAG = "MiniNextPrayDashboardCard";
    // Runtime
    private Pray nextPray;
    // Views
    private SpannableTextView stvNextPrayName; // TODO: 3/3/21 use text switcher.
    private MaterialTextView nextPrayTimeTS; // TODO: 3/3/21 use text switcher.
    private ImageView prayNotifMethodIFV;
    private CountdownView timeRemainingCD;
    // Listeners
    private PrayTimeCameListener prayTimeCameListener;

    public MiniNextPrayDashboardCard(Context context) {
        super(context);
    }

    public MiniNextPrayDashboardCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MiniNextPrayDashboardCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void prepareRuntime(Context context) {
        if (isInEditMode()) return;
        this.nextPray = PrayerManager.getNextPray(context);
    }

    @Override
    public void prepareCardView(Context context) {
        // Inflate card view and change its bg color
        inflate(getContext(), R.layout.layout_minimized_next_pray_card, this);
        setCardBackgroundColor(getColor(R.color.nextPrayCardSurfaceStartColor));
        // Setup content views
        this.prayNotifMethodIFV = $(R.id.prayNotifMethodIFV);
        this.stvNextPrayName = $(R.id.tv_next_pray_name);
        this.nextPrayTimeTS = $(R.id.tv_next_pray_time);
        this.timeRemainingCD = $(R.id.cdv_next_pray_remaining);
        // Callbacks
        this.setOnClickListener(this);
        this.setOnLongClickListener(this);
        timeRemainingCD.setOnCountdownEndListener(cv -> {
            // Refresh runtime
            if (prayTimeCameListener != null) this.setNextPray(prayTimeCameListener.onPrayTimeCame(nextPray));
            else this.refreshRuntime();
            // Refresh UI
            this.refreshUI();
        });
        // Refresh UI
        if (isInEditMode()) nextPray = new Pray(FAJR, "Fajr", System.currentTimeMillis() + 60 * 1000);
        this.refreshUI();
    }

    @Override
    public void refreshRuntime() {
        prepareRuntime(getContext());
    }

    @Override
    public void refreshUI() {
        // Update views
        this.updateNotifyMethod();
        this.stvNextPrayName.reset();
        this.showNextPrayNameOnTv();
        this.stvNextPrayName.display();
        this.nextPrayTimeTS.setText(this.nextPray.getFormattedTime(getContext()));
        this.timeRemainingCD.start(this.nextPray.getIn().toMillis() - System.currentTimeMillis());
        // Animate the card bg color according to pray
        if (isInEditMode()) return;
        animateCardColor(getColor(nextPray.getSurfaceColorRes()));
    }

    private void showNextPrayNameOnTv() {
        Pray currPray = null;
        if (!isInEditMode()) currPray = PrayerManager.getCurrentPray(getContext());
        // Add PrayName slice
        stvNextPrayName.addSlice(new Slice.Builder(getString(nextPray.getPrayNameRes()))
                .textColor(getColor(isInEditMode() ? R.color.yellow : nextPray.getOnSurfaceColorRes()))
                .textSize(DisplayUtils.sp2px(getContext(), 15f))
                .style(Typeface.BOLD)
                .build());
        // Add Suhur, Iftar, Qiyam slice if in Ramadan
        if (RamadanManager.isInRamadan() && (nextPray.getType() == FAJR || nextPray.getType() == MAGHRIB || nextPray.getType() == ISHA)) {
            final String sliceText;
//			if (nextPray.getType() == FAJR) sliceText = getString(R.string.suhur);
            if (nextPray.getType() == MAGHRIB) sliceText = getString(R.string.iftar);
            else if (nextPray.getType() == ISHA) sliceText = getString(R.string.qiyam);
            else sliceText = "";
            if (!TextUtils.isEmpty(sliceText)) {
                stvNextPrayName.addSlice(new Slice.Builder(String.format(Locale.getDefault(), "  (%s)", sliceText))
                        .textSize(20)
                        .textColor(getColor(nextPray.getOnSurfaceColorRes()))
                        .build());
            }
        }
        // Add Tomorrow slice if before 12 am next day and next pray is FAJR
        if (currPray != null && nextPray.getType() == FAJR && !nextPray.getIn().dateMatches(Timestamp.NOW())) {
            stvNextPrayName.addSlice(new Slice.Builder(String.format(Locale.getDefault(), "  (%s)", getString(R.string.tomorrow)))
                    .textSize(20)
                    .textColor(getColor(nextPray.getOnSurfaceColorRes()))
                    .build());
        }
    }

    public Pray getHoldingPray() {
        return nextPray;
    }

    public void setNextPray(Pray nextPray) {
        // Update runtime
        if (nextPray == null) this.refreshRuntime();
        else if (nextPray.hasPassed()) this.refreshRuntime();
        else this.nextPray = nextPray;
    }

    public void setPrayTimeCameListener(PrayTimeCameListener listener) {
        this.prayTimeCameListener = listener;
    }

    private void updateNotifyMethod() {
        // Update content views
        switch (AMSettings.getPrayNotifyMethod(getContext(), this.nextPray.getType())) {
            case AZAN:
                if (this.nextPray.getType() != Prays.SUNRISE) {
                    this.prayNotifMethodIFV.setImageResource(R.drawable.ic_notify_with_sound);
                    this.prayNotifMethodIFV.setBackgroundTintList(ColorStateList.valueOf(ResMan.getColor(getContext(), R.color.white)));
                    this.prayNotifMethodIFV.setColorFilter(ResMan.getColor(getContext(), R.color.colorPrimary));
                }
                break;
            case NOTIFICATION_ONLY:
                this.prayNotifMethodIFV.setImageResource(R.drawable.ic_notify_without_sound);
                this.prayNotifMethodIFV.setBackgroundTintList(ColorStateList.valueOf(ResMan.getColor(getContext(), R.color.white)));
                this.prayNotifMethodIFV.setColorFilter(ResMan.getColor(getContext(), R.color.ef_colorPrimary));
                break;
            case OFF:
                this.prayNotifMethodIFV.setImageResource(R.drawable.ic_notify_off);
                this.prayNotifMethodIFV.setBackgroundTintList(ColorStateList.valueOf(ResMan.getColor(getContext(), R.color.white)));
                this.prayNotifMethodIFV.setColorFilter(ResMan.getColor(getContext(), R.color.red));
                break;
        }
    }

    public void changeNotifyMethod(PrayNotifyMethod newMethod) {
        if (newMethod == null) return;
        // Update content views
        switch (newMethod) {
            case AZAN:
                if (this.nextPray.getType() != Prays.SUNRISE) {
                    this.prayNotifMethodIFV.setImageResource(R.drawable.ic_notify_with_sound);
                    this.prayNotifMethodIFV.setBackgroundTintList(ColorStateList.valueOf(ResMan.getColor(getContext(), R.color.white)));
                    this.prayNotifMethodIFV.setColorFilter(ResMan.getColor(getContext(), R.color.colorPrimary));
                }
                break;
            case NOTIFICATION_ONLY:
                this.prayNotifMethodIFV.setImageResource(R.drawable.ic_notify_without_sound);
                this.prayNotifMethodIFV.setBackgroundTintList(ColorStateList.valueOf(ResMan.getColor(getContext(), R.color.white)));
                this.prayNotifMethodIFV.setColorFilter(ResMan.getColor(getContext(), R.color.ef_colorPrimary));
                break;
            case OFF:
                this.prayNotifMethodIFV.setImageResource(R.drawable.ic_notify_off);
                this.prayNotifMethodIFV.setBackgroundTintList(ColorStateList.valueOf(ResMan.getColor(getContext(), R.color.white)));
                this.prayNotifMethodIFV.setColorFilter(ResMan.getColor(getContext(), R.color.red));
                break;
        }
    }

    @NotNull
    @Override
    public String toString() {
        return "MiniNextPrayDashboardCard";
    }

    @Override
    public void onClick(View v) {
        if (!isBottomSheetShown) {
            BottomSheets.newTodayPrays(getContext(), (forPray, newMethod) -> {
                if (forPray == this.nextPray.getType()) this.changeNotifyMethod(newMethod);
            }, isShown -> this.isBottomSheetShown = isShown).show();
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return true;
    }

    @Override
    public void onTimeChanged(Timestamp now) {
        AManager.log(TAG, "onTimeChanged: " + now);
    }
}

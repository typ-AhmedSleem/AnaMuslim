/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.prays;

import static com.typ.muslim.features.prays.enums.Prays.FAJR;
import static com.typ.muslim.features.prays.enums.Prays.ISHA;
import static com.typ.muslim.features.prays.enums.Prays.MAGHRIB;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.textview.MaterialTextView;
import com.mpt.android.stv.Slice;
import com.mpt.android.stv.SpannableTextView;
import com.typ.muslim.R;
import com.typ.muslim.enums.PrayNotifyMethod;
import com.typ.muslim.features.prays.PrayerManager;
import com.typ.muslim.features.prays.enums.Prays;
import com.typ.muslim.features.ramadan.RamadanManager;
import com.typ.muslim.interfaces.PrayTimeCameListener;
import com.typ.muslim.managers.AMSettings;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.managers.LocaleManager;
import com.typ.muslim.models.Pray;
import com.typ.muslim.models.Timestamp;
import com.typ.muslim.ui.BottomSheets;
import com.typ.muslim.ui.home.DashboardCard;
import com.typ.muslim.utils.DisplayUtils;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import cn.iwgang.countdownview.CountdownView;

public class NextPrayDashboardCard1 extends DashboardCard {

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

    public NextPrayDashboardCard1(Context context) {
        super(context);
    }

    public NextPrayDashboardCard1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NextPrayDashboardCard1(Context context, AttributeSet attrs, int defStyleAttr) {
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
        setStrokeColor(Color.TRANSPARENT);
//        setCardBackgroundColor(getColor(R.color.nextPrayCardSurfaceStartColor));
        inflate(getContext(), R.layout.layout_minimized_next_pray_card, this);
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
        if (isInEditMode()) nextPray = new Pray(FAJR, "FAJR", System.currentTimeMillis() + 90000 * 1000);
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
        this.timeRemainingCD.start(this.nextPray.getIn().toMillis() - System.currentTimeMillis());
    }

    private void showNextPrayNameOnTv() {
        final Pray currPray;
        if (isInEditMode()) currPray = new Pray(ISHA, "ISHA", System.currentTimeMillis() + 10 * 1000);
        else currPray = PrayerManager.getCurrentPray(getContext());
        // Add PrayName slice
        stvNextPrayName.addSlice(new Slice.Builder(getString(nextPray.getPrayNameRes()))
                .textColor(getColor(com.google.android.material.R.color.material_dynamic_primary90))
                .textSize(DisplayUtils.sp2px(getContext(), 20f))
                .style(Typeface.BOLD)
                .build());
        // Add Suhur, Iftar, Qiyam slice if in Ramadan
        if (RamadanManager.isInRamadan() && (nextPray.getType() == FAJR || nextPray.getType() == MAGHRIB || nextPray.getType() == ISHA)) {
            final String sliceText;
            if (nextPray.getType() == FAJR) sliceText = getString(R.string.fasting);
            else if (nextPray.getType() == MAGHRIB) sliceText = getString(R.string.iftar);
            else if (nextPray.getType() == ISHA) sliceText = getString(R.string.qiyam);
            else sliceText = "";
            if (!TextUtils.isEmpty(sliceText)) {
                stvNextPrayName.addSlice(new Slice.Builder(String.format(Locale.getDefault(), "\n(%s)", sliceText))
                        .textSize(30)
                        .textColor(getColor(com.google.android.material.R.color.material_dynamic_neutral80))
                        .build());
            }
        }
        // Add Tomorrow slice if before 12 am next day and next pray is FAJR
        if (currPray != null && nextPray.getType() == FAJR && !nextPray.getIn().dateMatches(Timestamp.NOW())) {
            stvNextPrayName.addSlice(new Slice.Builder(String.format(Locale.getDefault(), " (%s)", getString(R.string.tomorrow)))
                    .textSize(30)
                    .textColor(getColor(com.google.android.material.R.color.material_dynamic_neutral80))
                    .build());
        }
        // Add pray time slice
        stvNextPrayName.addSlice(new Slice.Builder(String.format(LocaleManager.getCurrLocale(getContext()), "\n%s", nextPray.getFormattedTime(getContext())))
                .textSize(DisplayUtils.sp2px(getContext(), 16f))
                .textColor(getColor(com.google.android.material.R.color.material_dynamic_secondary90))
                .build());
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
        updateNotifyMethod(AMSettings.getPrayNotifyMethod(getContext(), this.nextPray.getType()));
    }

    public void updateNotifyMethod(PrayNotifyMethod newMethod) {
        if (newMethod == null) return;
        // Update content views
        switch (newMethod) {
            case AZAN -> {
                if (this.nextPray.getType() != Prays.SUNRISE) {
                    this.prayNotifMethodIFV.setImageResource(R.drawable.ic_notify_with_sound);
                }
            }
            case NOTIFICATION_ONLY -> this.prayNotifMethodIFV.setImageResource(R.drawable.ic_notify_without_sound);
            case OFF -> this.prayNotifMethodIFV.setImageResource(R.drawable.ic_notify_off);
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
                if (forPray == this.nextPray.getType()) this.updateNotifyMethod(newMethod);
            }, isShown -> this.isBottomSheetShown = isShown).show();
        }
    }

    @Override
    public void onTimeChanged(Timestamp now) {
        AManager.log(TAG, "onTimeChanged: " + now);
    }
}

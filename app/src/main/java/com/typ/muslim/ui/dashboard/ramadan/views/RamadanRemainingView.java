/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.dashboard.ramadan.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textview.MaterialTextView;
import com.mpt.android.stv.Slice;
import com.mpt.android.stv.SpannableTextView;
import com.typ.muslim.R;
import com.typ.muslim.enums.FormatPatterns;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.models.Period;
import com.typ.muslim.ramadan.RamadanManager;
import com.typ.muslim.ramadan.models.Ramadan;
import com.typ.muslim.ui.ViewContainer;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class RamadanRemainingView extends ViewContainer {

    // Statics
    private static final String TAG = "RamadanRemainingView";
    // Views
    private MaterialTextView tvRamadanStartDate;
    private SpannableTextView stvRemainingTillRamadan;

    public RamadanRemainingView(@NonNull @NotNull Context context) {
        super(context);
    }

    public RamadanRemainingView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RamadanRemainingView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void prepareView(Context context) {
        // inflate view
        inflate(context, R.layout.layout_ramadan_remaining_view, this);
        // Init views
        this.tvRamadanStartDate = $(R.id.tv_ramadan_start_date);
        this.stvRemainingTillRamadan = $(R.id.stv_ramadan_remaining);
        // Perform UI Refresh
        if (!isInEditMode()) refreshUI();
    }

    @Override
    public void refreshUI() {
        // Get runtime vars at first
        Ramadan ramadan = RamadanManager.getNextRamadan();
        Period rem = ramadan.getPeriodTillStart();
        AManager.log(TAG, "[%s] | [%s] | [%s] | [%s]", ramadan.toString(), rem.toString(), RamadanManager.isRamadanThisYearPassed(), RamadanManager.isInRamadan());
        // Display data in views
        tvRamadanStartDate.setText(ramadan.getStartsIn().getFormatted(FormatPatterns.DATE_NORMAL));
        stvRemainingTillRamadan.reset();
        if (rem.getMonths() > 0) {
            stvRemainingTillRamadan.addSlice(new Slice.Builder(String.valueOf(rem.getMonths()))
                    .textColor(getColor(R.color.yellow))
                    .style(Typeface.BOLD)
                    .textSize(60)
                    .build());
            stvRemainingTillRamadan.addSlice(new Slice.Builder(String.format(Locale.getDefault(), " %s,   ", getString(rem.getMonths() == 1 ? R.string.month : R.string.months)))
                    .textColor(getColor(R.color.white))
                    .textSize(30)
                    .build());
        }
        stvRemainingTillRamadan.addSlice(new Slice.Builder(String.valueOf(rem.getDays()))
                .textColor(getColor(R.color.yellow))
                .style(Typeface.BOLD)
                .textSize(60)
                .build());
        stvRemainingTillRamadan.addSlice(new Slice.Builder(String.format(Locale.getDefault(), " %s", getString(rem.getDays() == 1 ? R.string.day : R.string.days)))
                .textColor(getColor(R.color.white))
                .textSize(30)
                .build());
        stvRemainingTillRamadan.display();
    }
}

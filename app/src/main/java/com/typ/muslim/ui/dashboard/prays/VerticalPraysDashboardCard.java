/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.dashboard.prays;

import android.content.Context;
import android.util.AttributeSet;

import com.typ.muslim.core.praytime.enums.Prays;
import com.typ.muslim.core.ummelqura.UmmalquraCalendar;
import com.typ.muslim.enums.PrayNotifyMethod;
import com.typ.muslim.models.Pray;
import com.typ.muslim.ui.VerticalPrayView;
import com.typ.muslim.ui.dashboard.BaseDashboardCard;

import java.util.List;

public class VerticalPraysDashboardCard extends BaseDashboardCard  {

    // Statics
    private static final String TAG = "PraysDashboardCard";
    private final VerticalPrayView[] praysItemsViews = new VerticalPrayView[6];
    // Runtime
    private final UmmalquraCalendar hijriCalendar = new UmmalquraCalendar();
    private List<Pray> todayPrays;

    public VerticalPraysDashboardCard(Context context) {
        super(context);
        this.prepareDashboard(context);
    }

    public VerticalPraysDashboardCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.prepareDashboard(context);
    }

    public VerticalPraysDashboardCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.prepareDashboard(context);
    }

    private void prepareDashboard(Context context) {
//        // Prepare runtime
//        if (!isInEditMode()) {
//            Location location = AMSettings.getCurrentLocation(getContext());
//            this.todayPrays = PrayTimeCore.getSingletonInstance(context, location).getPrayTimes();
//            super.setTitleText(String.format(Locale.getDefault(), "%s %s", AMRes.getString(getContext(), R.string.today_prays_in), location.getCityName()));
//        }
//        // Init base dashboard card
//        super.showArrow(true)
//                .setTitleClickable(true)
//                .setCardContent(R.layout.layout_vertical_prays_card)
//                .setIconTint(R.color.white)
//                .setTitleTextColor(R.color.green)
//                .setIconBgColor(R.color.green)
//                .setArrowTint(R.color.green)
//                .setIcon(R.drawable.ic_time_clock_outlined);
//        // Setup inner views
//        MaterialTextView hijriDateTV = findViewById(R.id.hijriDateTV);
//        this.praysItemsViews[0] = findViewById(R.id.fajrPIV);
//        this.praysItemsViews[1] = findViewById(R.id.sunrisePIV);
//        this.praysItemsViews[2] = findViewById(R.id.dhuhrPIV);
//        this.praysItemsViews[3] = findViewById(R.id.asrPIV);
//        this.praysItemsViews[4] = findViewById(R.id.maghribPIV);
//        this.praysItemsViews[5] = findViewById(R.id.ishaPIV);
//        // Init views
//        hijriDateTV.setText(String.format(Locale.getDefault(), "%d %s %d %s", hijriCalendar.get(UmmalquraCalendar.DATE), hijriCalendar.getDisplayName(UmmalquraCalendar.MONTH, LONG, Locale.getDefault()), hijriCalendar.get(YEAR), AMRes.getString(getContext(), R.string.H))); // Hijri Date.
//        if (!isInEditMode()) for (int index = 0; index < praysItemsViews.length; index++) praysItemsViews[index].setPray(todayPrays.get(index)).changeIndicator(todayPrays); // Prays.
    }

//    @Override
//    public Pray onPrayTimeCame(Pray pray) {
//        if (pray == null) return null;
//        // Change indicator of pray to passed
//        this.praysItemsViews[pray.getType().ordinal()].changeIndicator(todayPrays);
//        // Change indicator of next pray to current if pray isn't Isha
//        if (pray.getType() != Prays.ISHA) this.praysItemsViews[pray.getType().ordinal() + 1].changeIndicator(todayPrays);
//        return null;
//    }
//
//    public VerticalPraysDashboardCard setPrays(List<Pray> todayPrays) {
//        // Do null check
//        if (todayPrays == null) return this;
//        // Update runtime
//        this.todayPrays = todayPrays;
//        // Update views
//        for (int index = 0; index < praysItemsViews.length; index++) praysItemsViews[index].setPray(todayPrays.get(index)).changeIndicator(todayPrays); // Set prays.
//        return this;
//    }
//
//    public void setPrayNotifyMethodChangeCallback(PrayNotifyMethodChangedCallback callback) {
//        for (PrayItemView piv : praysItemsViews) piv.setCallback(callback);
//    }
//
    public interface PrayNotifyMethodChangedCallback {

        void onPrayNotifyMethodChanged(Prays forPray, PrayNotifyMethod newMethod);

    }
}

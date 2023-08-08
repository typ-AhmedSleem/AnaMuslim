/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.tracker.dashboard;

import static java.util.Calendar.MINUTE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.widget.ViewSwitcher;

import androidx.core.app.ActivityOptionsCompat;

import com.typ.muslim.R;
import com.typ.muslim.features.prays.PrayerManager;
import com.typ.muslim.features.prays.enums.PrayStatus;
import com.typ.muslim.features.prays.interfaces.PrayTimeCameListener;
import com.typ.muslim.features.prays.models.Pray;
import com.typ.muslim.managers.PrayTrackerManager;
import com.typ.muslim.models.Timestamp;
import com.typ.muslim.ui.BottomSheets;
import com.typ.muslim.ui.home.DashboardCard;
import com.typ.muslim.ui.tracker.TrackerActivity;
import com.typ.muslim.ui.tracker.views.PrayTrackerAskView;
import com.typ.muslim.ui.tracker.views.PrayTrackerStatisticsView2;

import org.jetbrains.annotations.NotNull;

import kotlin.Pair;

public class TrackerDashboardCard extends DashboardCard implements PrayTimeCameListener {

    // Runtime
    private Pray currentPray;
    private boolean didHePray;
    // Views
    private ViewSwitcher viewSwitcher;

    public TrackerDashboardCard(Context context) {
        super(context);
    }

    public TrackerDashboardCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TrackerDashboardCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void prepareRuntime(Context context) {
        if (isInEditMode()) return;
        // Get current pray & tracker record for it
        this.currentPray = PrayerManager.getCurrentPray(getContext());
        this.didHePray = PrayerManager.didHePray(getContext(), this.currentPray, false);
    }

    @Override
    public void prepareCardView(Context context) {
        // Inflate content view
        this.viewSwitcher = inflate(context, R.layout.layout_pray_tracker_card_base, this).findViewById(R.id.vs);
        setRippleColorResource(R.color.ripple_white);
        // Switch view depending on whether he prayed or not
        refreshUI();
    }

    public void showStatistics() {
        if (viewSwitcher.getCurrentView() instanceof PrayTrackerAskView) this.viewSwitcher.showNext();
        ((PrayTrackerStatisticsView2) this.viewSwitcher.getCurrentView()).refreshUI();
    }

    public void askIfPrayed(Pray whichPray) {
        if (whichPray == null) return;
        this.currentPray = whichPray;
        this.didHePray = false;
        if (viewSwitcher.getCurrentView() instanceof PrayTrackerStatisticsView2) {
            this.viewSwitcher.showPrevious();
            ((PrayTrackerAskView) this.viewSwitcher.getCurrentView()).askIfPrayed(whichPray);
        }
    }

    @Override
    public void refreshRuntime() {
        this.prepareRuntime(getContext());
    }

    @Override
    public void refreshUI() {
        // Refresh runtime
        refreshRuntime();
        // Switch view depending on whether he prayed or not
        if (didHePray) showStatistics();
        else askIfPrayed(this.currentPray);
    }
    
    public void onClick(Activity activity) {
        if (viewSwitcher.getCurrentView() instanceof PrayTrackerAskView) {
            // Show AnswerPrayed BottomSheet
            BottomSheets.newAnswerPrayed(getContext(),
                    currentPray,
                    this::submitRecord,
                    isShown -> isBottomSheetShown = false).show();
        } else {
            // Open PrayTrackerActivity
            startActivity(
                    new Intent(activity, TrackerActivity.class),
                    ActivityOptionsCompat.makeSceneTransitionAnimation(activity, this, "transition_card_to_activity").toBundle());
        }
    }


    private void submitRecord(Pair<PrayStatus, Boolean> result) {
        if (result == null) return;
        long prayedIn = 0L; // Defaults to missed to pray.
        if (result.getFirst() == PrayStatus.ON_TIME) {
            // Add 15 minutes to PrayTime as he prayed at mosque or use now if not in mosque.
            prayedIn = result.getSecond() ? Timestamp.NOW().roll(MINUTE, 15).toMillis() : Timestamp.NOW().toMillis();
        } else if (result.getFirst() == PrayStatus.DELAYED) prayedIn = Timestamp.NOW().toMillis();
        // Build record then persist it
        if (PrayTrackerManager.record(getContext(),
                currentPray.type,
                result.getFirst(),
                result.getSecond(),
                currentPray.time.toMillis(),
                prayedIn,
                Timestamp.NOW().toMillis())) {
            // Show statistics
            showStatistics();
        }
    }

    @Override
    public Pray onPrayTimeCame(Pray pray) {
        askIfPrayed(pray);
        return null;
    }

    @Override
    public void onTimeChanged(Timestamp now) {
        prepareRuntime(getContext());
        refreshUI();
    }

    @NotNull
    @Override
    public String toString() {
        return "TrackerDashboardCard";
    }

}

/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2022.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.fasting;

import android.content.Context;
import android.util.AttributeSet;

import com.typ.muslim.ui.home.DashboardCard;

public class FastingDashboardCard extends DashboardCard {

    // todo: Show {NoFastingView} if user has no fasting plans or isn't in ramadan.
    // todo: Show {FastingDetailsView} if user has an active fasting plan or is in ramadan.
    // todo: Create class {FastingManager} which manages a FastingPlan like KhatmaManager.
    // todo: Create interface called {FastingManagerCallback} like KhatmaManagerCallback.

    public FastingDashboardCard(Context context) {
        super(context);
    }

    public FastingDashboardCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FastingDashboardCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void prepareCardView(Context context) {

    }
}

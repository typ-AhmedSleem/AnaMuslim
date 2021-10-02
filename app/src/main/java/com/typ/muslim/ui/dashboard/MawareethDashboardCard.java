/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.dashboard;

import android.content.Context;
import android.util.AttributeSet;

import com.typ.muslim.R;

public class MawareethDashboardCard extends DashboardCard {

	public MawareethDashboardCard(Context context) {
		super(context);
	}

	public MawareethDashboardCard(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MawareethDashboardCard(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public void prepareCardView(Context context) {
		inflate(R.layout.layout_mawareeth_card);
	}

}

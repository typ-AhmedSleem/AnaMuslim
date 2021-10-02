/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.dashboard.fasting;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.material.textview.MaterialTextView;
import com.typ.muslim.R;
import com.typ.muslim.enums.QiblaCompassMode;
import com.typ.muslim.enums.SensorAccuracy;
import com.typ.muslim.interfaces.PrayTimeCameListener;
import com.typ.muslim.interfaces.QiblaCompassCallback;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.models.Pray;
import com.typ.muslim.ui.dashboard.DashboardCard;
import com.typ.muslim.ui.dashboard.fasting.views.FastingDetailsView;
import com.typ.muslim.ui.dashboard.fasting.views.NoFastingView;
import com.typ.muslim.ui.qibla.QiblaCompassView2;
import com.typ.muslim.utils.MathUtils;

public class FastingDashboardCard extends DashboardCard implements PrayTimeCameListener {

	// todo: Show {NoFastingView} if user has no fasting plans or isn't in ramadan.
	// todo: Show {FastingDetailsView} if user has an active fasting plan or is in ramadan.
	// todo: Create class {FastingManager} which manages a FastingPlan like KhatmaManager.
	// todo: Create interface called {FastingManagerCallback} like KhatmaManagerCallback.

	// Statics
	private static final String TAG = "FastingDashboardCard";
	// Runtime
	// todo: private FastingPlan plan;
	// Views
	private ViewSwitcher vs;

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
		inflate(R.layout.layout_fasting_card);
		if (isInEditMode()) return;
		MaterialTextView tvAngle = $(R.id.tv_qibla_bearing_angle);
		((QiblaCompassView2) $(R.id.qibla_compass)).registerListeners(new QiblaCompassCallback() {
			@Override
			public void onCompassReady() {
				AManager.log(TAG, "onCompassReady");
			}

			@Override
			public void onCompassNotSupported() {
				AManager.log(TAG, "onCompassNotSupported");
			}

			@Override
			public void onSensorChanged(float[] sensorValues) {
				tvAngle.setText(String.valueOf(Math.round(MathUtils.fixAngle(sensorValues[1]))));
			}

			@Override
			public void onAccuracyChanged(int sensor, SensorAccuracy oldAccuracy, SensorAccuracy newAccuracy) {
				AManager.log(TAG, "onAccuracyChanged: SENSOR[%d] | ACC[%S -> %S]", sensor, oldAccuracy, newAccuracy);
			}

			@Override
			public void onCompassViewChanged(QiblaCompassMode newMode) {
				Toast.makeText(context, "onCompassViewChanged: NEW_MODE=" + newMode, Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void showNoFasting() {
		if (vs.getCurrentView() instanceof FastingDetailsView) vs.showPrevious();
	}

	private void showFastingDetails() {
		if (vs.getCurrentView() instanceof NoFastingView) {
			vs.showNext();
//			((FastingDetailsView) vs.getCurrentView()).showDetails(plan);
		}
	}

	@Override
	public Pray onPrayTimeCame(Pray pray) {
		// Check if pray is maghrib to show fasting for tomorrow (Suhur)
		return null; // Return nothing as this is a one way callback only.
	}
}

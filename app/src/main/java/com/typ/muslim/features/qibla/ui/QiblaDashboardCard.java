/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2022.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.features.qibla.ui;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.material.textview.MaterialTextView;
import com.typ.muslim.R;
import com.typ.muslim.enums.QiblaCompassMode;
import com.typ.muslim.enums.SensorAccuracy;
import com.typ.muslim.interfaces.QiblaCompassCallback;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.models.Location;
import com.typ.muslim.ui.dashboard.DashboardCard;
import com.typ.muslim.ui.qibla.QiblaCompassView;

import java.util.Locale;

public class QiblaDashboardCard extends DashboardCard implements QiblaCompassCallback {

    // Statics
    private static final String TAG = "FastingDashboardCard";
    // Runtime
    private Location location;
    private float qiblaBearing = 0f;
    private boolean isAutoSwitchEnabled = false;
    // Views
    private MaterialTextView tvLocation,
            tvQiblaAngle,
            tvCurrentAngle;
    private QiblaCompassView qiblaCompass;

    public QiblaDashboardCard(Context context) {
        super(context);
    }

    public QiblaDashboardCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public QiblaDashboardCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void prepareCardView(Context context) {
        inflate(R.layout.layout_qibla_card);
        if (isInEditMode()) return;
        // Init views
        qiblaCompass = $(R.id.qibla_compass);
        tvLocation = $(R.id.tv_location_city_name);
        tvQiblaAngle = $(R.id.tv_location_qibla_angle);
        tvCurrentAngle = $(R.id.tv_current_bearing_angle);
        // Bind data to UI
        if (isInEditMode() || location == null) return;
        qiblaCompass.disableVibration();
        tvLocation.setText(location.getCityName());
        tvQiblaAngle.setText(String.format(Locale.getDefault(), "%.2f%s", qiblaCompass.getQiblaAngle(), getString(R.string.degree_symbol)));
        // Register Qibla listener
        register();
    }

    public void register() {
        qiblaCompass.registerListeners(this);
    }

    public void unregister() {
        qiblaCompass.unregisterListeners();
    }

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
        tvCurrentAngle.setText(String.format(Locale.getDefault(), "%d%s", (int) qiblaCompass.getCurrentBearing(), getString(R.string.degree_symbol)));
    }

    @Override
    public void onAccuracyChanged(int sensor, SensorAccuracy oldAccuracy, SensorAccuracy newAccuracy) {
        AManager.log(TAG, "onAccuracyChanged: SENSOR[%d] | ACC[%S -> %S]", sensor, oldAccuracy, newAccuracy);
    }

    @Override
    public void onCompassViewChanged(QiblaCompassMode newMode) {
//        Toast.makeText(getContext(), "onCompassViewChanged: NEW_MODE=" + newMode, Toast.LENGTH_SHORT).show();
    }

    public QiblaCompassView getQiblaCompass() {
        return qiblaCompass;
    }
}

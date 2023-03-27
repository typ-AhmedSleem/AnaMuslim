/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2022.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.qibla;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionListenerAdapter;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.android.material.transition.platform.MaterialArcMotion;
import com.google.android.material.transition.platform.MaterialContainerTransform;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;
import com.mpt.android.stv.SpannableTextView;
import com.typ.muslim.R;
import com.typ.muslim.enums.QiblaCompassMode;
import com.typ.muslim.enums.SensorAccuracy;
import com.typ.muslim.features.qibla.QiblaCompassManager;
import com.typ.muslim.interfaces.QiblaCompassCallback;
import com.typ.muslim.managers.AMSettings;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.managers.LocaleManager;
import com.typ.muslim.managers.ResMan;
import com.typ.muslim.models.Location;
import com.typ.muslim.ui.qibla.views.QiblaCompassView;
import com.typ.muslim.utils.Conditions;

import java.util.Locale;

public class QiblaActivity extends AppCompatActivity implements QiblaCompassCallback {

    // Statics
    private static final String TAG = "QiblaActivity";
    // Runtime
    private Location location;
    private boolean isAutoSwitchEnabled = false;
    // Views
    private MaterialTextView tvLocationCity,
            tvLocationBearing,
            tvAccuracy,
            tvStatus,
            tvCurrentBearing;
    private QiblaCompassView qiblaCompass;
    private SpannableTextView stvInstructions;
    private MaterialButton btnSwitchQibla;

    private boolean isCompassPaused = false;

    @Override
    public void onCompassReady() {
    }

    @Override
    public void onCompassNotSupported() {
        // todo: Switch view to CompassNotSupportedView
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        findViewById(android.R.id.content).setTransitionName("transition_card_to_activity");
        // Setup transitions
        final MaterialContainerTransform transition = new MaterialContainerTransform();
        transition.setPathMotion(new MaterialArcMotion());
        transition.addTarget(android.R.id.content);
        transition.setScrimColor(Color.TRANSPARENT);
        transition.setDrawingViewId(android.R.id.content);
        // Setup the activity
        getWindow().setSharedElementEnterTransition(transition);
        getWindow().setSharedElementReenterTransition(transition);
        setEnterSharedElementCallback(new MaterialContainerTransformSharedElementCallback());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qibla);
        // Hide default actionBar
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        // Init runtime
        location = AMSettings.getCurrentLocation(this);
        isAutoSwitchEnabled = QiblaCompassManager.isAutoSwitchEnabled(this);
        // Init views
        tvLocationCity = findViewById(R.id.tv_location_city_name);
        tvLocationBearing = findViewById(R.id.tv_qibla_bearing_angle);
        tvAccuracy = findViewById(R.id.tv_accuracy);
        tvStatus = findViewById(R.id.tv_phone_moveing_status);
        tvCurrentBearing = findViewById(R.id.tv_current_bearing_angle);
        qiblaCompass = findViewById(R.id.qibla_compass);
        stvInstructions = findViewById(R.id.stv_instruction);
        btnSwitchQibla = findViewById(R.id.btn_switch_qibla);
        // Init callbacks and listeners
        getWindow().getSharedElementEnterTransition().addListener(new TransitionListenerAdapter() {

            @Override
            public void onTransitionStart(Transition transition) {
                isAutoSwitchEnabled = true;
                qiblaCompass.setAlpha(0f);
                qiblaCompass.setScaleX(0f);
                qiblaCompass.setScaleY(0f);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                isAutoSwitchEnabled = false;
                qiblaCompass.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(300).start();
                qiblaCompass.enableVibration();
                // Bind data to views
                tvLocationCity.setText(location.getCityName());
                tvLocationBearing.setText(String.format(LocaleManager.getCurrLocale(QiblaActivity.this), "%.2f%s", qiblaCompass.getQiblaAngle(), getString(R.string.degree_symbol)));
                tvAccuracy.setText(qiblaCompass.getCurrentAccuracy().getNameRes());
                tvStatus.setText(R.string.unknown);
                tvCurrentBearing.setText("--");
                stvInstructions.setText("--");
                if (isAutoSwitchEnabled) {
                    tvStatus.setText(R.string.auto_switch_enabled);
                    tvStatus.setTextColor(ResMan.getColor(QiblaActivity.this, R.color.darkAdaptiveColor));
                }
            }
        });
        ((MaterialToolbar) findViewById(R.id.toolbar)).setNavigationOnClickListener(v -> finishAfterTransition());
        btnSwitchQibla.setOnClickListener(v -> {
//            isAutoSwitchEnabled = !isAutoSwitchEnabled;
        });
    }

    /**
     * 1st value -> Old Qibla Angle.
     * 2nd value -> New Qibla Angle.
     * 3rd value -> North Angle.
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(float... sensorValues) {
//        if (isCompassPaused) {
//            tvCurrentBearing.setText("o__0");
//            stvInstructions.setText("(~o__o~)");
//            return;
//        }
        final int angle = (int) sensorValues[1];
        // Show current angle
        tvCurrentBearing.setText(String.format(LocaleManager.getCurrLocale(this), "%d%s", angle, getString(R.string.degree_symbol)));
        // Show instruction (todo: Show more detailed and specific instruction based on angle changes)
        final boolean isCentered = angle == 0 || angle == 360;
        final boolean isToLeft = Conditions.inRange(180, 359, angle);
        final String instruction = getString(isCentered ? R.string.you_are_heading_to : R.string.rotate_phone_to) + " " + getString(isCentered ? R.string.qibla : isToLeft ? R.string.left : R.string.right);
        stvInstructions.setText(instruction);
        stvInstructions.setTextColor(ResMan.getColor(this, isCentered ? R.color.green : R.color.darkAdaptiveColor));
    }

    @Override
    public void onAccuracyChanged(int sensor, SensorAccuracy oldAccuracy, SensorAccuracy newAccuracy) {
        // todo: Show prompts to user when accuracy is [UNRELIABLE,LOW]
        tvAccuracy.setText(newAccuracy.getNameRes());
        tvAccuracy.setTextColor(ResMan.getColor(this, newAccuracy.getTextColorRes()));
        AManager.log(TAG, String.format(Locale.ENGLISH, "onAccuracyChanged: SENSOR[%d] - ACCURACY[%s]\n", sensor, newAccuracy));
    }

    @Override
    protected void onPause() {
        super.onPause();
        qiblaCompass.unregisterListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        qiblaCompass.registerListeners(this);
    }

    @Override
    public void onCompassViewChanged(QiblaCompassMode newMode) {
        if (isAutoSwitchEnabled) {
            // todo: Switch the compass view if user has enabled [AutoSwitchQibla].
            tvStatus.setText(R.string.auto_switch_enabled);
            tvStatus.setTextColor(ResMan.getColor(this, R.color.darkAdaptiveColor));
        } else {
            switch (newMode) {
                case COMPASS_2D:
                    tvStatus.setText(R.string.okay);
                    if (isCompassPaused) isCompassPaused = false;
                    tvStatus.setTextColor(ResMan.getColor(this, R.color.green));
                    break;
                case LIVE_COMPASS:
                    tvStatus.setText(R.string.put_phone_on_back);
                    if (!isCompassPaused) isCompassPaused = true;
                    tvStatus.setTextColor(ResMan.getColor(this, R.color.red));
                    break;
            }
        }
    }

}

/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.fragments.panel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.typ.muslim.R;
import com.typ.muslim.enums.QiblaCompassMode;
import com.typ.muslim.enums.SensorAccuracy;
import com.typ.muslim.interfaces.QiblaCompassCallback;
import com.typ.muslim.libs.iclib.Dms;
import com.typ.muslim.libs.iclib.QiblaUtils;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.ui.qibla.QiblaCompassView2;

import org.jetbrains.annotations.NotNull;

public class QiblaFragment extends Fragment implements QiblaCompassCallback {

	// Statics
	private static final String TAG = "QiblaFragment";
	private final boolean is2DCompass = true;
	// Runtime
	private Dms qiblaBearing;
	// Views
	private MaterialTextView tvCityName, tvQiblaBearing;
	private QiblaCompassView2 qiblaCV;
	private MaterialButton btnMap, btnVisual;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.qiblaBearing = QiblaUtils.getCurrentLocationDms(getContext());
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_qibla, container, false);
	}

	@Override
	public void onViewCreated(@NonNull @NotNull View fragmentView, @Nullable Bundle savedInstanceState) {
		// Setup views
		this.tvCityName = fragmentView.findViewById(R.id.tv_city_name);
		//this.tvQiblaBearing = fragmentView.findViewById(R.id.tv_qibla_bearing_angle);
		this.qiblaCV = fragmentView.findViewById(R.id.qibla_view);
		//this.btnMap = fragmentView.findViewById(R.id.qibla_view);
		//this.btnVisual = fragmentView.findViewById(R.id.qibla_view);
		// Init views
		//this.tvCityName.setText(AMSettings.getCurrentLocation(getContext()).getCityName());
		//this.tvQiblaBearing.setText(this.qiblaBearing.toString());
		// Setup listeners
		this.qiblaCV.registerListeners(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		this.qiblaCV.registerListeners(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		this.qiblaCV.unRegisterListeners();
	}

	@Override
	public void onCompassReady() {
		AManager.log(TAG, "Compass is ready");
	}

	@Override
	public void onCompassNotSupported() {
		AManager.log(TAG, "Compass isn't supported on this device");
	}

	@Override
	public void onSensorChanged(float[] sensorValues) {

	}

	@Override
	public void onAccuracyChanged(int sensor, SensorAccuracy oldAccuracy, SensorAccuracy newAccuracy) {
		AManager.log(TAG, "onAccuracyChanged: SENSOR[%d] %s -> %s", sensor, oldAccuracy, newAccuracy);
	}

	@Override
	public void onCompassViewChanged(QiblaCompassMode newMode) {
		if (this.is2DCompass) this.qiblaCV.setVisibility(View.VISIBLE);
		else this.qiblaCV.setVisibility(View.GONE);
		AManager.log(TAG, "onCompassViewChanged: %s", this.is2DCompass ? "2D Compass." : "3D Compass.");
		Toast.makeText(getContext(), this.is2DCompass ? "Switched to 2D Compass" : "Switched to 3D Compass", Toast.LENGTH_SHORT).show();
	}

}

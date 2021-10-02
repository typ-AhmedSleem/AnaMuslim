/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.interfaces;

import com.typ.muslim.enums.QiblaCompassMode;
import com.typ.muslim.enums.SensorAccuracy;

public interface QiblaCompassCallback {

	void onCompassReady();

	void onCompassNotSupported();

	void onSensorChanged(float... sensorValues);

	void onAccuracyChanged(int sensor, SensorAccuracy oldAccuracy, SensorAccuracy newAccuracy);

	void onCompassViewChanged(QiblaCompassMode newMode);

}

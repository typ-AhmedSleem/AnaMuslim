/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.interfaces;

import android.app.AlarmManager;
import android.content.Intent;
import android.os.Vibrator;
import android.telecom.TelecomManager;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.IdRes;
import androidx.annotation.StringRes;

public interface ViewHelperMethods {

	TelecomManager getTelecomManager();

	Vibrator getVibrator();

	AlarmManager getAlarmManager();

	@ColorInt
	int getColor(@ColorRes int colorResId);

	String getString(@StringRes int stringResId);

	<T extends View> T $(@IdRes int resId);

	void startActivity(Intent intent);

	int sp2px(float sp);

	int dp2px(float dip);

}

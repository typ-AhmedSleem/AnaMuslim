/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.interfaces;

import android.app.AlarmManager;
import android.content.Intent;
import android.os.Bundle;
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

    String getString(boolean condition, @StringRes int whenTrue, @StringRes int whenFalse);

    String getString(int number, @StringRes int whenOne, @StringRes int whenMany);

    String getString(@StringRes int whenArabic, @StringRes int whenOther);

    <T extends View> T $(@IdRes int resId);

    void startActivity(Intent intent);

    void startActivity(Intent intent, Bundle options);

    int sp2px(float sp);

    int dp2px(float dip);

}

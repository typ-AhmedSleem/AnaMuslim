/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.enums;

import androidx.annotation.StringRes;

import com.typ.muslim.R;

public enum SensorAccuracy {
    UNRELIABLE,
    LOW,
    MEDIUM,
    HIGH;

    public static SensorAccuracy valueOf(int accuracy) {
        return accuracy == 1 ? LOW : accuracy == 2 ? MEDIUM : accuracy == 3 ? HIGH : UNRELIABLE;
    }

    public @StringRes
    int getNameRes() {
        switch (this) {
            default:
            case UNRELIABLE:
                return R.string.unreliable;
            case LOW:
                return R.string.low;
            case MEDIUM:
                return R.string.medium;
            case HIGH:
                return R.string.high;
        }
    }

}

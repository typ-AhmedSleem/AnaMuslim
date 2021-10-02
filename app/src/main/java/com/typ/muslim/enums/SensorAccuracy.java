/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.enums;

public enum SensorAccuracy {
    UNRELIABLE,
    LOW,
    MEDIUM,
    HIGH;

    public static SensorAccuracy valueOf(int accuracy) {
        return accuracy == 1 ? LOW : accuracy == 2 ? MEDIUM : accuracy == 3 ? HIGH : UNRELIABLE;
    }
}

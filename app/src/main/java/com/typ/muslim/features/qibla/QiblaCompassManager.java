/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2022.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.features.qibla;

import android.content.Context;

import com.typ.muslim.managers.PrefManager;

public final class QiblaCompassManager {

    // Keys
    private static final String KEY_AUTO_SWITCH = "enableAutoSwitch";

    public static boolean isAutoSwitchEnabled(Context context) {
        return PrefManager.get(context, KEY_AUTO_SWITCH, false);
    }

    public static void setAutoSwitchEnabled(Context context, boolean enabled) {
        PrefManager.set(context, KEY_AUTO_SWITCH, enabled);
    }

}

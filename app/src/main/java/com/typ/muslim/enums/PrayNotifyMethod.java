/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.enums;

/**
 * Enum indicating the way AnaMuslim will use to notify you when PrayTime alarm is fired
 */
public enum PrayNotifyMethod {
    /**
     * Turn off both notifications and sounds
     */
    OFF,
    /**
     * Turn on notifications for each pray notify way but turn off sounds
     */
    NOTIFICATION_ONLY,
    /**
     * Turn on both notifications and sound
     */
    AZAN;

    public static PrayNotifyMethod valueOf(int ordinal) {
        if (ordinal == 0) return OFF;
        else if (ordinal == 1) return NOTIFICATION_ONLY;
        else return AZAN;
    }

}

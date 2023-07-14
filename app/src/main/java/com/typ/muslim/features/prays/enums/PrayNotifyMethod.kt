/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */
package com.typ.muslim.features.prays.enums

/**
 * Enum indicating the way AnaMuslim will use to notify you when PrayTime alarm is fired
 */
enum class PrayNotifyMethod {
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

    companion object {
        fun valueOf(ordinal: Int): PrayNotifyMethod {
            return if (ordinal == 0) OFF else if (ordinal == 1) NOTIFICATION_ONLY else AZAN
        }
    }
}

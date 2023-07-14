/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */
package com.typ.muslim.features.prays.enums

enum class PrayStatus {
    UP_COMING,
    ON_TIME,
    DELAYED,
    FORGOT;

    companion object {
        @JvmStatic
        fun valueOf(ordinal: Int): PrayStatus {
            return when (ordinal) {
                0 -> UP_COMING
                1 -> ON_TIME
                2 -> DELAYED
                else -> FORGOT
            }
        }
    }
}

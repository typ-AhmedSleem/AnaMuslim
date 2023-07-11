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
            return if (ordinal == 0) UP_COMING else if (ordinal == 1) ON_TIME else if (ordinal == 2) DELAYED else FORGOT
        }
    }
}

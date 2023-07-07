/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */
package com.typ.muslim.features.tasbeeh.enums

enum class TasbeehTimes(val howMany: Int) {

    TIMES_33(33),
    TIMES_66(66),
    TIMES_99(99),
    TIMES_INFINITE(-1);

    companion object {
        @JvmStatic
        fun ofTimes(howMany: Int): TasbeehTimes {
            return if (howMany == TIMES_33.howMany) TIMES_33 else if (howMany == TIMES_66.howMany) TIMES_66 else if (howMany == TIMES_99.howMany) TIMES_99 else TIMES_INFINITE
        }

        fun valueOf(ordinal: Int): TasbeehTimes {
            return if (ordinal == TIMES_33.ordinal) TIMES_33 else if (ordinal == TIMES_66.ordinal) TIMES_66 else if (ordinal == TIMES_99.ordinal) TIMES_99 else TIMES_INFINITE
        }
    }
}

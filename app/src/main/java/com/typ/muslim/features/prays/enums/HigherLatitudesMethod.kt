package com.typ.muslim.features.prays.enums

/**
 * HigherLatitude used during calculating PrayTimes in high-latitude locations
 */
enum class HigherLatitudesMethod {
    /**
     * No Adjustments
     */
    NONE,

    /**
     * Middle of Night
     */
    MIDNIGHT,

    /**
     * 1/7th of Night
     */
    ONESEVENTH,

    /**
     * Angle/60th of night
     */
    ANGLEBASED;

    companion object {
        @JvmStatic
        fun valueOf(ordinal: Int): HigherLatitudesMethod {
            return if (ordinal == 1) MIDNIGHT else if (ordinal == 2) ONESEVENTH else if (ordinal == 3) ANGLEBASED else NONE
        }
    }
}

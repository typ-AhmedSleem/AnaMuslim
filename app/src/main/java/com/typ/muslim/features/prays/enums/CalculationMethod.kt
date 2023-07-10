package com.typ.muslim.features.prays.enums

/**
 * Calculation Method used during calculating PrayTimes
 */
enum class CalculationMethod {
    /**
     * Ithna Ashari
     */
    JAFARI,

    /**
     * University of Islamic Sciences, Karachi
     */
    KARACHI,

    /**
     * Islamic Society of North America (ISNA)
     */
    ISNA,

    /**
     * Muslim World League (MWL)
     */
    MWL,

    /**
     * Umm al-Qura, Makkah
     */
    MAKKAH,

    /**
     * Egyptian General Authority of Survey
     */
    EGYPT,

    /**
     * Institute of Geophysics, University of Tehran
     */
    TEHRAN,

    /**
     * Custom Setting
     */
    CUSTOM;

    companion object {
        @JvmStatic
        fun valueOf(ordinal: Int): CalculationMethod {
            return if (ordinal == 1) KARACHI else if (ordinal == 2) ISNA else if (ordinal == 3) MWL else if (ordinal == 4) MAKKAH else if (ordinal == 5) EGYPT else if (ordinal == 6) TEHRAN else if (ordinal == 7) CUSTOM else JAFARI
        }
    }
}

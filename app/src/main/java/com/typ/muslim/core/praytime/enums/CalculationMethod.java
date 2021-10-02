package com.typ.muslim.core.praytime.enums;

/**
 * Calculation Method used during calculating PrayTimes
 */
public enum CalculationMethod {
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

    public static CalculationMethod valueOf(int ordinal) {
        if (ordinal == 1) return KARACHI;
        else if (ordinal == 2) return ISNA;
        else if (ordinal == 3) return MWL;
        else if (ordinal == 4) return MAKKAH;
        else if (ordinal == 5) return EGYPT;
        else if (ordinal == 6) return TEHRAN;
        else if (ordinal == 7) return CUSTOM;
        else return JAFARI;
    }

}

package com.typ.muslim.core.praytime.enums;

/**
 * HigherLatitude used during calculating PrayTimes in high-latitude locations
 */
public enum HigherLatitudesMethod {
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

    public static HigherLatitudesMethod valueOf(int ordinal) {
        if (ordinal == 1) return MIDNIGHT;
        else if (ordinal == 2) return ONESEVENTH;
        else if (ordinal == 3) return ANGLEBASED;
        else return NONE;
    }

}

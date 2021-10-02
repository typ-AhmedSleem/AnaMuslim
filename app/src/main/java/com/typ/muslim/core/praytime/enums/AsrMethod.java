package com.typ.muslim.core.praytime.enums;

/**
 * AsrMethod used during calculating PrayTimes
 */
public enum AsrMethod {
    /**
     * Shafii
     */
    SHAFII,
    /**
     * Hanafi
     */
    HANAFI;

    public static AsrMethod valueOf(int ordinal) {
        if (ordinal == 1) return HANAFI;
        else return SHAFII;
    }

}

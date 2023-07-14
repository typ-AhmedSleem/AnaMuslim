package com.typ.muslim.features.prays.enums

/**
 * AsrMethod used during calculating PrayTimes
 */
enum class AsrMethod {
    /**
     * Shafii
     */
    SHAFII,

    /**
     * Hanafi
     */
    HANAFI;

    companion object {
        @JvmStatic
        fun valueOf(ordinal: Int): AsrMethod {
            return if (ordinal == 1) HANAFI else SHAFII
        }
    }
}

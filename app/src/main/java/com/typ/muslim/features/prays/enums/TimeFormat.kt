package com.typ.muslim.features.prays.enums

/**
 * The format of the PrayTimeCore returned from library
 */
enum class TimeFormat {
    /**
     * Time12 with Suffix Format
     */
    TIME12SX,

    /**
     * Time12 with NoSuffix Format
     */
    TIME12NSX,

    /**
     * Time24 Format
     */
    TIME24,
    /**
     * Time In Double Format
     */
    FLOATING,
    /**
     * Time In Long as Timestamp Format
     */
    TIMESTAMP
}

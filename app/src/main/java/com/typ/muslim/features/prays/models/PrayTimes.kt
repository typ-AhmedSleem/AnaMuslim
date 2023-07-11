/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */
package com.typ.muslim.features.prays.models

import com.typ.muslim.features.prays.enums.PrayType
import java.io.Serializable

class PrayTimes(// Must be sure that this
    private vararg val prays: Pray
) : Serializable {

    val fajr: Pray
        get() = prays[0]
    val sunrise: Pray
        get() = prays[1]
    val dhuhr: Pray
        get() = prays[2]
    val asr: Pray
        get() = prays[3]
    val maghrib: Pray
        get() = prays[4]
    val isha: Pray
        get() = prays[5]

    operator fun get(index: Int) = prays[index]

    fun toArray() = arrayOf(fajr, sunrise, dhuhr, asr, maghrib, isha)

    fun toArrayNoSunrise() = arrayOf(fajr, dhuhr, asr, maghrib, isha)

    override fun toString(): String {
        return """
            PrayTimes{fajr=${fajr}
            sunrise=${sunrise}
            dhuhr=${dhuhr}
            asr=${asr}
            maghrib=${maghrib}
            isha=${isha}}
            """.trimIndent()
    }

    class Builder internal constructor() {

        private lateinit var fajr: Pray
        private lateinit var sunrise: Pray
        private lateinit var dhuhr: Pray
        private lateinit var asr: Pray
        private lateinit var maghrib: Pray
        private lateinit var isha: Pray

        fun add(pray: Pray) {
            when {
                PrayType.FAJR === pray.type -> fajr = pray
                PrayType.SUNRISE === pray.type -> sunrise = pray
                PrayType.DHUHR === pray.type -> dhuhr = pray
                PrayType.ASR === pray.type -> asr = pray
                PrayType.MAGHRIB === pray.type -> maghrib = pray
                PrayType.ISHA === pray.type -> isha = pray
            }
        }

        fun build() = PrayTimes(fajr, sunrise, dhuhr, asr, maghrib, isha)

    }

    companion object {
        @JvmStatic
        fun newBuilder(): Builder {
            return Builder()
        }
    }
}

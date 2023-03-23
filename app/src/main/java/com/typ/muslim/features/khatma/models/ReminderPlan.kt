package com.typ.muslim.features.khatma.models

import com.typ.muslim.models.ReminderFrequency
import com.typ.muslim.models.Time
import org.json.JSONObject
import java.util.*

data class ReminderPlan(
    val time: Time = Time(0, 0, 0),
    val daysFreq: ReminderFrequency = ReminderFrequency()
) : java.io.Serializable {

    constructor(hrs: Int, mins: Int, daysFreq: ReminderFrequency) : this(Time(hrs, mins, 0), daysFreq)

    /**
     * Converts the current ReminderPlan model to a savable json string
     * @sample toJson
     *
     * @return JSON-like string with this fixed format:
     *
     * `{
     *  "time": "HH:mm",
     *  "freq": ["true", "true", "true", "true", "true", "true", "true"]
     * }`
     *
     */
    fun toJson(): String {
        val json = JSONObject()
        json.put("time", "%d:%d".format(Locale.ENGLISH, time.hours, time.minutes))
        json.put("freq", daysFreq.toStringList())
        return json.toString()
    }

    override fun toString(): String = toJson()

}

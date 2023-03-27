package com.typ.muslim.models

class ReminderFrequency(
    private val sat: Boolean = false,
    private val sun: Boolean = false,
    private val mon: Boolean = false,
    private val tue: Boolean = false,
    private val wed: Boolean = false,
    private val thu: Boolean = false,
    private val fri: Boolean = false
) : java.io.Serializable {

    fun saturdayEnabled() = this.sat
    fun sundayEnabled() = this.sun
    fun mondayEnabled() = this.mon
    fun tuesdayEnabled() = this.tue
    fun wednesdayEnabled() = this.wed
    fun thursdayEnabled() = this.thu
    fun fridayEnabled() = this.fri

    fun toStringList(): Array<String> {
        return arrayOf(
            sat.toString(),
            sun.toString(),
            mon.toString(),
            tue.toString(),
            wed.toString(),
            thu.toString(),
            fri.toString(),
        )
    }

    companion object {

        @JvmStatic
        fun fromStringList(list: Array<String>): ReminderFrequency {
            // List is either empty or missing element(s)
            if (list.isEmpty() or (list.size < 7)) return ReminderFrequency()
            // Build ReminderFreq with given list
            return ReminderFrequency(
                list[0] == "true",
                list[1] == "true",
                list[2] == "true",
                list[3] == "true",
                list[4] == "true",
                list[5] == "true",
                list[6] == "true",
            )
        }

    }

}

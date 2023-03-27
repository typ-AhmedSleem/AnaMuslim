package com.typ.muslim.features.khatma.utils

import androidx.room.TypeConverter
import com.typ.muslim.features.khatma.models.KhatmaPlan
import com.typ.muslim.features.khatma.models.ReminderPlan

class KhatmaConverters {
    @TypeConverter
    fun fromPlan(duration: Int): KhatmaPlan = KhatmaPlan(duration)

    @TypeConverter
    fun planToDuration(plan: KhatmaPlan): Int = plan.duration

    @TypeConverter
    fun toReminder(reminderPlan: String?): ReminderPlan? = ReminderPlan.fromJson(reminderPlan)

    @TypeConverter
    fun fromReminderToPlan(reminderPlan: ReminderPlan): String = reminderPlan.toJson()

}

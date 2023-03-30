package com.typ.muslim.features.khatma.utils

import androidx.room.TypeConverter
import com.typ.muslim.features.khatma.models.KhatmaPlan
import com.typ.muslim.features.khatma.models.KhatmaWerd
import com.typ.muslim.features.khatma.models.ReminderPlan
import com.typ.muslim.features.quran.models.QuranAyah

class KhatmaConverters {
    @TypeConverter
    fun importPlan(duration: Int): KhatmaPlan = KhatmaPlan(duration)

    @TypeConverter
    fun exportPlan(plan: KhatmaPlan): Int = plan.duration

    @TypeConverter
    fun exportReminder(reminderPlan: ReminderPlan): String = reminderPlan.toJson()

    @TypeConverter
    fun importReminder(reminderPlan: String?): ReminderPlan? = ReminderPlan.fromJson(reminderPlan)

    @TypeConverter
    fun exportWerd(werd: KhatmaWerd) = werd.export()

    @TypeConverter
    fun importWerd(werd: String) {
        return werd.split(':').let { werd ->
            val start = werd[0].split(',').let { ayah -> QuranAyah(ayah[0].toInt(), ayah[1].toInt()) }
            val end = werd[1].split(',').let { ayah -> QuranAyah(ayah[0].toInt(), ayah[1].toInt()) }
            KhatmaWerd(start, end)
        }
    }

}

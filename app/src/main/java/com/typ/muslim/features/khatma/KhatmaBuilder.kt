package com.typ.muslim.features.khatma

import com.typ.muslim.features.khatma.models.Khatma
import com.typ.muslim.features.khatma.models.KhatmaPlan
import com.typ.muslim.features.khatma.models.ReminderPlan
import com.typ.muslim.features.quran.Quran
import com.typ.muslim.models.Timestamp
import java.util.*

/**
 * Builder class used in process of creating a new khatma
 * */
class KhatmaBuilder(
    private var name: String? = null,
    private var plan: KhatmaPlan,
    private var reminder: ReminderPlan?
) {

    fun setName(name: String?): KhatmaBuilder {
        this.name = name
        return this
    }

    fun setPlan(plan: KhatmaPlan): KhatmaBuilder {
        this.plan = plan
        return this
    }

    fun setReminder(reminder: ReminderPlan?): KhatmaBuilder {
        this.reminder = reminder
        return this
    }

    fun build() {
        Khatma(
            id = UUID.randomUUID().toString().split("-")[0],
            name = this.name,
            plan = this.plan,
            createdIn = Timestamp.NOW().toMillis(),
            reminder = this.reminder,
            werdLength = Quran.QURAN_JUZ2S_COUNT / plan.duration
        )
    }

    fun reset() {
        name = null
        plan = KhatmaPlan.plan30Days
        reminder = null
    }

}
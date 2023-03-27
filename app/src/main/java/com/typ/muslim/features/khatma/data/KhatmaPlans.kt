package com.typ.muslim.features.khatma.data

import com.typ.muslim.features.khatma.models.KhatmaPlan

object KhatmaPlans {

    val plan10Days
        get() = KhatmaPlan(10) // 3 Parts a day.
    val plan15Days
        get() = KhatmaPlan(15) // 2 Parts a day.
    val plan30Days
        get() = KhatmaPlan(30) // 1 Part a day.
    val plan60Days
        get() = KhatmaPlan(60) // 0.5 Part a day.

}
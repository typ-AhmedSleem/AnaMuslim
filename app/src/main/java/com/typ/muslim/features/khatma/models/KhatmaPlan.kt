package com.typ.muslim.features.khatma.models

data class KhatmaPlan(val duration: Int = 30) {

    companion object {
        val plan10Days = KhatmaPlan(10) // 3 Parts a day.
        val plan15Days = KhatmaPlan(15) // 2 Parts a day.
        val plan30Days = KhatmaPlan(30) // 1 Part a day.
        val plan60Days = KhatmaPlan(60) // 0.5 Part a day.
        fun planCustom(duration: Int) = KhatmaPlan(duration) // Custom plan
    }

}

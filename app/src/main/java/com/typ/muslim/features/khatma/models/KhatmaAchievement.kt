package com.typ.muslim.features.khatma.models

import androidx.annotation.DrawableRes
import com.typ.muslim.models.Timestamp

data class KhatmaAchievement(
    val title: String,
    val unlockedIn: Timestamp,
    @field:DrawableRes val badge: Int
)


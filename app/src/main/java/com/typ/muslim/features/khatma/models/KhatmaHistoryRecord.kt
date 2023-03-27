package com.typ.muslim.features.khatma.models

import com.typ.muslim.models.Timestamp

data class KhatmaHistoryRecord(
    val id: Int = -1,
    val werd: KhatmaWerd,
    val recordedIn: Timestamp
)

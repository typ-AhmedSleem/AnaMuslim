package com.typ.muslim.features.prays

import com.typ.muslim.enums.PrayNotifyMethod
import com.typ.muslim.features.prays.enums.PrayType

interface PrayNotifyMethodChangedCallback {
    fun onPrayNotifyMethodChanged(forPray: PrayType, newMethod: PrayNotifyMethod)
}

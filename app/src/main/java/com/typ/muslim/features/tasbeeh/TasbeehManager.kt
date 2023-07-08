/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */
package com.typ.muslim.features.tasbeeh

import android.content.Context
import com.typ.muslim.R
import com.typ.muslim.app.Keys
import com.typ.muslim.features.tasbeeh.enums.TasbeehMode
import com.typ.muslim.features.tasbeeh.enums.TasbeehTimes
import com.typ.muslim.features.tasbeeh.models.TasbeehConfig
import com.typ.muslim.features.tasbeeh.models.TasbeehItem
import com.typ.muslim.managers.PrefManager
import com.typ.muslim.managers.ResMan

object TasbeehManager {

    fun getDefaultTasbeehat(ctx: Context): Array<TasbeehItem> {
        return arrayOf(
            TasbeehItem(1, ResMan.getString(ctx, R.string.daily_tasbeeh_1)),
            TasbeehItem(2, ResMan.getString(ctx, R.string.daily_tasbeeh_2)),
            TasbeehItem(3, ResMan.getString(ctx, R.string.daily_tasbeeh_3)),
            TasbeehItem(4, ResMan.getString(ctx, R.string.daily_tasbeeh_4)),
        )
    }

    fun saveConfig(ctx: Context, config: TasbeehConfig) {
        PrefManager.set(ctx, Keys.TasbeehTimes, config.times)
        PrefManager.set(ctx, Keys.TasbeehMode, config.mode.name)
        PrefManager.set(ctx, Keys.TasbeehIsVibrationEnabled, config.isVibrationEnabled)
        PrefManager.set(ctx, Keys.TasbeehIsVolumeCounterEnabled, config.isVolumeCounterEnabled)
        PrefManager.set(ctx, Keys.TasbeehIsSpeakOnFlipEnabled, config.isSpeakTasbeehOnFlipEnabled)
    }

    fun loadConfig(ctx: Context): TasbeehConfig {
        return TasbeehConfig(
            times = PrefManager.get(ctx, Keys.TasbeehTimes, TasbeehTimes.TIMES_33.howMany),
            isVibrationEnabled = PrefManager.get(ctx, Keys.TasbeehIsVibrationEnabled, true),
            isVolumeCounterEnabled = PrefManager.get(ctx, Keys.TasbeehIsVolumeCounterEnabled, true),
            mode = TasbeehMode.valueOf(PrefManager.get(ctx, Keys.TasbeehMode, TasbeehMode.TASBEEH_MODE.name)),
            isSpeakTasbeehOnFlipEnabled = PrefManager.get(ctx, Keys.TasbeehIsSpeakOnFlipEnabled, true),
            tasbeehat = arrayOf(
                TODO()
            )
        )
    }

}

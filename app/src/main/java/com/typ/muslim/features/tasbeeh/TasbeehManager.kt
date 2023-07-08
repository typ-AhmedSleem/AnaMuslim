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
        PrefManager.set(ctx, Keys.TasbeehItems, exportTasbeehatCfg(config.tasbeehat))
        PrefManager.set(ctx, Keys.TasbeehIsVibrationEnabled, config.isVibrationEnabled)
        PrefManager.set(ctx, Keys.TasbeehIsVolumeCounterEnabled, config.isVolumeCounterEnabled)
        PrefManager.set(ctx, Keys.TasbeehIsSpeakOnFlipEnabled, config.isSpeakTasbeehOnFlipEnabled)
    }

    private fun exportTasbeehatCfg(tasbeehat: Array<TasbeehItem>): String {
        if (tasbeehat.isEmpty()) return "1"
        return buildString {
            for (item in tasbeehat) append("${item.id},")
        }
    }

    private fun importTasbeehatCfg(ctx: Context, cfg: String?): Array<TasbeehItem> {
        val def = getDefaultTasbeehat(ctx)
        if (cfg.isNullOrEmpty()) return def

        val cfgSplits = cfg.split(",")
        val list = mutableListOf<TasbeehItem>()
        for (id in cfgSplits) {
            try {
                list.add(def[id.toInt() - 1])
            } catch (_: NumberFormatException) {
            } catch (_: IndexOutOfBoundsException) {
            }
        }
        return list.toTypedArray()
    }

    fun loadConfig(ctx: Context): TasbeehConfig {
        return TasbeehConfig(
            times = PrefManager.get(ctx, Keys.TasbeehTimes, TasbeehTimes.TIMES_33.howMany),
            isVibrationEnabled = PrefManager.get(ctx, Keys.TasbeehIsVibrationEnabled, true),
            isVolumeCounterEnabled = PrefManager.get(ctx, Keys.TasbeehIsVolumeCounterEnabled, true),
            mode = TasbeehMode.valueOf(PrefManager.get(ctx, Keys.TasbeehMode, TasbeehMode.TASBEEH_MODE.name)),
            isSpeakTasbeehOnFlipEnabled = PrefManager.get(ctx, Keys.TasbeehIsSpeakOnFlipEnabled, true),
            tasbeehat = importTasbeehatCfg(ctx, PrefManager.get(ctx, Keys.TasbeehItems, ""))
        )
    }

}

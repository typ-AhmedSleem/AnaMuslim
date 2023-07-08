package com.typ.muslim.features.tasbeeh.models

import com.typ.muslim.features.tasbeeh.enums.TasbeehMode

class TasbeehConfig(
    times: Int,
    mode: TasbeehMode,
    tasbeehat: Array<TasbeehItem>,
    isVibrationEnabled: Boolean,
    isVolumeCounterEnabled: Boolean,
    isSpeakTasbeehOnFlipEnabled: Boolean
) {

    var times = times
        private set
    var mode = mode
        private set
    var tasbeehat: Array<TasbeehItem> = tasbeehat
        private set
    var isVibrationEnabled = isVibrationEnabled
        private set
    var isVolumeCounterEnabled = isVolumeCounterEnabled
        private set
    var isSpeakTasbeehOnFlipEnabled = isSpeakTasbeehOnFlipEnabled
        private set

    fun update(
        times: Int = this.times,
        mode: TasbeehMode = this.mode,
        tasbeehat: Array<TasbeehItem> = this.tasbeehat,
        isVibrationEnabled: Boolean = this.isVibrationEnabled,
        isVolumeCounterEnabled: Boolean = this.isVolumeCounterEnabled,
        isSpeakTasbeehOnFlipEnabled: Boolean = this.isSpeakTasbeehOnFlipEnabled
    ) {
        this.times = times
        this.mode = mode
        this.tasbeehat = tasbeehat
        this.isVibrationEnabled = isVibrationEnabled
        this.isVolumeCounterEnabled = isVolumeCounterEnabled
        this.isSpeakTasbeehOnFlipEnabled = isSpeakTasbeehOnFlipEnabled
    }

    fun update(config: TasbeehConfig) {
        this.update(
            config.times,
            config.mode,
            config.tasbeehat,
            config.isVibrationEnabled,
            config.isVolumeCounterEnabled,
            config.isSpeakTasbeehOnFlipEnabled,
        )
    }

    fun clone(): TasbeehConfig {
        return TasbeehConfig(
            times,
            mode,
            tasbeehat,
            isVibrationEnabled,
            isVolumeCounterEnabled,
            isSpeakTasbeehOnFlipEnabled
        )
    }

    override fun toString(): String {
        return buildString {
        append("TasbeehConfig(times=")
        append(times)
        append(", mode=")
        append(mode)
        append(", isVibrationEnabled=")
        append(isVibrationEnabled)
        append(", isVolumeCounterEnabled=")
        append(isVolumeCounterEnabled)
        append(", isSpeakTasbeehOnFlipEnabled=")
        append(isSpeakTasbeehOnFlipEnabled)
        append(")")
    }
    }


}
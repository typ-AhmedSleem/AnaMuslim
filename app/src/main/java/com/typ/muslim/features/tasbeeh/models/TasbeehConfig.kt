package com.typ.muslim.features.tasbeeh.models

import com.typ.muslim.features.tasbeeh.enums.TasbeehMode

class TasbeehConfig(
    times: Int,
    mode: TasbeehMode,
    isVibrationEnabled: Boolean,
    isVolumeCounterEnabled: Boolean,
    isSpeakTasbeehOnFlipEnabled: Boolean
) {

    var times = times
        private set
    var mode = mode
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
        isVibrationEnabled: Boolean = this.isVibrationEnabled,
        isVolumeCounterEnabled: Boolean = this.isVolumeCounterEnabled,
        isSpeakTasbeehOnFlipEnabled: Boolean = this.isSpeakTasbeehOnFlipEnabled
    ) {
        this.times = times
        this.mode = mode
        this.isVibrationEnabled = isVibrationEnabled
        this.isVolumeCounterEnabled = isVolumeCounterEnabled
        this.isSpeakTasbeehOnFlipEnabled = isSpeakTasbeehOnFlipEnabled
    }

    fun update(config: TasbeehConfig) {
        this.update(
            config.times,
            config.mode,
            config.isVibrationEnabled,
            config.isVolumeCounterEnabled,
            config.isSpeakTasbeehOnFlipEnabled,
        )
    }

    fun clone(
        times: Int = this.times,
        mode: TasbeehMode = this.mode,
        isVibrationEnabled: Boolean = this.isVibrationEnabled,
        isVolumeCounterEnabled: Boolean = this.isVolumeCounterEnabled,
        isSpeakTasbeehOnFlipEnabled: Boolean = this.isSpeakTasbeehOnFlipEnabled
    ) = TasbeehConfig(times, mode, isVibrationEnabled, isVolumeCounterEnabled, isSpeakTasbeehOnFlipEnabled)

}
/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */
package com.typ.muslim.ui.tasbeeh

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import com.app.progresviews.ProgressWheel
import com.google.android.material.textview.MaterialTextView
import com.typ.muslim.R
import com.typ.muslim.features.tasbeeh.TasbeehManager
import com.typ.muslim.features.tasbeeh.enums.TasbeehMode
import com.typ.muslim.features.tasbeeh.models.TasbeehConfig
import com.typ.muslim.features.tasbeeh.models.TasbeehItem
import com.typ.muslim.ui.home.DashboardCard
import com.typ.muslim.utils.toLocaleString

@SuppressLint("NewApi")
class TasbeehDashboardCard : DashboardCard {

    // Constants
    private val tickVibEffect: VibrationEffect
    private val flipVibEffect: VibrationEffect

    // Runtime
    private var currIdx: Int = 0
    private var ticksMade: Int = 0
    private val config: TasbeehConfig
    private var lastTickTime: Long = 0
    private val tickPlayer: MediaPlayer
    private var tasbeehat: Array<TasbeehItem>

    // UI
    private lateinit var tvContent: MaterialTextView
    private lateinit var pwCounter: ProgressWheel

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        // Load config
        config = if (isInEditMode) {
            TasbeehConfig(
                times = 33,
                mode = TasbeehMode.TASBEEH_MODE,
                isVibrationEnabled = true,
                isVolumeCounterEnabled = true,
                isSpeakTasbeehOnFlipEnabled = true
            )
        } else TasbeehManager.loadConfig(context)
        // Create VibrationEffects
        tickVibEffect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK)
        flipVibEffect = VibrationEffect.createWaveform(longArrayOf(50, 50, 50, 50), -1)
        // Create tick player instance
        tickPlayer = MediaPlayer.create(context, R.raw.sound_tick)
        // Get default tasbeehat
        tasbeehat = TasbeehManager.getDefaultTasbeehat(context)
    }

    override fun prepareCardView(context: Context) {
        inflate(R.layout.layout_tasbeeh_card)
        tvContent = findViewById(R.id.tv_tasbeeh_content)
        pwCounter = findViewById(R.id.pw_tasbeeh_count)
    }

    override fun refreshUI() {
        if (isInEditMode) return
        tvContent.text = tasbeehat[currIdx].content
        pwCounter.setStepCountText(ticksMade.toLocaleString(context))
        pwCounter.setPercentage(((ticksMade / config.times.toFloat()).coerceIn(0.0f, 1.0f) * 360).toInt())
    }

    override fun reset() {
        // Reset runtime
        config.update(TasbeehManager.loadConfig(context))
        tasbeehat = TasbeehManager.getDefaultTasbeehat(context)
        // Refresh UI
        refreshUI()
    }

    fun handleKeyEvent(keyCode: Int, event: KeyEvent?): Boolean {
        // Increase if VolumeCounter is enabled and any of volume buttons is clicked
        if (config.isVolumeCounterEnabled && (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {
            this.plusOne()
            return true
        }
        return false
    }

    private fun plusOne() {
        // Check warmup duration exceeded
        if (System.currentTimeMillis() - lastTickTime > WARMUP_MILLIS) {
            // Do plusOne logic
            if (config.mode == TasbeehMode.TASBEEH_MODE) {
                if (ticksMade >= config.times) {
                    // Current item has finished
                    if (currIdx >= tasbeehat.size) currIdx = 0
                    else currIdx++
                    doFlipEffects()
                } else {
                    // Current item still not completed
                    ticksMade++
                    doTickEffects()
                }
                refreshUI()
            } else {
                ticksMade++
                doTickEffects()
                pwCounter.setStepCountText(ticksMade.toLocaleString(context))
            }
            // Save lastTickTime
            lastTickTime = System.currentTimeMillis()
        }
    }

    private fun doTickEffects() {
        // Play tick sound and perform a tap haptic feedback
        try {
            tickPlayer.seekTo(0)
            tickPlayer.start()
        } catch (_: Throwable) {
            MediaPlayer.create(context, R.raw.sound_tick).apply {
                start()
                release()
            }
        }
        // Do tick vibration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) vibrator.vibrate(tickVibEffect) else vibrator.vibrate(50)
    }

    private fun doFlipEffects() {
        try {
            // Play the sound of next tasbeeh
            // fixme: replace with next tasbeeh name sound file resId}
            MediaPlayer.create(context, R.raw.sound_flip).start()
        } catch (_: Throwable) {
        }
        // Do flip vibration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) vibrator.vibrate(flipVibEffect) else vibrator.vibrate(longArrayOf(50, 50, 50, 50), -1)
    }

    override fun onClick(v: View?) = this.plusOne()

    override fun onLongClick(v: View?): Boolean {
        TODO("Show TasbeehOptionsBottomSheet")
        return true
    }

    override fun toString() = "TasbeehDashboardCard"

    companion object {
        private const val WARMUP_MILLIS = 50L
    }

}

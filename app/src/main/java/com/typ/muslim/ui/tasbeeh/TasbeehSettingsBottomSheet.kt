package com.typ.muslim.ui.tasbeeh

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.transition.TransitionManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.textview.MaterialTextView
import com.typ.muslim.R
import com.typ.muslim.features.tasbeeh.TasbeehManager
import com.typ.muslim.features.tasbeeh.enums.TasbeehMode
import com.typ.muslim.features.tasbeeh.enums.TasbeehTimes
import com.typ.muslim.features.tasbeeh.models.TasbeehConfig
import com.typ.muslim.features.tasbeeh.models.TasbeehItem
import com.typ.muslim.interfaces.ResultCallback
import com.typ.muslim.ui.views.SwitcherCard

class TasbeehSettingsBottomSheet(
    val ctx: Context,
    val config: TasbeehConfig,
    val callback: ResultCallback<TasbeehConfig>
) {

    // UI
    private val bs: BottomSheetDialog = BottomSheetDialog(ctx)
    private val btgMode: MaterialButtonToggleGroup
    private val btgCounter: MaterialButtonToggleGroup
    private val btgItems: MaterialButtonToggleGroup
    private val swcUseVolumeCounter: SwitcherCard
    private val swcVibrateOnCount: SwitcherCard
    private val swcSpeakTasbeeh: SwitcherCard

    init {
        // id <-> tasbeeh-id map
        val idsMap = mapOf(
            1 to R.id.btn_item1,
            2 to R.id.btn_item2,
            3 to R.id.btn_item3,
            4 to R.id.btn_item4,
        )

        bs.apply {
            setContentView(R.layout.bs_tasbeeh_options)

            val container = findViewById<FrameLayout>(R.id.container)!!

            btgMode = findViewById<MaterialButtonToggleGroup>(R.id.btg_mode)!!.apply {
                check(
                    when (config.mode) {
                        TasbeehMode.FREE_MODE -> R.id.btn_free_mode
                        else -> R.id.btn_tasbeeh_mode
                    }
                )
            }

            val tvCounter = findViewById<MaterialTextView>(R.id.t2)!!.apply {
                if (config.mode == TasbeehMode.FREE_MODE) visibility = View.GONE
            }
            btgCounter = findViewById<MaterialButtonToggleGroup>(R.id.btg_counter)!!.apply {
                check(
                    when (config.times) {
                        66 -> R.id.btn_66_times
                        99 -> R.id.btn_99_times
                        else -> R.id.btn_33_times
                    }
                )
                if (config.mode == TasbeehMode.FREE_MODE) visibility = View.GONE
            }

            // Create mode change listener
            val modeChangeListener = MaterialButtonToggleGroup.OnButtonCheckedListener { btg, id, checked ->
                TransitionManager.beginDelayedTransition(container)
                if (id == R.id.btn_tasbeeh_mode && checked) {
                    // Show counter
                    tvCounter.animate().alpha(1f).scaleY(1f).setDuration(250).withStartAction {
                        tvCounter.visibility = View.VISIBLE
                    }.start()
                    btgCounter.animate().alpha(1f).scaleY(1f).setDuration(250).withStartAction {
                        btgCounter.visibility = View.VISIBLE
                    }.start()
                } else if (id == R.id.btn_free_mode && checked) {
                    // Hide counter
                    tvCounter.animate().alpha(0f).scaleY(0f).setDuration(250).withEndAction {
                        tvCounter.visibility = View.GONE
                    }.start()
                    btgCounter.animate().alpha(0f).scaleY(0f).setDuration(250).withEndAction {
                        btgCounter.visibility = View.GONE
                    }.start()
                    TransitionManager.endTransitions(container)
                }
            }
            btgMode.addOnButtonCheckedListener(modeChangeListener)

            btgItems = findViewById<MaterialButtonToggleGroup>(R.id.btg_tasbeehat)!!.apply {
                for (item in config.tasbeehat) idsMap[item.id]?.let { check(it) }
            }

            swcUseVolumeCounter = findViewById<SwitcherCard>(R.id.swc_volume_counter)!!.apply {
                check(config.isVolumeCounterEnabled)
            }
            swcVibrateOnCount = findViewById<SwitcherCard>(R.id.swc_vibrate_on_tick)!!.apply {
                check(config.isVibrationEnabled)
            }
            swcSpeakTasbeeh = findViewById<SwitcherCard>(R.id.swc_speak_tasbeeh)!!.apply {
                check(config.isSpeakTasbeehOnFlipEnabled)
            }

            findViewById<MaterialButton>(R.id.btn_reset_tasbeeh_settings)?.setOnClickListener {
                btgMode.check(R.id.btn_tasbeeh_mode)
                btgCounter.check(R.id.btn_33_times)
                btgItems.apply {
                    check(R.id.btn_item1)
                    check(R.id.btn_item2)
                    check(R.id.btn_item3)
                    check(R.id.btn_item4)
                }
                swcUseVolumeCounter.check(true)
                swcVibrateOnCount.check(true)
                swcSpeakTasbeeh.check(true)
            }
            findViewById<MaterialButton>(R.id.btn_apply_tasbeeh_settings)?.setOnClickListener {
                callback.onResult(
                    TasbeehConfig(
                        mode = when (btgMode.checkedButtonId) {
                            R.id.btn_free_mode -> TasbeehMode.FREE_MODE
                            else -> TasbeehMode.TASBEEH_MODE
                        },
                        tasbeehat = getSelectedTasbeehat(idsMap),
                        times = when (btgCounter.checkedButtonId) {
                            R.id.btn_33_times -> TasbeehTimes.TIMES_33.howMany
                            R.id.btn_66_times -> TasbeehTimes.TIMES_66.howMany
                            R.id.btn_99_times -> TasbeehTimes.TIMES_99.howMany
                            else -> TasbeehTimes.TIMES_INFINITE.howMany
                        },
                        isVibrationEnabled = swcVibrateOnCount.isOn,
                        isVolumeCounterEnabled = swcUseVolumeCounter.isOn,
                        isSpeakTasbeehOnFlipEnabled = swcSpeakTasbeeh.isOn
                    )
                )
                btgMode.removeOnButtonCheckedListener(modeChangeListener)
                cancel()
            }
        }
    }

    private fun getSelectedTasbeehat(map: Map<Int, Int>): Array<TasbeehItem> {
        val ids = btgItems.checkedButtonIds
        if (ids.isEmpty()) return emptyArray()

        val defItems = TasbeehManager.getDefaultTasbeehat(ctx)
        val list = mutableListOf<TasbeehItem>()
        for (entry in map) {
            try {
                if (ids.contains(entry.value)) list.add(defItems[entry.key - 1])
            } catch (_: IndexOutOfBoundsException) {
            }
        }
        return list.toTypedArray()
    }

    fun show() = bs.show()

}
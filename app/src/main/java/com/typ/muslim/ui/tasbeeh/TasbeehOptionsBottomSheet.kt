package com.typ.muslim.ui.tasbeeh

import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButtonToggleGroup
import com.typ.muslim.R
import com.typ.muslim.ui.views.SwitcherCard

class TasbeehOptionsBottomSheet(val ctx: Context) {

    // UI
    private val bs: BottomSheetDialog = BottomSheetDialog(ctx)
    private val btgMode: MaterialButtonToggleGroup = TODO()
    private val btgCounter: MaterialButtonToggleGroup
    private val swcUseVolumeCounter: SwitcherCard
    private val swcVibrateOnCount: SwitcherCard
    private val swcSpeakTasbeeh: SwitcherCard

    init {
        // Init UI
        bs.setContentView(R.layout.bs_tasbeeh_options)

    }

}
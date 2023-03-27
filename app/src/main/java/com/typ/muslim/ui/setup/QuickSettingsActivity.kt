/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */
package com.typ.muslim.ui.setup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.typ.muslim.R
import com.typ.muslim.enums.OngoingNotificationStyle
import com.typ.muslim.enums.PrayNotifyMethod

class QuickSettingsActivity : AppCompatActivity(R.layout.fragment_wizard_quick_settings) {

    // Current settings
    private val muteDuringPrays = IntArray(5)
    private val notifyBeforePray = IntArray(5)
    private val isOngoingNotifEnabled = true
    private val isFlipToStopAzanEnabled = false
    private val isVolumeToStopAzanEnabled = false
    private val iqamaAfterPrays = BooleanArray(5)
    private val ongoingNotifStyle = OngoingNotificationStyle.STYLE1
    private val praysNotifMethods: Array<PrayNotifyMethod> = TODO("PraysNotifMethod array not yet initialized.")

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() // Hide default actionBar
        prepareSettings() // Prepare settings
        prepareUI() // Prepare UI
    }

    private fun prepareSettings() {
        TODO("Settings not yet prepared.")
    }

    private fun prepareUI() {
        TODO("UI not yet prepared.")
    }

    private fun resetSettings() {
        TODO("Reset settings not yet coded.")
    }

}
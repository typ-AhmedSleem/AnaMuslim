/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */
package com.typ.muslim.ui.khatma.dashboard.views

import android.content.Context
import android.util.AttributeSet
import com.app.progresviews.ProgressWheel
import com.google.android.material.textview.MaterialTextView
import com.typ.muslim.R
import com.typ.muslim.features.khatma.models.Khatma
import com.typ.muslim.managers.LocaleManager
import com.typ.muslim.managers.LocaleManager.Locales.ARABIC
import com.typ.muslim.managers.ResMan
import com.typ.muslim.ui.ViewContainer

class ActiveKhatmaView : ViewContainer {

    // Views
    private lateinit var tvKhatmaName: MaterialTextView
    private lateinit var tvKhatmaWerd: MaterialTextView
    private lateinit var prwRemParts: ProgressWheel

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun prepareView(context: Context) {
        // Inflate view
        inflate(context, R.layout.layout_active_khatma_card, this)
        // Init views
        tvKhatmaName = findViewById(R.id.tv_khatma_name)
        tvKhatmaWerd = findViewById(R.id.tv_khatma_curr_werd)
        prwRemParts = findViewById(R.id.prw_khatma_rem_parts)
    }

    fun refreshUI(khatma: Khatma) {
        khatma.let {
            // Khatma name
            tvKhatmaName.text = it.name
            // Khatma current werd
            val werd = it.currentWerd
            tvKhatmaWerd.text = String.format(
                LocaleManager.getCurrLocale(context),
                "${werd.start.surah.getName(context)} (${werd.start.number}) - ${werd.end.surah.getName(context)} (${werd.end.number})"
            )
            // Khatma progress
            val remWerds = it.remainingWerds
            val isLocaleArabic = LocaleManager.isArabic(context)
            val strRemParts = if (isLocaleArabic) {
                // Arabic only requires some grammar formatting for sentence
                when (remWerds) {
                    1 -> ResMan.getString(context, R.string.one_part) // Part 1
                    2 -> ResMan.getString(context, R.string.two_parts)// Part 2
                    in 3..10 -> String.format(ARABIC, "$remWerds ${ResMan.getString(context, R.string._parts1)}") // Parts (3 -> 10)
                    else -> String.format(ARABIC, "$remWerds ${ResMan.getString(context, R.string._parts2)}") // Parts (11 -> 30)
                }
            } else {
                // English and other languages except Arabic
                String.format(
                    LocaleManager.getCurrLocale(context),
                    "$remWerds ${ResMan.getString(context, if (remWerds == 1) R.string.part else R.string.parts)}"
                )
            }
            prwRemParts.setPercentage(360 - (it.progressPercentage / 100 * 360).toInt()) // Decremental.
            prwRemParts.setStepCountText(strRemParts)
        }
    }

}
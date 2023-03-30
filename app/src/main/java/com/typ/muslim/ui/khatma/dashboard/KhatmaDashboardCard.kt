/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2023.  TYP INC. All Rights Reserved
 */
package com.typ.muslim.ui.khatma.dashboard

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.widget.ViewSwitcher
import androidx.core.app.ActivityOptionsCompat
import com.typ.muslim.R
import com.typ.muslim.features.khatma.KhatmaManager
import com.typ.muslim.features.khatma.interfaces.KhatmaManagerCallback
import com.typ.muslim.features.khatma.models.Khatma
import com.typ.muslim.features.khatma.models.KhatmaWerd
import com.typ.muslim.managers.ResMan
import com.typ.muslim.ui.home.DashboardCard
import com.typ.muslim.ui.khatma.KhatmaActivity
import com.typ.muslim.ui.khatma.KhatmaPlannerActivity
import com.typ.muslim.ui.khatma.dashboard.views.ActiveKhatmaView
import com.typ.muslim.ui.utils.ViewUtils.dp

class KhatmaDashboardCard : DashboardCard, KhatmaManagerCallback {

    // Statics
    private val tag = "KhatmaDashboardCard"

    // Runtime
    private var manager: KhatmaManager? = null
    private val isShowingActive: Boolean
        get() = switcher.currentView is ActiveKhatmaView

    // Views
    private lateinit var switcher: ViewSwitcher

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        if (!isInEditMode) manager = KhatmaManager.newInstance(context, KhatmaManager.getLastActiveKhatma(context), this)
    }

    override fun prepareCardView(context: Context) {
        // Inflate card view and init switcher
        transitionName = "transition_card_to_activity"
        inflate(context, R.layout.layout_khatma_card, this)
        switcher = findViewById(R.id.vs_khatma_card)
//        switcher.setInAnimation(context, R.anim.fade_in_slide_in)
//        switcher.setOutAnimation(context, R.anim.fade_out_slide_out)
        setCardBackgroundColor(ResMan.getColor(context, R.color.white))
        rippleColor = ColorStateList.valueOf(ResMan.getColor(context, R.color.ripple_white))
    }

    override fun refreshRuntime() {
        manager = KhatmaManager.newInstance(context, KhatmaManager.getLastActiveKhatma(context), this)
    }

    override fun refreshUI() {
        if (((manager?.holdsActiveKhatma) == true)) flipToActiveKhatma()
        else {
            if (switcher.currentView is ActiveKhatmaView) flipToNoKhatma()
        }
    }

    private fun flipToActiveKhatma() {
        if (!isShowingActive) {
            switcher.showNext()
            strokeWidth =0
        }
        manager?.khatma?.let { (switcher.currentView as ActiveKhatmaView).refreshUI(it) }
    }

    private fun flipToNoKhatma() {
        if (isShowingActive) {
            switcher.showPrevious()
            strokeWidth = 0.5f.dp(context)
        }
    }

    override
    fun onPrepareManager() {
        flipToNoKhatma()
    }

    override fun onManageKhatma(khatma: Khatma) {
        flipToActiveKhatma()
    }

    override fun onHaveNoKhatma() {
        flipToNoKhatma()
    }

    override fun onProgressUpdated(nextWerd: KhatmaWerd) {
        flipToActiveKhatma()
    }

    override fun onSwitchKhatma(khatma: Khatma) {
        flipToActiveKhatma()
    }

    override fun onFinishKhatma() {
        flipToNoKhatma()
    }

    fun handleClick(activity: Activity) {
        val opt = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, this, "transition_card_to_activity")
        startActivity(Intent(context, if (isShowingActive) KhatmaActivity::class.java else KhatmaPlannerActivity::class.java), opt.toBundle())
    }

}
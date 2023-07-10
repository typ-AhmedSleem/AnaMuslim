/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */
package com.typ.muslim.ui.prays

import android.content.Context
import android.graphics.Typeface
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import cn.iwgang.countdownview.CountdownView
import com.mpt.android.stv.Slice
import com.mpt.android.stv.SpannableTextView
import com.typ.muslim.R
import com.typ.muslim.enums.PrayNotifyMethod
import com.typ.muslim.features.prays.PrayerManager
import com.typ.muslim.features.prays.enums.PrayType
import com.typ.muslim.features.prays.interfaces.PrayTimeCameListener
import com.typ.muslim.features.prays.models.Pray
import com.typ.muslim.features.ramadan.RamadanManager
import com.typ.muslim.managers.AMSettings
import com.typ.muslim.managers.LocaleManager
import com.typ.muslim.models.Timestamp
import com.typ.muslim.ui.home.DashboardCard
import com.typ.muslim.utils.DisplayUtils
import java.util.Calendar
import java.util.Locale

class NextPrayDashboardCard : DashboardCard {

    // Runtime
    private var locale: Locale
    private var nextPray: Pray
    private var currentPray: Pray
    private var notifyMethod: PrayNotifyMethod

    // Views
    private lateinit var stvNextPrayName: SpannableTextView
    private lateinit var ifvPrayNotifMethod: ImageView
    private lateinit var cdTimeRemaining: CountdownView

    // Listeners
    private lateinit var ptcListener: PrayTimeCameListener

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        // Current locale
        locale = LocaleManager.getCurrLocale(context)
        // Current pray
        currentPray = if (isInEditMode) {
            // [FOR DEVELOPMENT PREVIEW ONLY]
            Pray(PrayType.ISHA, "Isha", Timestamp.NOW())
        } else PrayerManager.getCurrentPray(context)
        // Next pray
        nextPray = if (isInEditMode) {
            // [FOR DEVELOPMENT PREVIEW ONLY]
            Pray(PrayType.MAGHRIB, "MAGHRIB", Timestamp.TOMORROW().set(Calendar.HOUR_OF_DAY, 4))
        } else PrayerManager.getNextPray(context)
        // Notify method for next pray
        notifyMethod = if (isInEditMode) {
            // [FOR DEVELOPMENT PREVIEW ONLY]
            PrayNotifyMethod.values().random()
        } else AMSettings.getPraysNotifyMethod(context)[nextPray.type.ordinal]

        // [DEV LOCALE ONLY]
        if (isInEditMode) locale = LocaleManager.Locales.ARABIC
        refreshUI()
    }

    override fun prepareCardView(context: Context) {
        // Init card
        strokeWidth = 0
        inflate(getContext(), R.layout.layout_minimized_next_pray_card, this)
        // Setup content views
        ifvPrayNotifMethod = findViewById(R.id.prayNotifMethodIFV)
        stvNextPrayName = findViewById(R.id.tv_next_pray_name)
        cdTimeRemaining = findViewById(R.id.cdv_next_pray_remaining)
        // Callbacks
        setOnClickListener(this)
        setOnLongClickListener(this)
        cdTimeRemaining.setOnCountdownEndListener {
            setNextPray(ptcListener.onPrayTimeCame(nextPray))
            refreshUI()
        }
    }

    override fun refreshRuntime() {
        locale = LocaleManager.getCurrLocale(context)
        notifyMethod = AMSettings.getPraysNotifyMethod(context)[nextPray.type.ordinal]
    }

    override fun refreshUI() {
        updateNotifyMethod()
        showNextPrayNameOnTv()
        cdTimeRemaining.start(nextPray.getIn().toMillis() - System.currentTimeMillis())
    }

    private fun updateNotifyMethod() {
        ifvPrayNotifMethod.setImageResource(
            when (notifyMethod) {
                PrayNotifyMethod.AZAN -> R.drawable.ic_notify_with_sound
                PrayNotifyMethod.NOTIFICATION_ONLY -> R.drawable.ic_notify_without_sound
                PrayNotifyMethod.OFF -> R.drawable.ic_notify_off
            }
        )
    }

    private fun showNextPrayNameOnTv() {
        stvNextPrayName.apply {
            reset()
            // Pray name
            addSlice(
                Slice.Builder(getString(nextPray.prayNameRes))
                    .textColor(getColor(com.google.android.material.R.color.material_dynamic_primary90))
                    .textSize(DisplayUtils.sp2px(context, 22f))
                    .style(Typeface.BOLD)
                    .build()
            )
            // Suhur, Iftar, Qiyam slice if in Ramadan
            if (RamadanManager.isInRamadan() && (nextPray.type in arrayOf(PrayType.FAJR, PrayType.MAGHRIB, PrayType.ISHA))) {
                val sliceText = when (nextPray.type) {
                    PrayType.FAJR -> getString(R.string.fasting)
                    PrayType.MAGHRIB -> getString(R.string.iftar)
                    PrayType.ISHA -> getString(R.string.qiyam)
                    else -> ""
                }
                if (!TextUtils.isEmpty(sliceText)) {
                    addSlice(
                        Slice.Builder(String.format(locale, "\n(%s)", sliceText))
                            .textSize(30)
                            .textColor(getColor(com.google.android.material.R.color.material_dynamic_neutral80))
                            .build()
                    )
                }
            }
            // Tomorrow if before 12 am next day and next pray is FAJR
            if (nextPray.type == PrayType.FAJR && !nextPray.getIn().dateMatches(Timestamp.NOW())) {
                stvNextPrayName.addSlice(
                    Slice.Builder(String.format(locale, " (%s)", getString(R.string.tomorrow)))
                        .textSize(30)
                        .textColor(getColor(com.google.android.material.R.color.material_dynamic_neutral80))
                        .build()
                )
            }
            // Pray time
            stvNextPrayName.addSlice(
                Slice.Builder(String.format(locale, "\n%s", nextPray.getFormattedTime(context, locale)))
                    .textSize(DisplayUtils.sp2px(context, 17f))
                    .textColor(getColor(com.google.android.material.R.color.material_dynamic_primary80))
                    .build()
            )
            stvNextPrayName.display()
        }
    }

    private fun setNextPray(nextPray: Pray) {
        this.currentPray = this.nextPray
        this.nextPray = if (!nextPray.hasPassed()) nextPray else PrayerManager.getNextPray(context)
    }

    override fun onClick(v: View) {
        // todo: show PraysBottomSheet
    }

    override fun onLongClick(v: View?): Boolean {
        // todo: show PraySettingsBottomSheet
        return true
    }

    fun setPrayTimeCameListener(listener: PrayTimeCameListener) {
        ptcListener = listener
    }

    override fun toString() = "NextPrayDashboardCard-v1"

    override fun onTimeChanged(now: Timestamp) {
        setNextPray(PrayerManager.getNextPray(context))
        refreshUI()
    }

}
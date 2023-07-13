/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */
package com.typ.muslim.ui.prays.views

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.ImageButton
import android.widget.ImageView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import com.mpt.android.stv.Slice
import com.mpt.android.stv.SpannableTextView
import com.typ.muslim.R
import com.typ.muslim.app.Keys
import com.typ.muslim.core.BackwardCompatible
import com.typ.muslim.features.prays.PrayerManager
import com.typ.muslim.features.prays.enums.PrayNotifyMethod
import com.typ.muslim.features.prays.enums.PrayType
import com.typ.muslim.features.prays.enums.PrayType.Companion.valueOf
import com.typ.muslim.features.prays.models.Pray
import com.typ.muslim.features.ramadan.RamadanManager
import com.typ.muslim.managers.AMSettings
import com.typ.muslim.managers.LocaleManager
import com.typ.muslim.managers.LocaleManager.Locales
import com.typ.muslim.models.Timestamp
import com.typ.muslim.utils.colorRes
import com.typ.muslim.utils.colorStateList
import com.typ.muslim.utils.dp2px
import com.typ.muslim.utils.sp2px
import com.typ.muslim.utils.stringRes
import com.typ.muslim.utils.todo
import java.util.Locale
import kotlin.random.Random

class PrayView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    constructor(context: Context, pray: Pray) : this(context) {
        this.pray = pray
    }

    // Runtime
    private lateinit var pray: Pray
    private var locale: Locale
    private lateinit var notifyMethod: PrayNotifyMethod

    private val hasPrayPassed: Boolean
        get() = pray.passed

    // Views
    private var ivIndicator: ImageView
    private var tvPrayTime: MaterialTextView
    private var tvPrayName: SpannableTextView
    private var ifvNotifyMethod: ImageButton

    init {
        // Init runtime
        locale = if (isInEditMode) Locales.ARABIC else LocaleManager.getCurrLocale(context)
        if (isInEditMode) notifyMethod = PrayNotifyMethod.values().random()
        // Parse attrs (if specified)
        if (attrs != null) {
            // Used only to view different data on each PrayItemView during development
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PrayView)
            val pray = valueOf(typedArray.getInt(R.styleable.PrayView_pivPray, PrayType.FAJR.ordinal))
            if (isInEditMode) this.pray = Pray(pray, pray.name, Timestamp.NOW().toMillis() + Random.nextInt(-60000, 60000))
            typedArray.recycle()
        } else {
            if (isInEditMode) this.pray = Pray(PrayType.values().random(), "[PRAY]", Timestamp.NOW().toMillis() + Random.nextInt(-60000, 60000))
        }
        // Init card
        radius = 30f
        strokeWidth = 0
        cardElevation = 0f
        setCardBackgroundColor(Color.TRANSPARENT)
        setStrokeColor(colorStateList(context, R.color.bg_input_box))
        // Init content views
        inflate(context, R.layout.layout_pray_view, this)
        ivIndicator = findViewById(R.id.iv_pray_pass_indicator)
        tvPrayName = findViewById(R.id.stv_pray_name)
        tvPrayTime = findViewById(R.id.tv_pray_time)
        ifvNotifyMethod = findViewById<ImageButton?>(R.id.ibtn_pray_notif_method).apply {
            setOnClickListener {
                // todo: Replace static icons with dynamic icons
                notifyMethod = when (notifyMethod) {
                    PrayNotifyMethod.AZAN -> PrayNotifyMethod.NOTIFICATION_ONLY
                    PrayNotifyMethod.NOTIFICATION_ONLY -> PrayNotifyMethod.OFF
                    PrayNotifyMethod.OFF -> if (pray.type == PrayType.SUNRISE) PrayNotifyMethod.NOTIFICATION_ONLY else PrayNotifyMethod.AZAN
                }
                AMSettings.save(context, Keys.PRAY_NOTIFY_METHOD(pray.type), notifyMethod)
                updateNotifyMethodView()
            }
        }
        // Listeners
        setOnClickListener {
            todo(context, "Show PrayViewerBottomSheet")
        }
        setOnLongClickListener {
            todo(context, "Show PraySettingsBottomSheet")
            true
        }
    }

    private fun internalUIRefresh(nextPray: Pray?) {
        // PrayName
        tvPrayName.apply {
            reset()
            // Pray name
            addSlice(
                Slice.Builder(stringRes(context, pray.prayNameRes))
                    .textSize(sp2px(context, 18f))
                    .textColor(colorRes(context, R.color.colorPrimary))
                    .build()
            )
            // Add Suhur, Iftar, Qiyam subscript to pray name (if in Ramadan)
            if (RamadanManager.isInRamadan() && (pray.type === PrayType.FAJR || pray.type === PrayType.MAGHRIB || pray.type === PrayType.ISHA)) {
                val sliceText = if (pray.type === PrayType.FAJR) stringRes(context, R.string.fasting) else if (pray.type === PrayType.MAGHRIB) stringRes(context, R.string.iftar) else if (pray.type === PrayType.ISHA) stringRes(context, R.string.qiyam) else ""
                if (!TextUtils.isEmpty(sliceText)) {
                    addSlice(
                        Slice.Builder("  (%s)".format(locale, sliceText))
                            .textSize(sp2px(context, 5f))
                            .textColor(colorRes(context, R.color.colorSecondary))
                            .build()
                    )
                }
            }
            display()
        }
        // Pray time
        tvPrayTime.text = pray.getFormattedTime(context, locale)
        tvPrayTime.setTextColor(colorRes(context, R.color.colorPrimary))
        // Indicators
        changeIndicator(nextPray)
        updateNotifyMethodView()
    }

    @BackwardCompatible
    fun setPray(pray: Pray) {
        this.pray = pray
        this.notifyMethod = AMSettings.getPrayNotifyMethod(context, pray.type)
        internalUIRefresh(PrayerManager.getNextPray(context))
    }

    fun setPray(pray: Pray, nextPray: Pray?) {
        this.pray = pray
        this.notifyMethod = AMSettings.getPrayNotifyMethod(context, pray.type)
        internalUIRefresh(nextPray)
    }

    private fun changeIndicator(nextPray: Pray?) {
        // Passed pray
        if (nextPray == null || pray.passed) {
            alpha = 0.6f
            strokeWidth = dp2px(context, 1.5f)
            ivIndicator.setImageResource(R.drawable.ic_done)
            setStrokeColor(colorStateList(context, R.color.bg_input_box))
            return
        }
        // Next pray
        if (nextPray == pray) {
            alpha = 1f
            strokeWidth = dp2px(context, 2f)
            setStrokeColor(colorStateList(context, R.color.colorPrimary))
            ivIndicator.setImageResource(R.drawable.ic_arrow_to_right)
            return
        }
        // Coming pray
        alpha = 1f
        ivIndicator.setImageDrawable(null)
        strokeWidth = dp2px(context, 2f)
        setStrokeColor(colorStateList(context, R.color.bg_input_box))
    }

    private fun updateNotifyMethodView() {
        when (notifyMethod) {
            PrayNotifyMethod.AZAN -> if (pray.type != PrayType.SUNRISE) ifvNotifyMethod.setImageResource(R.drawable.ic_alert_full)
            PrayNotifyMethod.NOTIFICATION_ONLY -> ifvNotifyMethod.setImageResource(R.drawable.ic_alert_notif)
            PrayNotifyMethod.OFF -> ifvNotifyMethod.setImageResource(R.drawable.ic_alert_off)
        }
    }

    fun changeIndicatorVisibility(makeVisible: Boolean) {
        ivIndicator.visibility = if (makeVisible) VISIBLE else INVISIBLE
    }

    fun changeNotifMethodVisibility(makeVisible: Boolean) {
        ifvNotifyMethod.visibility = if (makeVisible) VISIBLE else INVISIBLE
    }

    fun changeIndicatorsVisibility(makeVisible: Boolean) {
        changeIndicatorVisibility(makeVisible)
        changeNotifMethodVisibility(makeVisible)
    }

    companion object {
        private const val TAG = "PrayView"
    }
}

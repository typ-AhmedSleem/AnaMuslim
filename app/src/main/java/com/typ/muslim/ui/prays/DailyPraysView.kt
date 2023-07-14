package com.typ.muslim.ui.prays

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.children
import com.typ.muslim.features.prays.enums.PrayType
import com.typ.muslim.features.prays.models.Pray
import com.typ.muslim.features.prays.models.PrayTimes
import com.typ.muslim.models.Timestamp
import com.typ.muslim.ui.prays.views.PrayView
import com.typ.muslim.utils.dp2px
import java.util.Calendar

class DailyPraysView @JvmOverloads constructor(
    val ctx: Context,
    attrs: AttributeSet? = null
) : LinearLayout(ctx, attrs) {

    private val prayViewsCount: Int
        get() = children.count { it is PrayView }

    init {
        orientation = VERTICAL
        setBackgroundColor(Color.TRANSPARENT)
        // Add children
        if (childCount == 0) {
            val m = dp2px(ctx, 10f)
            // Use default view
            addView(
                PrayView(context),
                MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                    setMargins(m, m, m, 0)
                })
            addView(
                PrayView(context),
                MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                    setMargins(m, m, m, 0)
                })
            addView(PrayView(context),
                MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                    setMargins(m, m, m, 0)
                })
            addView(
                PrayView(context),
                MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                    setMargins(m, m, m, 0)
                })
            addView(PrayView(context),
                MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                    setMargins(m, m, m, 0)
                })
            addView(
                PrayView(context),
                MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                    setMargins(m, m, m, m)
                })
        }
        // [DEV PREVIEW]
        if (isInEditMode) {
            val now = Timestamp.NOW()
            val prayTimes = PrayTimes(
                Pray(PrayType.FAJR, "FAJR", now.copy().roll(Calendar.HOUR_OF_DAY, -6)),
                Pray(PrayType.SUNRISE, "SUNRISE", now.copy().roll(Calendar.HOUR_OF_DAY, -5)),
                Pray(PrayType.DHUHR, "DHUHR", now.copy().roll(Calendar.HOUR_OF_DAY, -4)),
                Pray(PrayType.ASR, "ASR", now.roll(Calendar.MINUTE, 15)),
                Pray(PrayType.MAGHRIB, "MAGHRIB", now.copy().roll(Calendar.HOUR_OF_DAY, 2)),
                Pray(PrayType.ISHA, "ISHA", now.copy().roll(Calendar.HOUR_OF_DAY, 4)),
            )
            val nextPray = Pray(PrayType.ASR, "ASR", now)
            display(prayTimes, nextPray)
        }
    }

    private fun doInternalRefresh(prayTimes: PrayTimes, nextPray: Pray?) {
        // Set each pray into its PrayView
        var idx = 0
        for (child in children) {
            if (idx >= 6) break
            if (child is PrayView) {
                child.setPray(prayTimes[idx], nextPray)
                idx += 1
            }
        }
    }

    fun display(prayTimes: PrayTimes, nextPray: Pray) {
        doInternalRefresh(prayTimes, nextPray)
    }

}
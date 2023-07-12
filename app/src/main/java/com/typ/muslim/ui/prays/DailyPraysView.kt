package com.typ.muslim.ui.prays

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.core.view.children
import com.typ.muslim.features.prays.PrayerManager
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

    // Runtime
    private var idx = 0
    private var nextPray: Pray?
    private var praysTimes: PrayTimes
    private var selectedDay: Timestamp = Timestamp.NOW()

    private val prayViewsCount: Int
        get() = children.count { it is PrayView }

    init {
        orientation = VERTICAL
        // [DEV PREVIEW]
        if (isInEditMode) {
            val now = Timestamp.NOW()
            praysTimes = PrayTimes(
                Pray(PrayType.SUNRISE, "FAJR", now.copy().roll(Calendar.HOUR_OF_DAY, -6)),
                Pray(PrayType.FAJR, "SUNRISE", now.copy().roll(Calendar.HOUR_OF_DAY, -5)),
                Pray(PrayType.DHUHR, "DHUHR", now.copy().roll(Calendar.HOUR_OF_DAY, -4)),
                Pray(PrayType.ASR, "ASR", now.roll(Calendar.MINUTE, 15)),
                Pray(PrayType.MAGHRIB, "MAGHRIB", now.copy().roll(Calendar.HOUR_OF_DAY, 2)),
                Pray(PrayType.ISHA, "ISHA", now.copy().roll(Calendar.HOUR_OF_DAY, 4)),
            )
            nextPray = Pray(PrayType.ASR, "ASR", now)
        } else {
            // Get prays for selected day
            praysTimes = PrayerManager.getPrays(ctx, selectedDay)
            nextPray = PrayerManager.getNextPray(praysTimes)
        }
        internalRefresh()
    }

    private fun internalRefresh() {
        // Check children
        if (childCount == 0) {
            val m = dp2px(ctx, 10f)
            // Use default view
            addView(
                PrayView(context, praysTimes.fajr),
                MarginLayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                    setMargins(m, m, m, 0)
                })
            addView(PrayView(context, praysTimes.sunrise),
                MarginLayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                    setMargins(m, m, m, 0)
                })
            addView(PrayView(context, praysTimes.dhuhr),
                MarginLayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                    setMargins(m, m, m, 0)
                })
            addView(PrayView(context, praysTimes.asr),
                MarginLayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                    setMargins(m, m, m, 0)
                })
            addView(PrayView(context, praysTimes.maghrib),
                MarginLayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                    setMargins(m, m, m, 0)
                })
            addView(PrayView(context, praysTimes.isha),
                MarginLayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                    setMargins(m, m, m, m)
                })
        } else if (prayViewsCount != 6) {
            // Check for missing children
            throw IllegalStateException("DailyPraysView must have 6 child views of type <PrayView> to display pray times.")
        }
        // Set each pray into its PrayView
        idx = 0
        for (child in children) {
            if (idx >= 6) break
            if (child is PrayView) {
                child.setPray(praysTimes[idx], nextPray)
                idx += 1
            }
        }
    }

}
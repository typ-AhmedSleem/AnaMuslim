package com.typ.muslim.ui.prays

import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.typ.muslim.R
import com.typ.muslim.features.prays.PrayerManager
import com.typ.muslim.ui.prays.views.PrayView

class PraysBottomSheet(
    val ctx: Context
) {

    init {
        val prays = PrayerManager.getTodayPrays(ctx)
        val nextPray = PrayerManager.getNextPray(ctx, prays)
        BottomSheetDialog(ctx).apply {
            setContentView(R.layout.bs_prays)
            findViewById<PrayView>(R.id.fajrPIV)?.setPray(prays.fajr, nextPray)
            findViewById<PrayView>(R.id.sunrisePIV)?.setPray(prays.sunrise, nextPray)
            findViewById<PrayView>(R.id.dhuhrPIV)?.setPray(prays.dhuhr, nextPray)
            findViewById<PrayView>(R.id.asrPIV)?.setPray(prays.asr, nextPray)
            findViewById<PrayView>(R.id.maghribPIV)?.setPray(prays.maghrib, nextPray)
            findViewById<PrayView>(R.id.ishaPIV)?.setPray(prays.isha, nextPray)

            show()
        }
    }

}
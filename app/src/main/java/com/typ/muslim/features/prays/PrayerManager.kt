/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */
package com.typ.muslim.features.prays

import android.content.Context
import com.typ.muslim.core.NeedsTesting
import com.typ.muslim.features.prays.enums.PrayStatus
import com.typ.muslim.features.prays.models.Pray
import com.typ.muslim.features.prays.models.PrayTimes
import com.typ.muslim.managers.AMSettings
import com.typ.muslim.managers.PrayTrackerManager
import com.typ.muslim.models.Location
import com.typ.muslim.models.Timestamp

object PrayerManager {

    const val TAG = "PrayerManager"

    @JvmStatic
    fun getTodayPrays(ctx: Context) = getPrays(ctx, 0)

    @JvmStatic
    fun getTodayPrays(ctx: Context, loc: Location) = getPrays(ctx, loc, 0)

    @JvmStatic
    fun getPrays(ctx: Context, rollDays: Int) = getPrays(ctx, AMSettings.getCurrentLocation(ctx), rollDays)

    @JvmStatic
    fun getPrays(context: Context, location: Location, rollDays: Int): PrayTimes {
        return PrayTimeCore.singletonInstance(context, location).getPrayTimes(rollDays)
    }

    @JvmStatic
    fun getPrays(ctx: Context, timestamp: Timestamp): PrayTimes {
        return PrayTimeCore.newInstance(ctx, AMSettings.getCurrentLocation(ctx)).getPrayTimes(timestamp)
    }

    @JvmStatic
    fun getUpcomingPrays(ctx: Context): Array<Pray> {
        val upPrays = mutableListOf<Pray>()
        for (pray in getTodayPrays(ctx).toArray()) {
            if (pray.passed) continue
            upPrays.add(pray)
        }
        return upPrays.toTypedArray()
    }

    @JvmStatic
    fun getNextPray(ctx: Context) = getNextPray(ctx, getTodayPrays(ctx))

    @NeedsTesting
    @JvmStatic
    fun getNextPray(ctx: Context, prays: PrayTimes): Pray {
        // Get next pray in today prays but if Isha has passed so, get tomorrow prays and return fajr
        if (prays.isha.passed) return getPrays(ctx, 1).fajr
        var nextPray = prays.fajr
        for (pray in prays.toArray()) {
            if (pray.passed) continue
            nextPray = pray
            break
        }
        return nextPray
    }

    @NeedsTesting
    @JvmStatic
    fun getNextPray(prays: PrayTimes): Pray? {
        if (prays.isha.passed) return null
        var nextPray = prays.fajr
        for (pray in prays.toArray()) {
            if (pray.passed) continue
            nextPray = pray
            break
        }
        return nextPray
    }

    @NeedsTesting
    @JvmStatic
    fun getCurrentPray(ctx: Context): Pray {
        // Get current pray
        val prays = getTodayPrays(ctx)
        var currentPray: Pray = prays.fajr
        for (pray in prays.toArrayNoSunrise()) {
            if (pray.passed) currentPray = pray
            else break
        }
        return currentPray
    }

    @JvmStatic
    fun didHePray(ctx: Context, pray: Pray, trueIfForgot: Boolean = false): Boolean {
        // Check if he prayed that pray or not
        var didHePray = false
        val todayRecords = PrayTrackerManager.getTodayRecords(ctx)
        if (todayRecords.isEmpty()) return false // He didn't.
        for (record in todayRecords) {
            if (record.pray === pray.type && record.prayTime.dateMatches(pray.time)) {
                didHePray = record.status == PrayStatus.FORGOT || record.wasPrayed()
                break
            }
        }
        return didHePray
    }
}

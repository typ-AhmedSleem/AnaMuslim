/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.features.prays;

import android.content.Context;

import androidx.annotation.NonNull;

import com.typ.muslim.enums.PrayStatus;
import com.typ.muslim.features.prays.enums.PrayType;
import com.typ.muslim.libs.easyjava.data.EasyList;
import com.typ.muslim.managers.AMSettings;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.managers.PrayTrackerManager;
import com.typ.muslim.models.Location;
import com.typ.muslim.features.prays.models.Pray;
import com.typ.muslim.features.prays.models.PrayTimes;
import com.typ.muslim.models.PrayTrackerRecord;
import com.typ.muslim.models.Timestamp;

import java.util.List;

public class PrayerManager {

    // Statics
    private static final String TAG = "PrayerManager";

    public static PrayTimes getTodayPrays(Context context) {
        return getPrays(context, 0);
    }

    public static PrayTimes getTodayPrays(Context context, Location location) {
        return getPrays(context, location, 0);
    }

    public static PrayTimes getPrays(Context context, int rollDays) {
        return PrayTimeCore.getSingletonInstance(context, AMSettings.getCurrentLocation(context)).getPrayTimes(rollDays);
    }

    public static PrayTimes getPrays(Context context, Location location, int rollDays) {
        return PrayTimeCore.getSingletonInstance(context, location).getPrayTimes(rollDays);
    }

    public static PrayTimes getPrays(Context context, Timestamp in) {
        return PrayTimeCore.getNewInstance(context, AMSettings.getCurrentLocation(context)).getPrayTimes(in);
    }

    public static EasyList<Pray> getUpcomingPrays(Context context) {
        return PrayTimeCore.getSingletonInstance(context, AMSettings.getCurrentLocation(context)).getUpcomingPrays();
    }

    public static Pray getNextPray(Context context) {
        return getNextPray(context, getTodayPrays(context));
    }

    public static Pray getNextPray(Context context, @NonNull PrayTimes prays) {
        Pray nextPray = null;
        // Check if Isha time has passed and refresh prays list if found so
        if (prays.getIsha().hasPassed()) prays = getPrays(context, 1);
        AManager.log(TAG, "getNextPray: Prays[%s]", prays);
        // Search for next pray
        for (Pray pray : prays.asList()) {
            if (pray.hasPassed()) continue;
            nextPray = pray;
            break;
        }
        AManager.log(TAG, "getNextPray: " + nextPray);
        return nextPray;
    }

    public static Pray getCurrentPray(Context context) {
        // TODO: 6/20/2021 Ask ashraf and Montaser about how to solve problem like this on their own
        PrayTimes prays = getTodayPrays(context);
        Pray currentPray = null;
        // Search for current pray
        for (@NonNull Pray pray : prays.asList()) {
            AManager.log(TAG, "getCurrentPray: trying pray: %s", pray);
            if (pray.hasPassed()) {
                if (pray.getType() == PrayType.SUNRISE) continue;
                currentPray = pray;
            } else break;
        }
        AManager.log(TAG, "getCurrentPray: CurrentPray is [%s].", currentPray);
        return currentPray;
    }

    public static boolean didHePray(Context context, Pray pray, boolean trueIfForgot) {
        if (pray == null) {
            AManager.log(TAG, "didHePray: Pray is null");
            return true;
        }
        // Check if he prayed that pray or not
        boolean didHePray = false;
        List<PrayTrackerRecord> todayRecords = PrayTrackerManager.getTodayRecords(context);
        if (todayRecords.size() == 0) {
            AManager.log(TAG, "didHePray: No records today");
            return false; // He didn't.
        }
        for (PrayTrackerRecord record : todayRecords) {
            if (record.getPray() == pray.getType() &&
                    record.getPrayTime().dateMatches(pray.getIn())) {
                didHePray = record.getStatus() == PrayStatus.FORGOT || record.wasPrayed();
                break;
            }
//			if (record.getPray() == pray.getType() &&
//			    record.getPrayTime().dateMatches(pray.getIn()) &&
//			    record.getStatus() != PrayStatus.FORGOT) {
//				didHePray = true; // Yes he prayed.
//				break;
//			}
        }
        AManager.log(TAG, "didHePray: %s | result[%b]", pray, didHePray);
        return didHePray;
    }
}

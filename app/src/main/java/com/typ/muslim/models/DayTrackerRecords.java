/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.models;

import android.content.Context;

import com.typ.muslim.app.Consumers;
import com.typ.muslim.features.prays.enums.PrayType;
import com.typ.muslim.features.prays.models.PrayTimes;
import com.typ.muslim.libs.easyjava.data.EasyList;
import com.typ.muslim.libs.easyjava.interfaces.EasyListLooper;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.managers.PrayTrackerManager;
import com.typ.muslim.features.prays.PrayerManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DayTrackerRecords implements Serializable {

    private final EasyList<PrayTrackerRecord> records;
    private final boolean filledMissing;

    public DayTrackerRecords(Context context, Timestamp when) {
        // Get records of the given date from local db
        records = EasyList.createList(PrayTrackerManager.getDayRecords(context, when));
        if (Timestamp.NOW().isAfter(when)) {
            // Fill missed prays
            final PrayTimes prays = PrayerManager.getPrays(context, when);
            final List<PrayType> missedPrays = new ArrayList<>();
            for (PrayType pray : PrayType.values()) {
                // Pass sunrise
                Consumers.doIf(() -> missedPrays.add(pray), records.getIf((i, rec) -> rec.getPray() == pray) == null);
            }
            for (PrayType mp : missedPrays) records.add(mp.ordinalWithoutSunrise(), PrayTrackerRecord.newMissedPrayRecord(prays.get(mp.ordinal())));
            filledMissing = true;
        } else filledMissing = false;
        AManager.log("DayTrackerRecord", toString());
    }

    public PrayTrackerRecord getFajrRecord() {
        return getIf((i, rec) -> rec.getPray() == PrayType.FAJR);
    }

    public PrayTrackerRecord getDhuhrRecord() {
        return getIf((i, rec) -> rec.getPray() == PrayType.DHUHR);
    }

    public PrayTrackerRecord getAsrRecord() {
        return getIf((i, rec) -> rec.getPray() == PrayType.ASR);
    }

    public PrayTrackerRecord getMaghribRecord() {
        return getIf((i, rec) -> rec.getPray() == PrayType.MAGHRIB);
    }

    public PrayTrackerRecord getIshaRecord() {
        return getIf((i, rec) -> rec.getPray() == PrayType.ISHA);
    }

    public PrayTrackerRecord getIf(EasyListLooper<PrayTrackerRecord> condition) {
        return records.getIf(condition);
    }

    public boolean hasFilledMissing() {
        return filledMissing;
    }

    @Override
    public String toString() {
        return "DayTrackerRecords{" +
                "records=" + records +
                "recordsCount=" + records.size() +
                ", filledMissing=" + filledMissing +
                '}';
    }
}

/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.managers;

import static com.typ.muslim.db.OldDatabase.TABLE_PRAYER_TRACKER;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.typ.muslim.features.prays.enums.PrayType;
import com.typ.muslim.db.OldDatabase;
import com.typ.muslim.enums.PrayStatus;
import com.typ.muslim.models.PrayTrackerRecord;
import com.typ.muslim.models.Timestamp;
import com.typ.muslim.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PrayTrackerManager {

    private static final String TAG = "PrayTrackerManager";

    /**
     * Add new track record to tracker table in database
     *
     * @return {@code true} if operations succeed,{@code false} if faced an error or operation failed
     */
    public static boolean record(Context context, PrayType pray, PrayStatus status, boolean atMosque, long prayTime, long prayedIn, long dayTimestamp) {
        return record(context, new PrayTrackerRecord(pray, status, atMosque, prayTime, prayedIn, dayTimestamp));
    }

    /**
     * Add new track record to tracker table in database
     *
     * @return {@code true} if operations succeed,{@code false} if faced an error or operation failed
     */
    public static boolean record(Context context, PrayTrackerRecord trackerRecord) {
        if (context == null || trackerRecord == null) return false;
        AManager.log(TAG, "record: PRAY[%s] | STATUS[%s] | AT_MOSQUE[%s] | PRAY_TIME[%s] | PRAYED_IN[%s] | DAY_TIME[%s] | NOW[%s]",
                trackerRecord.getPray(),
                trackerRecord.getStatus(),
                trackerRecord.wasAtMosque(),
                DateUtils.dfDateTimeSNX(true).format(new Date(trackerRecord.getPrayTime().toMillis())),
                DateUtils.dfDateTimeSNX(true).format(new Date(trackerRecord.getPrayedIn().toMillis())),
                DateUtils.dfDateTimeSNX(true).format(new Date(trackerRecord.getDayTimestamp().toMillis())),
                DateUtils.dfDateTimeSNX(true).format(new Date(System.currentTimeMillis())));
        try {
            OldDatabase.getInstance(context).execute("INSERT INTO " + TABLE_PRAYER_TRACKER + " (` what_pray `,` status `,` at_mosque `,` pray_time `,` prayed_in `,` day_timestamp `) VALUES(" +
                    "" + trackerRecord.getPray().ordinal() + "," +
                    "" + trackerRecord.getStatus().ordinal() + "," +
                    "" + (trackerRecord.wasAtMosque() ? 1 : 0) + "," +
                    "" + trackerRecord.getPrayTime().toMillis() + "," +
                    "" + trackerRecord.getPrayedIn().toMillis() + "," +
                    "" + trackerRecord.getDayTimestamp().toMillis() + ")");
            return true;
        } catch (SQLiteException | NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Return all track records between within the given range of two timestamps.
     * NOTE: values must meet this condition or it will return null [rangeEnd >= rangeStart].
     *
     * @param rangeStart The start of the range as timestamp in ms.
     * @param rangeEnd   The end of the range as timestamp in ms.
     * @return List of TrackerRecords if found, null if faced an error or an empty list if found no data.
     */
    @NonNull
    public static List<PrayTrackerRecord> getPrayTrackerRecordsInRange(Context context, long rangeStart, long rangeEnd) {
        Log.i("AnaMuslim", "getPrayTrackerRecordsInRange: " + "SELECT * FROM " + TABLE_PRAYER_TRACKER + " WHERE ` day_timestamp ` BETWEEN " + rangeStart + " AND " + rangeEnd);
        if (rangeStart > rangeEnd) return new ArrayList<>(); // Doesn't meet the condition.
        Cursor cursor = OldDatabase.getInstance(context).query("SELECT * FROM " + TABLE_PRAYER_TRACKER + " WHERE ` day_timestamp ` BETWEEN " + rangeStart + " AND " + rangeEnd);
        if (cursor == null) return new ArrayList<>(); // Error executing query.
        List<PrayTrackerRecord> trackerRecords = new ArrayList<>(); // Create new empty list.
        if (cursor.getCount() == 0) return trackerRecords; // No Records were found.
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            trackerRecords.add(new PrayTrackerRecord(PrayType.valueOf(cursor.getInt(0)),
                    PrayStatus.valueOf(cursor.getInt(1)),
                    cursor.getInt(2) == 1,
                    cursor.getLong(3),
                    cursor.getLong(4),
                    cursor.getLong(5)));
            cursor.moveToNext();
        }
        return trackerRecords;
    }

    @NonNull
    public static List<PrayTrackerRecord> getDayRecords(Context context, Timestamp when) {
        // Prepare today timestamp range
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);
        cal.set(year, month, day, 0, 0, 0);
        long dayStart = cal.getTimeInMillis();
        cal.set(year, month, day, 23, 59, 59);
        long dayEnd = cal.getTimeInMillis();
        AManager.log(TAG, "getDayRecords: START[%s] | NOW[%s] | END[%s]",
                DateUtils.dfDateTimeSNX(true).format(new Date(dayStart)),
                DateUtils.dfDateTimeSNX(true).format(new Date(System.currentTimeMillis())),
                DateUtils.dfDateTimeSNX(true).format(new Date(dayEnd)));
        // Return query results
        return getPrayTrackerRecordsInRange(context, dayStart, dayEnd);
    }

    @NonNull
    public static List<PrayTrackerRecord> getTodayRecords(Context context) {
        return getDayRecords(context, Timestamp.NOW());
    }

    @NonNull
    public static List<PrayTrackerRecord> getWeekRecords(Context context, @IntRange(from = 1, to = 4) int whichWeek) {
        // TODO: 6/19/2021 care of length of week and first day of week during calculations
        // Prepare week timestamps range
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DATE);
        int firstDayOfWeek = cal.getFirstDayOfWeek() + 1; // todo: care of it in settings. Default: Sunday
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) + 1;
        int weekLength = DateUtils.getWeekLength(whichWeek, month);
        int monthLength = DateUtils.getMonthLength(month, false);
        // Prepare constrains
        cal.set(year, month - 1, day, 0, 0, 0);
        if (dayOfWeek > 1) cal.add(Calendar.DATE, -(dayOfWeek - 1));
        long weekStart = cal.getTimeInMillis();
        cal.add(Calendar.DATE, weekLength);
        cal.add(Calendar.SECOND, -1);
        long weekEnd = cal.getTimeInMillis();
        // Query local database for given week then return results
        AManager.log(TAG, "getWeekRecords: START[%s] | NOW[%s] | END[%s] | Week[%d,%d]",
                DateUtils.dfDateTimeSNX(true).format(new Date(weekStart)),
                DateUtils.dfDateTimeSNX(true).format(new Date(System.currentTimeMillis())),
                DateUtils.dfDateTimeSNX(true).format(new Date(weekEnd)),
                weekLength, dayOfWeek);
        return getPrayTrackerRecordsInRange(context, weekStart, weekEnd);
    }

    @NonNull
    public static List<PrayTrackerRecord> getMonthRecords(Context context, @IntRange(from = 1, to = 12) int whichMonth) {
        // Prepare month timestamps range
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int monthLength = DateUtils.getMonthLength(whichMonth, false);
        // Prepare constrains
        cal.set(year, whichMonth - 1, 1, 0, 0, 0);
        long monthStart = cal.getTimeInMillis();
        cal.set(year, whichMonth - 1, monthLength, 23, 59, 59);
        long monthEnd = cal.getTimeInMillis();
        // Query local database for given month then return results
        AManager.log(TAG, "getMonthRecords: START[%s] | NOW[%s] | END[%s]",
                DateUtils.dfDateTimeSNX(true).format(new Date(monthStart)),
                DateUtils.dfDateTimeSNX(true).format(new Date(System.currentTimeMillis())),
                DateUtils.dfDateTimeSNX(true).format(new Date(monthEnd)));

        return getPrayTrackerRecordsInRange(context, monthStart, monthEnd);
    }

}

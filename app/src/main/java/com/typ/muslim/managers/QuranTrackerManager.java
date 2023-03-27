/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.managers;

import static com.typ.muslim.db.OldDatabase.TABLE_READ_QURAN;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.typ.muslim.db.OldDatabase;
import com.typ.muslim.models.ReadQuranRecord;
import com.typ.muslim.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QuranTrackerManager {

    static final String KEY_TARGET_MINS = "quranTargetMinutes";
    // Statics
    private static final String TAG = "QuranTrackerManager";

    /**
     * Add new read quran record to read quran table in database
     *
     * @return {@code true} if operations succeed,{@code false} if faced an error or operation failed
     */
    public static boolean record(Context context, ReadQuranRecord quranRecord) {
        if (quranRecord == null) return false;
        try {
            OldDatabase.getInstance(context).execute("INSERT INTO " + TABLE_READ_QURAN + " (` from_ayah `,` from_surah `,` to_ayah `,` to_surah `,` duration `,` day_timestamp `) VALUES (" +
                    "" + quranRecord.getFromAyah() + "," +
                    "" + quranRecord.getFromSurah() + "," +
                    "" + quranRecord.getToAyah() + "," +
                    "" + quranRecord.getFromSurah() + "," +
                    "" + quranRecord.getDuration() + "," +
                    "" + quranRecord.getWhen() + ")");
            return true;
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Return all quran records between within the given range of two timestamps.
     * NOTE: values must meet this condition or it will return null [rangeEnd >= rangeStart].
     *
     * @param rangeStart The start of the range as timestamp in ms.
     * @param rangeEnd   The end of the range as timestamp in ms.
     * @return List of ReadQuranRecord if found, null if faced an error or an empty list if found no data.
     */
    public static List<ReadQuranRecord> getReadQuranRecordsInRange(Context context, long rangeStart, long rangeEnd) {
        if (rangeStart > rangeEnd) return new ArrayList<>(); // Invalid range.
        Cursor cursor = OldDatabase.getInstance(context).query(String.format(Locale.ENGLISH, "SELECT * FROM %s WHERE ` day_timestamp ` BETWEEN %d AND %d", TABLE_READ_QURAN, rangeStart, rangeEnd));
        if (cursor == null) return new ArrayList<>(); // Faced an error.
        List<ReadQuranRecord> readQuranRecords = new ArrayList<>();
        if (cursor.getCount() == 0) return readQuranRecords;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            readQuranRecords.add(new ReadQuranRecord(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getLong(5)));
            cursor.moveToNext();
        }
        return readQuranRecords;
    }

    @NonNull
    public static List<ReadQuranRecord> getTodayRecords(Context context) {
        // Prepare today timestamp range
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);
        cal.set(year, month, day, 0, 0, 0);
        long todayStart = cal.getTimeInMillis();
        cal.set(year, month, day, 23, 59, 59);
        long todayEnd = cal.getTimeInMillis();
        AManager.log(TAG, "getTodayRecords: START[%s] | NOW[%s] | END[%s]\n",
                DateUtils.dfDateTimeSNX(true).format(new Date(todayStart)),
                DateUtils.dfDateTimeSNX(true).format(new Date(System.currentTimeMillis())),
                DateUtils.dfDateTimeSNX(true).format(new Date(todayEnd)));
        // Return query results
        return getReadQuranRecordsInRange(context, todayStart, todayEnd);
    }

    @NonNull
    public static List<ReadQuranRecord> getWeekRecords(Context context, @IntRange(from = 1, to = 4) int whichWeek) {
        // TODO: 6/19/2021 care of length of week during calculations
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
        AManager.log(TAG, "getWeekRecords: START[%s] | NOW[%s] | END[%s]\n",
                DateUtils.dfDateTimeSNX(true).format(new Date(weekStart)),
                DateUtils.dfDateTimeSNX(true).format(new Date(System.currentTimeMillis())),
                DateUtils.dfDateTimeSNX(true).format(new Date(weekEnd)));
        return getReadQuranRecordsInRange(context, weekStart, weekEnd);
    }

    @NonNull
    public static List<ReadQuranRecord> getMonthRecords(Context context, @IntRange(from = 1, to = 12) int whichMonth) {
        // TODO: 6/19/2021 care of weeks num within given week during calculations
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
        AManager.log(TAG, "getMonthRecords: START[%s] | NOW[%s] | END[%s]\n",
                DateUtils.dfDateTimeSNX(true).format(new Date(monthStart)),
                DateUtils.dfDateTimeSNX(true).format(new Date(System.currentTimeMillis())),
                DateUtils.dfDateTimeSNX(true).format(new Date(monthEnd)));
        return getReadQuranRecordsInRange(context, monthStart, monthEnd);
    }

    public static int getTargetMinutes(Context context) {
        return PrefManager.get(context, KEY_TARGET_MINS, 60);
    }

    public static void setTargetMinutes(Context context, int newTarget) {
        PrefManager.set(context, KEY_TARGET_MINS, newTarget);
    }

}

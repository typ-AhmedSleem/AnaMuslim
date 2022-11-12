/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.managers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import androidx.annotation.Nullable;

import com.typ.muslim.db.LocalDatabase;
import com.typ.muslim.enums.KhatmaPlans;
import com.typ.muslim.interfaces.KhatmaManagerCallback;
import com.typ.muslim.interfaces.SqlExecCallback;
import com.typ.muslim.models.Khatma;
import com.typ.muslim.models.Timestamp;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Contains code that handles operations with Khatma
 */
public class KhatmaManager {

    // fixme: We have a lot of work here. A lot of code pieces needs to be modified

    // Start new khatma -> DONE
    // Get all user khatmas of all time -> FIXME
    // Get all user khatmas between range of time -> FIXME
    // Remove khatma -> FIXME
    // Get active khatma -> DONE
    // Check if user has started any khatma -> DONE
    // Get the fixed khatma completing progress as percentage to (total 100) -> DONE
    // Save new progress -> DONE

    // ======================================= Static Manager Methods =================================================//

    // Statics
    private static final String TAG = "KhatmaManager";
    // Listeners
    private final KhatmaManagerCallback callback;
    // Runtime
    protected LocalDatabase localDB;
    protected Context context;
    protected @Nullable
    Khatma underManageKhatma;

    private KhatmaManager(Context context, @Nullable Khatma khatmaToManage, KhatmaManagerCallback callback) {
        // Prepare runtime
        this.context = context;
        this.underManageKhatma = khatmaToManage;
        this.callback = callback;
        // Send onPrepareManager signal to callback
        if (callback != null) callback.onPrepareManager();
        // Prepare database
        this.localDB = LocalDatabase.getInstance(context);
        // Notify callback about the khatma
        if (callback != null) {
            if (underManageKhatma != null && underManageKhatma.isActive()) {
                callback.onPutUnderManagement(this.underManageKhatma); // Send onPutUnderManagement signal to callback if an active khatma is ready
            } else callback.onNoActiveKhatmaFound(); // Send onActiveNoKhatmaFound signal to callback if no active khatma was found
        }
    }

    /**
     * Creates new khatma in local database and add it as latest created one.
     *
     * @param khatma The Khatma model to be added in local database.
     * @return {@code true} if Khatma was added to database. {@code false} otherwise.
     */
    public static void createNewKhatma(Context context, Khatma khatma, SqlExecCallback<Khatma> callback) {
        try {
            // Execute insert command
            LocalDatabase.getInstance(context).execute(String.format(Locale.ENGLISH, "INSERT INTO %s (%s) VALUES ('%s',%d,%d,%d,%d)",
                    LocalDatabase.TABLE_KHATMA, "` name `,` plan `,` completed_days `,` started_in `,` reminder_in `",
                    khatma.getName(),
                    khatma.getPlan().ordinal(),
                    khatma.getCompletedWerds(),
                    khatma.getStartedIn().toMillis(),
                    khatma.hasReminder() ? khatma.getRemindIn().toMillis() : -1L));
            if (callback != null) callback.onFinish(true, khatma);
        } catch (SQLiteException | NullPointerException e) {
            e.printStackTrace();
            if (callback != null) callback.onFinish(false, khatma);
        }
    }

    // =============================================================================================================//

    /**
     * @return A {@link List<Khatma>} of {@link Khatma} user has started since he started using app to now.
     */
    public static List<Khatma> getAllKhatmas(Context context) {
        // Query local database for all created khatmas
        Cursor cursor = LocalDatabase.getInstance(context).query("SELECT * FROM " + LocalDatabase.TABLE_KHATMA);
        // Fill list with returned data in cursor
        if (cursor != null) {
            List<Khatma> khatmas = new ArrayList<>();
            if (cursor.getCount() == 0) return khatmas; // Empty List.
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                khatmas.add(new Khatma(cursor.getInt(0),
                        cursor.getString(1),
                        KhatmaPlans.valueOf(cursor.getInt(2)),
                        cursor.getInt(3),
                        new Timestamp(cursor.getLong(4)),
                        new Timestamp(cursor.getLong(5))));
                cursor.moveToNext();
            }
            cursor.close();
            return khatmas;
        }
        return null; // Error querying.
    }

    /**
     * Removes Khatma with its given number
     *
     * @return {@code true} if removed successfully. {@code false} if failed.
     */
    public static boolean removeKhatma(Context context, int khatmaNumber) {
        try {
            // Execute remove command
            LocalDatabase.getInstance(context).execute("DELETE FROM " + LocalDatabase.TABLE_KHATMA + " WHERE `khatma_number `=" + khatmaNumber);
            // Executed successfully
            return true;
        } catch (SQLiteException e) {
            e.printStackTrace();
            // Error executing
            return false;
        }
    }

    /**
     * @return The currently last active {@link Khatma} or null if no khatma was found
     */
    public static Khatma getLastActiveKhatma(Context context) {
        Khatma lastActiveKhatma = null;
        // Query local database for any active khatma
        Cursor cursor = LocalDatabase.getInstance(context).query("SELECT * FROM " + LocalDatabase.TABLE_KHATMA + " ORDER BY ` started_in ` DESC LIMIT 1");
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                // Get the latest khatma
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    lastActiveKhatma = new Khatma(
                            cursor.getInt(0),
                            cursor.getString(1),
                            KhatmaPlans.valueOf(cursor.getInt(2)),
                            cursor.getInt(3),
                            new Timestamp(cursor.getLong(4)),
                            cursor.getLong(5) != -1 ? new Timestamp(cursor.getLong(5)) : null);
                    AManager.log(TAG, "getLastActiveKhatma: %s", lastActiveKhatma);
                    // Check if khatma is active or not
                    if (lastActiveKhatma.isActive()) break;
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
        return lastActiveKhatma;
    }

    /**
     * Check if user has any khatma active and not completed.
     *
     * @return {@code true} if user has an active khatma. {@code false} if no active khatma was found.
     */
    public static boolean hasAnyActiveKhatma(Context context) {
        return getLastActiveKhatma(context) != null;
    }

    public static KhatmaManager putUnderManagement(Context context, Khatma khatmaToManage, KhatmaManagerCallback callback) {
        return new KhatmaManager(context, khatmaToManage, callback);
    }

    public static KhatmaManager getInstance(Context context, KhatmaManagerCallback callback) {
        return putUnderManagement(context, KhatmaManager.getLastActiveKhatma(context), callback);
    }

    /**
     * @param rangeStart Timestamp of start point of range.
     * @param rangeEnd   Timestamp of end point of range.
     * @return A {@link List<Khatma>} of {@link Khatma} user has started between the given range.
     */
    public List<Khatma> getKhatmasInRange(Context context, long rangeStart, long rangeEnd) {
        // Query local database for all created khatmas in given range
        Cursor cursor = LocalDatabase.getInstance(context).query("SELECT * FROM " + LocalDatabase.TABLE_KHATMA + " WHERE ` started_in ` BETWEEN " + rangeStart + " AND " + rangeEnd);
        // Fill list with returned data in cursor
        if (cursor != null) {
            List<Khatma> khatmas = new ArrayList<>();
            if (cursor.getCount() == 0) return khatmas; // Empty List.
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                khatmas.add(new Khatma(
                        cursor.getInt(0),
                        cursor.getString(1),
                        KhatmaPlans.valueOf(cursor.getInt(2)),
                        cursor.getInt(3),
                        new Timestamp(cursor.getLong(4)),
                        new Timestamp(cursor.getLong(5))));
                cursor.moveToNext();
            }
            cursor.close();
            return khatmas;
        }
        return null; // Error querying.
    }

    /**
     * @return The currently last active {@link Khatma} or null if no khatma was found
     */
    public Khatma getUnderManagementKhatma() {
        return underManageKhatma;
    }

    public void putUnderManagement(Khatma khatma, boolean notifyCallback) {
        underManageKhatma = khatma;
        if (notifyCallback && callback != null) {
            // Notify callback about the khatma
            if (this.underManageKhatma != null) callback.onPutUnderManagement(this.underManageKhatma); // Send onPutUnderManagement signal to callback if an active khatma is ready
            else callback.onNoActiveKhatmaFound(); // Send onActiveNoKhatmaFound signal to callback if no active khatma was found
        }
    }

    public boolean hasKhatma() {
        return underManageKhatma != null;
    }

    /**
     * Saves the progress in which user has completed the current werd.
     */
    public void saveProgress() {
        try {
            localDB.execute(String.format(Locale.ENGLISH, "UPDATE %s SET ` completed_days ` = %d WHERE `khatma_number ` = %d",
                    LocalDatabase.TABLE_KHATMA,
                    underManageKhatma.getCompletedWerds() + 1,
                    underManageKhatma.getNumber()));
            // Save progress and notify callback
            underManageKhatma.saveProgress();
            if (callback != null) callback.onKhatmaProgressUpdated();
        } catch (SQLiteException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void delete(boolean notifyCallback) {
        boolean wasDeleted = false;
        if (underManageKhatma != null) wasDeleted = KhatmaManager.removeKhatma(context, underManageKhatma.getNumber());
        if (callback != null && notifyCallback && wasDeleted) callback.onNoActiveKhatmaFound();
    }

    public void releaseKhatmaFromManagement(boolean notifyCallback) {
        // Update runtime
        this.underManageKhatma = null;
        // Notify callback if ordered notify it
        if (notifyCallback && this.callback != null) callback.onKhatmaReleased();
    }

}

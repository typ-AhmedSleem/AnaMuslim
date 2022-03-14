/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.managers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Pair;

import androidx.annotation.Nullable;

import com.typ.muslim.app.Constants;
import com.typ.muslim.db.LocalDatabase;
import com.typ.muslim.enums.KhatmaPlans;
import com.typ.muslim.interfaces.KhatmaManagerCallback;
import com.typ.muslim.interfaces.SqlExecCallback;
import com.typ.muslim.models.Khatma;
import com.typ.muslim.models.Khatma.Step;
import com.typ.muslim.models.Timestamp;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Contains code that handles operations with Khatma
 */
public class KhatmaManager {

	// fixme: We have a lot of work here. A lot of code pieces needs to be modified

	// Start new khatma -> FIXME
	// Get all user khatmas of all time -> FIXME
	// Get all user khatmas between range of time -> FIXME
	// Remove khatma -> FIXME
	// Get active khatma -> FIXME
	// Check if user has started any khatma -> FIXME
	// Get the fixed khatma completing progress as percentage to (total 100) -> DONE
	// Save new progress -> TODO

	// ======================================= Static Manager Methods =================================================//

	// Statics
	private static final String TAG = "KhatmaManager";
	// Listeners
	private final KhatmaManagerCallback callback;
	// Runtime
	protected LocalDatabase localDB;
	protected Context context;
	protected Khatma underManageKhatma;

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
			if (this.underManageKhatma != null) callback.onPutUnderManagement(this.underManageKhatma); // Send onPutUnderManagement signal to callback if an active khatma is ready
			else callback.onNoActiveKhatmaFound(); // Send onActiveNoKhatmaFound signal to callback if no active khatma was found
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
					khatma.getCompletedDays(),
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
		Cursor cursor = LocalDatabase.getInstance(context).query("SELECT * FROM " + LocalDatabase.TABLE_KHATMA + " ORDER BY ` started_in ` DESC");
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

	public static Step resolveStep(KhatmaPlans plan, int completedDays) {
		// todo: Code this method to match new code modifications
		return null;
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

	/**
	 * Check if user has any khatma active and not completed.
	 *
	 * @return {@code true} if user has an active khatma. {@code false} if no active khatma was found or has completed.
	 */
	public boolean isKhatmaActive() {
		if (underManageKhatma != null) return !isKhatmaCompleted();
		return false;
	}

	public boolean hasKhatma() {
		return underManageKhatma != null;
	}

	/**
	 * Saves the new progress user has made in the given Khatma.
	 */
	public void saveNewProgress() {
		try {
			localDB.execute(String.format(Locale.ENGLISH, "UPDATE %s SET (` completed_days ` = %d) WHERE ` khatma_number ` = %d",
					LocalDatabase.TABLE_KHATMA,
					underManageKhatma.getCompletedDays() + 1,
					underManageKhatma.getNumber()));
			// Save progress and notify callback
			underManageKhatma.saveProgress();
			if (callback != null) callback.onKhatmaProgressUpdated();
		} catch (SQLiteException | NullPointerException e) {
			e.printStackTrace();
		}
	}

	public boolean delete() {
		if (underManageKhatma == null) return false;
		return KhatmaManager.removeKhatma(context, underManageKhatma.getNumber());
	}

	/**
	 * Completed Percentage <= 100
	 */
	public int getProgressPercentage() {
		// null check
		if (underManageKhatma == null) return 0;
		// Process khatma steps to get percentage
		return Math.min(underManageKhatma.getProgressPercentage(), 100); // <= 100
	}

	public void releaseKhatmaFromManagement(boolean notifyCallback) {
		// Update runtime
		this.underManageKhatma = null;
		// Notify callback if ordered notify it
		if (notifyCallback && this.callback != null) callback.onKhatmaReleased();
	}

	public Khatma.Step getCurrentStep() {
		return underManageKhatma.getCurrentStep();
	}

	public Khatma.Step getNextStep() {
		// TODO: Get next Ayah and Next Surah according to plan and current step
		if (underManageKhatma == null) return null; // No active khatma.
		if (isKhatmaCompleted()) return null; // Khatma hsa completed.
		if (underManageKhatma.getProgressPercentage() == 100) return null; // No next step.
		Khatma.Step currStep = underManageKhatma.getCurrentStep();
		if (currStep == null) return null; // No active khatma or khatma hasn't been prepared yet.
		int nextAyahNumber = 0;
		int nextSurahNumber = 0;
		return new Khatma.Step(getCurrentStep().getEnd().getNumber(),
				getCurrentStep().getEnd().getSurahNumber(),
				nextAyahNumber,
				nextSurahNumber);
	}

	public boolean isKhatmaCompleted() {
		return underManageKhatma.isActive();
	}

	/**
	 * Contains code to calculate [Step & Total Steps]
	 */
	public static class StepCalculator {

		/**
		 * Calculate [Step & Total Steps] for given readingStep.
		 * <p/>
		 * Example:-
		 * INPUT => 1.5 =Means=> 1 Juz2 & 1 Hezb.
		 * OUTPUT => Pair[Step({1} juz2,{1} hizb) | {20} day]
		 */
		public static Pair<Khatma.Step, Integer> calculate(float readingStep) {
			// fixme: Recode this method to match the new modifications in code structure
			try {
				// Calculations
				String[] temp = String.valueOf(readingStep).split("\\.");
				int totalDuration = Math.round(Constants.QURAN_JUZ2S_COUNT / readingStep);
				// Return result
				return Pair.create(new Khatma.Step(1, Integer.parseInt(temp[0]), Integer.parseInt(temp[1]) / 5, 1), totalDuration);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				return null;
			}
		}

	}

}

/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.typ.muslim.managers.AManager;

import java.util.Arrays;

/**
 * Contains necessary code that works with all AnaMuslim databases directly and easily.
 * <p>
 * Holds data of [PrayTracker - ReadQuran - ReadAzkar - Khatmas - MyWerds - ToDoInRamadan - FastingPlans - MuslimLeague- WhenSilentNotificationsAndCalls]
 */
public class LocalDatabase extends SQLiteOpenHelper {

	// TAG
	private static final String TAG = "LocalDatabase";
	// Tables
	public static final String TABLE_PRAYER_TRACKER = "prayer_tracker";
	public static final String TABLE_READ_QURAN = "read_quran";
	public static final String TABLE_READ_AZKAR = "read_azkar";
	public static final String TABLE_KHATMA = "khatmas";
	public static final String TABLE_TELEGRAM = "telegram";
	public static final String TABLE_ZAKAT_SADAKAH = "zakat_sadakah";
	public static final String TABLE_MAWAREETH_CALC = "mawareeth_calc";
	public static final String TABLE_MUSLIM_LEAGUE = "muslim_league";
	public static final String TABLE_COMMUNITY = "muslim_league";
	public static final String TABLE_TO_DO_IN_RAMADAN = "to_do_in_ramadan";
	public static final String TABLE_WERDS = "werds";
	public static final String TABLE_FASTING = "fasting";
	public static final String TABLE_NOTIFS_WHEN_SILENT = "notifs_when_silent";

	// Current Singleton Instance
	protected static LocalDatabase mInstance;

	private LocalDatabase(Context context) {
		super(context, "local.db", null, 3);
		onCreate(getWritableDatabase());
	}

	/**
	 * Gets the current working singleton instance or create new one
	 *
	 * @return The current singleton instance
	 */
	public static LocalDatabase getInstance(Context context) {
		if (mInstance == null) mInstance = new LocalDatabase(context);
		return mInstance;
	}

	@Override
	public void onCreate(SQLiteDatabase localDB) {
		for (String query : Arrays.asList(
				// Khatma Table
				"CREATE TABLE IF NOT EXISTS " + TABLE_KHATMA +
						"  (\"khatma_number \" INTEGER PRIMARY KEY NOT NULL,\n" +
						"  \" name \" VARCHAR,\n" +
						"  \" plan \" INTEGER,\n" +
						"  \" completed_days \" INTEGER,\n" +
						"  \" started_in \" NUMERIC,\n" +
						"  \" reminder_in \" NUMERIC)",
				// Prayer Tracker Table
				"CREATE TABLE IF NOT EXISTS " + TABLE_PRAYER_TRACKER +
						"  (\" what_pray \" INTEGER,\n" +
						"  \" status \" INTEGER,\n" +
						"  \" at_mosque \" BOOLEAN,\n" +
						"  \" pray_time \" NUMERIC,\n" +
						"  \" prayed_in \" NUMERIC,\n" +
						"  \" day_timestamp \" NUMERIC)",
				// Read Quran Table
				"CREATE TABLE IF NOT EXISTS " + TABLE_READ_QURAN +
						"  (\" from_ayah \" INTEGER,\n" +
						"  \" from_surah \" INTEGER,\n" +
						"  \" to_ayah \" INTEGER,\n" +
						"  \" to_surah \" INTEGER,\n" +
						"  \" duration \" INTEGER,\n" +
						"  \" day_timestamp \" NUMERIC)",
				// Telegram Integration Table
				"CREATE TABLE IF NOT EXISTS " + TABLE_TELEGRAM +
						"  (\" bot_token \" VARCHAR,\n" +
						"  \" id \" INTEGER,\n" +
						"  \" first_name \" VARCHAR,\n" +
						"  \" last_name \" VARCHAR,\n" +
						"  \" username \" VARCHAR,\n" +
						"  \" can_join_groups \" BOOLEAN,\n" +
						"  \" can_read_all_groups_msgs \" BOOLEAN,\n" +
						"  \" support_inline_queries \" BOOLEAN)")) {
			localDB.execSQL(query);
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	/**
	 * Runs the provided SQL and returns a {@link Cursor} over the result set.
	 *
	 * @param query the SQL query. The SQL string must not be ; terminated
	 * @return A {@link Cursor} object, which is positioned before the first entry. Note that
	 * {@link Cursor}s are not synchronized, see the documentation for more details.
	 */
	public Cursor query(String query) {
		AManager.log(TAG, "query: " + query);
		return this.getReadableDatabase().rawQuery(query, null);
	}

	/**
	 * Execute a single SQL statement that is NOT a SELECT
	 * or any other SQL statement that returns data.
	 *
	 * @param sqlCommand the SQL statement to be executed. Multiple statements separated by semicolons are not supported.
	 * @throws SQLException if the SQL string is invalid
	 */
	public void execute(String sqlCommand) throws SQLException {
		AManager.log(TAG, "execute: " + sqlCommand);
		getWritableDatabase().execSQL(sqlCommand);
	}

}

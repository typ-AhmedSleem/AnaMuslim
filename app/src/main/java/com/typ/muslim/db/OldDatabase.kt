/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */
package com.typ.muslim.db

import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*

// TODO: Migrate from traditional SQLite db to Room db

/**
 * Contains necessary code that works with all AnaMuslim databases directly and easily.
 *
 *
 * Holds data of [PrayTracker - ReadQuran - ReadAzkar - MyWerds - ToDoInRamadan - FastingPlans - MuslimLeague- WhenSilentNotificationsAndCalls]
 */
class OldDatabase private constructor(context: Context) : SQLiteOpenHelper(context, "local.db", null, 3) {
    init {
        onCreate(writableDatabase)
    }

    override fun onCreate(localDB: SQLiteDatabase) {
        for (query in listOf(// Prayer Tracker Table
            """CREATE TABLE IF NOT EXISTS $TABLE_PRAYER_TRACKER  (" what_pray " INTEGER,
    " status " INTEGER,
    " at_mosque " BOOLEAN,
    " pray_time " NUMERIC,
    " prayed_in " NUMERIC,
    " day_timestamp " NUMERIC)""",  // Read Quran Table
            """CREATE TABLE IF NOT EXISTS $TABLE_READ_QURAN (
    " from_ayah " INTEGER,
    " from_surah " INTEGER,
    " to_ayah " INTEGER,
    " to_surah " INTEGER,
    " duration " INTEGER,
    " day_timestamp " NUMERIC)""",  // Telegram Integration Table
            """CREATE TABLE IF NOT EXISTS $TABLE_TELEGRAM  (" bot_token " VARCHAR,
    " id " INTEGER,
    " first_name " VARCHAR,
    " last_name " VARCHAR,
    " username " VARCHAR,
    " can_join_groups " BOOLEAN,
    " can_read_all_groups_msgs " BOOLEAN,
    " support_inline_queries " BOOLEAN)"""
        )) {
            localDB.execSQL(query)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    /**
     * Runs the provided SQL and returns a [Cursor] over the result set.
     *
     * @param query the SQL query. The SQL string must not be ; terminated
     * @return A [Cursor] object, which is positioned before the first entry. Note that
     * [Cursor]s are not synchronized, see the documentation for more details.
     */
    fun query(query: String): Cursor = readableDatabase.rawQuery(query, null)

    /**
     * Execute a single SQL statement that is NOT a SELECT
     * or any other SQL statement that returns data.
     *
     * @param sqlCommand the SQL statement to be executed. Multiple statements separated by semicolons are not supported.
     * @throws SQLException if the SQL string is invalid
     */
    @Throws(SQLException::class)
    fun execute(sqlCommand: String) {
        writableDatabase.execSQL(sqlCommand)
    }

    companion object {
        // TAG
        private const val TAG = "LocalDatabase"

        // Tables
        const val TABLE_PRAYER_TRACKER = "prayer_tracker"
        const val TABLE_READ_QURAN = "read_quran"
        const val TABLE_READ_AZKAR = "read_azkar"
        const val TABLE_KHATMA = "khatmas"
        const val TABLE_TELEGRAM = "telegram"
        const val TABLE_ZAKAT_SADAKAH = "zakat_sadakah"
        const val TABLE_MAWAREETH_CALC = "mawareeth_calc"
        const val TABLE_MUSLIM_LEAGUE = "muslim_league"
        const val TABLE_COMMUNITY = "muslim_league"
        const val TABLE_TO_DO_IN_RAMADAN = "to_do_in_ramadan"
        const val TABLE_WERDS = "werds"
        const val TABLE_FASTING = "fasting"
        const val TABLE_NOTIFS_WHEN_SILENT = "notifs_when_silent"

        // Current Singleton Instance
        @Volatile
        private var mInstance: OldDatabase? = null

        /**
         * Gets the current working singleton instance or create new one
         *
         * @return The current singleton instance
         */
        @JvmStatic
        fun getInstance(context: Context): OldDatabase? {
            if (mInstance == null) {
                synchronized(OldDatabase::class.java) {
                    if (mInstance == null) mInstance = OldDatabase(context)
                }
            }
            return mInstance
        }
    }
}
/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.db;

import android.content.Context;

import androidx.annotation.NonNull;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class QuranDatabase extends SQLiteAssetHelper {

	// Singleton instance
	private static QuranDatabase instance;

	public QuranDatabase(@NonNull Context context) {
		super(context, "quran.sqlite", null, 11);
	}

	public static QuranDatabase getInstance(Context context) {
		if (instance == null) {
			synchronized (QuranDatabase.class) {
				instance = new QuranDatabase(context);
			}
		}
		return instance;
	}

}

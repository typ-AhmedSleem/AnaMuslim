/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.models;

import android.database.Cursor;

import androidx.annotation.Nullable;

public class EnhancedCursor {

	@Nullable
	private final Cursor cursor;

	public EnhancedCursor(@Nullable Cursor cursor) {
		this.cursor = cursor;
	}

	public boolean isEmpty() {
		if (cursor == null) return true;
		else return cursor.getCount() <= 0;
	}

	public void iterate(Runnable action) {
		if (cursor == null) return;
		if (cursor.getCount() <= 0) return;
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			action.run();
			cursor.moveToNext();
		}
	}

}

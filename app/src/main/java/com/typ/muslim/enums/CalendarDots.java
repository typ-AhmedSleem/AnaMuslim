/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.enums;

import android.content.Context;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;

import com.typ.muslim.R;
import com.typ.muslim.managers.ResMan;

public enum CalendarDots {

	RED(R.color.red, R.color.white),
	BLUE(R.color.cardBackground3, R.color.nextPrayCardSurfaceStartColor),
	CYAN(R.color.cardBackground1, R.color.nextPrayCardSurfaceStartColor),
	GREEN(R.color.green, R.color.white),
	YELLOW(R.color.yellow, R.color.nextPrayCardSurfaceStartColor),
	ORANGE(R.color.orange, R.color.nextPrayCardSurfaceStartColor),
	PURPLE(R.color.color_1, R.color.nextPrayCardSurfaceStartColor);

	public final @ColorRes int surfaceColorRes;
	public final @ColorRes int onSurfaceColorRes;

	CalendarDots(int surfaceColorRes, int onSurfaceColorRes) {
		this.surfaceColorRes = surfaceColorRes;
		this.onSurfaceColorRes = onSurfaceColorRes;
	}

	public static CalendarDots of(int ordinal) {
		if (ordinal == RED.ordinal()) return RED;
		else if (ordinal == BLUE.ordinal()) return BLUE;
		else if (ordinal == CYAN.ordinal()) return CYAN;
		else if (ordinal == YELLOW.ordinal()) return YELLOW;
		else if (ordinal == ORANGE.ordinal()) return ORANGE;
		else if (ordinal == PURPLE.ordinal()) return PURPLE;
		return GREEN;
	}

	public @ColorInt
	int getSurfaceColor(Context context) {
		return ResMan.getColor(context, surfaceColorRes);
	}

	public @ColorInt
	int getOnSurfaceColor(Context context) {
		return ResMan.getColor(context, onSurfaceColorRes);
	}
}

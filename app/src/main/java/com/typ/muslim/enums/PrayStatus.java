/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.enums;

public enum PrayStatus {

	UP_COMING,
	ON_TIME,
	DELAYED,
	FORGOT;

	public static PrayStatus valueOf(int ordinal) {
		return ordinal == 0 ? UP_COMING : ordinal == 1 ? ON_TIME : ordinal == 2 ? DELAYED : FORGOT;
	}

}

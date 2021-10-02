/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.enums;

public enum SoMNotifyMethod {
	SOUND,
	NOTIFICATION;

	public static SoMNotifyMethod of(int ordinal) {
		for (SoMNotifyMethod freq : values()) if (freq.ordinal() == ordinal) return freq;
		return SOUND;
	}
}

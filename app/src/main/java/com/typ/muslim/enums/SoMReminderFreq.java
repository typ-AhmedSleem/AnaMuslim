/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.enums;

import androidx.annotation.IntRange;

public enum SoMReminderFreq {

	EVERY_15MIN(15),
	EVERY_30MIN(30),
	EVERY_1HR(60),
	EVERY_2HRS(120);
	private final int frequency;

	SoMReminderFreq(int frequency) {
		this.frequency = frequency;
	}

	public static SoMReminderFreq of(int ordinal) {
		for (SoMReminderFreq freq : values()) if (freq.ordinal() == ordinal) return freq;
		return EVERY_30MIN;
	}

	/**
	 * Frequency in minutes
	 */
	@IntRange(from = 15, to = 120)
	public int getFrequency() {
		return frequency;
	}
}

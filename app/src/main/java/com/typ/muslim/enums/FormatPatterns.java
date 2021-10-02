/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.enums;

import androidx.annotation.NonNull;

import com.typ.muslim.models.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Locale;

public enum FormatPatterns {

	/**
	 * @return Pattern [hh:mm aa]. eg: 3:25 pm
	 */
	TIME12SX("hh:mm aa"),

	/**
	 * @return Pattern [hh:mm]. eg: 3:25
	 */
	TIME12NSX("hh:mm"),

	/**
	 * @return Pattern [HH:mm]. eg: 15:30
	 */
	TIME24("HH:mm"),

	/**
	 * @return Pattern [dd/MM/yyyy]. eg: 01/08/2001
	 */
	DATE_SHORT("dd/MM/yyyy"),

	/**
	 * @return Pattern [dd MMM yyyy]. eg: 01 Aug 2001
	 */
	DATE_NORMAL("dd MMM yyyy"),

	/**
	 * @return Pattern [dd MMM yyyy]. eg: 01 Aug 2001
	 */
	DATE_FULL("dd MMMM yyyy"),

	/**
	 * @return Pattern [dd/MM/yyyy hh:mm aa]. eg: 01/08/2001 12:03 am
	 */
	DATETIME_NORMAL("dd/MM/yyyy hh:mm aa"),

	/**
	 * @return Pattern [dd MMM yyyy hh:mm aa]. eg: 01 Aug 2001 12:03 am
	 */
	DATETIME_FULL("dd MMM yyyy hh:mm aa");

	public static FormatPatterns valueOf(int ordinal) {
		FormatPatterns formatPattern = null;
		for (FormatPatterns fp : values()) {
			if (fp.ordinal() == ordinal) {
				formatPattern = fp;
				break;
			}
		}
		return formatPattern;
	}

	final String pattern;

	FormatPatterns(String pattern) {
		this.pattern = pattern;
	}

	@NonNull
	public String getPattern() {
		return pattern;
	}

	@NonNull
	public String format(Timestamp timestamp) {
		return new SimpleDateFormat(pattern, Locale.getDefault()).format(timestamp.asDate());
	}

}

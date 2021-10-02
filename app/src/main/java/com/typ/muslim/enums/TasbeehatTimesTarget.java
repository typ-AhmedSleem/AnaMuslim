/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.enums;

public enum TasbeehatTimesTarget {

	TIMES_33(33),
	TIMES_66(66),
	TIMES_99(99),
	TIMES_INFINITE(-1);

	private final int howMany;

	TasbeehatTimesTarget(int howMany) {
		this.howMany = howMany;
	}

	public static TasbeehatTimesTarget ofTimes(int howMany) {
		if (howMany == TIMES_33.howMany()) return TIMES_33;
		else if (howMany == TIMES_66.howMany()) return TIMES_66;
		else if (howMany == TIMES_99.howMany()) return TIMES_99;
		else return TIMES_INFINITE;
	}

	public static TasbeehatTimesTarget valueOf(int ordinal) {
		if (ordinal == TIMES_33.ordinal()) return TIMES_33;
		else if (ordinal == TIMES_66.ordinal()) return TIMES_66;
		else if (ordinal == TIMES_99.ordinal()) return TIMES_99;
		else return TIMES_INFINITE;
	}

	/**
	 * @return times count for each tasbeeh item or -1 which means infinite
	 */
	public int howMany() {
		return howMany;
	}
}

/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.enums;

import androidx.annotation.StringRes;

import com.typ.muslim.R;

public enum TrackerRange {

	TODAY(R.string.today),
	THIS_WEEK(R.string.this_week),
	THIS_MONTH(R.string.this_month);

	@StringRes int name;

	TrackerRange(@StringRes int name) {
		this.name = name;
	}

	public static TrackerRange valueOf(int ordinal) {
		return ordinal == 0 ? TODAY : ordinal == 1 ? THIS_WEEK : THIS_MONTH;
	}

	public int getName() {
		return name;
	}

}

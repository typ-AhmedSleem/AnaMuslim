/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.enums;

import androidx.annotation.LayoutRes;

/**
 * [MISSING CORRECT ONGOING NOTIFICATION LAYOUT RES IDs]
 */
public enum OngoingNotificationStyle {

	/**
	 * Pre-defined Styles with its layout res id
	 */
	STYLE1(0),
	STYLE2(0),
	STYLE3(0);

	private final @LayoutRes int layoutResId;

	OngoingNotificationStyle(@LayoutRes int layoutResId) {
		this.layoutResId = layoutResId;
	}

	public static OngoingNotificationStyle of(@LayoutRes int layoutResId) {
		for (OngoingNotificationStyle style : values()) if (style.layoutResId == layoutResId) return style;
		return STYLE1;
	}

	public int getLayoutResId() {
		return layoutResId;
	}

}

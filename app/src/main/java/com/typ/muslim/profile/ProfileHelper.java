/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.profile;

import androidx.annotation.DrawableRes;

import com.typ.muslim.R;

public class ProfileHelper {

	/**
	 * @return The right default profile avatar photo caring of user gender
	 * */
	@DrawableRes
	public static int getDefaultProfileAvatar(boolean isUserMale) {
		if (isUserMale) return R.drawable.bg_header3;
		else return R.drawable.bg_header3;
	}
}

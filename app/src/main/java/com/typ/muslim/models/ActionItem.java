/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.models;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

public class ActionItem {

	public final int id;
	public final @DrawableRes int icon;
	public final @StringRes int text;

	public ActionItem(int id, int icon, int text) {
		this.id = id;
		this.icon = icon;
		this.text = text;
	}

}

/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui;

import static com.typ.muslim.utils.DisplayUtils.dp2px;
import static com.typ.muslim.utils.DisplayUtils.px2dp;
import static com.typ.muslim.utils.DisplayUtils.px2sp;
import static com.typ.muslim.utils.DisplayUtils.sp2px;

import android.content.Context;

import androidx.annotation.Px;

public class Dimension {

	// Context used for converting
	private final Context context;
	private PixelType type;
	private float value;

	public Dimension(Context context) {
		this.context = context;
		this.type = PixelType.REAL_PIXEL;
		this.value = 0f;
	}

	public Dimension(Context context, PixelType pixelType, float value) {
		this.context = context;
		this.type = PixelType.REAL_PIXEL;
		this.value = 0f;
	}

	public void set(PixelType pixelType, float newValue) {
		this.type = pixelType;
		this.value = newValue;
	}

	@Px
	public int toPixels() {
		if (type == PixelType.DP) return dp2px(context, value);
		else if (type == PixelType.SP) return sp2px(context, value);
		else return (int) value;
	}

	public float toDPs() {
		if (type == PixelType.REAL_PIXEL) return px2dp(context, value);
		else if (type == PixelType.SP) return px2dp(context, sp2px(context, value));
		else return value;
	}

	public float toSPs() {
		if (type == PixelType.REAL_PIXEL) return px2sp(context, value);
		else if (type == PixelType.DP) return px2sp(context, dp2px(context, value));
		else return value;
	}

	public enum PixelType {
		REAL_PIXEL,
		DP,
		SP,
//		RAW_PIXEL
	}
}

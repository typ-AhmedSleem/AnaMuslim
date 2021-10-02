/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.utils;

public class MathUtils {

	public static float fixAngle(float angle) {
		return angle > 360 ? angle - 360 : angle < 0 ? angle + 360 : angle;
	}

}

/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.utils;

import java.util.List;

public class ListUtils {

	public static <T> String toString(List<T> list) {
		StringBuilder str = new StringBuilder("{");
		for (T item : list) str.append(item).append(",");
		return str.append("}").toString();
	}

}

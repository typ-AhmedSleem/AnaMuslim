/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.utils;

import com.typ.muslim.libs.easyjava.data.EasyList;
import com.typ.muslim.libs.easyjava.interfaces.EasyListCounter;

import java.util.List;

/**
 * Contains some useful code to count all items within nested lists to beautify code appearance
 */
public class Counter {

	public static int countItems(List<?> list) {
		// Some checks
		if (list == null) return 0;
		if (list.isEmpty()) return 0;
		// Start counting
		int count = 0;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) instanceof List<?>) count += countItems((List<?>) list.get(i));
			else count++;
		}
		return count;
	}

	@SafeVarargs
	public static <T> int countItems(T... items) {
		// Some checks
		if (items == null) return 0;
		if (items.length == 0) return 0;
		// Start counting
		int count = 0;
		for (T item : items) {
			if (item instanceof Object[]) count += countItems(item);
			else count++;
		}
		return count;
	}

	public static <T> int countValues(List<T> list, EasyListCounter<T> counter) {
		if (counter == null) return -1;
		if (list == null) return -1;
		if (list.isEmpty()) return -1;
		if (list instanceof EasyList) return countValues((EasyList<T>) list, counter);
		int total = 0;
		for (T item : list) total += counter.count(item);
		return total;
	}

	public static <T> int countValuesNested(List<T> list, EasyListCounter<T> counter) {
		if (counter == null) return 0;
		if (list == null) return 0;
		if (list.isEmpty()) return 0;
		if (list instanceof EasyList) return ((EasyList<T>) list).countValues(counter);
		int total = 0;
		for (T item : list) {
			if (item instanceof List) countValues((List<T>) item, counter);
			else total += counter.count(item);
		}
		return total;
	}

}

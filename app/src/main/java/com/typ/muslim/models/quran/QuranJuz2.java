/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.models.quran;

import androidx.annotation.IntRange;

import java.io.Serializable;

public class QuranJuz2 implements Serializable {

	private final @IntRange(from = 1, to = 30) int number;
	private final QuranSurahPart start;
	private final QuranSurahPart end;

	public QuranJuz2(@IntRange(from = 1, to = 30) int number, QuranSurahPart start, QuranSurahPart end) {
		this.number = number;
		this.start = start;
		this.end = end;
	}

	public QuranJuz2(@IntRange(from = 1, to = 30) int number,
	                 @IntRange(from = 1, to = 114) int fromSurahNumber,
	                 @IntRange(from = 1, to = 286) int fromAyahNumber,
	                 @IntRange(from = 1, to = 114) int toSurahNumber,
	                 @IntRange(from = 1, to = 286) int toAyahNumber) {
		this(number,
		     new QuranSurahPart(fromSurahNumber, fromAyahNumber),
		     new QuranSurahPart(toSurahNumber, toAyahNumber));
	}

	public int getNumber() {
		return number;
	}

	public QuranSurahPart getStart() {
		return start;
	}

	public QuranSurahPart getEnd() {
		return end;
	}

	@Override public String toString() {
		return "QuranJuz2{" +
		       "number=" + number +
		       ", start=" + start +
		       ", end=" + end +
		       '}';
	}
}

/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.models.quran;

import android.content.Context;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.typ.muslim.core.AnaMuslimApp;
import com.typ.muslim.managers.QuranProvider;

import java.io.Serializable;
import java.util.List;

public class QuranSurah implements Serializable {

	private final @IntRange(from = 1, to = 114) int number;
	private final @IntRange(from = 1, to = 286) int totalAyat;
	private final boolean isMakkian;
	private List<QuranAyah> ayat;

	public QuranSurah(@IntRange(from = 1, to = 114) int number,
	                  @IntRange(from = 1, to = 286) int totalAyat,
	                  boolean isMakkian) {
		this.number = number;
		this.totalAyat = totalAyat;
		this.isMakkian = isMakkian;
	}

	public int getNumber() {
		return number;
	}

	@NonNull
	public String getName(Context context) {
		return QuranProvider.getSurahName(context, this.number);
	}

	public int getTotalAyat() {
		return totalAyat;
	}

	public boolean isMakkian() {
		return isMakkian;
	}

	public List<QuranAyah> getAyat(Context context) {
		if (ayat == null) this.ayat = QuranProvider.getAyatOfSurah(context,this.number);
		return ayat;
	}

	@Override public String toString() {
		return "QuranSurah{" +
		       "number=" + number +
		       ", totalAyat=" + totalAyat +
		       ", isMakkian=" + isMakkian +
		       ", ayat=" + ayat +
		       '}';
	}
}

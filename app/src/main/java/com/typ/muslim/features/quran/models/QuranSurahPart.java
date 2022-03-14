/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2022.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.features.quran.models;

import com.typ.muslim.features.quran.Quran;

/**
 * Used in Juz2 if a part of a Surah is on a Juz2 and the other part of it is on an another Juz2
 */
public class QuranSurahPart {

	private final int surahNumber;
	private final int startAyah;

	public QuranSurahPart(int surahNumber, int startAyah) {
		this.surahNumber = surahNumber;
		this.startAyah = startAyah;
	}

	public int getSurahNumber() {
		return surahNumber;
	}

	public int getStartAyah() {
		return startAyah;
	}

	public int getSurahAyatCount() {
        return Quran.getAyatCountInSurah(surahNumber);
    }

	@Override public String toString() {
		return "QuranSurahPart{" +
		       "surahNumber=" + surahNumber +
		       ", startAyah=" + startAyah +
		       ", surahAyatCount=" + getSurahAyatCount() +
		       '}';
	}

}

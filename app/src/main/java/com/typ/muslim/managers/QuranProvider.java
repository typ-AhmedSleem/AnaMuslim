/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.managers;

import android.content.Context;
import android.database.Cursor;

import androidx.annotation.IntRange;

import com.typ.muslim.R;
import com.typ.muslim.db.QuranDatabase;
import com.typ.muslim.models.quran.QuranAyah;
import com.typ.muslim.models.quran.QuranJuz2;
import com.typ.muslim.models.quran.QuranSurah;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains necessary code to handle any operation in Quran feature
 */
public class QuranProvider {

	// Statics
	private static final String TAG = "QuranProvider";
	private static final QuranJuz2[] QURAN_JUZ2S;
	private static final QuranSurah[] QURAN_SURAHS;

	static {
		// Quran Juz2s
		QURAN_JUZ2S = new QuranJuz2[]{
				new QuranJuz2(1, 1, 1, 2, 141),
				new QuranJuz2(2, 2, 142, 2, 252),
				new QuranJuz2(3, 2, 253, 3, 91),
				new QuranJuz2(4, 3, 92, 4, 23),
				new QuranJuz2(5, 4, 24, 4, 147),
				new QuranJuz2(6, 4, 148, 5, 82),
				new QuranJuz2(7, 5, 83, 6, 110),
				new QuranJuz2(8, 6, 111, 7, 87),
				new QuranJuz2(9, 7, 88, 8, 40),
				new QuranJuz2(10, 8, 41, 9, 93),
				new QuranJuz2(11, 9, 94, 11, 5),
				new QuranJuz2(12, 11, 6, 12, 52),
				new QuranJuz2(13, 12, 53, 14, 52),
				new QuranJuz2(14, 15, 1, 16, 128),
				new QuranJuz2(15, 17, 1, 18, 74),
				new QuranJuz2(16, 18, 75, 20, 135),
				new QuranJuz2(17, 21, 1, 22, 78),
				new QuranJuz2(18, 23, 1, 25, 20),
				new QuranJuz2(19, 25, 20, 27, 55),
				new QuranJuz2(20, 27, 56, 29, 45),
				new QuranJuz2(21, 29, 46, 33, 30),
				new QuranJuz2(22, 33, 31, 36, 27),
				new QuranJuz2(23, 36, 28, 39, 31),
				new QuranJuz2(24, 39, 32, 41, 46),
				new QuranJuz2(25, 41, 47, 45, 32),
				new QuranJuz2(26, 45, 33, 51, 30),
				new QuranJuz2(27, 51, 31, 57, 29),
				new QuranJuz2(28, 58, 1, 66, 12),
				new QuranJuz2(29, 67, 1, 77, 50),
				new QuranJuz2(30, 78, 1, 114, 6)};
		// Quran Surahs
		QURAN_SURAHS = new QuranSurah[]{
				new QuranSurah(1, 7, true),
				new QuranSurah(2, 286, false),
				new QuranSurah(3, 200, false),
				new QuranSurah(4, 176, false),
				new QuranSurah(5, 120, false),
				new QuranSurah(6, 165, true),
				new QuranSurah(7, 206, true),
				new QuranSurah(8, 75, false),
				new QuranSurah(9, 129, false),
				new QuranSurah(10, 109, true),
				new QuranSurah(11, 123, true),
				new QuranSurah(12, 111, true),
				new QuranSurah(13, 43, false),
				new QuranSurah(14, 52, true),
				new QuranSurah(15, 99, true),
				new QuranSurah(16, 128, true),
				new QuranSurah(17, 111, true),
				new QuranSurah(18, 110, true),
				new QuranSurah(19, 98, true),
				new QuranSurah(20, 135, true),
				new QuranSurah(21, 112, true),
				new QuranSurah(22, 78, false),
				new QuranSurah(23, 118, true),
				new QuranSurah(24, 64, false),
				new QuranSurah(25, 77, true),
				new QuranSurah(26, 227, true),
				new QuranSurah(27, 93, true),
				new QuranSurah(28, 88, true),
				new QuranSurah(29, 69, true),
				new QuranSurah(30, 60, true),
				new QuranSurah(31, 34, true),
				new QuranSurah(32, 30, true),
				new QuranSurah(33, 73, false),
				new QuranSurah(34, 54, true),
				new QuranSurah(35, 45, true),
				new QuranSurah(36, 83, true),
				new QuranSurah(37, 182, true),
				new QuranSurah(38, 88, true),
				new QuranSurah(39, 75, true),
				new QuranSurah(40, 85, true),
				new QuranSurah(41, 54, true),
				new QuranSurah(42, 53, true),
				new QuranSurah(43, 89, true),
				new QuranSurah(44, 59, true),
				new QuranSurah(45, 37, true),
				new QuranSurah(46, 35, true),
				new QuranSurah(47, 38, false),
				new QuranSurah(48, 29, false),
				new QuranSurah(49, 18, false),
				new QuranSurah(50, 45, true),
				new QuranSurah(51, 60, true),
				new QuranSurah(52, 49, true),
				new QuranSurah(53, 62, true),
				new QuranSurah(54, 55, true),
				new QuranSurah(55, 78, false),
				new QuranSurah(56, 96, true),
				new QuranSurah(57, 29, false),
				new QuranSurah(58, 22, false),
				new QuranSurah(59, 24, false),
				new QuranSurah(60, 13, false),
				new QuranSurah(61, 14, false),
				new QuranSurah(62, 11, false),
				new QuranSurah(63, 11, false),
				new QuranSurah(64, 18, false),
				new QuranSurah(65, 12, false),
				new QuranSurah(66, 12, false),
				new QuranSurah(67, 30, true),
				new QuranSurah(68, 52, true),
				new QuranSurah(69, 52, true),
				new QuranSurah(70, 44, true),
				new QuranSurah(71, 28, true),
				new QuranSurah(72, 28, true),
				new QuranSurah(73, 20, true),
				new QuranSurah(74, 56, true),
				new QuranSurah(75, 40, true),
				new QuranSurah(76, 31, false),
				new QuranSurah(77, 50, true),
				new QuranSurah(78, 40, true),
				new QuranSurah(79, 46, true),
				new QuranSurah(80, 42, true),
				new QuranSurah(81, 29, true),
				new QuranSurah(82, 19, true),
				new QuranSurah(83, 36, true),
				new QuranSurah(84, 25, true),
				new QuranSurah(85, 22, true),
				new QuranSurah(86, 17, true),
				new QuranSurah(87, 19, true),
				new QuranSurah(88, 26, true),
				new QuranSurah(89, 30, true),
				new QuranSurah(90, 20, true),
				new QuranSurah(91, 15, true),
				new QuranSurah(92, 21, true),
				new QuranSurah(93, 11, true),
				new QuranSurah(94, 8, true),
				new QuranSurah(95, 8, true),
				new QuranSurah(96, 19, true),
				new QuranSurah(97, 5, true),
				new QuranSurah(98, 8, false),
				new QuranSurah(99, 8, false),
				new QuranSurah(100, 11, true),
				new QuranSurah(101, 11, true),
				new QuranSurah(102, 8, true),
				new QuranSurah(103, 3, true),
				new QuranSurah(104, 9, true),
				new QuranSurah(105, 5, true),
				new QuranSurah(106, 4, true),
				new QuranSurah(107, 7, true),
				new QuranSurah(108, 3, true),
				new QuranSurah(109, 6, true),
				new QuranSurah(110, 3, true),
				new QuranSurah(111, 5, true),
				new QuranSurah(112, 4, false),
				new QuranSurah(113, 5, true),
				new QuranSurah(114, 6, true)};
	}

	public static String getSurahName(Context context, int number) {
		return AMRes.getStringArray(context, R.array.QuranSurahNames)[number - 1];
	}

	public static QuranSurah getSurah(@IntRange(from = 1, to = 114) int number) {
		return QURAN_SURAHS[number - 1];
	}

	public static List<QuranAyah> getAyatOfSurah(Context context, int surahNum) {
		// Get QuranDB instance and query ayat of surah from it
		QuranDatabase qdb = QuranDatabase.getInstance(context);
		Cursor cursor = qdb.getReadableDatabase().rawQuery("SELECT AyaNum,AyaDiac FROM Quran where SoraNum = " + surahNum, null);
		// Populate ayat to list
		List<QuranAyah> ayat = new ArrayList<>();
		if (cursor != null) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				ayat.add(new QuranAyah(cursor.getInt(0), surahNum, cursor.getString(1)));
				cursor.moveToNext();
			}
			cursor.close();
		}
		AManager.log(TAG, "getAyatOfSurah: Surah[%d|%s] | AyatCount[%d]", surahNum, getSurahName(context, surahNum), ayat.size());
		return ayat;
	}

	public static int getAyatCountInSurah(int number) {
		return getSurah(number).getTotalAyat();
	}

	public static QuranAyah getAyahInSurah(Context context, int ayahNumber, int surahNumber) {
		return getSurah(surahNumber).getAyat(context).get(ayahNumber - 1);
	}

	public static QuranJuz2 getJuz2(@IntRange(from = 1, to = 30) int juz2Number) {
		return QURAN_JUZ2S[juz2Number - 1];
	}

}

/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2022.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.features.quran;

import android.content.Context;

import com.typ.muslim.features.quran.models.QuranAyah;

import java.util.List;

public class QuranSurah {

    private final int number;
    private final int totalAyat;
    private final boolean isMakkian;
    private List<QuranAyah> ayat = null;

    public QuranSurah(int number, int totalAyat, boolean isMakkian) {
        this.number = number;
        this.totalAyat = totalAyat;
        this.isMakkian = isMakkian;
    }

    public String getName(Context context) {
        return Quran.getSurahName(context, this.number);
    }

    public int getNumber() {
        return number;
    }

    public int getTotalAyat() {
        return totalAyat;
    }

    public boolean isMakkian() {
        return isMakkian;
    }

    public List<QuranAyah> getAyat(Context context) {
        if (ayat == null) ayat = Quran.getAyatOfSurah(context, this.number);
        return ayat;
    }

    @Override
    public String toString() {
        return "QuranSurah2{" +
                "number=" + number +
                ", totalAyat=" + totalAyat +
                ", isMakkian=" + isMakkian +
                ", ayat=" + ayat +
                '}';
    }

}

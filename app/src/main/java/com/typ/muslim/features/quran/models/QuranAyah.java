/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2022.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.features.quran.models;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.typ.muslim.features.quran.Quran;
import com.typ.muslim.interfaces.Exportable;

import java.io.Serializable;
import java.util.Locale;

public class QuranAyah implements Serializable, Exportable<String> {

    private final @IntRange(from = 1, to = 114)
    int surah;
    private final @IntRange(from = 1, to = 286)
    int number;
    private @Nullable
    String content;

    public QuranAyah(@IntRange(from = 1, to = 286) int number, @IntRange(from = 1, to = 114) int surah) {
        this(number, surah, null);
    }

    public QuranAyah(@IntRange(from = 1, to = 286) int number, @IntRange(from = 1, to = 114) int surah, @Nullable String content) {
        this.number = number;
        this.surah = surah;
        this.content = content;
    }

    public static QuranAyah of(Context c, @IntRange(from = 1, to = 286) int ayahNumber, @IntRange(from = 1, to = 114) int surahNumber) {
        return Quran.getAyahInSurah(c, ayahNumber, surahNumber);
    }

    public int getNumber() {
        return number;
    }

    public int getSurahNumber() {
        return surah;
    }

    public QuranSurah getSurah() {
        return Quran.getSurah(this.surah);
    }

    @Override
    public String export() {
        return String.format(Locale.ENGLISH, "%d,%d", number, surah);
    }

    @NonNull
    public String getContent(Context context) {
        if (TextUtils.isEmpty(content) && context != null) {
            this.content = Quran.getAyahInSurah(context, number, surah).content;
        }
        assert content != null;
        return content;
    }

    @NonNull
    @Override
    public String toString() {
        return "QuranAyah{" +
                "number=" + number +
                ", surah=" + surah +
                ", content='" + content + '\'' +
                '}';
    }

    public String toString(Context context) {
        return "QuranAyah{" +
                "number=" + number +
                ", surah=" + getSurah() +
                ", content='" + getContent(context) + '\'' +
                '}';
    }

}

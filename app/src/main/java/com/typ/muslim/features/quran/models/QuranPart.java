/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2022.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.features.quran.models;

import androidx.annotation.IntRange;

import java.io.Serializable;

public class QuranPart implements Serializable {

    private final @IntRange(from = 1, to = 30)
    int number;
    private final QuranAyah start;
    private final QuranAyah end;

    public QuranPart(@IntRange(from = 1, to = 30) int number, QuranAyah start, QuranAyah end) {
        this.number = number;
        this.start = start;
        this.end = end;
    }

    public QuranPart(@IntRange(from = 1, to = 30) int number,
                     @IntRange(from = 1, to = 114) int fromSurahNumber,
                     @IntRange(from = 1, to = 286) int fromAyahNumber,
                     @IntRange(from = 1, to = 114) int toSurahNumber,
                     @IntRange(from = 1, to = 286) int toAyahNumber) {
        this(number,
                new QuranAyah(fromAyahNumber, fromSurahNumber),
                new QuranAyah(toAyahNumber, toSurahNumber));
    }

    public int getNumber() {
        return number;
    }

    public QuranAyah getStart() {
        return start;
    }

    public QuranAyah getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "QuranJuz2{" +
                "number=" + number +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}

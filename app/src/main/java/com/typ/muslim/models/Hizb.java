/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Hizb implements Serializable {

    private int surahNumber;
    private int startAyah;
    private int endAyah;

    public Hizb(int surahNumber, int startAyah, int endAyah) {
        this.surahNumber = surahNumber;
        this.startAyah = startAyah;
        this.endAyah = endAyah;
    }

    public int getSurahNumber() {
        return surahNumber;
    }

    public Hizb setSurahNumber(int surahNumber) {
        this.surahNumber = surahNumber;
        return this;
    }

    public int getStartAyah() {
        return startAyah;
    }

    public Hizb setStartAyah(int startAyah) {
        this.startAyah = startAyah;
        return this;
    }

    public int getEndAyah() {
        return endAyah;
    }

    public Hizb setEndAyah(int endAyah) {
        this.endAyah = endAyah;
        return this;
    }

    @NonNull
    @Override public String toString() {
        return "Hizb{" +
                "surahNumber=" + surahNumber +
                "startAyah=" + startAyah +
                ", endAyah=" + endAyah +
                '}';
    }
}

/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Juz2 implements Serializable {

    private int number;
    private int startHizbNumber;
    private int endHizbNumber;

    public Juz2(int number, int startHizbNumber, int endHizbNumber) {
        this.number = number;
        this.startHizbNumber = startHizbNumber;
        this.endHizbNumber = endHizbNumber;
    }

    public int getNumber() {
        return number;
    }

    public Juz2 setNumber(int number) {
        this.number = number;
        return this;
    }

    public int getStartHizbNumber() {
        return startHizbNumber;
    }

    public Juz2 setStartHizbNumber(int startHizbNumber) {
        this.startHizbNumber = startHizbNumber;
        return this;
    }

    public int getEndHizbNumber() {
        return endHizbNumber;
    }

    public Juz2 setEndHizbNumber(int endHizbNumber) {
        this.endHizbNumber = endHizbNumber;
        return this;
    }

    public int getHizbsCount() {
        return this.endHizbNumber - this.startHizbNumber;
    }

    @NonNull
    @Override public String toString() {
        return "Juz2{" +
                "number=" + number +
                ", startHizbNumber=" + startHizbNumber +
                ", endHizbNumber=" + endHizbNumber +
                ", hizbsCount=" + getHizbsCount() +
                '}';
    }
}

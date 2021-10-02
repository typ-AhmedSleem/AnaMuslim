/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.models;

import androidx.annotation.NonNull;

import com.typ.muslim.utils.DateUtils;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Model class represents a single ReadQuran record
 */
public class ReadQuranRecord implements Serializable {

    // TODO: 2/21/21 Ignore the piece if duration is less than minutes

    private final int fromAyah;
    private final int fromSurah;
    private final int toAyah;
    private final int toSurah;
    /**
     * Duration in minutes
     */
    private final int duration;
    private final long when;

    /**
     * Constructor of ReadQuranRecord
     *
     * @param fromAyah  First Ayah of the page user started reading from.
     * @param fromSurah Surah index user started reading from.
     * @param toAyah    First Ayah of the page that user stopped at.
     * @param toSurah   Surah index user stopped at.
     * @param duration  Duration of reading Quran in Minutes.
     * @param when      Timestamp of the daytime this record finished in.
     */
    public ReadQuranRecord(int fromAyah, int fromSurah, int toAyah, int toSurah, int duration, long when) {
        this.fromAyah = fromAyah;
        this.fromSurah = fromSurah;
        this.toAyah = toAyah;
        this.toSurah = toSurah;
        this.duration = duration;
        this.when = when;
    }

    public int getFromSurah() {
        return fromSurah;
    }

    public int getFromAyah() {
        return fromAyah;
    }

    public int getToSurah() {
        return toSurah;
    }

    public int getToAyah() {
        return toAyah;
    }

    public int getDuration() {
        return duration;
    }

    public long getWhen() {
        return when;
    }

    @NonNull
    @Override
    public String toString() {
        return "ReadQuranRecord{" +
               "fromSurah=" + fromSurah +
               ", fromJuz2=" + fromAyah +
               ", toSurah=" + toSurah +
               ", toJuz2=" + toAyah +
               ", duration=" + TimeUnit.SECONDS.toMinutes(duration) +
               ", when=" + DateUtils.dfDateTimeSNX(true).format(when) +
               '}';
    }
}

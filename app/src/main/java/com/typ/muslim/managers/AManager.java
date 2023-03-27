/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.managers;

import static android.content.Context.CONNECTIVITY_SERVICE;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import com.typ.muslim.R;
import com.typ.muslim.app.Keys;
import com.typ.muslim.enums.FormatPatterns;
import com.typ.muslim.models.AllahName;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kotlin.random.Random;

/**
 * Contains code that handles many things allover the app
 */
public abstract class AManager {

    private static final String ANAMUSLIM_TAG = "AnaMuslim";
    private static final String TAG = "AManager";

    AManager() {
    }

    /**
     * Logs the given message in Logger
     */
    public static void log(String TAG, Object what) {
        Log.i(ANAMUSLIM_TAG + ":" + TAG, String.valueOf(what));
    }

    /**
     * Logs the given message in Logger
     */
    public static void log(String TAG, String formattedMsg, Object... what) {
        Log.i(ANAMUSLIM_TAG + ":" + TAG, String.format(Locale.ENGLISH, formattedMsg, what));
    }

    /**
     * Checks if network connection is available or not
     *
     * @return {@code true} if internet connection is available, {@code false} otherwise
     */
    public static boolean isNetworkEnabled(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        return (cm != null ? cm.getActiveNetwork() : null) != null;
    }

    /**
     * Returns TimeFormat object that user has chose in Settings to format PrayTimes with
     *
     * @return {@link DateFormat} used to format times
     */
    public static FormatPatterns getSelectedTimeFormat(Context context) {
        return FormatPatterns.valueOf(PrefManager.get(context, Keys.TIME_FORMAT_STYLE, FormatPatterns.TIME12SX.ordinal()));
    }

    /**
     * @param context The context to access resources through
     * @param ordinal The ordinal of AllahName to return. -2 to return randomized one.
     * @return Locale-aware  {@link AllahName} with the given ordinal or randomized one if given ordinal is -1
     */
    public static AllahName getAllahName(Context context, int ordinal) {
        final List<AllahName> allahNames = getAllahNames(context);
        if (ordinal == -2) {
            return allahNames.get(Random.Default.nextInt(0, allahNames.size() - 1));
        } else {
            if (ordinal >= allahNames.size()) return allahNames.get(0);
            else if (ordinal < 0) return allahNames.get(allahNames.size() - 1);
            else return allahNames.get(ordinal);
        }
    }

    public static AllahName getRandomizedAllahName(Context context) {
        return getAllahName(context, -2);
    }

    public static AllahName getRandomizedAllahName(Context context, AllahName currName) {
        AllahName randName = getRandomizedAllahName(context);
        if (randName.equals(currName)) randName = getRandomizedAllahName(context, currName);
        return randName;
    }

    /**
     * @param context The context to access resources through
     * @return Locale-aware {@link List<AllahName>} contains all AllahNames
     */
    public static List<AllahName> getAllahNames(Context context) {
        final String[] names = ResMan.getStringArray(context, R.array.AllahNames);
        final String[] namesDescs = ResMan.getStringArray(context, R.array.AllahNamesDescs);
        final List<AllahName> allahNames = new ArrayList<>();
        for (int index = 0; index < names.length; index++) allahNames.add(new AllahName(index, names[index], namesDescs[index]));
        return allahNames;
    }

}

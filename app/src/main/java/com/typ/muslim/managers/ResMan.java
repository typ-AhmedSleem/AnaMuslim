/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.managers;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import androidx.annotation.ArrayRes;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.FontRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

/**
 * Contains necessary code to access and get data from resources
 */
public class ResMan {

    /**
     * Get a specific string using its resourceId.
     *
     * @param context     Context to access resources.
     * @param stringResId The String ResourceId.
     * @return The String with givenResId.
     */
    @NonNull
    public static String getString(Context context, @StringRes int stringResId) {
        try {
            return context.getString(stringResId);
        } catch (Resources.NotFoundException e) {
            return "";
        }
    }

    /**
     * Get a specific color int using its resourceId.
     *
     * @param context    Context to access resources.
     * @param colorResId The Color ResourceId.
     * @return The Color Int with givenResId.
     */
    public static int getColor(Context context, @ColorRes int colorResId) {
        try {
            return ContextCompat.getColor(context, colorResId);
        } catch (Resources.NotFoundException e) {
            return Color.BLACK;
        }
    }

    /**
     * Get a specific drawable int using its resourceId.
     *
     * @param context       Context to access resources.
     * @param drawableResId The Drawable ResourceId.
     * @return The Drawable with givenResId.
     */
    public static Drawable getDrawable(Context context, @DrawableRes int drawableResId) {
        try {
            return ResourcesCompat.getDrawable(context.getResources(), drawableResId, null);
        } catch (Resources.NotFoundException e) {
            return null;
        }
    }

    /**
     * Get a specific string-array int using its resourceId.
     *
     * @param context    Context to access resources.
     * @param arrayResId The Array ResourceId.
     * @return The Array with givenResId.
     */
    public static String[] getStringArray(Context context, @ArrayRes int arrayResId) {
        try {
            return context.getResources().getStringArray(arrayResId);
        } catch (Resources.NotFoundException e) {
            return new String[]{};
        }
    }

    /**
     * Get a specific int-array int using its resourceId.
     *
     * @param context    Context to access resources.
     * @param arrayResId The Array ResourceId.
     * @return The Array with givenResId.
     */
    public static int[] getIntArray(Context context, @ArrayRes int arrayResId) {
        try {
            return context.getResources().getIntArray(arrayResId);
        } catch (Resources.NotFoundException e) {
            return new int[]{};
        }
    }

    public static Typeface getFont(Context context, @FontRes int fontResId) {
        try {
            return ResourcesCompat.getFont(context, fontResId);
        } catch (Resources.NotFoundException e) {
            return null;
        }
    }
}

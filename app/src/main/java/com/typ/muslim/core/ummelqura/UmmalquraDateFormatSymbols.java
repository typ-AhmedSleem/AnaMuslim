/*
 * This product is developed by TYP Software
 * Project Head : Ahmed Sleem
 * Programmer : Ahmed Sleem
 * Pre-Release Tester : Ahmed Sleem & Ahmed Hafez
 *
 * Copyright (c) TYP Electronics Corporation.  All Rights Reserved
 */

package com.typ.muslim.core.ummelqura;

import android.os.Build;

import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Mouaffak A. Sarhan.
 */
public class UmmalquraDateFormatSymbols {

    /**
     * The locale which is used for initializing this DateFormatSymbols object.
     *
     * @serial
     */
    Locale locale = null;

    /**
     * Month strings. For example: "Muharram", "Safar", etc.  An array of 12 strings, indexed by
     * <code>UmmalquraCalendar.MUHARRAM</code>, <code>UmmalquraCalendar.SAFAR</code>, etc.
     *
     * @serial
     */
    String[] months = null;

    /**
     * Short month strings. For example: "Muh", "Saf", etc.  An array of 12 strings, indexed by
     * <code>UmmalquraCalendar.MUHARRAM</code>, <code>UmmalquraCalendar.SAFAR</code>, etc.
     *
     * @serial
     */
    String[] shortMonths = null;

    public UmmalquraDateFormatSymbols() {
        initializeData(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? Locale.getDefault(Locale.Category.FORMAT) : Locale.getDefault());
    }

    public UmmalquraDateFormatSymbols(Locale locale) {
        initializeData(locale);
    }

    /**
     * Gets month strings. For example: "Muharram", "Safar", etc.
     *
     * @return the month strings.
     */
    public String[] getMonths() {
        return Arrays.copyOf(months, months.length);
    }

    /**
     * Gets short month strings. For example: "Muh", "Saf", etc.
     *
     * @return the short month strings.
     */
    public String[] getShortMonths() {
        return Arrays.copyOf(shortMonths, shortMonths.length);
    }

    private void initializeData(Locale desiredLocale) {
        if (!("ar".equalsIgnoreCase(desiredLocale.getLanguage()) || "en"
                .equalsIgnoreCase(desiredLocale.getLanguage()))) {
            throw new IllegalArgumentException("Supported locales are 'English' and 'Arabic'");
        }
        locale = desiredLocale;

        // Initialize the fields from the ResourceBundle for locale.
        ResourceBundle resource = ResourceBundle
                .getBundle("com.typ.muslim.core.ummelqura.text.UmmalquraFormatData",
                        locale);

        months = resource.getStringArray("MonthNames");
        shortMonths = resource.getStringArray("MonthAbbreviations");
    }

}

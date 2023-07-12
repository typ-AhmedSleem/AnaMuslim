/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.enums;

import android.content.Context;

import androidx.annotation.NonNull;

import com.typ.muslim.managers.AManager;
import com.typ.muslim.models.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Locale;

public enum FormatPattern {

    /**
     * Pattern [hh:mm aa]. eg: 3:25 pm
     */
    TIME12SX("hh:mm aa"),

    /**
     * Pattern [hh:mm]. eg: 3:25
     */
    TIME12NSX("hh:mm"),

    /**
     * Pattern [HH:mm]. eg: 15:30
     */
    TIME24("HH:mm"),

    /**
     * Pattern [dd MMM]. eg: 01 Aug
     */
    DATE_MONTH("dd MMM"),

    /**
     * Pattern [dd/MM/yyyy]. eg: 01/08/2001
     */
    DATE_SHORT("dd/MM/yyyy"),

    /**
     * Pattern [dd MMM yyyy]. eg: 01 Aug 2001
     */
    DATE_NORMAL("dd MMM yyyy"),

    /**
     * Pattern [dd MMM yyyy]. eg: 01 Aug 2001
     */
    DATE_FULL("dd MMMM yyyy"),

    /**
     * Pattern [dd/MM/yyyy hh:mm aa]. eg: 01/08/2001 12:03 am
     */
    DATETIME_NORMAL("dd/MM/yyyy hh:mm aa"),

    /**
     * Pattern [dd MMM yyyy hh:mm aa]. eg: 01 Aug 2001 12:03 am
     */
    DATETIME_FULL("dd MMM yyyy hh:mm aa"),

    HMSA("hh:mm:ss aa");

    final String pattern;

    FormatPattern(String pattern) {
        this.pattern = pattern;
    }

    public static FormatPattern valueOf(int ordinal) {
        FormatPattern formatPattern = null;
        for (FormatPattern fp : values()) {
            if (fp.ordinal() == ordinal) {
                formatPattern = fp;
                break;
            }
        }
        return formatPattern;
    }

    @NonNull
    public String getPattern() {
        return pattern;
    }

    @NonNull
    public String format(Timestamp timestamp) {
        return format(timestamp, Locale.getDefault());
    }

    @NonNull
    public String format(Timestamp timestamp, Locale locale) {
        return new SimpleDateFormat(pattern, locale).format(timestamp.asDate());
    }

    public static FormatPattern getDefault(Context ctx) {
        return AManager.getSelectedTimeFormat(ctx);
    }

}

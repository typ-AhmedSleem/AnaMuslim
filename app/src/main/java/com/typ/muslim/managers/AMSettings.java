/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.managers;

import static com.typ.muslim.features.prays.enums.AsrMethod.SHAFII;
import static com.typ.muslim.features.prays.enums.CalculationMethod.EGYPT;
import static com.typ.muslim.features.prays.enums.CalculationMethod.MAKKAH;
import static com.typ.muslim.features.prays.enums.HigherLatitudesMethod.NONE;
import static com.typ.muslim.features.prays.enums.PrayType.ASR;
import static com.typ.muslim.features.prays.enums.PrayType.DHUHR;
import static com.typ.muslim.features.prays.enums.PrayType.FAJR;
import static com.typ.muslim.features.prays.enums.PrayType.ISHA;
import static com.typ.muslim.features.prays.enums.PrayType.MAGHRIB;
import static com.typ.muslim.enums.SoMNotifyMethod.NOTIFICATION;
import static com.typ.muslim.enums.SoMReminderFreq.EVERY_30MIN;

import android.content.Context;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.typ.muslim.R;
import com.typ.muslim.app.Keys;
import com.typ.muslim.features.prays.enums.CalculationMethod;
import com.typ.muslim.features.prays.enums.PrayType;
import com.typ.muslim.enums.OngoingNotificationStyle;
import com.typ.muslim.features.prays.enums.PrayNotifyMethod;
import com.typ.muslim.enums.SoMNotifyMethod;
import com.typ.muslim.enums.SoMReminderFreq;
import com.typ.muslim.enums.TrackerRange;
import com.typ.muslim.models.Location;
import com.typ.muslim.models.LocationConfig;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains necessary code that works with settings of AnaMuslim
 */
public class AMSettings {

    private static final String TAG = "AMSettings";

    public static boolean save(Context context, String key, boolean newValue) {
        PrefManager.set(context, key, newValue);
        AManager.log(TAG, "save: KEY[" + key + "] - VALUE[" + newValue + "]");
        return newValue;
    }

    public static int save(Context context, String key, int newValue) {
        PrefManager.set(context, key, newValue);
        AManager.log(TAG, "save: KEY[" + key + "] - VALUE[" + newValue + "]");
        return newValue;
    }

    public static String save(Context context, String key, String newValue) {
        PrefManager.set(context, key, newValue);
        AManager.log(TAG, "save: KEY[" + key + "] - VALUE[" + newValue + "]");
        return newValue;
    }

    public static long save(Context context, String key, long newValue) {
        PrefManager.set(context, key, newValue);
        AManager.log(TAG, "save: KEY[" + key + "] - VALUE[" + newValue + "]");
        return newValue;
    }

    public static <T extends Enum<?>> T save(Context context, String key, T newValue) {
        if (newValue instanceof OngoingNotificationStyle) PrefManager.set(context, key, ((OngoingNotificationStyle) newValue).getLayoutResId());
        else PrefManager.set(context, key, newValue.ordinal());
        AManager.log(TAG, "update: KEY[" + key + "] - VALUE[" + newValue + "]");
        return newValue;
    }

    public static void inverse(Context context, String key) {
        if (PrefManager.contains(context, key))
            PrefManager.set(context, key, !PrefManager.get(context, key, true));
    }

    public static void setDefault(Context context, String key, boolean newValue) {
        PrefManager.set(context, key, newValue);
    }

    public static void setDefault(Context context, String key, int newValue) {
        PrefManager.set(context, key, newValue);
    }

    //  public static void tunePray(Context context, Prays whatPray, @IntRange(from = -60, to = 60) int offset) {PrefManager.set(context, Keys.KEY_PRAY_OFFSET(whatPray), offset);}

    public static boolean isPressVolumeToStopAzanEnabled(Context context) {
        return PrefManager.get(context, Keys.PRESS_VOLUME_TO_STOP_AZAN, false);
    }

    public static boolean isFlipToStopAzanEnabled(Context context) {
        return PrefManager.get(context, Keys.FLIP_TO_STOP_AZAN, false);
    }

    @IntRange(from = 0, to = 60)
    public static int getMuteTimeDuringPray(Context context, PrayType whatPray) {
        return PrefManager.get(context, Keys.MUTE_DURING_PRAY(whatPray), whatPray == PrayType.FAJR ? 20 : 30);
    }

    public static int[] getMuteTimeDuringPrays(Context context) {
        int[] result = new int[5];
        for (PrayType pray : PrayType.values()) {
            if (pray == PrayType.SUNRISE) continue;
            result[pray.ordinalWithoutSunrise()] = getMuteTimeDuringPray(context, pray);
        }
        return result;
    }

    public static boolean isIqamaEnabled(Context context, PrayType whatPray) {
        return PrefManager.get(context, Keys.IQAMA_ENABLED_FOR(whatPray), false);

    }

    public static boolean[] isIqamaEnabledForPrays(Context context) {
        boolean[] result = new boolean[5];
        for (PrayType pray : PrayType.values()) {
            if (pray == PrayType.SUNRISE) continue;
            result[pray.ordinalWithoutSunrise()] = isIqamaEnabled(context, pray);
        }
        return result;
    }

    @IntRange(from = 0, to = 30)
    public static int getNotifyTimeBeforePray(Context context, PrayType whatPray) {
        return PrefManager.get(context, Keys.NOTIFY_BEFORE_PRAY(whatPray), 15);
    }

    public static int[] getNotifyTimeBeforePrays(Context context) {
        int[] result = new int[5];
        for (PrayType pray : PrayType.values()) {
            if (pray == PrayType.SUNRISE) continue;
            result[pray.ordinalWithoutSunrise()] = getNotifyTimeBeforePray(context, pray);
        }
        return result;
    }

    public static boolean isOngoingNotificationEnabled(Context context) {
        return PrefManager.get(context, Keys.ONGOING_NOTIFICATION_ENABLED, true);
    }

    public static OngoingNotificationStyle getOngoingNotificationStyle(Context context) {
        return OngoingNotificationStyle.of(PrefManager.get(context, Keys.ONGOING_NOTIFICATION_STYLE, OngoingNotificationStyle.STYLE1.getLayoutResId()));
    }

    public static PrayNotifyMethod getPrayNotifyMethod(Context context, PrayType whatPray) {
        return PrayNotifyMethod.values()[PrefManager.get(context, Keys.PRAY_NOTIFY_METHOD(whatPray), whatPray != PrayType.SUNRISE ? PrayNotifyMethod.AZAN.ordinal() : PrayNotifyMethod.NOTIFICATION_ONLY.ordinal())];
    }

    public static PrayNotifyMethod[] getPraysNotifyMethod(Context context) {
        final PrayNotifyMethod[] notifyMethods = new PrayNotifyMethod[6];
        for (PrayType pray : PrayType.values()) notifyMethods[pray.ordinal()] = getPrayNotifyMethod(context, pray);
        return notifyMethods;
    }

    public static boolean isRemindForSunriseEnabled(Context context) {
        return PrefManager.get(context, Keys.REMIND_FOR_SUNRISE, false);
    }

    @IntRange(from = -60, to = 60)
    public static int getOffsetMinutesForPray(Context context, PrayType whatPray) {
        return PrefManager.get(context, Keys.PRAY_OFFSET(whatPray), 0);
    }

    public static int[] getOffsetMinutesForPrays(Context context) {
        int[] result = new int[6];
        for (PrayType pray : PrayType.values()) result[pray.ordinal()] = getNotifyTimeBeforePray(context, pray);
        return result;
    }

    /**
     * Checks and return app's first run status
     *
     * @return {@code true} if app is running for first time,{@code false} if not.
     */
    public static boolean isFirstRun(Context context) {
        return PrefManager.get(context, Keys.IS_FIRST_RUN, true);
    }

    /**
     * Saves the given configuration in local pref if new or update current otherwise
     *
     * @param config Location Configuration to be saved or null to reset it
     */
    public static boolean saveConfiguration(Context context, LocationConfig config) {
        if (context != null && config != null) {
            PrefManager.set(context, Keys.CONFIG_CALC_METHOD, config.getCalculationMethod().ordinal());
            PrefManager.set(context, Keys.CONFIG_ASR_METHOD, config.getAsrMethod().ordinal());
            PrefManager.set(context, Keys.CONFIG_HIGHER_LAT_METHOD, config.getHigherLatitude().ordinal());
            return true;
        }
        return false;
    }

    /**
     * @return The current saved config in pref or Default config which has {@link CalculationMethod#MAKKAH} if no config was found
     */
    public static LocationConfig getCurrentConfiguration(Context ctx) {
        if (isConfigSet(ctx)) return new LocationConfig(
                PrefManager.get(ctx, Keys.CONFIG_CALC_METHOD, MAKKAH.ordinal()),
                PrefManager.get(ctx, Keys.CONFIG_ASR_METHOD, SHAFII.ordinal()),
                PrefManager.get(ctx, Keys.CONFIG_HIGHER_LAT_METHOD, NONE.ordinal()));
        else return null;
    }

    public static LocationConfig getMakkahConfig() {
        return new LocationConfig(MAKKAH, SHAFII, NONE);
    }

    public static LocationConfig getCairoConfig() {
        return new LocationConfig(EGYPT, SHAFII, NONE);
    }

    public static boolean resetLocation(Context context) {
        if (context == null) return false;
        // Reset location
        PrefManager.remove(context, Keys.LOC_COUNTRY_CODE);
        PrefManager.remove(context, Keys.LOC_COUNTRY_NAME);
        PrefManager.remove(context, Keys.LOC_CITY_NAME);
        PrefManager.remove(context, Keys.LOC_LATITUDE);
        PrefManager.remove(context, Keys.LOC_LONGITUDE);
        PrefManager.remove(context, Keys.LOC_TIMEZONE);
        AMSettings.saveConfiguration(context, null);
        return true;
    }

    /**
     * Saves the given location in local pref if new or update current otherwise
     *
     * @param location Location to be saved or null to reset it
     */
    public static boolean saveLocation(Context context, Location location) {
        if (context != null && location != null) {
            // Save location
            PrefManager.set(context, Keys.LOC_COUNTRY_CODE, location.getCountryCode());
            PrefManager.set(context, Keys.LOC_COUNTRY_NAME, location.getCountryName());
            PrefManager.set(context, Keys.LOC_CITY_NAME, location.getCityName());
            PrefManager.set(context, Keys.LOC_LATITUDE, (float) location.getLatitude());
            PrefManager.set(context, Keys.LOC_LONGITUDE, (float) location.getLongitude());
            PrefManager.set(context, Keys.LOC_TIMEZONE, (float) location.getTimezone());
            return true;
        }
        return false;
    }

    /**
     * @return The current saved location in pref or {@code null} if no location was found
     */
    public static Location getCurrentLocation(Context context) {
        if (isLocationSet(context)) return new Location(
                PrefManager.get(context, Keys.LOC_COUNTRY_CODE, "EG"),
                PrefManager.get(context, Keys.LOC_COUNTRY_NAME, context.getString(R.string.egypt)),
                PrefManager.get(context, Keys.LOC_CITY_NAME, context.getString(R.string.cairo)),
                PrefManager.get(context, Keys.LOC_LATITUDE, 30.044281f),
                PrefManager.get(context, Keys.LOC_LONGITUDE, 30.340002f),
                PrefManager.get(context, Keys.LOC_TIMEZONE, 2.00f),
                getCurrentConfiguration(context));
        else return null;
    }

    public static boolean isLocationSet(Context context) {
        return PrefManager.contains(context, Keys.LOC_LATITUDE) && PrefManager.contains(context, Keys.LOC_LONGITUDE) && PrefManager.contains(context, Keys.LOC_TIMEZONE);
    }

    public static boolean isConfigSet(Context ctx) {
        return PrefManager.contains(ctx, Keys.CONFIG_CALC_METHOD) && PrefManager.contains(ctx, Keys.CONFIG_ASR_METHOD) && PrefManager.contains(ctx, Keys.CONFIG_HIGHER_LAT_METHOD);
    }

    public static TrackerRange getTrackerRange(Context context) {
        return TrackerRange.valueOf(PrefManager.get(context, Keys.TRACKER_RANGE, TrackerRange.TODAY.ordinal()));
    }

    public static boolean isUsingVolumeButtonsInTasbeeh(Context context) {
        return PrefManager.get(context, Keys.USE_VOLUME_BUTTONS_IN_TASBEEH, true);
    }

    public static boolean getBoolean(Context c, @NonNull String key, boolean defValue) {
        return PrefManager.get(c, key, defValue);
    }

    public static float getFloat(Context c, @NonNull String key, float defValue) {
        return PrefManager.get(c, key, defValue);
    }

    public static int getInt(Context c, @NonNull String key, int defValue) {
        return PrefManager.get(c, key, defValue);
    }

    public static long getLong(Context c, @NonNull String key, long defValue) {
        return PrefManager.get(c, key, defValue);
    }

    public static String getString(Context c, @NonNull String key, String defValue) {
        return PrefManager.get(c, key, defValue);
    }

    public static boolean isSOMEnabled(Context c) {
        return AMSettings.getBoolean(c, Keys.IS_SOM_REMINDER_ENABLED, true);
    }

    public static SoMReminderFreq getSoMReminderFreq(Context c) {
        return SoMReminderFreq.of(PrefManager.get(c, Keys.SOM_REMINDER_FREQUENCY, EVERY_30MIN.ordinal()));
    }

    public static SoMNotifyMethod getSoMNotifyMethod(Context c) {
        return SoMNotifyMethod.of(PrefManager.get(c, Keys.SOM_NOTIFY_METHOD, NOTIFICATION.ordinal()));
    }

    @NotNull
    @Contract("_ -> new")
    public static Map<String, ?> exportSettings(Context c) {
        Map<String, Object> settings = new HashMap<>();
        // Map settings
        settings.put(Keys.EX_LOCATION, getCurrentLocation(c));
        settings.put(Keys.PRESS_VOLUME_TO_STOP_AZAN, getCurrentLocation(c));
        settings.put(Keys.FLIP_TO_STOP_AZAN, isFlipToStopAzanEnabled(c));
        settings.put(Keys.ONGOING_NOTIFICATION_ENABLED, isOngoingNotificationEnabled(c));
        settings.put(Keys.ONGOING_NOTIFICATION_STYLE, getOngoingNotificationStyle(c).getLayoutResId());
        settings.put(Keys.REMIND_FOR_SUNRISE, isRemindForSunriseEnabled(c));
        settings.put(Keys.TIME_FORMAT_STYLE, AManager.getSelectedTimeFormat(c).ordinal());
        settings.put(Keys.TRACKER_RANGE, getTrackerRange(c).ordinal());
        settings.put(Keys.USE_VOLUME_BUTTONS_IN_TASBEEH, isUsingVolumeButtonsInTasbeeh(c));
        settings.put(Keys.IS_SOM_REMINDER_ENABLED, isSOMEnabled(c));
        settings.put(Keys.SOM_NOTIFY_METHOD, getSoMNotifyMethod(c).ordinal());
        settings.put(Keys.SOM_REMINDER_FREQUENCY, getSoMReminderFreq(c).ordinal());
        settings.put(Keys.IQAMA_ENABLED, Arrays.asList(
                isIqamaEnabled(c, FAJR),
                isIqamaEnabled(c, DHUHR),
                isIqamaEnabled(c, ASR),
                isIqamaEnabled(c, MAGHRIB),
                isIqamaEnabled(c, ISHA)));
        settings.put(Keys.NOTIFY_BEFORE_PRAY, Arrays.asList(getNotifyTimeBeforePrays(c)));
        settings.put(Keys.MUTE_DURING_PRAY, Arrays.asList(getMuteTimeDuringPrays(c)));
        settings.put(Keys.PRAY_NOTIFY_METHOD, Arrays.asList(getPraysNotifyMethod(c)));
        settings.put(Keys.PRAY_OFFSET, Arrays.asList(getOffsetMinutesForPrays(c)));
        // Return exported settings
        return settings;
    }

    @Contract(pure = true)
    @NotNull
    public static void importSettings(Context c, Map<String, ?> settings) {
        if (settings == null) return;
        for (String key : settings.keySet()) {
            Object val = settings.get(key);
            if (val instanceof Location) saveLocation(c, (Location) val);
            else if (val instanceof String) save(c, key, (String) val);
            else if (val instanceof Boolean) save(c, key, (boolean) val);
            else if (val instanceof Integer) save(c, key, (int) val);
            else if (val instanceof Long) save(c, key, (long) val);
            else if (val instanceof Enum<?>) save(c, key, (Enum<?>) val);
            else if (val instanceof List<?>) {
                // Prays adjustments
                List<Object> list = (List<Object>) val;
                for (PrayType pray : PrayType.values()) {
                    if (key.equals(Keys.IQAMA_ENABLED)) save(c, Keys.IQAMA_ENABLED_FOR(pray), (boolean) list.get(pray.ordinal()));
                    else if (key.equals(Keys.NOTIFY_BEFORE_PRAY)) save(c, Keys.NOTIFY_BEFORE_PRAY(pray), (int) list.get(pray.ordinal()));
                    else if (key.equals(Keys.MUTE_DURING_PRAY)) save(c, Keys.MUTE_DURING_PRAY(pray), (int) list.get(pray.ordinal()));
                    else if (key.equals(Keys.PRAY_NOTIFY_METHOD)) save(c, Keys.PRAY_NOTIFY_METHOD(pray), (PrayNotifyMethod) list.get(pray.ordinal()));
                    else if (key.equals(Keys.PRAY_OFFSET)) save(c, Keys.PRAY_OFFSET(pray), (int) list.get(pray.ordinal()));
                }
            }
        }
    }

    @NotNull
    public static Map<String, ?> resetSettings(Context c) {
        // Export settings at first
        Map<String, ?> exSettings = exportSettings(c);
        // Reset all settings
        saveLocation(c, null); // Reset location.
        PrefManager.remove(c, Keys.PRESS_VOLUME_TO_STOP_AZAN);
        PrefManager.remove(c, Keys.FLIP_TO_STOP_AZAN);
        PrefManager.remove(c, Keys.ONGOING_NOTIFICATION_ENABLED);
        PrefManager.remove(c, Keys.ONGOING_NOTIFICATION_STYLE);
        PrefManager.remove(c, Keys.REMIND_FOR_SUNRISE);
        PrefManager.remove(c, Keys.TIME_FORMAT_STYLE);
        PrefManager.remove(c, Keys.TRACKER_RANGE);
        PrefManager.remove(c, Keys.USE_VOLUME_BUTTONS_IN_TASBEEH);
        PrefManager.remove(c, Keys.IS_SOM_REMINDER_ENABLED);
        PrefManager.remove(c, Keys.SOM_NOTIFY_METHOD);
        PrefManager.remove(c, Keys.SOM_REMINDER_FREQUENCY);
        for (PrayType pray : PrayType.values()) {
            PrefManager.remove(c, Keys.IQAMA_ENABLED_FOR(pray));
            PrefManager.remove(c, Keys.NOTIFY_BEFORE_PRAY(pray));
            PrefManager.remove(c, Keys.MUTE_DURING_PRAY(pray));
            PrefManager.remove(c, Keys.PRAY_NOTIFY_METHOD(pray));
            PrefManager.remove(c, Keys.PRAY_OFFSET(pray));
        }
        return exSettings;
    }
}

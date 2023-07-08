/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2022.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.app;

import androidx.annotation.NonNull;

import com.typ.muslim.core.praytime.enums.Prays;

import org.jetbrains.annotations.NotNull;

/**
 * Contains Keys used in storing Preference Data and Server Data
 */
public class Keys {

	// Runtime and states
	public static final String IS_FIRST_RUN = "isFirstRun";

	// Theme
	public static final String THEME_GLOBAL = "globalTheme";
	public static final String THEME_COLOR = "colorTheme";
	public static final String COLOR_ACCENT = "colorAccent";
	public static final String COLOR_PRIMARY = "colorPrimary";
	public static final String COLOR_TEXT = "colorText";

	// Location
	public static final String LOC_COUNTRY_CODE = "locCountryCode";
	public static final String LOC_COUNTRY_NAME = "locCountryName";
	public static final String LOC_CITY_NAME = "locCityName";
	public static final String LOC_LATITUDE = "locLatitude";
	public static final String LOC_LONGITUDE = "locLongitude";
	public static final String LOC_TIMEZONE = "locTimezone";

	// Configuration
	public static final String CONFIG_CALC_METHOD = "configCalcMethod";
	public static final String CONFIG_ASR_METHOD = "configAsrMethod";
	public static final String CONFIG_HIGHER_LAT_METHOD = "configHigherLat";

	// App settings
	public static final String PRESS_VOLUME_TO_STOP_AZAN = "pressVolumeToStopAzan";
	public static final String FLIP_TO_STOP_AZAN = "flipToStopAzan";
	public static final String ONGOING_NOTIFICATION_ENABLED = "ongoingNotificationEnabled";
	public static final String ONGOING_NOTIFICATION_STYLE = "ongoingNotificationStyle";
	public static final String REMIND_FOR_SUNRISE = "remindForSunrise";
	public static final String TIME_FORMAT_STYLE = "timeFormatStyle";
	public static final String TRACKER_RANGE = "trackerRange";
	public static final String USE_VOLUME_BUTTONS_IN_TASBEEH = "useVolumeButtonsInTasbeeh";
	public static final String IS_SOM_REMINDER_ENABLED = "isSoMReminderEnabled";
	public static final String SOM_NOTIFY_METHOD = "somNotifyMethod";
	public static final String SOM_REMINDER_FREQUENCY = "somReminderFrequency";

	// Keys used in exporting profile
	public static final String EX_LOCATION = "location";
	public static final String IQAMA_ENABLED = "iqamaEnabled";
	public static final String NOTIFY_BEFORE_PRAY = "notifyBeforeAzan";
	public static final String MUTE_DURING_PRAY = "muteDuringPray";
	public static final String PRAY_NOTIFY_METHOD = "prayNotifyMethod";
	public static final String PRAY_OFFSET = "prayTimesOffsets";

	// Keys used by modules/Tasbeeh
	public static final String TasbeehMode = "TasbeehMode";
	public static final String TasbeehTimes = "TasbeehTimes";
	public static final String TasbeehItems = "TasbeehItems";
	public static final String TasbeehIsVibrationEnabled = "TasbeehIsVibrationEnabled";
	public static final String TasbeehIsVolumeCounterEnabled = "TasbeehIsVolumeCounterEnabled";
	public static final String TasbeehIsSpeakOnFlipEnabled = "TasbeehIsSpeakOnFlipEnabled";

	// Keys used in args bundle
	public static final String NAME_OF_ALLAH = "nameOfAllah";

	@NotNull
	public static String IQAMA_ENABLED_FOR(Prays whatPray) {
		return "iqamaDelayFor" + whatPray.name();
	}

	@NotNull
	public static String NOTIFY_BEFORE_PRAY(Prays whatPray) {
		return "notifyBeforeAzanFor" + whatPray.name();
	}

	@NotNull
	public static String MUTE_DURING_PRAY(Prays whatPray) {
		return "muteMinutesDuring" + whatPray.name();
	}

	@NotNull
	public static String PRAY_NOTIFY_METHOD(Prays whatPray) {
		return "notifyMethodFor" + whatPray.name();
	}

	@NonNull
	public static String PRAY_OFFSET(Prays whatPray) {
		return "offsetOf" + whatPray.name();
	}

}

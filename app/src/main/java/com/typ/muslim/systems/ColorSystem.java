/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.systems;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;

import com.typ.muslim.R;
import com.typ.muslim.app.Keys;
import com.typ.muslim.core.praytime.enums.Prays;
import com.typ.muslim.interfaces.ThemeChangeObserver;
import com.typ.muslim.managers.PrefManager;
import com.typ.muslim.managers.ResMan;
import com.typ.muslim.models.Pray;
import com.typ.muslim.ui.AMBaseActivity;

/**
 * Contains code that do all operations with views and colors
 */
public class ColorSystem {

	// TODO: 3/1/21 Take care of theme-aware colors that some colors may not be valid with all themes

	// Runtime
	private final AMBaseActivity baseActivity;
	private final ThemeChangeObserver themeChangeObserver;
	private boolean isPaused;
	private GlobalTheme currGlobalTheme;
	private ColorTheme currColorTheme;

	public ColorSystem(AMBaseActivity activity) {
		// Setup runtime
		this.baseActivity = activity;
		this.isPaused = false;
		this.currGlobalTheme = getCurrentTheme(activity);
		this.currColorTheme = getCurrentColorTheme(activity);
		this.themeChangeObserver = activity;
		// Send prepare theme signal to observer
		themeChangeObserver.onFirstTheme(currGlobalTheme, currColorTheme);
	}

	/**
	 * Creates new ColorSystem object that will work along with AMBaseActivity lifecycle
	 */
	public static ColorSystem create(AMBaseActivity context) {
		return new ColorSystem(context);
	}

	/**
	 * Returns the current theme accent color used for buttons backgrounds and TextColors sometimes
	 */
	public static @ColorInt
	int getPrimaryColor(Context context) {
		return PrefManager.get(context, Keys.COLOR_PRIMARY, ResMan.getColor(context, R.color.colorPrimary));
    }

	/**
	 * Returns the current theme accent color used for buttons backgrounds and TextColors sometimes
	 */
	public static @ColorInt
	int getAccentColor(Context context) {
		return PrefManager.get(context, Keys.COLOR_ACCENT, ResMan.getColor(context, R.color.colorAccent));
    }

	/**
	 * Returns the current theme accent color used for buttons backgrounds and TextColors sometimes
	 */
	public static @ColorInt
	int getTextColor(Context context) {
		return PrefManager.get(context, Keys.COLOR_TEXT, ResMan.getColor(context, R.color.colorText));
    }

	/**
	 * @return The current global theme as enum {@link GlobalTheme}
	 */
	public static GlobalTheme getCurrentTheme(Context context) {
		return GlobalTheme.valueOf(PrefManager.get(context, Keys.THEME_GLOBAL, GlobalTheme.LIGHT.ordinal()));
	}

	/* ================== Static Methods ================= */

	/**
	 * @return The current color theme as enum {@link ColorTheme}
	 */
	public static ColorTheme getCurrentColorTheme(Context context) {
		return ColorTheme.valueOf(PrefManager.get(context, Keys.THEME_COLOR, ColorTheme.GREEN.ordinal()));
	}

	public static void saveGlobalTheme(Context context, GlobalTheme newTheme) {
		if (newTheme == null || context == null) return;
		PrefManager.set(context, Keys.THEME_GLOBAL, newTheme.ordinal());
	}

	public static void saveColorTheme(Context context, ColorTheme newTheme) {
		if (newTheme == null || context == null) return;
		PrefManager.set(context, Keys.THEME_GLOBAL, newTheme.ordinal());
	}

	public void onResume() {
		// Update runtime
		this.isPaused = false;
		// Check if theme has changed
		if (getCurrentColorTheme(baseActivity) != currColorTheme || getCurrentTheme(baseActivity) != currGlobalTheme) {
			// Update runtime
			this.currGlobalTheme = getCurrentTheme(baseActivity);
			this.currColorTheme = getCurrentColorTheme(baseActivity);
			// Send theme changed signal to observer
			themeChangeObserver.onThemeChanged(null, currGlobalTheme, currColorTheme);
		}
	}

	public void onPause() {
		this.isPaused = true;
	}

	public void changeGlobalTheme(View madeCallView, GlobalTheme newGlobalTheme) {
		// Check if theme has changed
		if (this.currGlobalTheme == newGlobalTheme) return;
		// Save theme
		saveGlobalTheme(baseActivity, newGlobalTheme);
		// Update runtime
		this.currGlobalTheme = newGlobalTheme;
		// Send theme changed signal to observer
		themeChangeObserver.onThemeChanged(madeCallView, newGlobalTheme, currColorTheme);
	}

	public void changeColorTheme(View madeCallView, ColorTheme newColorTheme) {
		// Check if theme has changed
		if (this.currColorTheme == newColorTheme) return;
		// Save theme
		saveColorTheme(baseActivity, newColorTheme);
		// Update runtime
		this.currColorTheme = newColorTheme;
		// Send theme changed signal to observer
		themeChangeObserver.onThemeChanged(madeCallView, currGlobalTheme, newColorTheme);
	}

	/* ===================== ENUMS =================== */
	public enum GlobalTheme {
		LIGHT(R.color.bg_light),
		DARK(R.color.bg_night);

		// Runtime
		private final @ColorRes
		int bgColor;

		GlobalTheme(@ColorRes int bgColor) {
			this.bgColor = bgColor;
		}

		public static GlobalTheme valueOf(int ordinal) {
			if (ordinal >= values().length) return LIGHT;
			else return values()[ordinal];
		}

		public int getBgColor() {
			return bgColor;
		}
	}

	public enum ColorTheme {
		BLUE(R.color.blue, R.color.white),
		GREEN(R.color.colorPrimary, R.color.white),
		BROWN(R.color.colorPrimary2, R.color.white),
		RED(R.color.red, R.color.white),
		YELLOW(R.color.yellow, R.color.yellow);

		// Runtime
		private final @ColorRes int accentColor;
		private final @ColorRes int textColor;

		ColorTheme(@ColorRes int accentColor, @ColorRes int textColor) {
			this.accentColor = accentColor;
			this.textColor = textColor;
		}

		public static ColorTheme valueOf(int ordinal) {
			if (ordinal >= values().length) return GREEN;
			else return values()[ordinal];
		}

		@ColorRes
		public int getAccentColorRes() {
			return accentColor;
		}

		@ColorRes
		public int getTextColor() {
			return textColor;
		}
	}

	public static class Colors {

		// TODO: 7/7/2021 care of colors returned by checking if dark theme is enabled or not

		@ColorInt
		public static int getPrayCardSurfaceColor(Context context, Pray pray) {
			// TODO: 7/7/2021 to be completed
			if (pray.getType() == Prays.SUNRISE) return ResMan.getColor(context, R.color.color_dhuhr_sunrise_highlight);
			else if (pray.getType() == Prays.DHUHR) return ResMan.getColor(context, R.color.color_dhuhr_sunrise_bg);
			else if (pray.getType() == Prays.ASR) return ResMan.getColor(context, R.color.color_asr_bg);
			else if (pray.getType() == Prays.MAGHRIB) return ResMan.getColor(context, R.color.color_maghrib_isha_header);
			else if (pray.getType() == Prays.ISHA) return ResMan.getColor(context, R.color.color_isha_bg);
			return ResMan.getColor(context, R.color.color_fajr_header); // Fajr.
		}

		@ColorInt
		public static int getPrayCardHeadTextColor(Context context, Pray pray) {
			// TODO: 7/7/2021 to be completed
			if (pray.getType() == Prays.SUNRISE) return Color.WHITE;
			else if (pray.getType() == Prays.DHUHR) return Color.BLACK;
			else if (pray.getType() == Prays.ASR) return Color.BLACK;
			else if (pray.getType() == Prays.MAGHRIB) return ResMan.getColor(context, R.color.color_maghrib_isha_highlight);
			else if (pray.getType() == Prays.ISHA) return ResMan.getColor(context, R.color.color_maghrib_isha_highlight);
			return Color.WHITE; // Fajr.
		}

		@ColorInt
		public static int getPrayCardSubTextColor(Context context, Pray pray) {
			// TODO: 7/7/2021 to be completed
			if (pray.getType() == Prays.SUNRISE) return Color.parseColor("#B3FFFFFF");
			else if (pray.getType() == Prays.DHUHR) return Color.parseColor("#B3000000");
			else if (pray.getType() == Prays.ASR) return Color.parseColor("#B3FFFFFF");
			else if (pray.getType() == Prays.MAGHRIB) return Color.parseColor("#B3FFFFFF");
			else if (pray.getType() == Prays.ISHA) return Color.parseColor("#B3FFFFFF");
			return Color.parseColor("#B3FFFFFF"); // Fajr.
		}

	}
}

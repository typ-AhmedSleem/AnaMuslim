/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textview.MaterialTextView;
import com.typ.muslim.interfaces.ThemeChangeObserver;
import com.typ.muslim.managers.AMRes;
import com.typ.muslim.systems.ColorSystem;

public class AMTextView extends MaterialTextView implements ThemeChangeObserver {

	// TODO: 3/1/21 Add attribute to this view that indicates it is for Title or not. Means that to adapt it to theme changes or not. enum[ADAPT_THEME_CHANGES, NO_ADAPT]

	// Runtime
	private boolean isThemeAdaptEnabled = true;

	public AMTextView(@NonNull Context context) {
		super(context);
	}

	public AMTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public AMTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public void onFirstTheme(ColorSystem.GlobalTheme globalTheme, ColorSystem.ColorTheme colorTheme) {
		// todo : to be coded
	}

	@Override
	public void onThemeChanged(View madeCallView, ColorSystem.GlobalTheme globalTheme, ColorSystem.ColorTheme colorTheme) {
		if (globalTheme != null && colorTheme != null && isThemeAdaptEnabled) super.setTextColor(AMRes.getColor(getContext(), colorTheme.getAccentColorRes()));
	}

	@SuppressLint("ResourceType")
	@Override
	public void setTextColor(@ColorRes int textColor) {
		// Change TextColor if adaptTheme is off
		if (!isThemeAdaptEnabled) super.setTextColor(AMRes.getColor(getContext(), textColor));
	}

	public AMTextView setThemeAdaptive(boolean themeAdaptEnabled) {
		isThemeAdaptEnabled = themeAdaptEnabled;
		return this;
	}

	public boolean isThemeAdaptEnabled() {
		return isThemeAdaptEnabled;
	}
}

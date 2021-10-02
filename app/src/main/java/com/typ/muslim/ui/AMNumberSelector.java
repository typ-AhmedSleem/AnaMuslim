/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.typ.muslim.R;
import com.typ.muslim.interfaces.ThemeChangeObserver;
import com.typ.muslim.systems.ColorSystem;

public class AMNumberSelector extends RelativeLayout implements ThemeChangeObserver {

	// Runtime
	private int currentValue = 8;
	private int maxValue = 60;
	private int minValue = -60;
	// Views
	private ImageButton ibtnIncrease, ibtnDecrease;
	private AMTextSwitcher atsValue;

	public AMNumberSelector(Context context) {
		super(context);
		init(context);
	}

	public AMNumberSelector(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public AMNumberSelector(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context context) {
		// Inflate content view
		inflate(context, R.layout.layout_number_selector, this);
		// Setup inner views
		ibtnIncrease = findViewById(R.id.ibtn_increase);
		ibtnDecrease = findViewById(R.id.ibtn_decrease);
		atsValue = findViewById(R.id.ats_number);
		// Bind views
		String[] values = new String[maxValue + Math.abs(minValue)];
		for (int i = minValue; i < maxValue; i++) values[Math.abs(i)] = String.valueOf(i);
		atsValue.setTexts(values);
		// Listeners
		ibtnDecrease.setOnClickListener(v -> {
			if (currentValue >= minValue && currentValue <= maxValue) {
				currentValue--; // Decrease.
				atsValue.previous();
				if (ibtnIncrease.getVisibility() == INVISIBLE) ibtnIncrease.setVisibility(VISIBLE);
			}
		});
		ibtnIncrease.setOnClickListener(v -> {
			if (currentValue >= minValue && currentValue <= maxValue) {
				currentValue++; // Increase.
				atsValue.next();
				if (ibtnDecrease.getVisibility() == INVISIBLE) ibtnDecrease.setVisibility(VISIBLE);
			}
		});
		atsValue.setSwitcherCallback(new AMTextSwitcher.SwitcherCallback() {
			@Override
			public void onSwitch(int newPosition) {
				AMNumberSelector.this.currentValue = newPosition;
			}
		});
	}

	public void setLimits(int max, int min) {
		// Update runtime
		this.maxValue = max;
		this.minValue = min;
		// Update views
		if (ibtnIncrease != null && ibtnDecrease != null && atsValue != null) {
			if (currentValue == max) ibtnIncrease.setVisibility(INVISIBLE);
			else if (currentValue > max) {
				this.currentValue = max;
				ibtnIncrease.setVisibility(INVISIBLE);
				atsValue.displayText(String.valueOf(max));
			}
			if (currentValue == min) {
				ibtnDecrease.setVisibility(INVISIBLE);
			} else if (currentValue < min) {
				this.currentValue = min;
				ibtnDecrease.setVisibility(INVISIBLE);
				atsValue.displayText(String.valueOf(min));
			}
		}
	}

	public int getCurrentValue() {
		return this.currentValue;
	}

	public void setCurrentValue(int newValue) {
		// Update runtime
		if (currentValue == newValue || newValue > maxValue || newValue < minValue) return;
		this.currentValue = newValue;
		// Update views
		if (atsValue != null) atsValue.displayText(String.valueOf(newValue));
	}

	@Override
	public void onFirstTheme(ColorSystem.GlobalTheme globalTheme, ColorSystem.ColorTheme colorTheme) {

	}

	@Override
	public void onThemeChanged(View madeCallView, ColorSystem.GlobalTheme globalTheme, ColorSystem.ColorTheme colorTheme) {
		ibtnIncrease.setBackgroundResource(R.drawable.btn_bg_circle_green);
		ibtnDecrease.setBackgroundResource(R.drawable.btn_bg_circle_green);
		atsValue.setTextColor(colorTheme);
	}

}

/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui;


import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import com.typ.muslim.R;
import com.typ.muslim.managers.ResMan;
import com.typ.muslim.systems.ColorSystem;
import com.typ.muslim.utils.DisplayUtils;

public class AMTextSwitcher extends TextSwitcher {

	// TODO: Adjust switcher with attrs in xml code (TextSize, TextColor, Gravity)
	// TODO: Switch automatically after interval or manually (Previous & Next)
	// TODO: Switch to specific using its position

	// Statics
	private static final String TAG = "AMTextSwitcher";
	private final int textGravity = Gravity.CENTER_VERTICAL | Gravity.START;
	// Runtime
	private String[] texts;
	private float textSize = 15f;
	private int currentPosition = 0;
	private @ColorInt
	int textColor = Color.WHITE;
	// Callbacks
	private SwitcherCallback switcherCallback;
	private SwitcherTextClickCallback onTextClickCallback;

	public AMTextSwitcher(Context context) {
		super(context);
		this.initSwitcher(context);
	}

	public AMTextSwitcher(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.parseAttrs(attrs);
		this.initSwitcher(context);
	}

	private void parseAttrs(AttributeSet attrs) {
		// TODO: 6/17/2021 to be coded
	}

	private void initSwitcher(Context context) {
		// Prepare runtime if needed
		this.textSize = 15f;
		// Init in & out animations
		Animation inAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_in_slide_in);
		inAnimation.setInterpolator(new LinearOutSlowInInterpolator());
		inAnimation.setDuration(1000);
		Animation outAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_out_slide_out);
		outAnimation.setInterpolator(new LinearOutSlowInInterpolator());
		outAnimation.setDuration(1000);
		// Init switcher
		this.setInAnimation(inAnimation);
		this.setOutAnimation(outAnimation);
		this.setupViewFactory();
	}

	private void setupViewFactory() {
		removeAllViews();
		this.setFactory(() -> {
			TextView tv = new TextView(getContext());
			tv.setTextColor(textColor);
			tv.setMaxLines(1);
			tv.setAllCaps(true);
			tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
			tv.setGravity(textGravity);
			tv.setPadding(0, 0, 0, 0);
			tv.setOnClickListener(v -> {if (this.onTextClickCallback != null) this.onTextClickCallback.onTextClick(this.currentPosition, this.texts[this.currentPosition]); });
			return tv;
		});
	}

	public void setTexts(String... texts) {
		// Update runtime
		this.texts = texts != null ? texts : new String[1];
		// Update display
		this.currentPosition = 0;
		if (this.texts.length > 0) this.displayText(this.texts[this.currentPosition]);
	}

	public void setSwitcherCallback(SwitcherCallback switcherCallback) {
		this.switcherCallback = switcherCallback;
	}

	public void setOnTextClickListener(SwitcherTextClickCallback listener) {
		this.onTextClickCallback = listener;
	}

	public void displayText(String text) {
		// Force display text
		this.currentPosition = 0;
		this.setText(text);
	}

	public void displayText(@StringRes int textResId) {
		this.currentPosition = 0;
		this.displayText(ResMan.getString(getContext(), textResId));
	}

	public void next() {
		if (texts != null && texts.length > 0) {
			if (this.currentPosition >= texts.length) if (this.switcherCallback != null) this.switcherCallback.onNoNext();
			else setDisplayedChild(++this.currentPosition + 1);
		}
	}

	public void previous() {
		if (texts != null && texts.length > 0) {
			if (this.currentPosition <= 0) if (this.switcherCallback != null) this.switcherCallback.onNoPrevious();
			else setDisplayedChild(--this.currentPosition - 1);
		}
	}

	public AMTextSwitcher setTextColor(@ColorInt int textColor) {
		this.textColor = textColor;
		this.setupViewFactory();
		return this;
	}

	public AMTextSwitcher setTextColor(ColorSystem.ColorTheme colorTheme) {
		if (colorTheme != null) this.setTextColor(ResMan.getColor(getContext(), colorTheme.getTextColor()));
		return this;
	}

	public void setTextSize(float textSizePixels) {
		this.textSize = textSizePixels;
		this.setupViewFactory();
	}

	public void setTextSize(@Dimension int textSizeDips) {
		this.setTextSize((float) DisplayUtils.sp2px(getContext(), textSizeDips));
	}

	public interface SwitcherTextClickCallback {

		void onTextClick(int position, @NonNull String text);

	}

	public abstract static class SwitcherCallback {

		public abstract void onSwitch(int newPosition);

		public void onNoNext() {}

		public void onNoPrevious() {}

	}

}

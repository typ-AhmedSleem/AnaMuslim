/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ViewSwitcher;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.FontRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.google.android.material.textview.MaterialTextView;
import com.typ.muslim.R;
import com.typ.muslim.managers.AMRes;

public class AnimatedTextView extends ViewContainer {

	// Statics
	private static final String TAG = "AnimatedTextView";
	// Runtime
	private Dimension textSize;
	private @ColorInt int textColor;
	private int textGravity;
	private boolean textAllCaps;
	private Typeface typeface;
	private int textStyle;
	private @Nullable String text;
	private int maxLines = -1;
	// Views
	private ViewSwitcher switcher;

	public AnimatedTextView(@NonNull Context context) {
		super(context);
	}

	public AnimatedTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		parseAttrs(context, attrs);
		prepareView(context);
	}

	public AnimatedTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public void parseAttrs(Context context, @Nullable AttributeSet attrs) {
		textSize = new Dimension(context);
		if (attrs == null) {
			// Set defaults
			maxLines = -1;
			textAllCaps = false;
			textSize.set(Dimension.PixelType.DP, 14f);
			textGravity = Gravity.START | Gravity.CENTER_VERTICAL;
			textColor = getColor(R.color.darkAdaptiveColor);
			textStyle = Typeface.NORMAL;
			typeface = Typeface.DEFAULT;
		} else {
			// Parse attrs
			TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AnimatedTextView);
			final int font = ta.getResourceId(R.styleable.AnimatedTextView_android_fontFamily, -1);
			text = ta.getString(R.styleable.AnimatedTextView_android_text);
			textStyle = ta.getInteger(R.styleable.AnimatedTextView_android_textStyle, Typeface.NORMAL);
			textAllCaps = ta.getBoolean(R.styleable.AnimatedTextView_android_textAllCaps, false);
			textColor = ta.getColor(R.styleable.AnimatedTextView_android_textColor, getColor(R.color.darkAdaptiveColor));
			textSize.set(Dimension.PixelType.REAL_PIXEL, ta.getDimension(R.styleable.AnimatedTextView_android_textSize, 24f));
			typeface = AMRes.getFont(context, ta.getResourceId(R.styleable.AnimatedTextView_android_fontFamily, -1));
			textGravity = ta.getInteger(R.styleable.AnimatedTextView_android_gravity, Gravity.START | Gravity.CENTER_VERTICAL);
			maxLines = ta.getInteger(R.styleable.AnimatedTextView_android_maxLines, -1);
			ta.recycle();
		}
	}

	public void prepareView(Context context) {
		// Inflate view and init switcher
		inflate(R.layout.layout_animated_tv);
		switcher = $(R.id.vs_atv);
		switcher.setInAnimation(context, R.anim.fade_in_slide_in);
		switcher.setOutAnimation(context, R.anim.fade_out_slide_out);
		// Customize current tv with given parsed attrs
		final MaterialTextView[] tvs = {getCurrent(), getNext()};
		for (MaterialTextView tv : tvs) {
			tv.setTypeface(typeface, textStyle);
			tv.setTextColor(textColor);
			tv.setAllCaps(textAllCaps);
			tv.setGravity(textGravity);
			tv.setTextSize(textSize.toPixels());
			tv.setText(text);
			if (maxLines != -1) tv.setMaxLines(maxLines);
		}
	}

	private MaterialTextView getCurrent() {
		return (MaterialTextView) switcher.getCurrentView();
	}

	private MaterialTextView getNext() {
		return (MaterialTextView) switcher.getNextView();
	}

	public void setText(@Nullable String text, Direction animDir) {
		if ((text != null && text.equalsIgnoreCase(this.text)) || animDir == Direction.NONE) getCurrent().setText(text); // Update text with no animation.
		else {
			// Update with animation
			getNext().setText(text);
			if (animDir == Direction.NEXT) switcher.showNext();
			else switcher.showPrevious();
		}
	}

	public void setText(@StringRes int textResId, Direction animDir) {
		setText(getString(textResId), animDir);
	}

	public void setText(SpannableString spannedText, Direction animDir) {
		if (spannedText == null) return;
		this.text = spannedText.toString();
		if (animDir == Direction.NONE) getCurrent().setText(spannedText); // Update text with no animation.
		else {
			// Update with animation
			getNext().setText(spannedText);
			if (animDir == Direction.NEXT) switcher.showNext();
			else switcher.showPrevious();
		}
	}

	public AnimatedTextView setTextSize(Dimension.PixelType unit, float textSize) {
		this.textSize.set(unit, textSize);
		return this;
	}

	public AnimatedTextView setTextColor(@ColorRes int textColorResId) {
		if (textColor == -1) return this;
		this.textColor = getColor(textColorResId);
		return this;
	}

	public AnimatedTextView setTextGravity(int textGravity) {
		this.textGravity = textGravity;
		return this;
	}

	public AnimatedTextView setTextAllCaps(boolean allCaps) {
		if (this.textAllCaps == allCaps) return this;
		this.textAllCaps = allCaps;
		return this;
	}

	public AnimatedTextView setTypeface(@FontRes int fontResId) {
		this.typeface = AMRes.getFont(getContext(), fontResId);
		return this;
	}

	public AnimatedTextView setTextStyle(int textStyle) {
		this.textStyle = textStyle;
		typeface = Typeface.create(typeface, textStyle);
		return this;
	}

	public void applyChanges() {
		// Commits the changes made and apply it to tvs
		final MaterialTextView tv = getCurrent();
		tv.setTypeface(typeface);
		tv.setTextColor(textColor);
		tv.setAllCaps(textAllCaps);
		tv.setGravity(textGravity);
		tv.setTextSize(textSize.toPixels());
		tv.setText(text);
	}

	public enum Direction {
		NONE,
		NEXT,
		PREV
	}
}

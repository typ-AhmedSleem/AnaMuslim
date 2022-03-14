/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.typ.muslim.R;
import com.typ.muslim.managers.ResMan;

import org.jetbrains.annotations.NotNull;

public class ArrowIndicatorTextView extends ViewContainer {

	// Views
	private ImageView ivIndicator;
	private AMTextSwitcher tsValue;

	public ArrowIndicatorTextView(@NonNull @NotNull Context context) {
		super(context);
	}

	public ArrowIndicatorTextView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public ArrowIndicatorTextView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public void prepareView(Context context) {
		// Inflate layout to view
		inflate(context, R.layout.layout_arrow_indicator_textview, this);
		// Init views
		ivIndicator = $(R.id.img_arrow_indicator);
		tsValue = $(R.id.ts_value);
		// Set views to its default state
		ivIndicator.clearColorFilter();
		tsValue.setTextColor(Color.BLACK); // fixme: use adaptive color
		tsValue.setTextSize(12);
		tsValue.displayText(R.string.no_data);
	}


	public void change(IndicatorInfo info) {
        if (info == null) return;
        // Change indicator and content
        @ColorInt int targetColor = ResMan.getColor(getContext(), R.color.darkAdaptiveColor);
        if (info.indicator == -1) targetColor = ResMan.getColor(getContext(), R.color.red); // Down.
        else if (info.indicator == 1) targetColor = ResMan.getColor(getContext(), R.color.green); // Up.
        tsValue.setTextColor(targetColor);
        ivIndicator.setColorFilter(targetColor);
        ivIndicator.setImageResource(info.indicator == -1 ? R.drawable.ic_arrow_to_bottom : info.indicator == 0 ? R.drawable.ef_ic_arrow_back : R.drawable.ic_arrow_to_top);
    }

	public static class IndicatorInfo {

		private final @IntRange(from = -1, to = 1) int indicator;
		private final String content;

		/**
		 * Default constructor
		 *
		 * @param indicator [-1 |-> Down] | [0 |-> Default] | [1 |-> Up]
		 */
		public IndicatorInfo(int indicator, String content) {
			this.indicator = indicator;
			this.content = content;
		}
	}

}

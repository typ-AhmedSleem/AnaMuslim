/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.WeekView;
import com.typ.muslim.R;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.models.Timestamp;
import com.typ.muslim.utils.DisplayUtils;

public class HijriCalendarWeekView extends WeekView {

	// Statics
	private static final String TAG = "HijriCalendarWeekView";
	// For drawing
	private final float padding;
	private final float textBaseline;
	private final float roundRectRadius;
	private final TextPaint dayNumberTextPaint = new TextPaint();

	public HijriCalendarWeekView(Context context) {
		super(context);
		// Setup day number TextPaint
		dayNumberTextPaint.setAntiAlias(true);
		dayNumberTextPaint.setFakeBoldText(true);
		dayNumberTextPaint.setTextAlign(Paint.Align.CENTER);
		dayNumberTextPaint.setColor(context.getColor(R.color.darkAdaptiveColor));
		// Setup padding and rect radius
		final Paint.FontMetrics metrics = dayNumberTextPaint.getFontMetrics();
		padding = DisplayUtils.dp2px(context, 4f);
		roundRectRadius = DisplayUtils.dp2px(context, 7f);
		textBaseline = roundRectRadius - metrics.descent + (metrics.bottom - metrics.top) / 2 + DisplayUtils.dp2px(context, 1f);
	}

	@Override
	protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, boolean hasScheme) {
		// Draw selection rect
		mSelectedPaint.setStyle(Paint.Style.FILL);
		canvas.drawRoundRect(
				x,
				0f,
				x + mItemWidth,
				mItemHeight - padding,
				roundRectRadius,
				roundRectRadius,
				mSelectedPaint);
		AManager.log(TAG, "onDrawSelected: Scheme[%s]", calendar.getScheme());
		return true;
	}

	@Override
	protected void onDrawScheme(Canvas canvas, Calendar cal, int x) {
		// Draw a dot indicating that there's an event on this day
		mSelectedPaint.setColor(Color.GREEN);
		final int radius = DisplayUtils.dp2px(getContext(), 5f);
		canvas.drawCircle(
				x + mItemWidth / 2f,
				mItemHeight - radius,
				radius,
				mSchemePaint);
		AManager.log(TAG, "onDrawScheme");
	}

	@Override
	protected void onDrawText(Canvas canvas, Calendar cal, int x, boolean hasScheme, boolean isSelected) {
		final int hijriDay = new Timestamp(cal.getYear(), cal.getMonth() - 1, cal.getDay()).toHijri().getDay();
		dayNumberTextPaint.setTextSize(Math.max(mItemWidth / 2.5f, mItemHeight / 2.5f));
		dayNumberTextPaint.setColor(isSelected ? Color.RED : Color.BLACK);
		// Draw day number
		canvas.drawText(String.valueOf(hijriDay),
				x + mItemWidth / 2f,
				mItemHeight / 2f + padding,
				dayNumberTextPaint);
	}
}

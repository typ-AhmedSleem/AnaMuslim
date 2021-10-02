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
import com.haibin.calendarview.MonthView;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.utils.DisplayUtils;

public class HijriCalendarMonthView extends MonthView {

	// Statics
	private static final String TAG = "HijriCalendarMonthView";
	// For drawing
	private final float padding;
	private final float roundRectRadius;
	private final TextPaint dayTextPaint = new TextPaint();

	public HijriCalendarMonthView(Context context) {
		super(context);
		padding = DisplayUtils.dp2px(context, 3f);
		roundRectRadius = DisplayUtils.dp2px(context, 6f);
		dayTextPaint.setAntiAlias(true);
		dayTextPaint.setTextAlign(Paint.Align.CENTER);
		dayTextPaint.setColor(Color.BLACK);
		dayTextPaint.setTextSize(DisplayUtils.sp2px(context, 25f));
	}

	@Override
	protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
		mSelectedPaint.setStyle(Paint.Style.FILL);
		canvas.drawRoundRect(
				x + padding,
				y + padding,
				x + mItemWidth - padding,
				y + mItemHeight - padding,
				roundRectRadius,
				roundRectRadius,
				mSelectedPaint);
		AManager.log(TAG, "onDrawSelected: HS[%s]", hasScheme);
		return hasScheme;
	}

	@Override
	protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
		mSelectedPaint.setColor(Color.BLUE);
		canvas.drawCircle(
				x + mItemWidth / 2f,
				y + mItemHeight / 2f + padding,
				roundRectRadius,
				mSelectedPaint);
		AManager.log(TAG, "onDrawScheme");
	}

	@Override
	protected void onDrawText(Canvas canvas, Calendar cal, int x, int y, boolean hasScheme, boolean isSelected) {
		mCurMonthTextPaint.setColor(Color.BLACK);
		mOtherMonthTextPaint.setColor(Color.LTGRAY);
		dayTextPaint.setFakeBoldText(isSelected);
		dayTextPaint.setColor(Color.RED);
		canvas.drawText(String.valueOf(cal.getDay()),
				x + mItemWidth / 2f,
				y + (mItemHeight / 2f) + (dayTextPaint.measureText(String.valueOf(cal.getDay())) / 2f) - padding,
				isSelected ? dayTextPaint : cal.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
	}
}

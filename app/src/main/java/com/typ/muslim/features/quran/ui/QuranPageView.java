/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2022.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.features.quran.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.typ.muslim.ui.BaseView;

public class QuranPageView extends BaseView {

    // Statics
    private static final String TAG = "QuranPageView";
    // Paints
    private static final Paint paintBorder =new Paint();

    public QuranPageView(Context context) {
        super(context);
    }

    public QuranPageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public QuranPageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {

    }
}

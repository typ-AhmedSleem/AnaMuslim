/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.typ.muslim.core.praytime.enums.Prays;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.utils.DisplayUtils;

import java.util.Random;

public class PraysSceneryView extends FrameLayout {

    // Statics
    private static final String TAG = "SceneryView";
    // Views constants
    private static final int sunXTarget = 20;
    private static final int sunYTarget = 30;
    private static final int moonXTarget = 20;
    private static final int moonYTarget = 30;
    // Runtime
    private final int starsCount = 5;
    private final int cloudsCount = 0;
    private final boolean showSun = false;
    private final boolean showMoon = false;
    private final Random random = new Random();
    private final ValueAnimator colorAnimator = new ValueAnimator();
    Paint paint = new Paint();
    Paint p = new Paint();
    // Views
    private Sun sun;
    private int width = 0;

    public PraysSceneryView(@NonNull Context context) {
        super(context);
        prepareSceneryView(context);
    }

    public PraysSceneryView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        prepareSceneryView(context);
    }

    private void prepareSceneryView(Context context) {
        // Prepare color animator
        this.colorAnimator.setInterpolator(new LinearInterpolator());
        this.colorAnimator.setEvaluator(new ArgbEvaluator());
        this.colorAnimator.addUpdateListener(animation -> PraysSceneryView.this.setBackgroundColor((int) animation.getAnimatedValue()));
        // Dummy
        setBackgroundColor(Color.BLACK);
        int wh = DisplayUtils.dp2px(context, 70);
        // Add sun and moon
        sun = new Sun(context);
        sun.setX(0);
        sun.setY(sunYTarget);
        addView(sun, wh, wh);
        // Animate sun movement
        synchronized (this) {
            AManager.log(TAG, "SceneryView[W[%d] H[%d]] | Sun[W[%d] H[%d]]", getWidth(), getHeight(), sun.getWidth(), sun.getHeight());
            ObjectAnimator.ofFloat(sun, "x", 0, getWidth() + wh).setDuration(5000).start();
        }
    }

    public void changeScenery(Prays pray) {
        // Check if pray is null
        if (pray == null) return;
        // todo Prepare next scenery values before animating
        switch (pray) {
            case FAJR:
                break;
            case SUNRISE:
                break;
            case DHUHR:
                break;
            case ASR:
                break;
            case MAGHRIB:
                break;
            case ISHA:
                break;
        }
        // Start the color and views animations
        colorAnimator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        AManager.log(TAG, "MW[%d] MH[%d]", widthMeasureSpec, heightMeasureSpec);
    }

    private static class Sun extends View {

        // Runtime
        private final float radiusDiff;
        private final Paint paint = new Paint();

        public Sun(Context context) {
            super(context);
            // Prepare runtime
            paint.setAntiAlias(true);
            radiusDiff = (float) DisplayUtils.dp2px(context, 10);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            float outerRadius = Math.max(getWidth(), getHeight());
            // Draw outer circle
            paint.setColor(0x4DFFEB3B);
            canvas.drawCircle(outerRadius / 2, outerRadius / 2, outerRadius / 2, paint);
            // Draw middle circle
            paint.setColor(0x66FFEB3B);
            canvas.drawCircle(outerRadius / 2, outerRadius / 2, (outerRadius - radiusDiff) / 2, paint);
            // Draw inner circle
            paint.setColor(0xFFFFEB3B);
            canvas.drawCircle(outerRadius / 2, outerRadius / 2, (outerRadius - radiusDiff * 3) / 2, paint);
        }
    }

}

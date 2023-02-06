/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2022.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.libs.scenery;

import android.animation.Animator;
import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.typ.muslim.R;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.ui.BaseView;

public final class Moon extends BaseView {

    public Moon(Context context) {
        super(context);
    }

    public Moon(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Moon(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // Constants
    private static final int STATE_SHRINKING = 0;
    private static final int STATE_EXPANDING = 1;
    private final float CORE_RADIUS = 35f;
    private final float INNER_RING_MIN_RADIUS = 50f;
    private final float INNER_RING_MAX_RADIUS = 70f;
    private final float OUTER_RING_MIN_RADIUS = 70f;
    private final float OUTER_RING_MAX_RADIUS = 100f;
    // Runtime
    private int state = STATE_EXPANDING;
    private float innerRingRadius = INNER_RING_MIN_RADIUS;
    private float outerRingRadius = OUTER_RING_MIN_RADIUS;
    private Paint paintMoon = new Paint();

    @Override
    public void setupRuntime(@NonNull Context context) {
        // Setup paints
        if (paintMoon == null) paintMoon = new Paint();
        paintMoon.setAntiAlias(true);
        paintMoon.setColor(getColor(R.color.yellow));
        paintMoon.setStyle(Paint.Style.FILL);
        // Setup animator
        ValueAnimator ringsAnimator = ValueAnimator.ofInt(0, 20);
        ringsAnimator.setDuration(5000);
        ringsAnimator.setRepeatCount(ValueAnimator.INFINITE);
        ringsAnimator.setEvaluator(new IntEvaluator());
        ringsAnimator.setInterpolator(new LinearInterpolator());
        ringsAnimator.setRepeatMode(ValueAnimator.REVERSE);
        ringsAnimator.addUpdateListener(animation -> {
            final int delta = (int) animation.getAnimatedValue();
            if (state == STATE_EXPANDING) {
                if (innerRingRadius < INNER_RING_MAX_RADIUS && outerRingRadius < OUTER_RING_MAX_RADIUS) {
                    innerRingRadius += 0.05;
                    outerRingRadius += 0.08;
                }
            }
            if (state == STATE_SHRINKING) {
                if (innerRingRadius > INNER_RING_MIN_RADIUS && outerRingRadius > OUTER_RING_MIN_RADIUS) {
                    innerRingRadius -= 0.05;
                    outerRingRadius -= 0.08;
                }
            }
            invalidate(); // Re-draw the moon.
            AManager.log("Moon", "onUpdate: STATE[%d] DELTA[%d] INNER[%f] OUTER[%f]", state, delta, innerRingRadius, outerRingRadius);
        });
        ringsAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                if (state == STATE_EXPANDING) {
                    state = STATE_SHRINKING;
//                    innerRingRadius =;
//                    outerRingRadius =;
                } else {
                    state = STATE_EXPANDING;
//                    innerRingRadius =;
//                    outerRingRadius =;
                }
            }
        });
        ringsAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int cx = getWidth() / 2;
        final int cy = getHeight() / 2;

//        // Draw dark background to see the moon
//        canvas.drawColor(Color.BLACK);

        // Draw outer ring
        paintMoon.setColor(getColor(R.color.color_moon_outer_ring));
        canvas.drawCircle(cx, cy, dp2px(outerRingRadius), paintMoon);

        // Draw inner ring
        paintMoon.setColor(getColor(R.color.color_moon_inner_ring));
        canvas.drawCircle(cx, cy, dp2px(innerRingRadius), paintMoon);

        // Draw core
        paintMoon.setColor(getColor(R.color.color_moon_core));
        canvas.drawCircle(cx, cy, dp2px(CORE_RADIUS), paintMoon);
    }
}

/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.typ.muslim.R;
import com.typ.muslim.app.Consumers;
import com.typ.muslim.libs.easyjava.data.EasyList;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.managers.ResMan;
import com.typ.muslim.models.ProgressStep;

import org.jetbrains.annotations.NotNull;

public final class StepsProgressWheel extends BaseView {

    // Statics
    private static final String TAG = "StepsProgressWheel";
    // Paints
    private Paint paintBar = new Paint();
    private Paint paintStep = new Paint();
    private TextPaint paintValueText = new TextPaint();
    private TextPaint paintInfoText = new TextPaint();
    // Runtime
    private int percentage = 0;
    private int layoutWidth = 0;
    private int layoutHeight = 0;
    private float stepWidth = 24f;
    private float infoTextSize = 20f;
    private float valueTextSize = 40f;
    private float marginBtwTexts = 30f;
    private @Nullable
    String textInfo;
    private @Nullable
    String textValue;
    private @ColorInt
    int valueTextColor;
    private @ColorInt
    int infoTextColor;
    private EasyList<ProgressStep> steps;
    // Bounds
    private RectF rectBar;
    private RectF rectStep;

    public StepsProgressWheel(Context context) {
        super(context);
    }

    public StepsProgressWheel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Consumers.doIf(() -> steps = EasyList.createList(
                new ProgressStep(true, R.color.green),
                new ProgressStep(false, R.color.red),
                new ProgressStep(true, R.color.green),
                new ProgressStep(true, R.color.green),
                new ProgressStep(false, R.color.red)), isInEditMode());
    }

    public StepsProgressWheel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void parseAttrs(@NonNull @NotNull Context context, @Nullable AttributeSet attrs) {
        Consumers.doWhen(attrs != null, () -> {
            // When attrs specified
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.StepsProgressWheel);
            textInfo = ta.getString(R.styleable.StepsProgressWheel_spw_info_text);
            textValue = ta.getString(R.styleable.StepsProgressWheel_spw_value_text);
            stepWidth = ta.getDimension(R.styleable.StepsProgressWheel_spw_step_width, 24f);
            infoTextSize = ta.getDimension(R.styleable.StepsProgressWheel_spw_info_textSize, 20f);
            valueTextSize = ta.getDimension(R.styleable.StepsProgressWheel_spw_value_textSize, 40f);
            marginBtwTexts = ta.getDimension(R.styleable.StepsProgressWheel_spw_margin_btw_texts, 30f);
            infoTextColor = ta.getColor(R.styleable.StepsProgressWheel_spw_info_textColor, getColor(R.color.subtitleTextColor));
            valueTextColor = ta.getColor(R.styleable.StepsProgressWheel_spw_value_textColor, getColor(R.color.darkAdaptiveColor));
            ta.recycle();
        }, () -> {
            // Use defaults when no attrs is specified
            textInfo = null;
            textValue = null;
            stepWidth = 24f;
            infoTextSize = 20f;
            valueTextSize = 40f;
            marginBtwTexts = 30f;
            infoTextColor = getColor(R.color.subtitleTextColor);
            valueTextColor = getColor(R.color.darkAdaptiveColor);
        });
    }

    @Override
    public void setupRuntime(@NonNull @NotNull Context context) {
        if (steps == null) steps = new EasyList<>();
        setupPaints();
    }

    private void setupPaints() {
        // Bar paint
        if (paintBar == null) paintBar = new Paint();
        paintBar.setAntiAlias(true);
        paintBar.setStrokeWidth(stepWidth);
        paintBar.setStyle(Paint.Style.STROKE);
        paintBar.setColor(getColor(R.color.bg_input_box));
        // Step paint
        if (paintStep == null) paintStep = new Paint();
        paintStep.setAntiAlias(true);
        paintStep.setStrokeWidth(stepWidth);
        paintStep.setStyle(Paint.Style.STROKE);
        paintStep.setStrokeJoin(Paint.Join.ROUND);
        paintStep.setStrokeCap(Paint.Cap.ROUND);
        // Value TextPaint
        if (paintValueText == null) paintValueText = new TextPaint();
        paintValueText.setAntiAlias(true);
        paintValueText.setColor(valueTextColor);
        paintValueText.setTextSize(valueTextSize);
        paintValueText.setTextAlign(Paint.Align.CENTER);
        Consumers.doIfNot(() -> paintValueText.setTypeface(ResMan.getFont(getContext(), R.font.font_jf_flat_regular)), isInEditMode());
        // Info TextPaint
        if (paintInfoText == null) paintInfoText = new TextPaint();
        paintInfoText.setAntiAlias(true);
        paintInfoText.setColor(infoTextColor);
        paintInfoText.setTextSize(infoTextSize);
        paintInfoText.setTextAlign(Paint.Align.CENTER);
        Consumers.doIfNot(() -> paintInfoText.setTypeface(ResMan.getFont(getContext(), R.font.font_bukrar)), isInEditMode());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        layoutWidth = w;
        layoutHeight = h;
        setupBounds();
        setupPaints();
        invalidate();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setupBounds();
    }

    private void setupBounds() {
        // Bar bound
        rectBar = new RectF(
                getPaddingLeft() + stepWidth,
                getPaddingTop() + stepWidth,
                layoutWidth - getPaddingRight() - stepWidth,
                layoutHeight - getPaddingBottom() - stepWidth);
        // Step bound
        rectStep = new RectF(
                getPaddingLeft() + stepWidth,
                getPaddingTop() + stepWidth,
                layoutWidth - getPaddingRight() - stepWidth,
                layoutHeight - getPaddingBottom() - stepWidth
        );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Setup paints at first considering that anything changed
        setupPaints();
        // Setup layout bounds if needed
        if (rectBar == null || layoutWidth == 0 || layoutHeight == 0) {
            layoutWidth = getWidth();
            layoutHeight = getHeight();
            setupBounds();
        }
        // Draw rim
        canvas.drawCircle(rectBar.centerX(), rectBar.centerY(), rectBar.centerX() - stepWidth, paintBar);
        // Draw steps
        steps.iterate((pos, step) -> {
            // Start and target angles
            final float angleStep = getStepAngle();
            // Paint color
            paintStep.setColor(getColor(step.isDone() ? step.getColor() : R.color.red));
            // Draw the arc
            canvas.drawArc(
                    rectBar,
                    angleStep * (pos),
                    angleStep,
                    false,
                    paintStep);
        });
        // Draw Texts (if specified)
        Consumers.doIf(() -> canvas.drawText(textValue,
                layoutWidth / 2f,
                rectBar.centerY(),
                paintValueText), hasValueText());
        Consumers.doIf(() -> canvas.drawText(textInfo,
                layoutWidth / 2f,
                rectBar.centerY() + marginBtwTexts,
                paintInfoText), hasInfoText());
        AManager.log(TAG, "onDraw");
    }

    private float getStepAngle() {
        return steps.size() > 0 ? 360f / steps.size() : 0;
    }

    private void animateProgress() {
        // todo: Make the arcs drawing animated with steps colors
        // The animation will be used to draw every percentage and save the state of the canvas then
        // restore the canvas state to draw the next percentage
        final int startPercentage = percentage;
        final float endPercentage = (steps.countValues(step -> step.isDone() ? 1 : 0)) * getStepAngle();
        final ValueAnimator animator = ValueAnimator.ofInt(startPercentage, (int) endPercentage).setDuration(1000);
        animator.addUpdateListener(anim -> {
            percentage = (int) anim.getAnimatedValue();
            invalidate();
        });
        animator.start();
    }

    private boolean hasValueText() {
        return !TextUtils.isEmpty(textValue);
    }

    private boolean hasInfoText() {
        return !TextUtils.isEmpty(textInfo);
    }

    public StepsProgressWheel addStep(ProgressStep step) {
        if (step != null) {
            steps.add(step);
            invalidate();
        }
        return this;
    }

    public void removeStep(int stepPosition) {
        if (stepPosition >= steps.size()) {
            throw new IndexOutOfBoundsException("Can't remove a step at position [" + stepPosition + "] because it's out of the bounds");
        } else {
            steps.remove(stepPosition);
            invalidate();
        }
    }

    public void updateStep(int whichStep, boolean done) {
        if (whichStep >= steps.size()) return;
        steps.set(whichStep, steps.get(whichStep).setDone(done));
    }

    public void updateStep(int whichStep, boolean done, @ColorRes int color) {
        if (whichStep >= steps.size()) return;
        steps.set(whichStep, steps.get(whichStep).setDone(done).setColor(color));
    }

    public void clearSteps() {
        steps.clear();
        invalidate();
    }

    public void setStepWidth(@Dimension float dp) {
        stepWidth = dp2px(dp);
        setupBounds();
        invalidate();
    }

    public StepsProgressWheel setValueTextSize(@Dimension float dp) {
        valueTextSize = dp2px(dp);
        invalidate();
        return this;
    }

    public StepsProgressWheel setValueTextColor(@ColorRes int colorRes) {
        valueTextColor = getColor(colorRes);
        invalidate();
        return this;
    }

    public StepsProgressWheel setInfoTextSize(@Dimension float dp) {
        infoTextSize = dp2px(dp);
        invalidate();
        return this;
    }

    public StepsProgressWheel setInfoTextColor(@ColorRes int colorRes) {
        infoTextColor = getColor(colorRes);
        invalidate();
        return this;
    }

    public StepsProgressWheel setMarginBtwTexts(@Dimension float dp) {
        marginBtwTexts = dp2px(dp);
        invalidate();
        return this;
    }

    public StepsProgressWheel setSteps(ProgressStep... steps) {
        this.steps = EasyList.createList(steps);
        setupBounds();
        invalidate();
        return this;
    }

    public StepsProgressWheel setValueText(@Nullable String textValue) {
        this.textValue = textValue;
        invalidate();
        return this;
    }

    public StepsProgressWheel setInfoText(@Nullable String textInfo) {
        this.textInfo = textInfo;
        invalidate();
        return this;
    }

    public void drawSteps() {
        invalidate();
    }

}

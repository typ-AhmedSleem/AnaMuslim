/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2022.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.typ.muslim.R;

import java.util.ArrayList;
import java.util.List;

public class SegmentView extends BaseView {

    public static final @ColorRes int colorOnSelected = R.color.blue;
    public static final @ColorRes int textColorNormal = R.color.segment_view_text_nml;
    public static final @ColorRes int textColorOnSelected = R.color.segment_view_text_sel;
    private static final String TAG = "SegmentView";
    // Constants
    private static final int segmentsMargin = 20;
    private static final int segmentTextMargin = 5;
    private static final int segmentInnerPadding = 5;
    private static final int innerRadius = 20;
    private static final int segmentOuterCornerRadius = 20;
    // Colors
    private static final @ColorRes int colorBaseBg = R.color.segment_view_base_bg;
    // Segments
    private List<Segment> segments;
    private Paint paintSegment;
    private TextPaint paintText;
    // Runtime
    private int selectedSegment = 1;
    private boolean needsInit = true;
    private int width = 0, height = 0;
    // Drawing runtime
    private int segmentWidth = 0;
    private RectF rectViewBounds;
    private RectF rectSegmentBounds;
    private int lastX = 0, lastY = 0;
    // Listener
    private OnSegmentSelectedListener listener;

    public SegmentView(Context context) {
        super(context);
    }

    public SegmentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SegmentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // Getters and Setters
    public Segment getSegment(int id) {
        return this.segments.get(id);
    }

    public void addSegment(Segment segment) {
        this.segments.add(segment);
        // Invalidate the view
        needsInit = true;
        invalidate();
    }

    public void removeSegment(int id) {
        this.segments.remove(id);
        // Invalidate the view
        needsInit = true;
        invalidate();
    }

    public void selectSegment(int id) {
        if (this.segments.size() < id) return;
        // Update runtime
        this.selectedSegment = id;
        // Invalidate the view
        this.needsInit = true;
        this.invalidate();
    }

    public void setListener(OnSegmentSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public void setupRuntime(@NonNull Context context) {
        // FOR TEXT ONLY
        segments = new ArrayList<>();
        segments.add(new Segment(1, R.string.today_prays));
        segments.add(new Segment(2, R.string.qibla));
        segments.add(new Segment(3, R.string.hijri_calendar));
        // Setup paints
        paintSegment = new Paint();
        paintSegment.setAntiAlias(true);
        paintSegment.setStyle(Paint.Style.FILL);
        paintSegment.setColor(isInEditMode() ? Color.parseColor("#F5F5F5") : getColor(colorBaseBg));
        paintText = new TextPaint();
        paintText.setAntiAlias(true);
        paintText.setTextSize(sp2px(14f));
        paintText.setTextAlign(Paint.Align.CENTER);
        paintText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        // Reset selection
        this.selectedSegment = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Initialize dimensions and bounds
        if (needsInit) {
            // Update runtime
            this.width = getWidth();
            this.height = getHeight();
            this.lastX = segmentsMargin;
            this.lastY = segmentsMargin;
            this.segmentWidth = (this.width - (segmentsMargin * 2) - (segmentsMargin * (segments.size() - 1))) / segments.size();
            // Dimensions
            rectViewBounds = new RectF(0, 0, width, height);
            rectSegmentBounds = new RectF(0, 0, width, height - segmentsMargin);
            needsInit = false;
        }
        // Draw the base bar
        canvas.drawRoundRect(rectViewBounds, segmentOuterCornerRadius, segmentOuterCornerRadius, paintSegment);
        // Draw segments
        this.lastX = 0;
        this.lastY = 0;
        for (Segment segment : this.segments) {
            // Draw selection
            if (selectedSegment == segment.id) {
                paintSegment.setColor(isInEditMode() ? Color.parseColor("#FFFFFF") : getColor(colorOnSelected));
                // Bounds
                float left = segment.id > 1 ? (lastX + segmentsMargin) : 0;
                canvas.drawRoundRect(left, 0, lastX + segmentWidth, height, innerRadius, innerRadius, paintSegment);
            }
            // Draw text
            // Update x,y
            this.lastX += this.segmentWidth;
        }
    }

    // Interfaces
    public interface OnSegmentSelectedListener {

        void onSegmentSelected(Segment segment);

    }

    // Model
    public static class Segment {

        public final int id;
        public final @StringRes int title;
        public final @ColorRes int colorOnSelected = R.color.segment_view_sel_bg;
        public final @ColorRes int textColorOnSelected = R.color.segment_view_text_sel;

        public Segment(int id, @StringRes int title) {
            this.id = id;
            this.title = title;
        }
    }


}

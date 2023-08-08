/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */
package com.typ.muslim.ui

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import com.typ.muslim.R
import com.typ.muslim.app.Consumers
import com.typ.muslim.managers.AManager
import com.typ.muslim.managers.ResMan
import com.typ.muslim.models.ProgressStep
import com.typ.muslim.utils.Counter
import com.typ.muslim.utils.colorRes
import com.typ.muslim.utils.dp2px

class StepsProgressWheel constructor(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    // Paints
    private var paintBar: Paint? = Paint()
    private var paintStep: Paint? = Paint()
    private var paintValueText: TextPaint? = TextPaint()
    private var paintInfoText: TextPaint? = TextPaint()

    // Runtime
    private var percentage = 0
    private var layoutWidth = 0
    private var layoutHeight = 0
    private var stepWidth = 24f
    private var infoTextSize = 20f
    private var valueTextSize = 40f
    private var marginBtwTexts = 30f
    private var textInfo: String? = null
    private var textValue: String? = null

    private var needsSetup = true
    private var animating = false
    private val allStepsDone: Boolean
        get() {
            return steps.none { !it.isDone }
        }

    @ColorInt
    private var valueTextColor = 0

    @ColorInt
    private var infoTextColor = 0
    private val steps = mutableListOf<ProgressStep>()

    // Bounds
    private var rectBar: RectF? = null
    private var rectStep: RectF? = null

    init {
        parseAttrs(context, attrs)
        setupPaints()
    }

    fun parseAttrs(context: Context, attrs: AttributeSet?) {
        Consumers.doWhen(attrs != null, {

            // When attrs specified
            val ta = context.obtainStyledAttributes(attrs, R.styleable.StepsProgressWheel)
            textInfo = ta.getString(R.styleable.StepsProgressWheel_spw_info_text)
            textValue = ta.getString(R.styleable.StepsProgressWheel_spw_value_text)
            stepWidth = ta.getDimension(R.styleable.StepsProgressWheel_spw_step_width, 24f)
            infoTextSize = ta.getDimension(R.styleable.StepsProgressWheel_spw_info_textSize, 20f)
            valueTextSize = ta.getDimension(R.styleable.StepsProgressWheel_spw_value_textSize, 40f)
            marginBtwTexts = ta.getDimension(R.styleable.StepsProgressWheel_spw_margin_btw_texts, 30f)
            infoTextColor = ta.getColor(R.styleable.StepsProgressWheel_spw_info_textColor, colorRes(getContext(), R.color.subtitleTextColor))
            valueTextColor = ta.getColor(R.styleable.StepsProgressWheel_spw_value_textColor, colorRes(getContext(), R.color.darkAdaptiveColor))
            ta.recycle()
        }) {

            // Use defaults when no attrs is specified
            textInfo = null
            textValue = null
            stepWidth = 24f
            infoTextSize = 20f
            valueTextSize = 40f
            marginBtwTexts = 30f
            infoTextColor = colorRes(getContext(), R.color.subtitleTextColor)
            valueTextColor = colorRes(getContext(), R.color.darkAdaptiveColor)
        }
    }

    fun setupRuntime(context: Context) {
        setupPaints()
    }

    private fun setupPaints() {
        if (needsSetup) {
            // Bar paint
            if (paintBar == null) paintBar = Paint()
            paintBar!!.isAntiAlias = true
            paintBar!!.strokeWidth = stepWidth
            paintBar!!.style = Paint.Style.STROKE
            paintBar!!.color = colorRes(getContext(), R.color.bg_input_box)
            // Step paint
            if (paintStep == null) paintStep = Paint()
            paintStep!!.isAntiAlias = true
            paintStep!!.strokeWidth = stepWidth
            paintStep!!.style = Paint.Style.STROKE
            paintStep!!.strokeJoin = Paint.Join.ROUND
            paintStep!!.strokeCap = Paint.Cap.ROUND
            // Value TextPaint
            if (paintValueText == null) paintValueText = TextPaint()
            paintValueText!!.isAntiAlias = true
            paintValueText!!.color = valueTextColor
            paintValueText!!.textSize = valueTextSize
            paintValueText!!.textAlign = Paint.Align.CENTER
            Consumers.doIfNot({ paintValueText!!.typeface = ResMan.getFont(context, R.font.font_jf_flat_regular) }, isInEditMode)
            // Info TextPaint
            if (paintInfoText == null) paintInfoText = TextPaint()
            paintInfoText!!.isAntiAlias = true
            paintInfoText!!.color = infoTextColor
            paintInfoText!!.textSize = infoTextSize
            paintInfoText!!.textAlign = Paint.Align.CENTER
            Consumers.doIfNot({ paintInfoText!!.typeface = ResMan.getFont(context, R.font.font_bukrar) }, isInEditMode)
            needsSetup = false
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        layoutWidth = w
        layoutHeight = h
        setupBounds()
        setupPaints()
        invalidate()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setupBounds()
    }

    private fun setupBounds() {
        // Bar bound
        rectBar = RectF(
            paddingLeft + stepWidth,
            paddingTop + stepWidth,
            layoutWidth - paddingRight - stepWidth,
            layoutHeight - paddingBottom - stepWidth
        )
        // Step bound
        rectStep = RectF(
            paddingLeft + stepWidth,
            paddingTop + stepWidth,
            layoutWidth - paddingRight - stepWidth,
            layoutHeight - paddingBottom - stepWidth
        )
    }

    override fun onDraw(canvas: Canvas) {
        // Setup paints at first considering that anything changed
        setupPaints()
        // Setup layout bounds if needed
        if (rectBar == null || layoutWidth == 0 || layoutHeight == 0) {
            layoutWidth = width
            layoutHeight = height
            setupBounds()
        }
        // Draw
        // Draw rim (if not
        canvas.drawCircle(rectBar!!.centerX(), rectBar!!.centerY(), rectBar!!.centerX() - stepWidth, paintBar!!)
        // Draw steps
        steps.onEachIndexed { pos: Int, step: ProgressStep ->
            // Start and target angles
            val angleStep = stepAngle
            // Paint color
            paintStep!!.color = colorRes(getContext(), if (step.isDone) step.color else R.color.bg_input_box)
            // Draw the arc
            canvas.drawArc(
                rectBar!!,
                angleStep * pos,
                angleStep,
                false,
                paintStep!!
            )
        }
        // Value text (if specified)
        if (hasValueText()) {
            canvas.drawText(
                textValue!!,
                layoutWidth / 2f,
                rectBar!!.centerY(),
                paintValueText!!
            )
        }
        // Info text (if specified)
        if (hasInfoText()) {
            canvas.drawText(
                textInfo!!,
                layoutWidth / 2f,
                rectBar!!.centerY() + marginBtwTexts,
                paintInfoText!!
            )
        }
        AManager.log(TAG, "onDraw")
    }

    private val stepAngle: Float
        get() = if (steps.size > 0) 360f / steps.size else 0f

    private fun animateProgress() {
        // todo: Make the arcs drawing animated with steps colors
        // The animation will be used to draw every percentage and save the state of the canvas then
        // restore the canvas state to draw the next percentage
        val startPercentage = percentage
        val endPercentage = Counter.countValues(steps) { step: ProgressStep -> if (step.isDone) 1 else 0 } * stepAngle
        val animator = ValueAnimator.ofInt(startPercentage, endPercentage.toInt()).setDuration(1000)
        animator.addUpdateListener { anim: ValueAnimator ->
            percentage = anim.animatedValue as Int
            invalidate()
        }
        animator.start()
    }

    private fun hasValueText(): Boolean {
        return !TextUtils.isEmpty(textValue)
    }

    private fun hasInfoText(): Boolean {
        return !TextUtils.isEmpty(textInfo)
    }

    fun addStep(step: ProgressStep?): StepsProgressWheel {
        if (step != null) {
            steps.add(step)
            invalidate()
        }
        return this
    }

    fun removeStep(stepPosition: Int) {
        if (stepPosition >= steps.size) {
            throw IndexOutOfBoundsException("Can't remove a step at position [$stepPosition] because it's out of the bounds")
        } else {
            steps.removeAt(stepPosition)
            invalidate()
        }
    }

    fun updateStep(whichStep: Int, done: Boolean) {
        if (whichStep >= steps.size) return
        steps[whichStep] = steps[whichStep].setDone(done)
    }

    fun updateStep(whichStep: Int, done: Boolean, @ColorRes color: Int) {
        if (whichStep >= steps.size) return
        steps[whichStep] = steps[whichStep].setDone(done).setColor(color)
    }

    fun clearSteps() {
        steps.clear()
        invalidate()
    }

    fun setStepWidth(@Dimension dp: Float) {
        stepWidth = dp2px(context, dp).toFloat()
        setupBounds()
        invalidate()
    }

    fun setValueTextSize(@Dimension dp: Float): StepsProgressWheel {
        valueTextSize = dp2px(context, dp).toFloat()
        invalidate()
        return this
    }

    fun setValueTextColor(@ColorRes colorRes: Int): StepsProgressWheel {
        valueTextColor = colorRes(getContext(), colorRes)
        invalidate()
        return this
    }

    fun setInfoTextSize(@Dimension dp: Float): StepsProgressWheel {
        infoTextSize = dp2px(context, dp).toFloat()
        invalidate()
        return this
    }

    fun setInfoTextColor(@ColorRes colorRes: Int): StepsProgressWheel {
        infoTextColor = colorRes(getContext(), colorRes)
        invalidate()
        return this
    }

    fun setMarginBtwTexts(@Dimension dp: Float): StepsProgressWheel {
        marginBtwTexts = dp2px(context, dp).toFloat()
        invalidate()
        return this
    }

    fun setSteps(vararg steps: ProgressStep): StepsProgressWheel {
        this.steps.clear()
        steps.toCollection(this.steps)
        setupBounds()
        invalidate()
        return this
    }

    fun setValueText(textValue: String?): StepsProgressWheel {
        this.textValue = textValue
        invalidate()
        return this
    }

    fun setInfoText(textInfo: String?): StepsProgressWheel {
        this.textInfo = textInfo
        invalidate()
        return this
    }

    fun drawSteps() {
        invalidate()
    }

    companion object {
        // Statics
        private const val TAG = "StepsProgressWheel"
    }
}
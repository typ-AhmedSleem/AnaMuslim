/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import com.typ.muslim.utils.DisplayUtils
import java.io.Serializable
import java.util.*

class SegementTabs(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

	// Runtime
	private val tabs = ArrayList<SegementTab>()

	// Dimens
	private val pdg = 10f
	private var cornerRad = 20f
	private var textSize = 20f

	// Drawing
	private val paintBg = Paint()
	private val paintSelected = Paint()
	private val tpSelected = TextPaint()
	private val tpUnSelected = TextPaint()

	init {
		cornerRad = DisplayUtils.dp2px(context, 20f).toFloat()
		textSize = DisplayUtils.sp2px(context, 15f).toFloat()
		paintBg.apply {
			isAntiAlias = true
			color = Color.LTGRAY
		}
		paintSelected.apply {
			isAntiAlias = true
			color = Color.GREEN
		}
		tpSelected.apply {
			isAntiAlias = true
			color = Color.BLACK
		}
		tpUnSelected.apply {
			isAntiAlias = true
			color = Color.GRAY
		}
	}

	override fun onDraw(canvas: Canvas?) {
		canvas?.run {
			drawRoundRect(pdg, pdg, width - pdg, height - pdg, cornerRad, cornerRad, paintBg)
		}
	}

	class SegementTab(val name: String, @field:ColorInt val color: Int) : Serializable

}
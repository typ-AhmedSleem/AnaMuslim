/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */
package com.typ.muslim.ui.khatma.dashboard.views

import android.content.Context
import android.util.AttributeSet
import com.typ.muslim.R
import com.typ.muslim.ui.ViewContainer

class NoActiveKhatmaView : ViewContainer {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun prepareView(context: Context) {
        // Inflate view
        inflate(context, R.layout.layout_no_khatma_view, this)
    }
}
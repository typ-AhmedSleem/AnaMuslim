/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */
package com.typ.muslim.ui.prays.views

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.google.android.material.textview.MaterialTextView
import com.mpt.android.stv.SpannableTextView
import com.typ.muslim.R
import com.typ.muslim.features.prays.interfaces.PrayTimeCameListener
import com.typ.muslim.features.prays.models.Pray
import com.typ.muslim.models.Timestamp
import com.typ.muslim.ui.ViewContainer
import com.typ.muslim.ui.prays.PrayViewerBottomSheet

class SquarePrayView : ViewContainer, PrayTimeCameListener {

    // Runtime
    private lateinit var pray: Pray

    // Views
    private lateinit var ivIconIndicator: ImageView
    private lateinit var stvPrayName: SpannableTextView
    private lateinit var tvPrayTime: MaterialTextView

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun prepareView(context: Context) {
        inflate(R.layout.view_square_pray)
        ivIconIndicator = findViewById(R.id.img_pray_icon_indicator)
        stvPrayName = findViewById(R.id.stv_pray_name)
        tvPrayTime = findViewById(R.id.tv_pray_time)
    }

    fun setPray(pray: Pray): SquarePrayView {
        // Update runtime
        this.pray = pray
        // Set texts
        stvPrayName.reset()
        stvPrayName.setText(pray.prayNameRes)
        tvPrayTime.text = pray.getFormattedTime(context)
        // Change indicator or show icon
        if (pray.time.isBefore(Timestamp.NOW()) || pray.time.isToday) {
            // Show indicator
            if (pray.passed) {
                ivIconIndicator.setImageResource(R.drawable.ic_done)
                ivIconIndicator.setBackgroundResource(R.drawable.shape_passed_pray)
                ivIconIndicator.backgroundTintList = ColorStateList.valueOf(getColor(R.color.green))
            } else {
                ivIconIndicator.setImageDrawable(null)
                ivIconIndicator.setBackgroundResource(R.drawable.shape_next_pray)
                ivIconIndicator.backgroundTintList = ColorStateList.valueOf(getColor(R.color.green))
            }
        } else {
            // Show icon
            ivIconIndicator.setBackgroundResource(R.drawable.shape_next_pray)
            ivIconIndicator.backgroundTintList = ColorStateList.valueOf(getColor(R.color.darkAdaptiveColor))
        }
        return this
    }

    override fun onPrayTimeCame(pray: Pray): Pray {
        setPray(this.pray)
        return pray
    }

    override fun onClick(v: View) {
        PrayViewerBottomSheet(context, pray).show()
        // todo: if the pray has passed or is the next pray showing the info and count down of remaining time to the next pray
    }

    companion object {
        private const val TAG = "SquarePrayView"
    }
}

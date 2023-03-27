package com.typ.muslim.ui.khatma.planner

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.util.AttributeSet
import com.mpt.android.stv.Slice
import com.mpt.android.stv.SpannableTextView
import com.typ.muslim.R
import com.typ.muslim.enums.FormatPatterns
import com.typ.muslim.features.khatma.data.KhatmaPlans
import com.typ.muslim.features.khatma.models.KhatmaPlan
import com.typ.muslim.features.quran.Quran
import com.typ.muslim.managers.LocaleManager
import com.typ.muslim.models.Timestamp
import com.typ.muslim.ui.home.DashboardCard
import com.typ.muslim.ui.utils.ViewUtils.dp
import com.typ.muslim.ui.utils.ViewUtils.sp
import com.typ.muslim.ui.utils.ViewUtils.toColor
import java.util.Calendar

class KhatmaPlanButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : DashboardCard(context, attrs) {

    // Runtime and Views
    private lateinit var stvDuration: SpannableTextView
    private lateinit var stvStartEnd: SpannableTextView
    private lateinit var stvWerdSize: SpannableTextView
    lateinit var plan: KhatmaPlan

    override fun prepareRuntime(context: Context?) {
        this.plan = KhatmaPlans.plan30Days
    }

    override fun prepareCardView(context: Context) {
        // Init card
        super.setRadius(20f)
        super.setCardBackgroundColor(getColor(R.color.white))
        super.setStrokeWidth(0.5f.dp(context))
        super.setStrokeColor((R.color.btn_stroke_color).toColor(context))
        super.setContentPadding(5, 5, 5, 5)
        super.setRippleColor(ColorStateList.valueOf(getColor(R.color.bg_container)))
        inflate(context, R.layout.layout_khatma_plan_button, this)
        // Init views
        stvDuration = findViewById(R.id.stv_plan_duration)
        stvStartEnd = findViewById(R.id.stv_plan_start_end)
        stvWerdSize = findViewById(R.id.stv_plan_werd_size)
        // Display data in views
        refreshUI()
    }

    override fun refreshUI() {
        val locale = LocaleManager.getCurrLocale(context)
        if (isInEditMode) plan = KhatmaPlans.plan30Days // For viewing purpose only.
        // Reset STVs
        stvDuration.reset()
        stvStartEnd.reset()
        stvWerdSize.reset()
        // Plan duration
        stvDuration.apply {
            addSlice(
                Slice.Builder(String.format(locale, "%d", plan.duration))
                    .textColor((R.color.darkAdaptiveColor).toColor(context))
                    .textSize(26f.sp(context))
                    .style(Typeface.BOLD)
                    .build()
            )
            addSlice(
                Slice.Builder(
                    "\n" + getString(R.string.days)
                )
                    .textColor((R.color.darkGray).toColor(context))
                    .textSize(10f.sp(context))
                    .build()
            )
        }
        // Start and End dates
        stvStartEnd.apply {
            addSlice(
                Slice.Builder("%s\t ".format(locale, getString(R.string.starts)))
                    .textSize(14f.dp(context))
                    .build()
            )
            addSlice(
                Slice.Builder(" %s".format(locale, Timestamp.NOW().getFormatted(FormatPatterns.DATE_MONTH)))
                    .textSize(18f.sp(context))
                    .textColor((R.color.green).toColor(context))
                    .build()
            )
            addSlice(
                Slice.Builder("\t %s\t".format(locale, getString(R.string.ends)))
                    .textSize(14f.dp(context))
                    .build()
            )
            addSlice(
                Slice.Builder(Timestamp.NOW().roll(Calendar.DATE, plan.duration).getFormatted(FormatPatterns.DATE_MONTH))
                    .textSize(18f.sp(context))
                    .textColor((R.color.green).toColor(context))
                    .build()
            )
        }
        // Werd size
        stvWerdSize.apply {
            val werdSize = Quran.QURAN_JUZ2S_COUNT / plan.duration.toFloat()
            addSlice(
                Slice.Builder(
                    "(%.1f %s %s) × %d %s".format(
                        locale,
                        werdSize,
                        getString(werdSize.toInt(), R.string.part, R.string.parts),
                        getString(R.string.a_day),
                        plan.duration,
                        getString(plan.duration, R.string.day, R.string.days)
                    )
                ).textColor((R.color.darkGray).toColor(context))
                    .textSize(16f.sp(context)).build()
            )
        }
        // Display content
        stvDuration.display()
        stvStartEnd.display()
        stvWerdSize.display()
    }

    fun setPlan(plan: KhatmaPlan, refreshUI: Boolean = true) {
        this.plan = plan
        if (refreshUI) refreshUI()
    }

}
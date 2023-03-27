/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */
package com.typ.muslim.ui.khatma

import android.animation.LayoutTransition
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.progresviews.ProgressWheel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.typ.muslim.R
import com.typ.muslim.enums.FormatPatterns
import com.typ.muslim.features.khatma.KhatmaManager
import com.typ.muslim.features.khatma.KhatmaManager.Companion.getLastActiveKhatma
import com.typ.muslim.features.khatma.KhatmaManager.Companion.manageKhatma
import com.typ.muslim.features.khatma.data.KhatmaPlans.plan10Days
import com.typ.muslim.features.khatma.data.KhatmaPlans.plan30Days
import com.typ.muslim.features.khatma.interfaces.KhatmaManagerCallback
import com.typ.muslim.features.khatma.models.Khatma
import com.typ.muslim.features.khatma.models.KhatmaPlan
import com.typ.muslim.features.khatma.models.KhatmaWerd
import com.typ.muslim.features.quran.Quran
import com.typ.muslim.interfaces.ResultCallback
import com.typ.muslim.managers.LocaleManager
import com.typ.muslim.ui.AnimatedTextView
import com.typ.muslim.ui.BottomSheets.BaseBottomSheet
import com.typ.muslim.ui.ViewContainer
import com.zyyoona7.wheel.WheelView
import java.util.Objects

class KhatmaActivity : AppCompatActivity(), KhatmaManagerCallback {
    // Runtime
    private var manager: KhatmaManager? = null

    // Views
    private var flContainer: FrameLayout? = null
    private var akv: ActiveKhatmaView? = null
    private var nkv: NoKhatmaView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        findViewById<View>(android.R.id.content).transitionName = "transition_card_to_activity"
        // Setup transitions
        val transition = MaterialContainerTransform()
        transition.addTarget(android.R.id.content)
        transition.scrimColor = Color.TRANSPARENT
        transition.drawingViewId = android.R.id.content
        // Setup the activity
        window.sharedElementEnterTransition = transition
        window.sharedElementReenterTransition = transition
        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        super.onCreate(savedInstanceState)
        // Hide default actionBar
        if (supportActionBar != null) supportActionBar!!.hide()
        // Setup Khatma Manager instance
        manager = manageKhatma(this, getLastActiveKhatma(this), this)
        // Set content to activity
        setContentView(flContainer)
    }

    override fun onPrepareManager() {
        // Setup container view
        flContainer = FrameLayout(this)
        flContainer!!.layoutTransition = LayoutTransition()
        // Setup khatma views
        akv = ActiveKhatmaView(this)
        nkv = NoKhatmaView(this)
    }

    override fun onManageKhatma(khatma: Khatma) {
        flContainer!!.removeAllViews()
        flContainer!!.addView(akv, FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        akv!!.setKhatma(khatma)
    }

    override fun onHaveNoKhatma() {
        flContainer!!.removeAllViews()
        flContainer!!.addView(nkv, FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
    }

    override fun onProgressUpdated(nextWerd: KhatmaWerd) {
        if (flContainer!!.childCount > 0) {
            val akv = flContainer!!.getChildAt(0) as ActiveKhatmaView
            akv.refreshUI()
        }
    }

    override fun onSwitchKhatma(khatma: Khatma) {
        finishAfterTransition()
    }

    override fun onFinishKhatma() {}

    class CreateKhatmaBottomSheet(
        context: Context?, // Callbacks
        private val callback: ResultCallback<Khatma>
    ) : BaseBottomSheet(context, null, true) {
        // Runtime
        private var plan: KhatmaPlan? = null

        // Views
        private var inputKhatmaName: TextInputLayout? = null
        private var atvExpectedEnd: AnimatedTextView? = null
        private var wheelPlanPicker: WheelView<KhatmaPlan>? = null
        private var btnCreate: MaterialButton? = null

        init {
            setupListeners()
            bindInnerViews()
        }

        override fun prepareRuntime() {
            if (plan == null) plan = plan10Days
        }

        override fun onCreateView(): View {
            return inflate(R.layout.bs_create_khatma)
        }

        override fun prepareInnerViews() {
            inputKhatmaName = `$`(R.id.til_khatma_name)
            atvExpectedEnd = `$`(R.id.atv_plan_expected_end)
            wheelPlanPicker = `$`(R.id.wheel_plan_picker)
            btnCreate = `$`(R.id.btn_create_khatma)
        }

        override fun bindInnerViews() {
            Objects.requireNonNull(inputKhatmaName!!.editText)?.text = null
            wheelPlanPicker!!.selectedItemPosition = plan30Days.duration
            wheelPlanPicker!!.data = mutableListOf()
        }

        override fun setupListeners() {
            btnCreate!!.setOnClickListener { v: View? -> cancel() }
            wheelPlanPicker!!.onItemSelectedListener = WheelView.OnItemSelectedListener { wheelView: WheelView<KhatmaPlan>?, plan: KhatmaPlan?, position: Int -> }
        }
    }

    class CongratulationBottomSheet(context: Context?) : BaseBottomSheet(context, null, true) {
        // Views
        private val btnDismiss: MaterialButton? = null

        init {
            setupListeners()
            bindInnerViews()
        }

        override fun prepareRuntime() {}
        override fun onCreateView(): View {
            return inflate(R.layout.bs_congratulations_on_khatma)
        }

        override fun prepareInnerViews() {}
        override fun bindInnerViews() {}
        override fun setupListeners() {}
    }

    private inner class NoKhatmaView(context: Context) : ViewContainer(context) {
        override fun prepareView(context: Context) {
            inflate(R.layout.act_no_khatma_view)
            // Setup listeners of buttons
            findViewById<View>(R.id.btn_join_khatma).setOnClickListener { v: View? ->
                // todo: Open JoinKhatmaBottomSheet and listen for result
                Toast.makeText(context, "Under development.. will be available in the next update.", Toast.LENGTH_SHORT).show()
            }
            findViewById<View>(R.id.btn_create_khatma).setOnClickListener { v: View? -> CreateKhatmaBottomSheet(context) { khatma: Khatma? -> }.show() }
        }
    }

    private inner class ActiveKhatmaView(context: Context) : ViewContainer(context) {
        // Runtime
        private var khatma: Khatma? = null

        // Views
        private var pwPercentage: ProgressWheel? = null
        private var tvCompletedDays: MaterialTextView? = null
        private var tvName: MaterialTextView? = null
        private var tvPlanDays: MaterialTextView? = null
        private var tvRemDays: MaterialTextView? = null
        private var tvStartedIn: MaterialTextView? = null
        private var tvEndsIn: MaterialTextView? = null
        private var tvWerdStartSurah: MaterialTextView? = null
        private var tvWerdStartAyah: MaterialTextView? = null
        private var tvWerdEndSurah: MaterialTextView? = null
        private var tvWerdEndAyah: MaterialTextView? = null
        override fun prepareView(context: Context) {
            inflate(R.layout.layout_active_khatma_view)
            // Setup views
            pwPercentage = `$`(R.id.prw_khatma_rem_parts)
            tvCompletedDays = `$`(R.id.tv_khatma_completed_days)
            tvName = `$`(R.id.tv_khatma_num_name)
            tvPlanDays = `$`(R.id.tv_khatma_plan)
            tvRemDays = `$`(R.id.tv_khatma_rem_days)
            tvStartedIn = `$`(R.id.tv_khatma_start_date)
            tvEndsIn = `$`(R.id.tv_khatma_end_date)
            tvWerdStartSurah = `$`(R.id.tv_werd_start_surah)
            tvWerdStartAyah = `$`(R.id.tv_werd_start_ayah)
            tvWerdEndSurah = `$`(R.id.tv_werd_end_surah)
            tvWerdEndAyah = `$`(R.id.tv_werd_end_ayah)
            val btnViewProgress = `$`<MaterialButton>(R.id.btn_save_khatma_progress)
            val btnDeleteKhatma = `$`<MaterialButton>(R.id.btn_delete_khatma)
            val btnViewHistory = `$`<MaterialButton>(R.id.btn_khatma_history)
            // Setup listeners
            btnViewProgress.setOnClickListener { v: View? ->
                Handler().postDelayed({}, (10 * 1000).toLong())
                manager!!.saveProgress() // Save progress.
            }
            btnDeleteKhatma.setOnClickListener { v: View? -> manager!!.delete() } // todo: Ask user first for confirmation.
            btnViewHistory.setOnClickListener { v: View? -> Toast.makeText(context, "Under development.. Will be available in the next version.", Toast.LENGTH_SHORT).show() }
        }

        fun setKhatma(khatma: Khatma) {
            this.khatma = khatma
            refreshUI()
        }

        override fun refreshUI() {
            if (khatma == null) {
                onHaveNoKhatma()
                return
            } else if (manager!!.holdsActiveKhatma.not()) {
                onHaveNoKhatma()
                return
            }
            // Khatma details
            tvCompletedDays!!.text = khatma!!.name
            pwPercentage!!.setStepCountText(khatma!!.progressPercentage.toString() + "%")
            pwPercentage!!.setPercentage((khatma!!.progressPercentage / 100f * 360).toInt())
            tvName!!.text = tvCompletedDays!!.text
            tvPlanDays!!.text = String.format(LocaleManager.getCurrLocale(context), "%d %s (%d/%s)", khatma!!.plan.duration, getString(R.string.days), khatma!!.werdLength, getString(R.string.day))
            val remParts = Quran.QURAN_JUZ2S_COUNT - khatma!!.completedWerds * khatma!!.werdLength
            tvRemDays!!.text = String.format(LocaleManager.getCurrLocale(context), "%d %s", remParts, getString(if (remParts == 1) R.string.part else R.string.parts))
            tvStartedIn!!.text = khatma!!.startedIn.getFormatted(FormatPatterns.DATE_NORMAL)
            tvEndsIn!!.text = khatma!!.expectedEnd.getFormatted(FormatPatterns.DATE_NORMAL)
            // Werd
            khatma!!.currentWerd
            tvWerdStartSurah!!.text = khatma!!.currentWerd.start.surah.getName(context)
            tvWerdStartAyah!!.text = khatma!!.currentWerd.start.number.toString()
            tvWerdEndSurah!!.text = khatma!!.currentWerd.end.surah.getName(context)
            tvWerdEndAyah!!.text = khatma!!.currentWerd.end.number.toString()
        }
    }
}
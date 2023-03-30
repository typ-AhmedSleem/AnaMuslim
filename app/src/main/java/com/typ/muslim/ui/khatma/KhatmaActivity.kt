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
import com.irozon.alertview.AlertActionStyle
import com.irozon.alertview.AlertStyle
import com.irozon.alertview.AlertTheme
import com.irozon.alertview.AlertView
import com.irozon.alertview.objects.AlertAction
import com.typ.muslim.R
import com.typ.muslim.enums.FormatPatterns
import com.typ.muslim.features.khatma.KhatmaManager
import com.typ.muslim.features.khatma.data.KhatmaPlans.plan10Days
import com.typ.muslim.features.khatma.data.KhatmaPlans.plan30Days
import com.typ.muslim.features.khatma.interfaces.KhatmaManagerCallback
import com.typ.muslim.features.khatma.models.Khatma
import com.typ.muslim.features.khatma.models.KhatmaPlan
import com.typ.muslim.features.khatma.models.KhatmaWerd
import com.typ.muslim.interfaces.ResultCallback
import com.typ.muslim.managers.AManager
import com.typ.muslim.managers.LocaleManager
import com.typ.muslim.ui.AnimatedTextView
import com.typ.muslim.ui.BottomSheets.BaseBottomSheet
import com.typ.muslim.ui.ViewContainer
import com.zyyoona7.wheel.WheelView

class KhatmaActivity : AppCompatActivity(), KhatmaManagerCallback {


    private val tag = "KhatmaActivity"
    private lateinit var manager: KhatmaManager

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
        transition.scrimColor = Color.WHITE
        transition.drawingViewId = android.R.id.content
        // Setup the activity
        window.sharedElementEnterTransition = transition
        window.sharedElementReenterTransition = transition
        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        super.onCreate(savedInstanceState)
        // Hide default actionBar
        if (supportActionBar != null) supportActionBar!!.hide()
        // Set content to activity
        flContainer = FrameLayout(this)
        flContainer!!.layoutTransition = LayoutTransition()
        setContentView(flContainer)
        // Setup Khatma Manager instance
        manager = KhatmaManager.newInstance(this, KhatmaManager.getLastActiveKhatma(this), this)
        manager.putKhatmaUnderManagement()
    }

    override fun onPrepareManager() {
        // Setup khatma views
        akv = ActiveKhatmaView(this)
        nkv = NoKhatmaView(this)
        AManager.log(tag, "onPrepareManager")
    }

    override fun onManageKhatma(khatma: Khatma) {
        flContainer!!.removeAllViews()
        flContainer!!.addView(akv, FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        akv!!.refreshUI()
    }

    override fun onHaveNoKhatma() {
        finishAfterTransition()
        AManager.log(tag, "onHaveNoKhatma")
//        flContainer!!.removeAllViews()
//        flContainer!!.addView(nkv, FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
    }

    override fun onProgressUpdated(nextWerd: KhatmaWerd) {
        AManager.log(tag, "onProgressUpdated: ${nextWerd}")
        if (flContainer!!.childCount > 0) {
            val akv = flContainer!!.getChildAt(0) as ActiveKhatmaView
            akv.refreshUI()
        }
    }

    override fun onSwitchKhatma(khatma: Khatma) {
        finishAfterTransition()
    }

    override fun onFinishKhatma() {
        finishAfterTransition()
        Toast.makeText(this, R.string.congrat_finishing_khatma, Toast.LENGTH_SHORT).show()
        AManager.log(tag, "onFinishKhatma")
    }

    class CreateKhatmaBottomSheet(context: Context?, private val callback: ResultCallback<Khatma>) : BaseBottomSheet(context, null, true) {
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
            inputKhatmaName = findViewById(R.id.til_khatma_name)
            atvExpectedEnd = findViewById(R.id.atv_plan_expected_end)
            wheelPlanPicker = findViewById(R.id.wheel_plan_picker)
            btnCreate = findViewById(R.id.btn_create_khatma)
        }

        override fun bindInnerViews() {
            inputKhatmaName!!.editText?.text = null
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
                Toast.makeText(context, R.string.currently_under_development, Toast.LENGTH_SHORT).show()
            }
            findViewById<View>(R.id.btn_create_khatma).setOnClickListener { v: View? -> CreateKhatmaBottomSheet(context) { khatma: Khatma? -> }.show() }
        }
    }

    private inner class ActiveKhatmaView(context: Context) : ViewContainer(context) {

        private lateinit var khatma: Khatma

        // Views
        private lateinit var pwPercentage: ProgressWheel
        private lateinit var tvCompletedDays: MaterialTextView
        private lateinit var tvName: MaterialTextView
        private lateinit var tvPlanDays: MaterialTextView
        private lateinit var tvRemDays: MaterialTextView
        private lateinit var tvStartedIn: MaterialTextView
        private lateinit var tvEndsIn: MaterialTextView
        private lateinit var tvWerdStartSurah: MaterialTextView
        private lateinit var tvWerdStartAyah: MaterialTextView
        private lateinit var tvWerdEndSurah: MaterialTextView
        private lateinit var tvWerdEndAyah: MaterialTextView
        override fun prepareView(context: Context) {
            inflate(R.layout.layout_active_khatma_view)
            // Setup views
            pwPercentage = findViewById(R.id.prw_khatma_rem_parts)
            tvCompletedDays = findViewById(R.id.tv_khatma_completed_days)
            tvName = findViewById(R.id.tv_khatma_num_name)
            tvPlanDays = findViewById(R.id.tv_khatma_plan)
            tvRemDays = findViewById(R.id.tv_khatma_rem_days)
            tvStartedIn = findViewById(R.id.tv_khatma_start_date)
            tvEndsIn = findViewById(R.id.tv_khatma_end_date)
            tvWerdStartSurah = findViewById(R.id.tv_werd_start_surah)
            tvWerdStartAyah = findViewById(R.id.tv_werd_start_ayah)
            tvWerdEndSurah = findViewById(R.id.tv_werd_end_surah)
            tvWerdEndAyah = findViewById(R.id.tv_werd_end_ayah)
            val btnViewProgress = findViewById<MaterialButton>(R.id.btn_save_khatma_progress)
            val btnDeleteKhatma = findViewById<MaterialButton>(R.id.btn_delete_khatma)
            val btnViewHistory = findViewById<MaterialButton>(R.id.btn_khatma_history)
            // Setup listeners
            btnViewProgress.setOnClickListener { _ -> manager.saveProgress() }
            btnDeleteKhatma.setOnClickListener { _ ->
                val alert = AlertView(getString(R.string.delete_khatma), getString(R.string.msg_delete_khatma), AlertStyle.BOTTOM_SHEET).apply {
                    setTheme(AlertTheme.LIGHT)
                    addAction(AlertAction(getString(R.string.confirm), AlertActionStyle.NEGATIVE) { manager.deleteKhatma() })
                    addAction(AlertAction(getString(R.string.cancel), AlertActionStyle.DEFAULT) { })
                    show(this@KhatmaActivity)
                }
            }
            btnViewHistory.setOnClickListener { _ -> Toast.makeText(context, R.string.currently_under_development, Toast.LENGTH_SHORT).show() }
        }

        override fun refreshUI() {
            if (manager.holdsActiveKhatma.not()) {
                onFinishKhatma()
                return
            }
            this.khatma = manager.khatma!! // sure khatma isn't null.
            // Khatma details
            tvCompletedDays.text = khatma.name
            pwPercentage.setStepCountText("${khatma.progressPercentage.toInt().toString()} %")
            pwPercentage.setPercentage((khatma.progressPercentage / 100 * 360).toInt())
            tvName.text = tvCompletedDays.text
            tvPlanDays.text = String.format(LocaleManager.getCurrLocale(context), "%d %s (%d/%s)", khatma.plan.duration, getString(R.string.days), khatma.werdLength, getString(R.string.day))
            val remParts = khatma.remainingWerds
            tvRemDays.text = String.format(LocaleManager.getCurrLocale(context), "%d %s", remParts, getString(if (remParts == 1) R.string.part else R.string.parts))
            tvStartedIn.text = khatma.startedIn.getFormatted(FormatPatterns.DATE_NORMAL)
            tvEndsIn.text = khatma.expectedEnd.getFormatted(FormatPatterns.DATE_NORMAL)
            // Werd
            tvWerdStartSurah.text = khatma.currentWerd.start.surah.getName(context)
            tvWerdStartAyah.text = khatma.currentWerd.start.number.toString()
            tvWerdEndSurah.text = khatma.currentWerd.end.surah.getName(context)
            tvWerdEndAyah.text = khatma.currentWerd.end.number.toString()
        }
    }
}
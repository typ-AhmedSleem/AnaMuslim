package com.typ.muslim.ui.khatma

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.typ.muslim.R
import com.typ.muslim.features.khatma.KhatmaBuilder
import com.typ.muslim.features.khatma.KhatmaManager
import com.typ.muslim.features.khatma.data.KhatmaPlans
import com.typ.muslim.features.khatma.models.KhatmaPlan
import com.typ.muslim.features.khatma.models.ReminderPlan
import com.typ.muslim.ui.khatma.planner.KhatmaPlanButton

class KhatmaPlannerActivity : AppCompatActivity() {

    // Views
    private lateinit var btnCustom: MaterialButton
    private lateinit var bsConfigureKhatma: ConfigureNewKhatmaBottomSheet

    override fun onCreate(savedInstanceState: Bundle?) {
        // Init activity transitions
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        val root = findViewById<View>(android.R.id.content).apply { transitionName = "transition_card_to_activity" }
        val transition = MaterialContainerTransform().apply {
            addTarget(android.R.id.content)
            scrimColor = Color.WHITE
            drawingViewId = android.R.id.content
        }
        window.sharedElementEnterTransition = transition
        window.sharedElementReenterTransition = transition
        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        super.onCreate(savedInstanceState)
        // Init activity UI
        supportActionBar?.hide()
        setContentView(R.layout.activity_khatma_planner)

        bsConfigureKhatma = ConfigureNewKhatmaBottomSheet(this)
        findViewById<MaterialToolbar>(R.id.toolbar).setNavigationOnClickListener { finishAfterTransition() }

        for (pair in arrayOf<Pair<KhatmaPlan, KhatmaPlanButton?>>(
            KhatmaPlans.plan10Days to findViewById(R.id.kpb_10days),
            KhatmaPlans.plan15Days to findViewById(R.id.kpb_15days),
            KhatmaPlans.plan30Days to findViewById(R.id.kpb_30days),
            KhatmaPlans.plan60Days to findViewById(R.id.kpb_60days),
        )) {
            pair.second?.apply {
                setPlan(pair.first, true)
                setOnClickListener { bsConfigureKhatma.show { name, reminder -> createKhatma(name, plan, reminder) } }
            }
        }
        btnCustom = findViewById<MaterialButton?>(R.id.btn_custom_plan).apply {
            setOnClickListener {
                // todo: Create custom khatma plan
                Toast.makeText(context, R.string.currently_under_development, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createKhatma(name: String, plan: KhatmaPlan, reminderPlan: ReminderPlan?) {
        val khatma = KhatmaBuilder(name, plan, reminderPlan).build()
        val created = KhatmaManager.createKhatma(this, khatma)
        if (created) {
            bsConfigureKhatma.dismiss()
            startActivity(Intent(this, KhatmaActivity::class.java))
            finishAfterTransition()
        } else Toast.makeText(this, "Can't create khatma.", Toast.LENGTH_SHORT).show()
    }

    private inner class ConfigureNewKhatmaBottomSheet(val context: Context) {

        private val bs: BottomSheetDialog = BottomSheetDialog(context)
        val isShowing: Boolean
            get() = bs.isShowing

        private var reminderPlan: ReminderPlan? = null
        lateinit var callback: (String, ReminderPlan?) -> Unit

        init {
            bs.setContentView(R.layout.bs_configure_new_khatma)
            val tilName = bs.findViewById<TextInputLayout>(R.id.til_new_khatma_name)
            val tilReminder = bs.findViewById<TextInputLayout>(R.id.til_new_khatma_reminder)?.editText?.setOnClickListener {
                // todo: Show SetReminderBottomSheet
                Toast.makeText(context, R.string.feature_under_dev, Toast.LENGTH_SHORT).show()
            }
            bs.findViewById<MaterialButton>(R.id.btn_create_khatma)?.setOnClickListener {
                val name = tilName?.editText?.text?.toString() ?: ""
                // Check if name is empty
                if (name.isEmpty()) {
                    tilName?.requestFocus()
                    Toast.makeText(context, R.string.khatma_title_cant_be_empty, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                callback.invoke(name, reminderPlan)
            }
        }

        fun show(callback: (String, ReminderPlan?) -> Unit) {
            if (!isShowing) {
                bs.show()
                this.callback = callback
            }
        }

        fun dismiss() {
            if (isShowing) bs.dismiss()
        }

    }

}

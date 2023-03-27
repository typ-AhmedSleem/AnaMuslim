package com.typ.muslim.ui.khatma

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.typ.muslim.R
import com.typ.muslim.features.khatma.data.KhatmaPlans
import com.typ.muslim.ui.khatma.planner.KhatmaPlanButton

class KhatmaPlannerActivity : AppCompatActivity() {

    // Views
    private lateinit var kpb10Days: KhatmaPlanButton
    private lateinit var kpb15Days: KhatmaPlanButton
    private lateinit var kpb30Days: KhatmaPlanButton
    private lateinit var kpb60Days: KhatmaPlanButton
    private lateinit var btnCustom: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        // Init activity transitions
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        findViewById<View>(android.R.id.content).transitionName = "transition_card_to_activity"
        val transition = MaterialContainerTransform().apply {
            addTarget(android.R.id.content)
            scrimColor = Color.TRANSPARENT
            drawingViewId = android.R.id.content
        }
        window.enterTransition = transition
        window.reenterTransition = transition
        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        window.allowEnterTransitionOverlap = false
        window.sharedElementsUseOverlay = false
        super.onCreate(savedInstanceState)
        // Init activity UI
        supportActionBar?.hide()
        setContentView(R.layout.activity_khatma_planner)

        findViewById<MaterialToolbar>(R.id.toolbar).setNavigationOnClickListener {
            finishAfterTransition()
        }
        kpb10Days = findViewById<KhatmaPlanButton?>(R.id.kpb_10days).apply {
            setPlan(KhatmaPlans.plan10Days, true)
            setOnClickListener {
                // todo: Ask user to enter a name for khatma then create it
                Toast.makeText(context, R.string.feature_under_dev, Toast.LENGTH_SHORT).show()
            }
        }
        kpb15Days = findViewById<KhatmaPlanButton?>(R.id.kpb_15days).apply {
            setPlan(KhatmaPlans.plan15Days, true)
            setOnClickListener {
                Toast.makeText(context, R.string.feature_under_dev, Toast.LENGTH_SHORT).show()
            }
        }
        kpb30Days = findViewById<KhatmaPlanButton?>(R.id.kpb_30days).apply {
            setPlan(KhatmaPlans.plan30Days, true)
            setOnClickListener {
                Toast.makeText(context, R.string.feature_under_dev, Toast.LENGTH_SHORT).show()
            }
        }
        kpb60Days = findViewById<KhatmaPlanButton?>(R.id.kpb_60days).apply {
            setPlan(KhatmaPlans.plan60Days, true)
            setOnClickListener {
                Toast.makeText(context, R.string.feature_under_dev, Toast.LENGTH_SHORT).show()
            }
        }
        btnCustom = findViewById<MaterialButton?>(R.id.btn_custom_plan).apply {
            setOnClickListener {
                // todo: Create custom khatma plan
                Toast.makeText(context, "Create custom plan isn't yet implemented", Toast.LENGTH_SHORT).show()
            }
        }
    }

}

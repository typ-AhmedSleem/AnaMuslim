package com.typ.muslim.ui.core

import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.typ.muslim.R
import com.typ.muslim.managers.AMSettings
import com.typ.muslim.utils.colorRes

abstract class AnaMuslimActivity(private val layoutResId: Int) : AppCompatActivity() {

    val root: ViewGroup
        get() = findViewById(android.R.id.content)

    override fun onCreate(savedInstanceState: Bundle?) {
        // Setup activity transitions
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        root.transitionName = "transition_card_to_activity"
        val transition = MaterialContainerTransform().apply {
            addTarget(android.R.id.content)
            scrimColor = colorRes(this@AnaMuslimActivity, R.color.colorSurface)
            drawingViewId = android.R.id.content
        }
        window.sharedElementEnterTransition = transition
        window.sharedElementReenterTransition = transition
        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        // Init activity
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AMSettings.getDefaultNightMode(this))
        supportActionBar?.hide()
        setContentView(layoutResId)
        // Init
        findViews()
        initRuntime()
        initUI()
        initListeners()
    }

    abstract fun findViews()

    abstract fun initRuntime()

    abstract fun initUI()

    abstract fun initListeners()

}

package com.typ.muslim.ui.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import com.bitvale.switcher.SwitcherX
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import com.typ.muslim.R

@SuppressLint("ResourceType")
class SwitcherCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    // UI Components
    private val switcher: SwitcherX
    private val tvText: MaterialTextView

    // Runtime
    val isOn: Boolean
        get() = switcher.isChecked

    init {
        // Init card
        setOnClickListener { toggleCheck() }
        // Init components
        inflate(context, R.layout.layout_switcher_card, this)
        switcher = findViewById(R.id.switcherx_swc)
        tvText = findViewById(R.id.tv_swc_text)
        // Parse attrs (if specified)
        if (attrs != null) {
            context.obtainStyledAttributes(attrs, R.styleable.SwitcherCard).apply {
                val text = getString(R.styleable.SwitcherCard_swc_text)
                val checked = getBoolean(R.styleable.SwitcherCard_swc_checked, false)
                if (text != null) setText(text)
                check(checked)


                recycle()
            }
        }
    }

    fun setText(text: String) {
        tvText.text = text
    }

    fun check(checked: Boolean) = switcher.setChecked(checked, true)

    fun toggleCheck() = check(!isOn)

}

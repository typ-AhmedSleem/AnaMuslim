package com.typ.muslim.ui.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.typ.muslim.managers.AMSettings

open class AnaMuslimActivity : AppCompatActivity {

    constructor() : super()

    constructor(contentView: Int) : super(contentView)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AMSettings.getDefaultNightMode(this))
    }

}

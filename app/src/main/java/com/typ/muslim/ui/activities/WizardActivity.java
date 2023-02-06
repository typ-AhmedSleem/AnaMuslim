/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.typ.muslim.R;

public class WizardActivity extends AppCompatActivity {

    // Statics
    private static final String TAG = "WizardActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // TODO: Start the wizard service to collect data from all fragments within wizard
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard);
        if (getSupportActionBar() != null) getSupportActionBar().hide(); // Hide default ActionBar.
        /* There's nothing to do here, Fragments in graph do all work instead */

    }
}

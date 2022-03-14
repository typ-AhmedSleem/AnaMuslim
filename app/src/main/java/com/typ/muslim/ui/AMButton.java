/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;
import com.typ.muslim.R;
import com.typ.muslim.interfaces.ThemeChangeObserver;
import com.typ.muslim.managers.ResMan;
import com.typ.muslim.systems.ColorSystem;

public class AMButton extends MaterialButton implements ThemeChangeObserver {

    public AMButton(@NonNull Context context) {
        super(context);
        init(context);
    }

    public AMButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AMButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        // Init button
        super.setCornerRadiusResource(R.dimen.default_corner_radius);
        // Theme button
        this.onFirstTheme(ColorSystem.getCurrentTheme(context), ColorSystem.getCurrentColorTheme(context));
    }

    @Override
    public void onFirstTheme(ColorSystem.GlobalTheme globalTheme, ColorSystem.ColorTheme colorTheme) {
        this.onThemeChanged(null, globalTheme, colorTheme); // Called once when view is being created.
    }

    @Override
    public void onThemeChanged(View madeCallView, ColorSystem.GlobalTheme globalTheme, ColorSystem.ColorTheme colorTheme) {
        if (globalTheme != null && colorTheme != null) {
            // Change button theme
            super.setBackgroundColor(ResMan.getColor(getContext(), colorTheme.getAccentColorRes()));
            super.setTextColor(ResMan.getColor(getContext(), colorTheme.getTextColor()));
        }
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
    }
}

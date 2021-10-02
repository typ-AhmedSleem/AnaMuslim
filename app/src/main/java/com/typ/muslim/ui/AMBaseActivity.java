/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.jraska.falcon.Falcon;
import com.typ.muslim.R;
import com.typ.muslim.interfaces.ThemeChangeObserver;
import com.typ.muslim.systems.ColorSystem;
import com.typ.muslim.utils.DisplayUtils;

public class AMBaseActivity extends AppCompatActivity implements ThemeChangeObserver {

    // TODO: 6/10/2021 change the structure of this class to get root container from window not from param

    ImageView fakeDisplayImageView;
    // Runtime
    private ColorSystem colorSystem;
    private boolean isChangingThemeRunning = false;
    // Views
    private View contentView;

    @Override
    public void setContentView(@LayoutRes int contentView) {
        // Set the content view of activity
        super.setContentView(contentView);
        // Setup AMBase activity
        this.contentView = getWindow().findViewById(android.R.id.content);
        if (colorSystem == null) colorSystem = ColorSystem.create(this);
        this.fakeDisplayImageView = this.contentView.findViewById(R.id.img_theme_switch);
        // Hide default ActionBar
        if (getSupportActionBar() != null) getSupportActionBar().hide();
    }

    public ColorSystem getColorSystem() {
        return colorSystem;
    }

    @Override
    protected void onResume() {
        super.onResume();
        colorSystem.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        colorSystem.onPause();
    }

    @Override
    public void onFirstTheme(ColorSystem.GlobalTheme globalTheme, ColorSystem.ColorTheme colorTheme) {
        try {
            ViewGroup contentContainer = (ViewGroup) contentView;
            for (int childIndex = 0; childIndex < contentContainer.getChildCount(); childIndex++) {
                View childView = contentContainer.getChildAt(childIndex);
                if (childView instanceof AMButton) ((AMButton) childView).onThemeChanged(null, globalTheme, colorTheme); // TODO: 3/1/21 Adapt Button colors to current themes
                else if (childView instanceof AMTextView) ((AMTextView) childView).onThemeChanged(null, globalTheme, colorTheme); // TODO: 3/1/21 Adapt TextView to current theme
                else if (childView instanceof AMNumberSelector) ((AMNumberSelector) childView).onThemeChanged(null, globalTheme, colorTheme);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onThemeChanged(View madeCallView, ColorSystem.GlobalTheme globalTheme, ColorSystem.ColorTheme colorTheme) {
        if (isChangingThemeRunning) {
            Toast.makeText(this, "Changing theme is running", Toast.LENGTH_SHORT).show();
            return;
        }
        // Prepare x,y,endRadius
        int centerX = madeCallView != null ? madeCallView.getWidth() / 2 : 0;
        int centerY = madeCallView != null ? (madeCallView.getHeight() / 2) + madeCallView.getTop() : 0;
        int endRadius = DisplayUtils.getScreenH(this);
        // Create CircularRevealAnimation
        if (fakeDisplayImageView != null) {
            Animator cr = ViewAnimationUtils.createCircularReveal(fakeDisplayImageView, centerX, centerY, (float) endRadius, 0f);
            cr.setDuration(500);
            cr.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    // Update runtime
                    AMBaseActivity.this.isChangingThemeRunning = true;
                    // Show Fake Screen
                    Bitmap fakeScreenshotBitmap = Falcon.takeScreenshotBitmap(AMBaseActivity.this);
                    fakeDisplayImageView.setImageBitmap(fakeScreenshotBitmap);
                    fakeDisplayImageView.setVisibility(View.VISIBLE);
                    // Change Theme
                    try {
                        ViewGroup contentContainer = (ViewGroup) contentView;
                        contentView.setBackgroundColor(ColorGenerator.MATERIAL.getRandomColor());
                        for (int childIndex = 0; childIndex < contentContainer.getChildCount(); childIndex++) {
                            View childView = contentContainer.getChildAt(childIndex);
                            if (childView instanceof AMButton) ((AMButton) childView).onThemeChanged(null, globalTheme, colorTheme);
                            else if (childView instanceof AMTextView) ((AMTextView) childView).onThemeChanged(null, globalTheme, colorTheme);
                            else if (childView instanceof AMNumberSelector) ((AMNumberSelector) childView).onThemeChanged(null, globalTheme, colorTheme);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    // Hide fake
                    fakeDisplayImageView.setVisibility(View.INVISIBLE);
                    // Clean fake bitmap for performance
                    fakeDisplayImageView.setImageBitmap(null);
                    // Update runtime
                    AMBaseActivity.this.isChangingThemeRunning = false;
                }
            });
            // Start Animation
            cr.start();
        } else {
            // Change the theme without CR. change it with color transition or activity recreate
            // TODO: 6/10/2021 code this part
        }
    }

}

/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.interfaces;

import android.view.View;

import com.typ.muslim.systems.ColorSystem;

public interface ThemeChangeObserver {

    /**
     * Fired when user has changed the theme of app.
     * This callback is delivered through ColorSystem to stylize the views when activity is being created.
     *
     * @param globalTheme The Current Global Theme.
     * @param colorTheme  The Current Color Theme.
     */
    void onFirstTheme(ColorSystem.GlobalTheme globalTheme, ColorSystem.ColorTheme colorTheme);

    /**
     * Fired when user has changed the theme of app.
     * This callback is delivered through ColorSystem after changing the current theme with the new one.
     *
     * @param madeCallView The view user clicked to change theme. Used to calculate the position of the ChangeThemeCircularReveal
     * @param globalTheme  The New Global Theme.
     * @param colorTheme   The New Color Theme.
     */
    void onThemeChanged(View madeCallView, ColorSystem.GlobalTheme globalTheme, ColorSystem.ColorTheme colorTheme);

}

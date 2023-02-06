/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.dashboard;

import android.animation.LayoutTransition;
import android.animation.ValueAnimator;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.telecom.TelecomManager;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;
import androidx.core.util.Pair;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import com.google.android.material.card.MaterialCardView;
import com.typ.muslim.R;
import com.typ.muslim.interfaces.ThemeChangeObserver;
import com.typ.muslim.interfaces.TimeChangedListener;
import com.typ.muslim.interfaces.ViewHelperMethods;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.managers.ResMan;
import com.typ.muslim.models.Timestamp;
import com.typ.muslim.systems.ColorSystem;
import com.typ.muslim.utils.DisplayUtils;

public abstract class DashboardCard extends MaterialCardView implements ViewHelperMethods, View.OnClickListener, View.OnLongClickListener, ThemeChangeObserver, TimeChangedListener {

    // todo: Don't ignore view xml attributes and add attrs for bgColor, rippleColor, elevation and radius.
    // todo: Add header view in the card that (its content and icon using xmlAttrs or java setters) & (visible or not).
    // todo: Add option for the header to be clickable and in that state it will collapse or expand the card state.

    // Statics
    private static final String TAG = "DashboardCard";
    private final ValueAnimator colorChangeAnim = ValueAnimator.ofArgb(Color.BLACK);
    // Runtime
    public ColorSystem.GlobalTheme currGlobalTheme;
    public ColorSystem.ColorTheme currColorTheme;
    public boolean isBottomSheetShown;

    public DashboardCard(Context context) {
        super(context);
        this.isBottomSheetShown = false;
        this.initColorAnimator();
        this.parseAttrs(context, null);
        this.initCard();
        this.prepareRuntime(context);
        this.prepareCardView(context);
    }

    public DashboardCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.isBottomSheetShown = false;
        this.initColorAnimator();
        this.parseAttrs(context, attrs);
        this.initCard();
        this.prepareRuntime(context);
        this.prepareCardView(context);
    }

    public DashboardCard(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    /**
     * Called while initializing card for the first time before preparing the views
     */
    public void prepareRuntime(Context context) {
    }

    /**
     * Called after preparing runtime and this is the last step of initializing the card
     */
    public abstract void prepareCardView(Context context);

    public void parseAttrs(Context context, AttributeSet attrs) {
    }

    /**
     * Performs necessary update in runtime & UI of the card
     * when the parent activity lifecycle changes
     */
    public void refreshUI() {
    }

    /**
     * Called when the card is fully initialized and wants at any time to refresh its runtime after views are  prepared
     */
    public void refreshRuntime() {
    }

    public final void performRefresh() {
        refreshRuntime();
        refreshUI();
    }

    public void reset() {
    }

    public Pair<View, String>[] getViewsTransitionPairs() {
        return new Pair[]{};
    }

    public void saveInstanceState(Bundle out) {
        // todo: code this method
    }

    public void restoreInstance(Bundle in) {
        // todo: code this method
    }

    private void initColorAnimator() {
        this.colorChangeAnim.setRepeatCount(0);
        this.colorChangeAnim.setDuration(1500L);
        this.colorChangeAnim.setInterpolator(new LinearOutSlowInInterpolator());
        this.colorChangeAnim.addUpdateListener(anim -> this.setCardBackgroundColor((int) anim.getAnimatedValue()));
    }

    private void initCard() {
        this.setRadius(40f);
        this.setElevation(0f);
        this.setRippleColorResource(R.color.ripple_white);
        this.setCardBackgroundColor(getColor(R.color.adaptiveBackgroundColor));
        this.setOnClickListener(this);
        this.setOnLongClickListener(this);
        this.setHapticFeedbackEnabled(true);
        this.setLayoutTransition(new LayoutTransition());
    }

    public void animateCardColor(@ColorInt int targetColor) {
        this.colorChangeAnim.setIntValues(this.getCardBackgroundColor().getDefaultColor(), targetColor);
        this.colorChangeAnim.start();
    }

    /* START: Some helper methods */

    public final View inflate(@LayoutRes int layoutRes) {
        return inflate(getContext(), layoutRes, this);
    }

    @Override
    public <T extends View> T $(int resId) {
        return findViewById(resId);
    }

    @Override
    public final @ColorInt
    int getColor(@ColorRes int color) {
        return ResMan.getColor(getContext(), color);
    }

    @Override
    public final String getString(@StringRes int stringResId) {
        return ResMan.getString(getContext(), stringResId);
    }

    @Override
    public final void startActivity(Intent intent) {
        getContext().startActivity(intent);
    }

    /* END: Some helper methods */

    @Override
    public void onFirstTheme(ColorSystem.GlobalTheme globalTheme, ColorSystem.ColorTheme colorTheme) {
        this.currGlobalTheme = globalTheme;
        this.currColorTheme = colorTheme;
    }

    @Override
    public void onThemeChanged(View madeCallView, ColorSystem.GlobalTheme globalTheme, ColorSystem.ColorTheme colorTheme) {
        this.currGlobalTheme = globalTheme;
        this.currColorTheme = colorTheme;
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public boolean onLongClick(View v) {
        return true;
    }

    public void onTimeChanged(Timestamp now) {
        AManager.log(TAG, "onTimeChanged: " + now);
    }

    @Override
    public final int sp2px(float sp) {
        return DisplayUtils.sp2px(getContext(), sp);
    }

    @Override
    public final int dp2px(float dp) {
        return DisplayUtils.dp2px(getContext(), dp);
    }

    @Override
    public TelecomManager getTelecomManager() {
        return (TelecomManager) getContext().getSystemService(Context.TELECOM_SERVICE);
    }

    @Override
    public Vibrator getVibrator() {
        return (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public AlarmManager getAlarmManager() {
        return (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
    }
}

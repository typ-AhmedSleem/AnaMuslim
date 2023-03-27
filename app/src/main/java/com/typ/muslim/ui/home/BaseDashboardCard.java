/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.home;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.typ.muslim.R;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.managers.ResMan;

public class BaseDashboardCard extends MaterialCardView {

    // Statics
    private static final String TAG = "BaseDashboardCard";
    private final float titleTextSize = 20;
    private final int titleColor = Color.BLACK;
    // Runtime
    private String titleText = "Card Title";
    private boolean isTitleShown = true;
    private int titleStyle = Typeface.NORMAL;
    private int titleGravity = Gravity.START;
    private Drawable icon;
    private @ColorInt
    int iconTintColor;
    private boolean isArrowShown = true;
    private @LayoutRes
    int contentLayoutRes;
    // Views
    private MaterialCardView titleCard;
    private MaterialTextView titleTV;
    private ImageView iconIFV;
    private ImageView arrowIV;
    private ViewGroup contentFL;

    public BaseDashboardCard(Context context) {
        super(context);
        this.initialize(context);
    }

    public BaseDashboardCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        //this.parseAttrs(context, attrs);
        this.initialize(context);
    }

    public BaseDashboardCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //this.parseAttrs(context, attrs);
        this.initialize(context);
    }

    private void parseAttrs(Context context, AttributeSet attrs) {
        // Do necessary check
        if (attrs == null) return;
        // Parse attrs and put them in runtime variables
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseDashboardCard);
        this.titleText = typedArray.getString(R.styleable.BaseDashboardCard_dcTitle);
        //this.titleTextSize = typedArray.getDimensionPixelSize(R.styleable.BaseDashboardCard_dcTitleTextSize, 20);
        //this.titleColor = typedArray.getColor(R.styleable.BaseDashboardCard_dcTitleTextColor, Color.BLACK);
        this.isTitleShown = typedArray.getBoolean(R.styleable.BaseDashboardCard_dcShowTitle, true);
        this.titleStyle = typedArray.getInt(R.styleable.BaseDashboardCard_dcTitleTextStyle, Typeface.NORMAL);
        this.titleGravity = typedArray.getInt(R.styleable.BaseDashboardCard_dcTitleGravity, Gravity.START);
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) this.titleFont = typedArray.getFont(R.styleable.BaseDashboardCard_dcTitleFont);
        this.contentLayoutRes = typedArray.getInt(R.styleable.BaseDashboardCard_dcContentLayout, 0);
        this.icon = typedArray.getDrawable(R.styleable.BaseDashboardCard_dcIcon);
        this.iconTintColor = typedArray.getColor(R.styleable.BaseDashboardCard_dcIconTintColor, Color.RED);
        this.isArrowShown = typedArray.getBoolean(R.styleable.BaseDashboardCard_dcShowArrow, true);
        AManager.log(TAG, "CONTENT:  " + contentLayoutRes);
        typedArray.recycle();
    }

    private void initialize(Context context) {
        // Prepare runtime and do some checks on it
        if (this.titleText == null) this.titleText = "Card Title";
        // Inflate content view
        inflate(context, R.layout.layout_base_card, this);
        // Setup super card container of this class
        super.setRadius(20f);
        super.setCardElevation(10f);
        //super.setCardBackgroundColor(AMRes.getColor(context, R.color.adaptiveBackgroundColor));
        // Setup inner views
        titleCard = findViewById(R.id.cardTitleContainer);
        titleTV = findViewById(R.id.tvCardTitle);
        iconIFV = findViewById(R.id.ifvCardIcon);
        arrowIV = findViewById(R.id.ivCardArrow);
        contentFL = findViewById(R.id.flCardContentContainer);
        // Init inner views
        titleTV.setText(titleText == null ? "Card" : titleText); // Title text.
        if (!isArrowShown) arrowIV.setVisibility(INVISIBLE); // Hide arrow.
        if (!isTitleShown) titleTV.setVisibility(INVISIBLE); // Hide title.
        if (icon == null) iconIFV.setVisibility(INVISIBLE); // Hide icon if not set.
        else iconIFV.setImageDrawable(icon);
        if (this.contentLayoutRes != 0) this.changeCardContent(context, contentLayoutRes); // Inflate card content.
        // Animate card for entrance
        if (!isInEditMode()) super.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dashboard_cards_enter_anim));
    }

    private void changeCardContent(Context context, @LayoutRes int contentLayoutRes) {
        // Remove old content
        contentFL.removeAllViews();
        // Add the new content view with default layout params.
        contentFL.addView(LayoutInflater.from(context).inflate(contentLayoutRes, null), new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public BaseDashboardCard setCardContent(@LayoutRes int contentLayoutRes) {
        // Change card content
        this.changeCardContent(getContext(), contentLayoutRes);
        return this;
    }

    public BaseDashboardCard setTitleText(@StringRes int titleStringRes) {
        // Update TextView
        this.titleTV.setText(titleStringRes);
        return this;
    }

    public BaseDashboardCard setTitleText(String titleText) {
        // Update TextView
        this.titleTV.setText(titleText);
        return this;
    }

    public BaseDashboardCard setTitleGravity(int gravity) {
        this.titleTV.setGravity(gravity);
        return this;
    }

    /**
     * @param iconRes The icon res id to use as card icon. 0 to clear it.
     */
    public BaseDashboardCard setIcon(@DrawableRes int iconRes) {
        // Update runtime
        if (iconRes == 0) {
            this.icon = null;
            this.iconIFV.setImageDrawable(null);
        } else this.iconIFV.setImageResource(iconRes);
        this.iconIFV.setVisibility(iconRes == 0 ? INVISIBLE : VISIBLE);
        return this;
    }

    public BaseDashboardCard setIcon(Drawable icon) {
        // Update runtime
        this.iconIFV.setImageDrawable(null);
        this.iconIFV.setVisibility(icon == null ? INVISIBLE : VISIBLE);
        return this;
    }

    public BaseDashboardCard setIconBackground(Drawable iconBackground, boolean clearIcon) {
        this.iconIFV.setBackground(iconBackground);
        if (clearIcon) this.iconIFV.setImageDrawable(null);
        this.iconIFV.setVisibility(iconBackground == null ? INVISIBLE : VISIBLE);
        return this;
    }

    /**
     * @param iconTint The tint color res id to use as tint of card icon. 0 to clear current tint.
     */
    public BaseDashboardCard setIconTint(@ColorRes int iconTint) {
        // Update card icon tint color
        if (iconTint != 0) this.iconIFV.setColorFilter(ResMan.getColor(getContext(), iconTint));
        else this.iconIFV.clearColorFilter();
        return this;
    }

    /**
     * @param iconBgColor The tint color res id to use as tint of card icon background.
     */
    public BaseDashboardCard setIconBgColor(@ColorRes int iconBgColor) {
        // Update card icon bg color
//        this.iconIFV.setBackgroundTintList(ColorStateList.valueOf(AMRes.getColor(getContext(), iconBgColor)));
        return this;
    }

    /**
     * @param titleTextColor The tint color res id to use as color of card title text
     */
    public BaseDashboardCard setTitleTextColor(@ColorRes int titleTextColor) {
        // Update card title text color
        this.titleTV.setTextColor(ResMan.getColor(getContext(), titleTextColor));
        return this;
    }

    /**
     * @param iconTint The tint color res id to use as tint of arrow. 0 to clear current tint.
     */
    public BaseDashboardCard setArrowTint(@ColorRes int iconTint) {
        // Update arrow icon tint color
        if (iconTint != 0) this.arrowIV.setColorFilter(ResMan.getColor(getContext(), iconTint));
        else this.arrowIV.clearColorFilter();
        return this;
    }

    public BaseDashboardCard showArrow(boolean showArrow) {
        this.isArrowShown = showArrow;
        this.arrowIV.setVisibility(isArrowShown ? VISIBLE : INVISIBLE);
        return this;
    }

    public BaseDashboardCard showTitle(boolean showTitle) {
        this.isTitleShown = showTitle;
        this.titleTV.setVisibility(isTitleShown ? VISIBLE : INVISIBLE);
        return this;
    }

    public BaseDashboardCard setTitleClickable(boolean titleClickable) {
        titleCard.setClickable(titleClickable);
        return this;
    }

    public boolean isArrowShown() {
        return isArrowShown;
    }

    public boolean isTitleShown() {
        return isTitleShown;
    }

    /**
     * Animates the card with enter animation if it's hidden
     */
    public void show() {
        // Check if card is shown
        if (super.getVisibility() == VISIBLE) return;
        // Start enter animation
        super.postOnAnimation(() -> super.setVisibility(VISIBLE));
        super.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dashboard_cards_enter_anim));
    }

    /**
     * Animates the card with exit animation if it's shown
     */
    public void hide() {
        // Check if card is hidden
        if (super.getVisibility() == INVISIBLE || super.getVisibility() == GONE) return;
        // Start enter animation
        super.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dashboard_cards_exit_anim));
        super.postOnAnimationDelayed(() -> super.setVisibility(INVISIBLE), 1000);
    }

}


/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.TooltipCompat;

import com.google.android.material.card.MaterialCardView;
import com.typ.muslim.R;
import com.typ.muslim.managers.ResMan;

public class AMBottomBar extends MaterialCardView {

	// Views
	private final ImageView[] tabsIVs = new ImageView[5];
	// Runtime
	private int currentTab = 0; // Selected tab is Home by default.
	// Callback
	private AMBottomBarCallback callback;

	public AMBottomBar(@NonNull Context context) {
		super(context);
		this.prepareBottomBar(context);
	}

	public AMBottomBar(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		this.prepareBottomBar(context);
	}

	public AMBottomBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.prepareBottomBar(context);
	}

	private void prepareBottomBar(Context context) {
        // Setup card
        super.setRadius(0f);
        super.setElevation(0f);
        super.setStrokeWidth(0);
        super.setCardBackgroundColor(ResMan.getColor(context, R.color.transparent));
        // Inflate content view
        inflate(context, R.layout.layout_bottom_bar, this);
        // Init content views
        this.tabsIVs[0] = findViewById(R.id.quranBottomBarIV); // Quran tab.
        this.tabsIVs[1] = findViewById(R.id.qiblaBottomBarIV); // Qibla tab.
        this.tabsIVs[2] = findViewById(R.id.homeBottomBarIV); // Home tab.
        this.tabsIVs[3] = findViewById(R.id.trackerBottomBarIV); // Tracker tab.
        this.tabsIVs[4] = findViewById(R.id.moreBottomBarIV); // More tab.
        // Set tabs tooltips
        TooltipCompat.setTooltipText(this.tabsIVs[0], context.getString(R.string.quran));
        TooltipCompat.setTooltipText(this.tabsIVs[1], context.getString(R.string.qibla));
        TooltipCompat.setTooltipText(this.tabsIVs[2], context.getString(R.string.app_name));
        TooltipCompat.setTooltipText(this.tabsIVs[3], context.getString(R.string.tracker));
        TooltipCompat.setTooltipText(this.tabsIVs[4], context.getString(R.string.more));
        // Setup click listeners
        this.tabsIVs[0].setOnClickListener(v -> this.selectTab(0));
        this.tabsIVs[1].setOnClickListener(v -> this.selectTab(1));
        this.tabsIVs[2].setOnClickListener(v -> this.selectTab(2));
        this.tabsIVs[3].setOnClickListener(v -> this.selectTab(3));
        this.tabsIVs[4].setOnClickListener(v -> this.selectTab(4));
		// Setup long click listeners for showing tooltips
	}

	private void selectTab(int selectedTab) {
		// Check if user is reSelecting current selected tab
		if (this.currentTab == selectedTab) {
			if (callback != null) callback.onTabReselected(selectedTab);
			return;
		}
		// Fire callback
		if (callback != null) if (callback.onTabSelected(selectedTab)) callback.onTabUnselected(this.currentTab);
		// Change current selection
		this.currentTab = selectedTab;
		// Update views
		for (int index = 0; index < tabsIVs.length; index++) {
            if (this.currentTab == index) tabsIVs[this.currentTab].setColorFilter(ResMan.getColor(getContext(), R.color.bottomBarSelectedTab)); // Show selection on current tab.
            else tabsIVs[index].setColorFilter(ResMan.getColor(getContext(), R.color.bottomBarUnSelectedTab)); // Clear selection from other tabs.
        }
	}

	public void setCallback(AMBottomBarCallback callback) {
		this.callback = callback;
	}

	public interface AMBottomBarCallback {

		/**
		 * Fired when user has selected a tab wasn't selected before
		 *
		 * @return {@code true} to fire onTabUnselected after. {@code false} to only fire this method.
		 */
		boolean onTabSelected(int tabIndex);

		/**
		 * Fired only when user has selected a tab wasn't selected before and onTabSelected has returned {@code true}.
		 */
		void onTabUnselected(int tabIndex);

		/**
		 * Fired when user is reSelecting the current selected tab.
		 */
		void onTabReselected(int tabIndex);

	}

}

/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.home;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textview.MaterialTextView;
import com.typ.muslim.R;
import com.typ.muslim.ui.ViewContainer;

import org.jetbrains.annotations.NotNull;

public class DashboardSectionHeader extends ViewContainer {

	// Statics
	private static final String TAG = DashboardSectionHeader.class.getSimpleName();
	// Runtime
	private String headerTitle, actionTitle;
	private boolean showAction = true, showArrow = true;

	public DashboardSectionHeader(@NonNull @NotNull Context context) {
		super(context);
	}

	public DashboardSectionHeader(@NonNull @NotNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public DashboardSectionHeader(@NonNull @NotNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public void parseAttrs(Context context, AttributeSet attrs) {
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DashboardSectionHeader);
		this.headerTitle = ta.getString(R.styleable.DashboardSectionHeader_headerTitle);
		this.actionTitle = ta.getString(R.styleable.DashboardSectionHeader_actionTitle);
		this.showAction = ta.getBoolean(R.styleable.DashboardSectionHeader_showAction, true);
		this.showArrow = ta.getBoolean(R.styleable.DashboardSectionHeader_showArrow, true);
		ta.recycle();
	}

	@Override
	public void prepareRuntime(Context context) {
		// Hide action if its title is not set or empty
		if (TextUtils.isEmpty(this.actionTitle)) this.showAction = false;
	}

	@Override
	public void prepareView(Context context) {
		inflate(context, R.layout.layout_dashboard_section_header, this);
		// Init views
		MaterialTextView tvHeaderTitle = findViewById(R.id.tv_section_header_title);
		MaterialTextView tvActionTitle = findViewById(R.id.tv_section_action_title);
		ImageView imgArrow = findViewById(R.id.img_section_action_arrow);
		// Bind data to views
		tvHeaderTitle.setText(this.headerTitle);
		if (this.showAction) {
			tvActionTitle.setText(this.actionTitle);
			imgArrow.setVisibility(this.showArrow ? VISIBLE : GONE);
		} else tvActionTitle.setVisibility(GONE);
	}

}

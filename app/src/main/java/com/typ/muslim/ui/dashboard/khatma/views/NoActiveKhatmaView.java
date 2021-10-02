/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.dashboard.khatma.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.typ.muslim.R;
import com.typ.muslim.ui.ViewContainer;

import org.jetbrains.annotations.NotNull;

public class NoActiveKhatmaView extends ViewContainer {

	public NoActiveKhatmaView(@NonNull @NotNull Context context) {
		super(context);
	}

	public NoActiveKhatmaView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public NoActiveKhatmaView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public void prepareView(Context context) {
		// Inflate view
		inflate(context, R.layout.layout_no_khatma_view, this);
	}
}

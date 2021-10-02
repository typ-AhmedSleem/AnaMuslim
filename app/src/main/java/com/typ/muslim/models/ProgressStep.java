/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.models;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;

import com.typ.muslim.R;

public class ProgressStep {

	private boolean done;
	private @ColorRes int color;

	public ProgressStep() {}

	public ProgressStep(boolean done, @ColorRes int color) {
		this.done = done;
		this.color = color;
	}

	public boolean isDone() {
		return done;
	}

	public ProgressStep setDone(boolean done) {
		this.done = done;
		return this;
	}

	@ColorRes
	public int getColor() {
		return color;
	}

	public ProgressStep setColor(int color) {
		this.color = color;
		return this;
	}

}

/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.animation.LayoutTransition;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.telecom.TelecomManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.typ.muslim.Consumers;
import com.typ.muslim.enums.ExpansionState;
import com.typ.muslim.interfaces.Expandable;
import com.typ.muslim.interfaces.ExpansionListener;
import com.typ.muslim.interfaces.ViewHelperMethods;
import com.typ.muslim.managers.AMRes;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.utils.DisplayUtils;

import org.jetbrains.annotations.Contract;

public abstract class ViewContainer extends FrameLayout implements ViewHelperMethods, Expandable {

	// Runtime
	public boolean isBottomSheetShown;
	private ExpansionState expansionState;
	// Listeners
	public ExpansionListener listener;

	public ViewContainer(@NonNull Context context) {
		super(context);
		isBottomSheetShown = false;
		expansionState = ExpansionState.EXPANDED;
		setLayoutTransition(new LayoutTransition());
		parseAttrs(context, null);
		prepareRuntime(context);
		prepareView(context);
		AManager.log("ViewContainer", "1st constructor");
//		setListeners();
	}

	public ViewContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		isBottomSheetShown = false;
		expansionState = ExpansionState.EXPANDED;
		setLayoutTransition(new LayoutTransition());
		parseAttrs(context, attrs);
		prepareRuntime(context);
		prepareView(context);
		AManager.log("ViewContainer", "2nd constructor");
//		setListeners();
	}

	public ViewContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		isBottomSheetShown = false;
		expansionState = ExpansionState.EXPANDED;
		setLayoutTransition(new LayoutTransition());
		parseAttrs(context, attrs);
		prepareRuntime(context);
		prepareView(context);
		AManager.log("ViewContainer", "3rd constructor");
//		setListeners();
	}

	/**
	 * Optional method called if the child view has attrs to be parsed
	 */
	public void parseAttrs(Context context, @Nullable AttributeSet attrs) {}

	/**
	 * Called immediately after the view is created to prepare runtime
	 */
	public void prepareRuntime(Context context) {}

	/**
	 * Called after view is created and runtime is prepared and ready to be used to prepare view
	 */
	public abstract void prepareView(Context context);

	public void refreshRuntime() {}

	public void refreshUI() {}

	public void performRefresh() {
		refreshRuntime();
		refreshUI();
	}

	public void onClick(View v) {}

	public void onLongClick(View v) {}

	@Override
	public void expand() {
		Consumers.doIf(() -> {
			// Expand the container if it's collapsed
			this.measure(MATCH_PARENT, WRAP_CONTENT);
			final int targetHeight = this.getMeasuredHeight();
			getLayoutParams().height = 0;
			setVisibility(VISIBLE);
			final Animation animExpand = new Animation() {
				@Contract(pure = true)
				@Override
				public boolean willChangeBounds() {
					return true;
				}

				@Override
				protected void applyTransformation(float it, Transformation t) {
					if (it == 1f) {
						// Animation has finished.
						getLayoutParams().height = WRAP_CONTENT;
					} else getLayoutParams().height = (int) (it * targetHeight); // Animating.
					requestLayout(); // Request container re-layout.
					// Call listener
					if (listener != null) listener.onStateUpdate(ExpansionState.EXPANDING, it);
				}
			};
			animExpand.setDuration(1000);
			animExpand.setInterpolator(new AccelerateDecelerateInterpolator());
			animExpand.setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					expansionState = ExpansionState.EXPANDED;
					if (listener != null) listener.onStateChanged(ExpansionState.EXPANDED);
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}
			});
			startAnimation(animExpand);
		}, expansionState == ExpansionState.COLLAPSED);
	}

	@Override
	public void collapse() {
		Consumers.doIf(() -> {
			// Collapse the container if it's expanded
			final int initialHeight = getMeasuredHeight();
			final Animation animCollapse = new Animation() {
				@Contract(pure = true)
				@Override
				public boolean willChangeBounds() {
					return true;
				}

				@Override
				protected void applyTransformation(float it, Transformation t) {
					if (it == 1f) {
						// Animation has finished
						getLayoutParams().height = 0;
						setVisibility(GONE);
					} else getLayoutParams().height = initialHeight - (int) (it * initialHeight); // Animating.
					requestLayout(); // Request container re-layout.
					// Call listener
					if (listener != null) listener.onStateUpdate(ExpansionState.COLLAPSING, it);
				}
			};
			animCollapse.setDuration(1000);
			animCollapse.setInterpolator(new AccelerateDecelerateInterpolator());
			animCollapse.setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					expansionState = ExpansionState.COLLAPSED;
					if (listener != null) listener.onStateChanged(ExpansionState.COLLAPSED);
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}
			});
			startAnimation(animCollapse);
		}, expansionState == ExpansionState.EXPANDED);
	}

	@Override
	public void toggle() {
		if (expansionState == ExpansionState.EXPANDED) this.collapse();
		else this.expand();
	}

	public void setExpansionListener(ExpansionListener listener) {
		this.listener = listener;
	}

	/* START: Some helper methods */

	public final View inflate(@LayoutRes int layoutRes) {
		return inflate(getContext(), layoutRes, this);
	}

	@Override
	public final <T extends View> T $(@IdRes int id) {
		return findViewById(id);
	}

	@Override
	public final @ColorInt
	int getColor(@ColorRes int color) {
		return AMRes.getColor(getContext(), color);
	}

	@Override
	public final String getString(@StringRes int stringResId) {
		return AMRes.getString(getContext(), stringResId);
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
	public final void startActivity(Intent intent) {
		if (intent == null) return;
		getContext().startActivity(intent);
	}

	@Override
	public final TelecomManager getTelecomManager() {
		return (TelecomManager) getContext().getSystemService(Context.TELECOM_SERVICE);
	}

	@Override
	public final Vibrator getVibrator() {
		return (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
	}

	@Override
	public final AlarmManager getAlarmManager() {
		return (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
	}

	/* END: Some helper methods */

	void setListeners() {
		this.setOnClickListener(this::onClick);
		this.setOnLongClickListener(v -> {
			onLongClick(v);
			return true;
		});
	}

}

/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.libs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.typ.muslim.interfaces.IScaleTouchListener;

import java.lang.ref.WeakReference;

public abstract class EnhancedScaleTouchListener implements View.OnTouchListener, IScaleTouchListener {

	private final Config config;
	ObjectAnimator scaleXDownAnimator;
	ObjectAnimator scaleXUpAnimator;
	ObjectAnimator scaleYDownAnimator;
	ObjectAnimator scaleYUpAnimator;
	ObjectAnimator alphaDownAnimator;
	ObjectAnimator alphaUpAnimator;
	AnimatorSet downSet;
	AnimatorSet upSet;
	AnimatorListenerAdapter finalAnimationListener;
	private long touchTime;
	private WeakReference<View> mView;
	private boolean createdAnimators = false;
	private boolean pressed = false;
	private boolean released = true;
	private float pointerX;
	private float pointerY;
	private Rect rect;

	public EnhancedScaleTouchListener() {
		this.config = new Config(100, 0.95f, 1f);
	}

	public EnhancedScaleTouchListener(int duration, float scaleDown, float alpha) {
		this.config = new Config(duration, scaleDown, alpha);
	}

	private void createAnimators() {
		this.alphaDownAnimator = ObjectAnimator.ofFloat(this.mView.get(), "alpha", this.config.getAlpha());
		this.alphaUpAnimator = ObjectAnimator.ofFloat(this.mView.get(), "alpha", 1.0F);
		this.scaleXDownAnimator = ObjectAnimator.ofFloat(this.mView.get(), "scaleX", this.config.getScaleDown());
		this.scaleXUpAnimator = ObjectAnimator.ofFloat(this.mView.get(), "scaleX", 1.0F);
		this.scaleYDownAnimator = ObjectAnimator.ofFloat(this.mView.get(), "scaleY", this.config.getScaleDown());
		this.scaleYUpAnimator = ObjectAnimator.ofFloat(this.mView.get(), "scaleY", 1.0F);
		this.downSet = new AnimatorSet();
		this.downSet.setDuration(this.config.getDuration());
		this.downSet.setInterpolator(new AccelerateInterpolator());
		this.downSet.playTogether(this.alphaDownAnimator, this.scaleXDownAnimator, this.scaleYDownAnimator);
		this.upSet = new AnimatorSet();
		this.upSet.setDuration(this.config.getDuration());
		this.upSet.setInterpolator(new FastOutSlowInInterpolator());
		this.upSet.playTogether(this.alphaUpAnimator, this.scaleXUpAnimator, this.scaleYUpAnimator);
		this.finalAnimationListener = new AnimatorListenerAdapter() {
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				mView.get().performClick();
			}
		};
	}

	public boolean onTouch(View v, MotionEvent event) {
		if (this.mView == null) this.mView = new WeakReference<>(v);
		if (!mView.get().hasOnClickListeners()) mView.get().setOnClickListener(view -> EnhancedScaleTouchListener.this.onClick(EnhancedScaleTouchListener.this.mView.get(), pointerX, pointerY));
//		if (!mView.get().hasOnLongClickListeners()) mView.get().setOnLongClickListener(view -> {
//			EnhancedScaleTouchListener.this.onLongClick(view);
//			return true;
//		});
		pointerX = event.getRawX();
		pointerY = event.getRawY();
		switch (event.getAction()) {
			case 0:
				this.setRect();
				this.touchTime = System.currentTimeMillis();
				if (!this.pressed) {
					this.effect(true);
					this.pressed = true;
					this.released = false;
//					mView.get().performLongClick();
				}

				return true;
			case 1:
				if (!this.released) {
					this.setRect();
					if (this.insideView(event)) {
						this.upSet.addListener(this.finalAnimationListener);
					}

					this.effect(false);
					this.released = true;
					this.pressed = false;
				}

				return false;
			case 2:
				if (!this.released) {
					this.setRect();
					if (!this.insideView(event)) {
						this.effect(false);
						this.released = true;
						this.pressed = false;
					}
				}

				return false;
			case 3:
				this.effect(false);
				this.released = true;
				this.pressed = false;
				return false;
			default:
				return false;
		}
	}

	private boolean insideView(MotionEvent event) {
		int x = (int) event.getRawX();
		int y = (int) event.getRawY();
		return x >= this.getRect().left && x <= this.getRect().right && y <= this.getRect().bottom && y >= this.getRect().top;
	}

	private void setRect() {
		if (this.rect == null) {
			this.rect = new Rect();
			this.mView.get().getGlobalVisibleRect(this.rect);
		}

	}

	private Rect getRect() {
		return this.rect;
	}

	private void effect(boolean press) {
		if (!this.createdAnimators) {
			this.createAnimators();
			this.createdAnimators = true;
		}

		if (press) {
			if (this.upSet != null) {
				this.upSet.removeAllListeners();
			}

			this.downSet.cancel();
			this.downSet.start();
		} else {
			long diffTime = System.currentTimeMillis() - this.touchTime;
			if (diffTime < (long) this.config.getDuration()) {
				this.upSet.setStartDelay((long) this.config.getDuration() - diffTime);
			}

			this.upSet.cancel();
			this.upSet.start();
		}

	}

	public Config getConfig() {
		return this.config;
	}

	public static class Config {
		private int duration;
		private float scaleDown;
		private float alpha;

		public Config(int duration, float scaleDown, float alpha) {
			this.duration = duration;
			this.scaleDown = scaleDown;
			this.alpha = alpha;
		}

		public int getDuration() {
			return this.duration;
		}

		public void setDuration(int duration) {
			this.duration = duration;
		}

		public float getScaleDown() {
			return this.scaleDown;
		}

		public void setScaleDown(float scaleDown) {
			this.scaleDown = scaleDown;
		}

		public float getAlpha() {
			return this.alpha;
		}

		public void setAlpha(float alpha) {
			this.alpha = alpha;
		}
	}

}

/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.github.zagum.expandicon.ExpandIconView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.typ.muslim.R;
import com.typ.muslim.enums.ExpansionState;
import com.typ.muslim.interfaces.ExpansionListener;
import com.typ.muslim.managers.AManager;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ExpandableContainer extends ViewContainer {

	// Statics
	private static final String TAG = "ExpandableContainer";
	// Runtime
	private ExpansionState expansionState;
	private boolean isUsingDefaultHeader = true;
	private @LayoutRes int layoutHeaderResId = -1, layoutContentResId = -1;
	private String defHeaderTitle = "Title";
	private @DrawableRes int defHeaderIcon;
	// Views
	private MaterialCardView cardHeader;
	private FrameLayout flContent;

	public ExpandableContainer(@NonNull @NotNull Context context) {
		super(context);
	}

	public ExpandableContainer(@NonNull @NotNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public ExpandableContainer(@NonNull @NotNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public void parseAttrs(Context context, @Nullable AttributeSet attrs) {
		if (attrs != null) {
			TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ExpandableContainer);
			// Get header and content layoutResIds from attrs
			layoutHeaderResId = ta.getResourceId(R.styleable.ExpandableContainer_exc_header, -1);
			layoutContentResId = ta.getResourceId(R.styleable.ExpandableContainer_exc_content, -1);
			defHeaderIcon = ta.getResourceId(R.styleable.ExpandableContainer_exc_def_header_icon, -1);
			defHeaderTitle = ta.getString(R.styleable.ExpandableContainer_exc_def_header_title);
			ta.recycle();
		} else layoutHeaderResId = -1;
	}

	@Override
	public void prepareRuntime(Context context) {
		// Determine actual expansion state
		expansionState = layoutContentResId == -1 ? ExpansionState.COLLAPSED : ExpansionState.EXPANDED;
	}

	@Override
	public void prepareView(Context context) {
		// Inflate view
		inflate(R.layout.view_expandable_container);
		// Get base inner views
		cardHeader = $(R.id.card_exp_header_view);
		flContent = $(R.id.fl_exp_content_view);
		if (isInEditMode()) {
			useDefaultHeader().setTitle(defHeaderTitle).setIcon(defHeaderIcon == -1 ? R.drawable.ic_done : defHeaderIcon);
			return;
		}
		// Header
		if (layoutHeaderResId != -1) setHeader(layoutHeaderResId); // Add custom header.
		else useDefaultHeader().setTitle(defHeaderTitle).setIcon(defHeaderIcon == -1 ? R.drawable.ic_done : defHeaderIcon); // Use default header.
		// Content
		if (layoutContentResId != -1) setContent(layoutContentResId); // Add custom content.
		expansionState =ExpansionState.COLLAPSED;
		flContent.getLayoutParams().height = 0;
		flContent.setVisibility(GONE);
		flContent.requestLayout();
		// Set listeners
		setListeners();
	}

	private boolean hasHeader() {
		AManager.log(TAG, "hasHeader: %d", cardHeader.getChildCount());
		return cardHeader.getChildCount() > 0;
	}

	public boolean hasContent() {
		AManager.log(TAG, "hasContent: %d", flContent.getChildCount());
		return flContent.getChildCount() > 0;
	}

	@Override
	void setListeners() {
		cardHeader.setOnClickListener(v -> toggle());
	}

	@Override
	public void expand() {
		// Do nothing if has no content or it's already expanded
		if (expansionState != ExpansionState.COLLAPSED || !hasContent()) {
			Toast.makeText(getContext(), "Can't expand", Toast.LENGTH_SHORT).show();
			return;
		}
		// Measure target content view height
		flContent.measure(MATCH_PARENT, WRAP_CONTENT);
		final int targetHeight = flContent.getMeasuredHeight();
		AManager.log(TAG, "wannaExpandToHeight: %d", targetHeight);
		// Make content container visible and make its height = 0
		flContent.getLayoutParams().height = 0;
		flContent.setVisibility(VISIBLE);
		// Create expand animation
		final Animation animExpand = new Animation() {
			@Override
			protected void applyTransformation(float it, Transformation t) {
				if (it == 1) {
					// Animation has finished
					flContent.getLayoutParams().height = WRAP_CONTENT;
				} else flContent.getLayoutParams().height = (int) (it * targetHeight); // Still animating
				flContent.requestLayout();
				((DefaultHeaderView) cardHeader.getChildAt(0)).syncArrow(it);
				// Call listener
				if (listener != null) listener.onStateUpdate(ExpansionState.EXPANDING, it);
				AManager.log(TAG, "ExpandingProgress[%.2f|%d]", it, (int) (it * targetHeight));
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};
		animExpand.setDuration(500);
		animExpand.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				expansionState = ExpansionState.EXPANDING;
				AManager.log(TAG, "Expanding...");
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
		// Start the animation on view
		flContent.startAnimation(animExpand);
	}

	@Override
	public void collapse() {
		// Collapse content
		final int initialHeight = flContent.getMeasuredHeight();
		AManager.log(TAG, "collapsingFromHeight: %d", initialHeight);
		final Animation animCollapse = new Animation() {
			@Contract(pure = true)
			@Override
			public boolean willChangeBounds() {
				return true;
			}

			@Override
			protected void applyTransformation(float it, Transformation t) {
				flContent.getLayoutParams().height = initialHeight - (int) (it * initialHeight); // Animating.
				flContent.requestLayout(); // Request container re-layout.
				if (isUsingDefaultHeader) ((DefaultHeaderView) cardHeader.getChildAt(0)).syncArrow(it);
				// Call listener
				if (listener != null) listener.onStateUpdate(ExpansionState.COLLAPSING, it);
				AManager.log(TAG, "CollapsingProgress[%f|%d]", it, initialHeight - (int) (it * initialHeight));
			}
		};
		animCollapse.setDuration(500);
		animCollapse.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				expansionState = ExpansionState.COLLAPSING;
				AManager.log(TAG, "Collapsing...");
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				flContent.setVisibility(GONE);
				expansionState = ExpansionState.COLLAPSED;
				if (listener != null) listener.onStateChanged(ExpansionState.COLLAPSED);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}
		});
		flContent.startAnimation(animCollapse);
	}

	@Override
	public void toggle() {

//		if (expansionState == ExpansionState.COLLAPSED) {
//			flContent.animate().scaleY(1f).alpha(1f).setDuration(300).start();
//			expansionState = ExpansionState.EXPANDED;
//		}
//		else {
//			flContent.animate().scaleY(0f).alpha(0f).setDuration(300).start();
//			expansionState = ExpansionState.COLLAPSED;
//		}


		if (expansionState == ExpansionState.COLLAPSED) expand();
		else collapse();
	}

	public ExpandableContainer setHeader(@NonNull View headerView) {
		isUsingDefaultHeader = headerView instanceof DefaultHeaderView;
		cardHeader.removeAllViews();
		cardHeader.addView(headerView, new FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
		return this;
	}

	public ExpandableContainer setHeader(@LayoutRes int headerResId) {
		setHeader(View.inflate(getContext(), headerResId, null));
		return this;
	}

	public ExpandableContainer setContent(@NonNull View contentView) {
		flContent.removeAllViews();
		flContent.addView(contentView, new FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
		flContent.measure(MATCH_PARENT, WRAP_CONTENT);
		return this;
	}

	public ExpandableContainer setContent(@LayoutRes int contentResId) {
		setContent(View.inflate(getContext(), contentResId, null));
		return this;
	}

	public DefaultHeaderView useDefaultHeader() {
		final DefaultHeaderView defHV = new DefaultHeaderView(getContext()).setTitle("Title").setIcon(R.drawable.ic_done);
		setHeader(defHV);
		return defHV;
	}

	public void setExpansionListener(ExpansionListener listener) {
		this.listener = listener;
	}

	@NonNull
	public FrameLayout getContentView() {
		return flContent;
	}

	private static class DefaultHeaderView extends ViewContainer {

		// Views
		private ImageView ivIcon;
		private MaterialTextView tvTitle;
		private ExpandIconView expandIcon;

		public DefaultHeaderView(@NonNull @NotNull Context context) {
			super(context);
		}

		public DefaultHeaderView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
			super(context, attrs);
		}

		public DefaultHeaderView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
			super(context, attrs, defStyleAttr);
		}

		@Override
		public void prepareView(Context context) {
			inflate(R.layout.layout_exp_cont_def_header);
			ivIcon = $(R.id.iv_header_icon);
			tvTitle = $(R.id.tv_header_title);
			expandIcon = $(R.id.expand_icon);
			// Reset views
			ivIcon.setImageDrawable(null);
			tvTitle.setText("");
			expandIcon.setState(ExpandIconView.MORE, false);
		}

		public DefaultHeaderView setIcon(@DrawableRes int iconResId) {
			ivIcon.setImageResource(iconResId);
			return this;
		}

		public DefaultHeaderView setIcon(@Nullable Drawable icon) {
			ivIcon.setImageDrawable(icon);
			return this;
		}

		public DefaultHeaderView setTitle(@StringRes int titleResId) {
			tvTitle.setText(titleResId);
			return this;
		}

		public DefaultHeaderView setTitle(@Nullable String title) {
			tvTitle.setText(title);
			return this;
		}

		public DefaultHeaderView toggleExpanded() {
			expandIcon.switchState(true);
			return this;
		}

		public void syncArrow(float progress) {
			expandIcon.setFraction(1f - progress, true);
		}

	}

}

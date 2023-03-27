/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.prays;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.typ.muslim.R;
import com.typ.muslim.core.praytime.enums.Prays;
import com.typ.muslim.interfaces.PrayTimeCameListener;
import com.typ.muslim.libs.easyjava.data.EasyList;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.managers.PrayerManager;
import com.typ.muslim.models.Pray;
import com.typ.muslim.ui.ViewContainer;
import com.typ.muslim.ui.utils.ViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PraysDashboardCard extends ViewContainer implements PrayTimeCameListener {

	// todo: Create PraySubCard.
	// todo: Replace RecyclerView with HorizontalScrollView and Queue to store cards.

	// Statics
	private static final String TAG = PraysDashboardCard.class.getSimpleName();
	// Callbacks
	private final List<PrayTimeCameListener> callbacks = new ArrayList<>();
	// Runtime
	private PraysAdapter praysAdapter;
	private EasyList<Pray> upcomingPrays;
	private Pray nextPray;
	// Views
	private EasyRecyclerView ervPrays;

	public PraysDashboardCard(@NonNull @NotNull Context context) {
		super(context);
	}

	public PraysDashboardCard(@NonNull @NotNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public PraysDashboardCard(@NonNull @NotNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public void prepareRuntime(Context context) {
		this.upcomingPrays = PrayerManager.getUpcomingPrays(context);
		this.nextPray = PrayerManager.getNextPray(context);
		this.praysAdapter = new PraysAdapter(context, this.upcomingPrays);
	}

	@Override
	public void prepareView(Context context) {
		// Inflate view layout
		inflate(context, R.layout.layout_prays_card, this);
		// Show prays cards
		ervPrays = findViewById(R.id.erv_prays);
		ervPrays.setItemAnimator(new PrayCardsAnimator());
		ervPrays.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
		ervPrays.setAdapter(praysAdapter);
//		this.todayPrays.iterate(this::addCard);
	}

	private void addCard(Pray pray) {
		ervPrays.addView(new PrayCard(getContext(), pray, this),
				new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
	}

	private void removeCard(Pray pray) {
		AManager.log(TAG, "Cards Count: %d", ervPrays.getChildCount());
		PrayCard card = (PrayCard) ervPrays.getChildAt(0);
		if (card.isHoldingPray(pray)) {
			Animation exitAnim = AnimationUtils.makeOutAnimation(getContext(), false);
			exitAnim.setDuration(1500);
			exitAnim.setInterpolator(new AccelerateDecelerateInterpolator());
			exitAnim.setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					AManager.log(TAG, "Removed PrayCard holding %s", pray);
					ervPrays.removeViews(0, 1);
					AManager.log(TAG, "Cards Count: %d", ervPrays.getChildCount());
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}
			});
			card.startAnimation(exitAnim);
		}
	}

	@Override
	public void refreshUI() {
		if (this.nextPray.getType() == Prays.ISHA) this.upcomingPrays = PrayerManager.getUpcomingPrays(getContext());
		this.nextPray = PrayerManager.getNextPray(getContext());
	}

	@Override
	public Pray onPrayTimeCame(Pray pray) {
		// Fire callbacks
		for (PrayTimeCameListener callback : callbacks) if (callback != null) callback.onPrayTimeCame(pray);
		// Perform refresh
		this.praysAdapter.refreshCards();
//		this.performRefresh();
		return this.nextPray;
	}

	public PraysDashboardCard attachCallback(PrayTimeCameListener callback) {
		this.callbacks.add(callback);
		return this;
	}

	public static class PrayCardsAnimator extends DefaultItemAnimator {

		@Override
		public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromX, int fromY, int toX, int toY) {
			return false;
		}
	}

	private class PraysAdapter extends RecyclerView.Adapter<ViewHolder<PrayCard>> {

		// Runtime
		private final Context context;
		private final EasyList<Pray> upcomingPrays;

		public PraysAdapter(Context context, EasyList<Pray> upcomingPrays) {
			this.context = context;
			this.upcomingPrays = upcomingPrays;
		}

		public void refreshCards() {
			// Remove passed pray and activate next card
			if (upcomingPrays.size() > 0) PraysDashboardCard.this.upcomingPrays.remove(0);
			// Notify adapter about change
			notifyItemRemoved(0);
		}

		@Override
		public int getItemViewType(int position) {
			return position;
		}

		@NonNull
		@NotNull
		@Override
		public ViewHolder<PrayCard> onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int pos) {
			Pray pray = PraysDashboardCard.this.upcomingPrays.get(pos);
			AManager.log(TAG, "onCreateViewHolder: %d | %s", pos, pray);
			return new ViewHolder<>(new PrayCard(context, pray, PraysDashboardCard.this));
		}

		@Override
		public void onBindViewHolder(@NonNull @NotNull ViewHolder<PrayCard> holder, int position) {
			PrayCard prayCard = (PrayCard) holder.itemView;
			if (position == 0) prayCard.activate();
			else prayCard.suspend();
		}

		@Override
		public int getItemCount() {
			return PraysDashboardCard.this.upcomingPrays.size();
		}

	}

}

/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textview.MaterialTextView;
import com.mpt.android.stv.SpannableTextView;
import com.typ.muslim.R;
import com.typ.muslim.interfaces.PrayTimeCameListener;
import com.typ.muslim.models.Pray;
import com.typ.muslim.models.Timestamp;

import org.jetbrains.annotations.NotNull;

public class SquarePrayView extends ViewContainer implements PrayTimeCameListener {

	// TAG
	private static final String TAG = "SquarePrayView";
	// Runtime
	private @NonNull Pray pray;
	// Views
	private ImageView ivIconIndicator;
	private SpannableTextView stvPrayName;
	private MaterialTextView tvPrayTime;

	public SquarePrayView(@NonNull @NotNull Context context) {
		super(context);
	}

	public SquarePrayView(@NonNull @NotNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public SquarePrayView(@NonNull @NotNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public void prepareView(Context context) {
		inflate(R.layout.view_square_pray);
		ivIconIndicator = $(R.id.img_pray_icon_indicator);
		stvPrayName = $(R.id.stv_pray_name);
		tvPrayTime = $(R.id.tv_pray_time);
	}

	public SquarePrayView setPray(@NonNull Pray pray) {
		// Update runtime
		this.pray = pray;
		// Set texts
		stvPrayName.reset();
		stvPrayName.setText(pray.getPrayNameRes());
		tvPrayTime.setText(pray.getFormattedTime(getContext()));
		// Change indicator or show icon
		if (pray.getIn().isBefore(Timestamp.NOW()) || pray.getIn().isToday()) {
			// Show indicator
			if (pray.hasPassed()) {
				ivIconIndicator.setImageResource(R.drawable.ic_done);
				ivIconIndicator.setBackgroundResource(R.drawable.shape_passed_pray);
				ivIconIndicator.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.green)));
			} else {
				ivIconIndicator.setImageDrawable(null);
				ivIconIndicator.setBackgroundResource(R.drawable.shape_next_pray);
				ivIconIndicator.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.green)));
			}
		} else {
			// Show icon
			ivIconIndicator.setImageResource(pray.getPrayIconRes());
			ivIconIndicator.setBackgroundResource(R.drawable.shape_next_pray);
			ivIconIndicator.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.darkAdaptiveColor)));
		}
		return this;
	}

	@Override
	public Pray onPrayTimeCame(Pray pray) {
		this.setPray(this.pray);
		return null;
	}

	@Override
	public void onClick(View v) {
		// todo: Expand the view to a dialog or BottomSheet only
		//  if the pray has passed or is the next pray showing the info and count down of remaining time to the next pray
	}
}

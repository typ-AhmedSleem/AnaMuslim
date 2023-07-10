/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.chip.Chip;
import com.google.android.material.textview.MaterialTextView;
import com.typ.muslim.R;
import com.typ.muslim.enums.PrayStatus;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.features.prays.models.Pray;
import com.typ.muslim.models.PrayTrackerRecord;
import com.typ.muslim.models.Timestamp;
import com.typ.muslim.ui.home.DashboardCard;

public class TrackerPrayRecordView extends DashboardCard {

	// Runtime
	private Pray pray;
	private PrayTrackerRecord prayRecord;
	// Views
	private MaterialTextView tvPrayName, tvPrayTime;
	private Chip chipPrayStatus, chipAtMosque;

	public TrackerPrayRecordView(@NonNull Context context) {
		super(context);
	}

	public TrackerPrayRecordView(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public TrackerPrayRecordView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public void prepareCardView(Context context) {
		inflate(R.layout.layout_tracker_pray_record_view);
		setCardBackgroundColor(getColor(R.color.bg_input_box));
		// Init views
		tvPrayName = $(R.id.tv_tpv_pray_name);
		tvPrayTime = $(R.id.tv_tpv_pray_time);
		chipPrayStatus = $(R.id.chip_tpv_pray_status);
		chipAtMosque = $(R.id.chip_tpv_at_mosque);
	}

	@Override
	public void refreshUI() {
		tvPrayName.setText(pray.getPrayNameRes());
		tvPrayTime.setText(pray.getFormattedTime(getContext()));
		if (prayRecord == null || Timestamp.NOW().isBefore(pray.getIn())) {
			chipAtMosque.setVisibility(GONE);
			chipPrayStatus.setVisibility(GONE);
		} else {
			chipPrayStatus.setVisibility(VISIBLE);
			chipAtMosque.setVisibility(prayRecord.wasAtMosque() ? VISIBLE : GONE);
			if (prayRecord.getStatus() == PrayStatus.ON_TIME) {
				chipPrayStatus.setText(R.string.prayed_on_time);
				chipPrayStatus.setChipIconResource(R.drawable.ic_done);
				chipPrayStatus.setChipBackgroundColorResource(R.color.green);
			} else if (prayRecord.getStatus() == PrayStatus.DELAYED) {
				chipPrayStatus.setText(R.string.prayed_late);
				chipPrayStatus.setChipIconResource(R.drawable.ic_time_clock_filled);
				chipPrayStatus.setChipBackgroundColorResource(R.color.cardBackground2);
			} else {
				chipPrayStatus.setText(R.string.forgot_or_missed);
				chipPrayStatus.setChipIconResource(R.drawable.ic_close);
				chipPrayStatus.setChipBackgroundColorResource(R.color.red);
			}
		}
	}

	public void holdRecord(Pray pray, PrayTrackerRecord record) {
		this.pray = pray;
		prayRecord = record;
		refreshUI();
		AManager.log("TPRV", "holdRecord: [%s] | [%s]", pray, record);
	}

}

/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.fragments.panel;

import static java.util.Calendar.LONG;
import static java.util.Calendar.YEAR;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textview.MaterialTextView;
import com.typ.muslim.R;
import com.typ.muslim.core.ummelqura.UmmalquraCalendar;
import com.typ.muslim.libs.easyjava.data.EasyList;
import com.typ.muslim.managers.AMRes;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.managers.PrayerManager;
import com.typ.muslim.models.Pray;
import com.typ.muslim.models.PrayTimes;
import com.typ.muslim.ui.VerticalPrayView;
import com.typ.muslim.ui.dashboard.prays.VerticalPraysDashboardCard;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import cn.iwgang.countdownview.CountdownView;

public class PraysFragment extends Fragment {

	// Statics
	private static final String TAG = "PraysFragment";
	private final UmmalquraCalendar hijriCalendar = new UmmalquraCalendar();
	private final EasyList<VerticalPrayView> prayIVs = new EasyList<>();
	// Interfaces
	private final VerticalPraysDashboardCard.PrayNotifyMethodChangedCallback notifyMethodChangedCallback;
	// Runtime
	private Pray nextPray;
	private PrayTimes prays;
	// Views
	private MaterialTextView hijriDateTV, nextPrayNameTV, nextPrayTimeTV;
	private CountdownView nextPrayCDV;

	public PraysFragment() {
		notifyMethodChangedCallback = null;
	}

	public PraysFragment(PrayTimes prays, VerticalPraysDashboardCard.PrayNotifyMethodChangedCallback notifyMethodChangedCallback) {
		this.prays = prays;
		this.notifyMethodChangedCallback = notifyMethodChangedCallback;

	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Prepare runtime data
		if (this.prays == null) this.performInternalUpdate();
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.nextPray = PrayerManager.getNextPray(getContext(), prays);
		return inflater.inflate(R.layout.bs_prays, container, false);
	}

	@Override
	public void onViewCreated(@NonNull @NotNull View fragmentView, @Nullable Bundle savedInstanceState) {
		// Setup views
		this.hijriDateTV = fragmentView.findViewById(R.id.stv_hijri_date);
		this.nextPrayNameTV = fragmentView.findViewById(R.id.tv_next_pray_name);
		this.nextPrayTimeTV = fragmentView.findViewById(R.id.tv_next_pray_time);
		this.nextPrayCDV = fragmentView.findViewById(R.id.cdv_next_pray_remaining);
		this.prayIVs.add(fragmentView.findViewById(R.id.fajrPIV)); // Fajr.
		this.prayIVs.add(fragmentView.findViewById(R.id.sunrisePIV)); // Sunrise.
		this.prayIVs.add(fragmentView.findViewById(R.id.dhuhrPIV)); // Dhuhr.
		this.prayIVs.add(fragmentView.findViewById(R.id.asrPIV)); // Asr.
		this.prayIVs.add(fragmentView.findViewById(R.id.maghribPIV)); // Maghrib.
		this.prayIVs.add(fragmentView.findViewById(R.id.ishaPIV)); // Isha.
		// Update views
		this.refreshViews();
		// Setup callbacks and listeners
		this.prayIVs.iterate(piv -> piv.setCallback(notifyMethodChangedCallback));
		this.nextPrayCDV.setOnCountdownEndListener(cv -> this.refreshViews());
	}

	private void performInternalUpdate() {
		// Update runtime of anythings except views
		this.prays = PrayerManager.getTodayPrays(getContext());
		this.nextPray = this.getNextPray(false);
	}

	protected void refreshViews() {
		// Update TextViews content
		this.hijriDateTV.setText(String.format(Locale.getDefault(), "%d %s %d %s", hijriCalendar.get(UmmalquraCalendar.DATE),
				hijriCalendar.getDisplayName(UmmalquraCalendar.MONTH, LONG, Locale.getDefault()),
				hijriCalendar.get(YEAR), AMRes.getString(requireContext(), R.string.H))); // Hijri Date.
		this.nextPrayNameTV.setText(this.getNextPray(false).getName());
		this.nextPrayTimeTV.setText(this.getNextPray(false).getFormattedTime(getContext()));
		// Update CDV
		this.nextPrayCDV.start(this.getNextPray(false).getIn().toMillis() - System.currentTimeMillis());
		// Update PIVs
		prayIVs.loop((index, piv) -> {
			piv.setPray(prays.get(index));
			return false; // Loop to last item in list.
		});
	}

	private Pray getNextPray(boolean performUpdate) {
		this.nextPray = performUpdate ? this.nextPray = PrayerManager.getNextPray(requireContext()) : PrayerManager.getNextPray(requireContext(), this.prays);
		AManager.log(TAG, "getNextPray: " + this.nextPray);
		return this.nextPray;
	}

	@Override
	public void onResume() {
		super.onResume();
		this.refreshViews(); // Update views to new refreshed data.
	}

	@Override
	public void onPause() {
		super.onPause();
		this.nextPrayCDV.pause();
	}

}

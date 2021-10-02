/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.calendar;

import static com.typ.muslim.ui.AnimatedTextView.Direction.NEXT;
import static com.typ.muslim.ui.AnimatedTextView.Direction.NONE;
import static com.typ.muslim.ui.AnimatedTextView.Direction.PREV;
import static com.typ.muslim.ui.Dimension.PixelType.SP;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.typ.muslim.R;
import com.typ.muslim.enums.FormatPatterns;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.managers.HijriCalendar;
import com.typ.muslim.models.HijriDate;
import com.typ.muslim.models.Timestamp;
import com.typ.muslim.ui.AnimatedTextView;
import com.typ.muslim.ui.Dimension;
import com.typ.muslim.ui.dashboard.DashboardCard;

import java.util.Locale;

public class SelectedDateViewCard extends DashboardCard {

	// Runtime
	private HijriDate hijriDate;
	private boolean isToday;
	// Views
	private AnimatedTextView atvHeader, atvHijri, atvGeorg;
	// Listeners
	private OnClickListener listener;

	public SelectedDateViewCard(Context context) {
		super(context);
	}

	public SelectedDateViewCard(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SelectedDateViewCard(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public void prepareRuntime(Context context) {
		hijriDate = HijriCalendar.getToday();
		isToday = true;
	}

	@Override
	public void prepareCardView(Context context) {
		inflate(R.layout.layout_sdv_card);
		setRippleColorResource(R.color.ripple_som_card);
		// Init views
		atvHeader = $(R.id.atv_sdv_header);
		atvHijri = $(R.id.atv_sdv_hijri);
		atvGeorg = $(R.id.atv_sdv_georg);
		// Perform UI refresh
		refreshUI();
	}

	@Override
	public void refreshUI() {
		// Header tv
		if (isToday) atvHeader.setText(R.string.today, AnimatedTextView.Direction.NONE);
		else atvHeader.setText(String.format(Locale.getDefault(), "%s (%s)", getString(R.string.selected), getString(R.string.click_to_jump_today)), AnimatedTextView.Direction.NONE);
		// Dates tvs
		atvHijri.setText(String.format(Locale.getDefault(), "%d %s, %d%s",
				hijriDate.getDay(),
				hijriDate.getMonthName(getContext()),
				hijriDate.getYear(), getString(R.string.H)), NONE);
		atvGeorg.setText(hijriDate.toGregorian().getFormatted(FormatPatterns.DATE_FULL), NONE);
	}

	@NonNull
	private SpannableString buildSelectedText() {
		// Build text slices and full final string
		final String slice1 = getString(R.string.selected);
		final String slice2 = getString(R.string.click_to_jump_today);
		final SpannableString spannedText = new SpannableString(String.format(Locale.getDefault(), "%s (%s)", slice1, slice2));
		// Apply spans to text
		int cursor = 0;
		spannedText.setSpan(new ForegroundColorSpan(getColor(R.color.darkAdaptiveColor)),
				cursor, cursor + slice1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannedText.setSpan(new AbsoluteSizeSpan(new Dimension(getContext(), SP, 10f).toPixels()),
				cursor, cursor + slice1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		cursor += slice1.length() + 1; // Plus one cuz of the white space.
		spannedText.setSpan(new ForegroundColorSpan(getColor(R.color.green)),
				cursor, cursor + slice1.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannedText.setSpan(new AbsoluteSizeSpan(new Dimension(getContext(), SP, 5f).toPixels()),
				cursor, cursor + slice2.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		// Return spanned text
		return spannedText;
	}

	public void select(Timestamp tsSelected) {
		if (tsSelected == null) return;
		Timestamp tsCurrent = hijriDate.toGregorian();
		if (!tsCurrent.dateMatches(tsSelected)) {
			// New date has been selected
			final AnimatedTextView.Direction animDir = tsSelected.isAfter(tsCurrent) ? NEXT : PREV;
			hijriDate = tsSelected.toHijri();
			isToday = tsSelected.isToday();
			// Update header tv
			atvHeader.setText(isToday ? R.string.today : R.string.selected, animDir);
			// Update dates tvs
			atvHijri.setText(String.format(Locale.getDefault(), "%d %s, %d%s",
					hijriDate.getDay(),
					hijriDate.getMonthName(getContext()),
					hijriDate.getYear(), getString(R.string.H)), animDir);
			atvGeorg.setText(tsSelected.getFormatted(FormatPatterns.DATE_FULL), animDir);
		}
	}

	public void setClickListener(@Nullable OnClickListener listener) {
		this.listener = listener;
	}

	@Override
	public void onClick(View v) {
		if (listener != null) listener.onClick(v);
	}
}

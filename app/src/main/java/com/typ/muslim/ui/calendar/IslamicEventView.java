/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.calendar;

import static com.typ.muslim.enums.FormatPatterns.DATE_NORMAL;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.card.MaterialCardView;
import com.mpt.android.stv.Slice;
import com.mpt.android.stv.SpannableTextView;
import com.typ.muslim.R;
import com.typ.muslim.managers.IslamicEvents;
import com.typ.muslim.managers.ResMan;
import com.typ.muslim.models.IslamicEvent;
import com.typ.muslim.models.Timestamp;
import com.typ.muslim.ui.BottomSheets;
import com.typ.muslim.ui.ViewContainer;
import com.typ.muslim.ui.activities.IslamicEventsActivity;
import com.typ.muslim.utils.DateUtils;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class IslamicEventView extends ViewContainer {

	// Runtime
	private IslamicEvent event;
	private boolean showDay = true, displayDates = true;
	// Views
	private SpannableTextView stvDay, stvTitle;

	public IslamicEventView(@NonNull @NotNull Context context) {
		super(context);
	}

	public IslamicEventView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public IslamicEventView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public void prepareRuntime(Context context) {
		event = IslamicEvents.getNearestEvent();
		displayDates = true;
		showDay = true;
	}

	@Override
	public void prepareView(Context context) {
		// Setup container
		inflate(R.layout.item_islamic_event_v2);
		((MaterialCardView) getChildAt(0)).setCardBackgroundColor(getColor(event != null ? event.getColorResId() : R.color.bg_input_box));
		// Init views
		stvDay = $(R.id.stv_islamic_event_day);
		stvTitle = $(R.id.tv_islamic_event_title);
		// Click listener
		setOnClickListener(v -> BottomSheets.newIslamicEventDetails(getContext(), event).show());
		setOnLongClickListener(v -> {
			// todo: Scroll to clicked event in IslamicEventsActivity by passing the event as bundle
			startActivity(new Intent(getContext(), IslamicEventsActivity.class));
			return true;
		});
		// Refresh UI
		refreshUI();
	}

	@Override
	public void refreshUI() {
		if (event == null) return;
		stvDay.reset();
		stvTitle.reset();
		// Event day
		if (showDay) {
			stvDay.addSlice(new Slice.Builder(DateUtils.getDayName(Timestamp.NOW(), "3") + "\n")
                    .textColor(ResMan.getColor(getContext(), R.color.darkAdaptiveColor))
					.textSize(sp2px(12f))
					.style(Typeface.BOLD)
					.build());
			stvDay.addSlice(new Slice.Builder(String.valueOf(event.getDay()))
                    .textColor(ResMan.getColor(getContext(), R.color.darkAdaptiveColor))
					.textSize(sp2px(26f))
					.build());
		} else stvDay.setVisibility(GONE);
		// Event title and date
		final Slice.Builder tsb = new Slice.Builder(getString(event.getTitleStringResId()) + (displayDates ? "\n" : ""))
				.style(Typeface.BOLD)
				.textColor(getColor(R.color.darkAdaptiveColor))
				.textSize(sp2px(displayDates ? 16f : 20f));
		if (displayDates) tsb.superscript();
		stvTitle.addSlice(tsb.build());
		if (displayDates) {
			stvTitle.addSlice(new Slice.Builder(event.toGregorian().getFormatted(DATE_NORMAL))
					.style(Typeface.BOLD)
					.textColor(getColor(R.color.subtitleTextColor))
					.textSize(sp2px(14f))
					.build());
		}
		stvDay.display();
		stvTitle.display();
	}

	private String buildDatesString() {
		return String.format(Locale.getDefault(),
				"%d %s %d%s | %s",
				event.getDay(),
				event.getMonthName(getContext()),
				event.getYear(),
				getString(R.string.H),
				event.toGregorian().getFormatted(DATE_NORMAL));
	}

	public IslamicEventView displayDates(boolean displayDates) {
		this.displayDates = displayDates;
		refreshUI();
		return this;
	}

	public IslamicEventView displayDay(boolean showDay) {
		this.showDay = showDay;
		stvDay.setVisibility(this.showDay ? VISIBLE : GONE);
		return this;
	}

	public void setEvent(IslamicEvent event) {
		this.event = event;
		refreshUI();
	}

}

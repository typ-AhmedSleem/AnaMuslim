/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.activities;

import static com.typ.muslim.utils.DisplayUtils.sp2px;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textview.MaterialTextView;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.StickyHeaderDecoration;
import com.mpt.android.stv.Slice;
import com.mpt.android.stv.SpannableTextView;
import com.typ.muslim.R;
import com.typ.muslim.managers.HijriCalendar;
import com.typ.muslim.managers.IslamicEvents;
import com.typ.muslim.managers.ResMan;
import com.typ.muslim.models.HijriDate;
import com.typ.muslim.models.IslamicEvent;
import com.typ.muslim.ui.BottomSheets;
import com.typ.muslim.utils.DateUtils;

import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class IslamicEventsActivity extends AppCompatActivity {

	// Runtime
	private List<IslamicEvent> thisYearEvents;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Hide default actionbar
		if (getSupportActionBar() != null) getSupportActionBar().hide();
		// Set content view
		setContentView(R.layout.activity_islamic_events);
		// Get events for this year
		thisYearEvents = IslamicEvents.getEventsThisYear();
		// Setup Adapter
		final IslamicEventsAdapter eventsAdapter = new IslamicEventsAdapter(this, thisYearEvents);
		eventsAdapter.setOnItemClickListener(pos -> BottomSheets.newIslamicEventDetails(this, thisYearEvents.get(pos)).show());
		// Init views
		final HijriDate today = HijriCalendar.getToday();
		EasyRecyclerView ervEvents = findViewById(R.id.erv_islamic_events);
		ervEvents.setItemAnimator(new SlideInUpAnimator());
		ervEvents.setLayoutManager(new LinearLayoutManager(this));
		ervEvents.addItemDecoration(new StickyHeaderDecoration(new StickyHeaderAdapter()));
		ervEvents.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
		ervEvents.setAdapter(eventsAdapter);
		ervEvents.showRecycler();
		// Listeners
		((MaterialToolbar) findViewById(R.id.toolbar)).setNavigationOnClickListener(b -> this.finish());
	}

	private static class IslamicEventsAdapter extends RecyclerArrayAdapter<IslamicEvent> {

		public IslamicEventsAdapter(Context context, List<IslamicEvent> events) {
			super(context, events);
		}

		@Override
		public IslamicEventItemVH OnCreateViewHolder(ViewGroup parent, int viewType) {
			return new IslamicEventItemVH(parent);
		}

		private static class IslamicEventItemVH extends BaseViewHolder<IslamicEvent> {

			// Views
			private final SpannableTextView stvEventDay;
			private final SpannableTextView tvEventTitle;

			public IslamicEventItemVH(ViewGroup parent) {
				super(parent, R.layout.item_islamic_event_v2);
				// Init views
				stvEventDay = $(R.id.stv_islamic_event_day);
				tvEventTitle = $(R.id.tv_islamic_event_title);
			}

			@Override
			public void setData(IslamicEvent event) {
				// Event hijri day
				stvEventDay.reset();
				stvEventDay.addSlice(new Slice.Builder(DateUtils.getDayName(event.toGregorian(), "3") + "\n")
                        .textColor(ResMan.getColor(getContext(), R.color.subtitleTextColor))
						.textSize(sp2px(getContext(), 12f))
						.build());
				stvEventDay.addSlice(new Slice.Builder(String.valueOf(event.getDay()))
                        .textColor(ResMan.getColor(getContext(), R.color.darkAdaptiveColor))
						.textSize(sp2px(getContext(), 26f))
						.build());
				stvEventDay.display();
				// Event title
				tvEventTitle.setText(event.getTitleStringResId());
			}
		}

		private static class StickyHeaderVH extends BaseViewHolder<String> {

			public StickyHeaderVH(ViewGroup parent) {
				super(parent, R.layout.sthdr_islamic_event_month);
			}

			@Override
			public void setData(String monthName) {
				if (itemView instanceof MaterialTextView) ((MaterialTextView) itemView).setText(monthName);
				else ((MaterialTextView) itemView.findViewById(R.id.tv_hijri_month_name)).setText(monthName);
			}
		}

	}

	private class StickyHeaderAdapter implements StickyHeaderDecoration.IStickyHeaderAdapter<IslamicEventsAdapter.StickyHeaderVH> {
		@Override
		public long getHeaderId(int position) {
			return thisYearEvents.get(position).getMonth();
		}

		@Override
		public IslamicEventsAdapter.StickyHeaderVH onCreateHeaderViewHolder(ViewGroup parent) {
			return new IslamicEventsAdapter.StickyHeaderVH(parent);
		}

		@Override
		public void onBindHeaderViewHolder(IslamicEventsAdapter.StickyHeaderVH stickyHeaderVH, int pos) {
			stickyHeaderVH.setData(thisYearEvents.get(pos).getMonthName());
		}
	}
}


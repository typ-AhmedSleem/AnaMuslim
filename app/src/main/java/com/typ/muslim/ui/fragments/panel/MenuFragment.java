/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.fragments.panel;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.typ.muslim.R;

import org.jetbrains.annotations.NotNull;

public class MenuFragment extends Fragment {

	// Runtime
	private MenuAdapter menuAdapter;
	// Views
	private RecyclerView rvMenu;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Create and setup adapter
		if (this.menuAdapter == null) this.menuAdapter = new MenuAdapter(getContext());
	}

	@Override
	public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_menu, container, false);
	}

	@Override
	public void onViewCreated(@NonNull @NotNull View fragmentView, @Nullable Bundle savedInstanceState) {
		// Setup views
		this.rvMenu = fragmentView.findViewById(R.id.rv_menu);
		// Init views
		this.rvMenu.setItemAnimator(new DefaultItemAnimator());
		this.rvMenu.setLayoutManager(new GridLayoutManager(getContext(), 2));
		this.rvMenu.setHasFixedSize(true);
		this.rvMenu.setAdapter(this.menuAdapter);
	}

	protected static class MenuItem {

		private @DrawableRes final int icon;
		private @StringRes final int title;
		private @StringRes final int desc;
		private final boolean needActions;

		public MenuItem(@DrawableRes int icon, @StringRes int title, @StringRes int desc, boolean needActions) {
			this.icon = icon;
			this.title = title;
			this.desc = desc;
			this.needActions = needActions;
		}
	}

	protected static class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

		// Runtime
		private final Context context;
		private final MenuItem[] items = new MenuItem[]{
				new MenuItem(R.drawable.ic_time_clock_outlined, R.string.pray_times, R.string.pray_times, false),
				new MenuItem(R.drawable.ic_kaaba, R.string.qibla_compass, R.string.qibla_compass, false),
				new MenuItem(R.drawable.ic_calendar, R.string.hijri, R.string.hijri, false),
				new MenuItem(R.drawable.ic_mosque4, R.string.find_mosque, R.string.find_mosque, false),
				new MenuItem(R.drawable.ic_nearby, R.string.nearby, R.string.nearby, false),
				new MenuItem(R.drawable.ic_allah1, R.string.names_of_allah, R.string.names_of_allah, false),
				new MenuItem(R.drawable.ic_ramadan_moon, R.string.ramadan, R.string.ramadan, false),
				new MenuItem(R.drawable.ic_quran_star, R.string.quran, R.string.quran, false),
				new MenuItem(R.drawable.ic_tracker_stats, R.string.tracker, R.string.tracker, false),
				new MenuItem(R.drawable.ic_du3a2, R.string.du3a2, R.string.du3a2, false),
				new MenuItem(R.drawable.ic_allah2, R.string.azkar, R.string.azkar, false),
				new MenuItem(R.drawable.ic_tasbih, R.string.tasbeeh, R.string.tasbeeh, false),
				new MenuItem(R.drawable.ic_notify_without_sound, R.string.notifications, R.string.notifications, true),
				new MenuItem(R.drawable.ic_settings, R.string.settings, R.string.settings, false)
		};

		public MenuAdapter(Context context) {
			this.context = context;
		}

		@NonNull
		@NotNull
		@Override
		public MenuAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
			return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.menu_item, parent, false));
		}

		@Override
		public void onBindViewHolder(@NonNull @NotNull MenuAdapter.ViewHolder holder, int position) {
			if (items[position] == null) return;
			holder.iconIV.setImageResource(items[position].icon);
			holder.titleTV.setText(items[position].title);
		}

		@Override
		public int getItemCount() {
			return items.length;
		}

		protected static class ViewHolder extends RecyclerView.ViewHolder {

			// Views
			private final ImageView iconIV;
			private final MaterialTextView titleTV;

			public ViewHolder(@NonNull @NotNull View itemView) {
				super(itemView);
				this.iconIV = itemView.findViewById(R.id.ifv_menu_icon);
				this.titleTV = itemView.findViewById(R.id.tv_menu_title);
			}
		}

	}

}

/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.managers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bitvale.switcher.SwitcherX;
import com.furkanakdemir.surroundcardview.SurroundCardView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.StickyHeaderDecoration;
import com.paulrybitskyi.valuepicker.model.PickerItem;
import com.typ.muslim.R;
import com.typ.muslim.adapters.CitiesAdapter;
import com.typ.muslim.interfaces.ResultCallback;
import com.typ.muslim.interfaces.OnItemClickListener;
import com.typ.muslim.libs.EnhancedScaleTouchListener;
import com.typ.muslim.models.Location;
import com.typ.muslim.ui.AMNumberSelector;

import java.util.List;
import java.util.Objects;

/**
 * Contains some custom views and ready, self-handling BottomSheets
 */
public class ViewManager {

	// TODO: 2/24/21 Create InfoBottomSheet that shows title,visual animated gif that describes it, message and understood button

	public static class PreviewLocationBottomSheet {

		/**
		 * Creates a new instance of PreviewLocationBottomSheet.
		 */
		@SuppressLint("InflateParams")
		public PreviewLocationBottomSheet(Context context, Location location, View.OnClickListener onClickListener) {
			// Setup views
			View bsView = LayoutInflater.from(context).inflate(R.layout.bs_preview_location, null, false);
			BottomSheetDialog bs = new BottomSheetDialog(context);
			bs.setContentView(bsView);
			bs.setDismissWithAnimation(true);
			MaterialTextView tvCountry = bsView.findViewById(R.id.tv_country_name);
			MaterialTextView tvCity = bsView.findViewById(R.id.tv_city_name);
			MaterialTextView tvLat = bsView.findViewById(R.id.tv_latitude);
			MaterialTextView tvLng = bsView.findViewById(R.id.tv_longitude);
			MaterialTextView tvTimezone = bsView.findViewById(R.id.tv_timezone);
			MaterialButton btnContinue = bsView.findViewById(R.id.btn_continue);
			MaterialButton btnCancel = bsView.findViewById(R.id.btn_cancel);
			// Show Location info in views
			tvCountry.setText(location.getCountryName());
			tvCity.setText(location.getCityName());
			tvLat.setText(String.valueOf(location.getLatitude()));
			tvLng.setText(String.valueOf(location.getLongitude()));
			tvTimezone.setText(String.valueOf(location.getTimezone()));
			// Click listeners
			btnContinue.setOnTouchListener(new EnhancedScaleTouchListener() {
				@Override
				public void onClick(View v, float x, float y) {
					onClickListener.onClick(v);
					bs.cancel();
				}
			});
			btnCancel.setOnTouchListener(new EnhancedScaleTouchListener() {
				@Override
				public void onClick(View v, float x, float y) {
					bs.cancel();
				}
			});
			// Show BottomSheet
			bs.show();
		}

	}

	public static abstract class SearchCityBottomSheet implements OnItemClickListener<Location> {

		private final LocationManager locationManager;
		private final CitiesAdapter citiesAdapter;
		// Views
		private final EditText inputSearch;
		private final BottomSheetDialog bs;
		private EasyRecyclerView rv;
		/**
		 * Creates a new instance of SearchCityBottomSheet.
		 */
		@SuppressLint("InflateParams")
		public SearchCityBottomSheet(Context context, LocationManager locationManager) {
			// Runtime
			this.locationManager = locationManager;
			// Setup views
			View bsView = LayoutInflater.from(context).inflate(R.layout.bs_search_city, null, false);
			bs = new BottomSheetDialog(context);
			bs.setContentView(bsView);
			bs.setCanceledOnTouchOutside(false);
			bs.setDismissWithAnimation(true);
			bs.getBehavior().setFitToContents(false);
			inputSearch = bsView.findViewById(R.id.input_search_city);
			rv = bsView.findViewById(R.id.erv_search_cities);
			citiesAdapter = new CitiesAdapter(context, rv); // Cities Adapter
			rv.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
			rv.setItemAnimator(new DefaultItemAnimator());
			rv.addItemDecoration(createStickyHeaderDecoration());
			rv.setAdapterWithProgress(citiesAdapter);
			// Item click listeners
			citiesAdapter.setOnItemClickListener(position -> {
				this.onItemClick(citiesAdapter.getItem(position));
				bs.cancel();
			});
			inputSearch.setOnEditorActionListener((v, actionId, event) -> {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) refreshWithQuery(v.getText().toString());
				return true;
			});
			// Show BottomSheet
			bs.getBehavior().setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
			bs.show();
		}

		private StickyHeaderDecoration createStickyHeaderDecoration() {
			return new StickyHeaderDecoration(new StickyHeaderDecoration.IStickyHeaderAdapter<HeaderItemView>() {
				@Override
				public long getHeaderId(int position) {
					return Objects.hash(citiesAdapter.getItem(position).getCountryCode());
				}

				@Override
				public HeaderItemView onCreateHeaderViewHolder(ViewGroup parent) {
					return new HeaderItemView(parent);
				}

				@Override
				public void onBindHeaderViewHolder(HeaderItemView headerItemView, int i) {
					headerItemView.setData(citiesAdapter.getItem(i));
				}
			});
		}

		public void refresh(List<Location> locations) {
			citiesAdapter.refresh(locations);
			// Adapt bottom sheet height with items count
			adaptBottomSheetState();
		}

		/**
		 * Perform new search operation in local DB using newQuery if changed
		 */
		public void refreshWithQuery(String newQuery) {
			// Update runtime
			inputSearch.setText(newQuery);
			// Perform search if query changed
			citiesAdapter.refresh(locationManager.searchForCities(newQuery));
			// Adapt bottom sheet height with items count
			adaptBottomSheetState();
		}

		private void adaptBottomSheetState() {
			if (citiesAdapter.getCount() == 0) bs.getBehavior().setFitToContents(true);
			else if (citiesAdapter.getCount() <= 5) bs.getBehavior().setFitToContents(true);
			else if (citiesAdapter.getCount() > 5 && citiesAdapter.getCount() < 20) {
				bs.getBehavior().setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
				bs.getBehavior().setFitToContents(false);
			} else if (citiesAdapter.getCount() >= 20) {
				bs.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
				bs.getBehavior().setFitToContents(false);
			}
		}

		private static class HeaderItemView extends BaseViewHolder<Location> {

			public HeaderItemView(ViewGroup parent) {
				super(parent, android.R.layout.simple_list_item_1);
			}

			@Override
			public void setData(Location location) {
				AManager.log("SHV", "setData: " + location);
				((TextView) itemView).setText(location.getCountryName());
				((TextView) itemView).setTextColor(AMRes.getColor(getContext(), R.color.colorText));
				itemView.setBackgroundColor(AMRes.getColor(getContext(), R.color.bg_input_box));
			}
		}
	}

	public static class ChoiceSelectorBottomSheet {

		@SuppressLint("InflateParams")
		public ChoiceSelectorBottomSheet(Context context, String title, String subtitle, Choice currentChoice, List<Choice> choices, OnItemClickListener<Choice> listener) {
			// Runtime
			ChoicesAdapter choicesAdapter = new ChoicesAdapter(context, choices, currentChoice);
			// Setup Views
			View bsView = LayoutInflater.from(context).inflate(R.layout.bs_choice_selector, null, false);
			BottomSheetDialog bs = new BottomSheetDialog(context);
			bs.setContentView(bsView);
			bs.setDismissWithAnimation(true);
			bs.setCanceledOnTouchOutside(false);
			bsView.findViewById(R.id.btn_cancel).setOnTouchListener(new EnhancedScaleTouchListener() {
				@Override
				public void onClick(View v, float x, float y) { bs.dismiss(); }
			}); // Cancel button.
			((MaterialTextView) bsView.findViewById(R.id.tv_title)).setText(title);
			((MaterialTextView) bsView.findViewById(R.id.tv_subtitle)).setText(subtitle);
			EasyRecyclerView rv = bsView.findViewById(R.id.erv_choices);
			rv.setItemAnimator(new DefaultItemAnimator());
			rv.setLayoutManager(new LinearLayoutManager(context));
			rv.setAdapter(choicesAdapter);
			// Item Click Listener
			choicesAdapter.setItemClickListener(choice -> {
				listener.onItemClick(choice);
				bs.cancel();
			});
			// Show BottomSheet
			bs.show();
		}

		public static class Choice {

			private final String id;
			private final String title;
			private final String desc;

			public Choice(String id, String title, String desc) {
				this.id = id;
				this.title = title;
				this.desc = desc;
			}

			public String getId() {
				return id;
			}

			public String getTitle() {
				return title;
			}

			public String getDesc() {
				return desc;
			}

			@Override
			public boolean equals(Object o) {
				if (o == null) return false;
				if (this == o) return true;
				if (!(o instanceof Choice)) return false;
				Choice choice = (Choice) o;
				return id.equals(choice.id);
			}

			@Override
			public int hashCode() {
				return Objects.hash(id);
			}

			@Override
			public String toString() {
				return "Choice{" +
						"id='" + id + '\'' +
						", title='" + title + '\'' +
						", desc='" + desc + '\'' +
						'}';
			}


		}

		private static class ChoicesAdapter extends RecyclerArrayAdapter<Choice> {

			// Runtime
			private final Choice currentChoice;
			private com.typ.muslim.interfaces.OnItemClickListener<Choice> listener;

			public ChoicesAdapter(Context context, List<Choice> objects, Choice currentChoice) {
				super(context, objects);
				this.currentChoice = currentChoice;
			}

			public void setItemClickListener(com.typ.muslim.interfaces.OnItemClickListener<Choice> listener) {
				this.listener = listener;
			}

			@Override
			public BaseViewHolder<Choice> OnCreateViewHolder(ViewGroup parent, int viewType) {
				return new ChoiceViewHolder(parent);
			}

			private class ChoiceViewHolder extends BaseViewHolder<Choice> {

				// Views
				private final MaterialTextView tvChoiceTitle;
				private final MaterialTextView tvChoiceDesc;

				public ChoiceViewHolder(ViewGroup parent) {
					super(parent, R.layout.item_choice);
					// Setup Views
					tvChoiceTitle = $(R.id.tv_choice_title);
					tvChoiceDesc = $(R.id.tv_choice_desc);
				}

				@Override
				public void setData(Choice choice) {
					SurroundCardView card = (SurroundCardView) itemView;
					AManager.log("ViewManager", "setChoiceData: " + choice + " | " + currentChoice);
					// Bind views
					tvChoiceTitle.setText(choice.getTitle());
					tvChoiceDesc.setText(choice.getDesc());
					// Release the selection from this card at first
					card.setSurrounded(false);
					// Check if this choice is selected
					if (currentChoice != null && Integer.parseInt(currentChoice.getId()) == Integer.parseInt(choice.getId())) card.surround();
					else if (card.isCardSurrounded()) card.setSurrounded(false);
					// Listeners
					card.setOnClickListener(v -> {
						card.surround();
						listener.onItemClick(choice);
					});
				}
			}

		}
	}

	public static abstract class PrayMinutesPickerBottomSheet<T> implements ResultCallback<T> {
		// Views
		private final AMNumberSelector[] numberSelectors = new AMNumberSelector[5];

		public PrayMinutesPickerBottomSheet(Context context, @StringRes int title, @StringRes int subtitle, int[] currValues, int[] pickerLimit) {
			// Create content view
			View bsView = LayoutInflater.from(context).inflate(R.layout.bs_pray_minutes_picker, null, false);
			// Create BottomSheet Dialog
			BottomSheetDialog bs = new BottomSheetDialog(context);
			bs.setContentView(bsView);
			bs.setDismissWithAnimation(true);
			// Setup inner views
			MaterialTextView titleTV = bsView.findViewById(R.id.tv_title);
			MaterialTextView subTitleTV = bsView.findViewById(R.id.tv_subtitle);
			numberSelectors[0] = bsView.findViewById(R.id.amns_fajr_min);
			numberSelectors[1] = bsView.findViewById(R.id.amns_dhuhr_min);
			numberSelectors[2] = bsView.findViewById(R.id.amns_asr_min);
			numberSelectors[3] = bsView.findViewById(R.id.amns_maghrib_min);
			numberSelectors[4] = bsView.findViewById(R.id.amns_isha_min);
			MaterialButton saveButton = bsView.findViewById(R.id.btn_save);
			// Set Title and Subtitle
			titleTV.setText(title);
			subTitleTV.setText(subtitle);
			// Bind Selectors
			for (int pickerIndex = 0; pickerIndex < numberSelectors.length; pickerIndex++) {
				numberSelectors[pickerIndex].setLimits(pickerLimit[1], pickerLimit[0]);
				numberSelectors[pickerIndex].setCurrentValue(currValues[pickerIndex]);
			}
			// Listeners
			saveButton.setOnTouchListener(new EnhancedScaleTouchListener(100, 0.95f, 1f) {
				@Override
				public void onClick(View v, float x, float y) {
					// Send results
					Integer[] results = new Integer[5];
					for (int i = 0; i < numberSelectors.length; i++) results[i] = numberSelectors[i].getCurrentValue();
					onResult((T) results);
					// Show confirmation toast and dismiss BottomSheet
					Toast.makeText(context, context.getString(R.string.saved), Toast.LENGTH_SHORT).show();
					bs.cancel();
				}
			});
			bsView.findViewById(R.id.card_save).setOnClickListener(v -> {
				// Send results
				Integer[] results = new Integer[5];
				for (int i = 0; i < numberSelectors.length; i++) results[i] = numberSelectors[i].getCurrentValue();
				onResult((T) results);
				// Show confirmation toast and dismiss BottomSheet
				Toast.makeText(context, context.getString(R.string.saved), Toast.LENGTH_SHORT).show();
				bs.cancel();
			});
			// Start process
			bs.show();
		}

		/**
		 * Helper code used to get the value item in Picker that holds the same id of runtime value
		 */
		private PickerItem findPickerItemPosition(int[] currValues, List<PickerItem> pickerItems, int pickerIndex) {
			int where = 0;
			for (int counter = 0; counter < pickerItems.size(); counter++) if (pickerItems.get(counter).getId() == currValues[pickerIndex]) where = counter;
			return pickerItems.get(where);
		}
	}

	public static abstract class PraysSwitchBottomSheet<T> implements ResultCallback<T> {

		// Views
		SwitcherX[] switchers = new SwitcherX[5];

		public PraysSwitchBottomSheet(Context context, @StringRes int title, @StringRes int subtitle, boolean[] currValues) {
			// Prepare runtime
			// Inflate content view
			View bsView = LayoutInflater.from(context).inflate(R.layout.bs_pray_switchers, null, false);
			// Create BottomSheet Dialog
			BottomSheetDialog bs = new BottomSheetDialog(context);
			bs.setContentView(bsView);
			bs.setDismissWithAnimation(true);
			// Setup inner views
			MaterialTextView titleTV = bsView.findViewById(R.id.tv_title);
			MaterialTextView subTitleTV = bsView.findViewById(R.id.tv_subtitle);
			switchers[0] = bsView.findViewById(R.id.switcherx_fajr);
			switchers[1] = bsView.findViewById(R.id.switcherx_dhuhr);
			switchers[2] = bsView.findViewById(R.id.switcherx_asr);
			switchers[3] = bsView.findViewById(R.id.switcherx_maghrib);
			switchers[4] = bsView.findViewById(R.id.switcherx_isha);
			MaterialButton saveButton = bsView.findViewById(R.id.btn_save);
			// Bind data to views
			titleTV.setText(title);
			subTitleTV.setText(subtitle);
			for (int counter = 0; counter < currValues.length; counter++) switchers[counter].setChecked(currValues[counter], true);
			// Listeners
			for (int i = 0; i < switchers.length; i++) {
				final int finalI = i;
				this.switchers[i].setOnCheckedChangeListener(isChecked -> {
					currValues[finalI] = isChecked;
					return null;
				});
			}
			saveButton.setOnTouchListener(new EnhancedScaleTouchListener() {
				@Override
				public void onClick(View v, float x, float y) {
					// Send results
					Boolean[] result = new Boolean[5];
					for (int i = 0; i < result.length; i++) result[i] = currValues[i];
					onResult((T) result);
					// Dismiss BottomSheet
					bs.cancel();
				}
			});
			// Start process
			bs.show();
		}

	}

}

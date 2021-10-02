/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.fragments.wizard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.android.material.transition.MaterialSharedAxis;
import com.irozon.alertview.AlertActionStyle;
import com.irozon.alertview.AlertStyle;
import com.irozon.alertview.AlertView;
import com.irozon.alertview.objects.AlertAction;
import com.irozon.sneaker.Sneaker;
import com.typ.muslim.Extras;
import com.typ.muslim.Keys;
import com.typ.muslim.R;
import com.typ.muslim.activities.MainActivity;
import com.typ.muslim.interfaces.ResultCallback;
import com.typ.muslim.libs.EnhancedScaleTouchListener;
import com.typ.muslim.managers.AMSettings;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.managers.PrayerManager;
import com.typ.muslim.models.Location;
import com.typ.muslim.models.PrayTimes;
import com.typ.muslim.profile.ProfileManager;
import com.typ.muslim.profile.models.Profile;

import java.util.Locale;

import kotlin.random.Random;

public class PreviewPrayTimeFragment extends Fragment {

	// Static
	private static final String TAG = "PreviewPrayTimeFragment";
	// Runtime
	private Location currentLocation;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Setup Enter/Exit Transitions
		setEnterTransition(new MaterialSharedAxis(MaterialSharedAxis.X, true));
		setExitTransition(new MaterialSharedAxis(MaterialSharedAxis.X, true));
		// Setup PrayTime instance with pre-set location and config
		if (getArguments() != null) currentLocation = (Location) getArguments().getSerializable(Extras.EXTRA_LOCATION);
		if (currentLocation == null) currentLocation = AMSettings.getCurrentLocation(getContext());
		else AManager.log(TAG, "onCreate: currentLocation is null");
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return LayoutInflater.from(requireContext()).inflate(R.layout.fragment_wizard_preview_prayers, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View fragmentView, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(fragmentView, savedInstanceState);
		// Calculate PrayTimes
		PrayTimes prayTimes = PrayerManager.getTodayPrays(getContext());
		// Setup Views
		MaterialTextView tvCity = fragmentView.findViewById(R.id.tv_city_name);
		MaterialTextView tvCountry = fragmentView.findViewById(R.id.tv_country_name);
		MaterialTextView tvConfig = fragmentView.findViewById(R.id.tv_config);
		MaterialTextView tvFajr = fragmentView.findViewById(R.id.tv_fajr_time);
		MaterialTextView tvSunrise = fragmentView.findViewById(R.id.tv_sunrise_time);
		MaterialTextView tvDhuhr = fragmentView.findViewById(R.id.tv_dhuhr_time);
		MaterialTextView tvAsr = fragmentView.findViewById(R.id.tv_asr_time);
		MaterialTextView tvMaghrib = fragmentView.findViewById(R.id.tv_maghrib_time);
		MaterialTextView tvIsha = fragmentView.findViewById(R.id.tv_isha_time);
		MaterialButton btnShowOptions = fragmentView.findViewById(R.id.btn_more_options);
		// Bind Views with data
		tvCity.setText(currentLocation.getCityName());
		tvCountry.setText(currentLocation.getCountryName());
		tvConfig.setText(String.format(Locale.getDefault(), "%s [%s - %s - %s]", getString(R.string.config), currentLocation.getConfig().getCalculationMethod().name(),
				currentLocation.getConfig().getAsrMethod().name(), currentLocation.getConfig().getHigherLatitude().name()));
		tvFajr.setText(prayTimes.getFajr().getFormattedTime(getContext()));
		tvSunrise.setText(prayTimes.getSunrise().getFormattedTime(getContext()));
		tvDhuhr.setText(prayTimes.getDhuhr().getFormattedTime(getContext()));
		tvAsr.setText(prayTimes.getAsr().getFormattedTime(getContext()));
		tvMaghrib.setText(prayTimes.getMaghrib().getFormattedTime(getContext()));
		tvIsha.setText(prayTimes.getIsha().getFormattedTime(getContext()));
		// Click Listener
		btnShowOptions.setOnTouchListener(new EnhancedScaleTouchListener(100, 0.95f, 1f) {
			@Override
			public void onClick(View v, float x, float y) {
				AlertView optionsAlert = new AlertView(getString(R.string.more_options), getString(R.string.all_is_set_you_are_free_to_choose), AlertStyle.BOTTOM_SHEET);
				optionsAlert.addAction(new AlertAction(getString(R.string.go_to_dashboard), AlertActionStyle.DEFAULT, alertAction -> {
					// Save Current Location
					AMSettings.saveLocation(getContext(), currentLocation);
					AMSettings.save(getContext(), Keys.IS_FIRST_RUN, false);
					ProfileManager.get(getContext()).resultsOn(new ResultCallback<Profile>() {
						@Override
						public void onResult(Profile createdProfile) {
							Toast.makeText(getContext(), "Welcome! " + createdProfile.getName(), Toast.LENGTH_SHORT).show();
							startActivity(new Intent(requireContext(), MainActivity.class));
							requireActivity().finish();
						}

						@Override
						public void onFailed() {
							Sneaker.with(PreviewPrayTimeFragment.this)
							       .setTitle("Wizard")
							       .setMessage("Can't create your profile.\nCheck your input and try again")
							       .sneakError();
						}
					}).createProfile(Profile.create(Random.Default.nextInt(1, 1000000),
							true,
							"Ahmed Sleem",
							""));
				}));
				optionsAlert.addAction(new AlertAction(getString(R.string.edit_quick_settings), AlertActionStyle.POSITIVE, alertAction -> {
					// Save Current Location
					AMSettings.saveLocation(getContext(), currentLocation);
					Navigation.findNavController(fragmentView).navigate(R.id.action_previewPrayTimeFragment_to_quickSettingsFragment);
				}));
				optionsAlert.addAction(new AlertAction(getString(R.string.go_back_to_config), AlertActionStyle.NEGATIVE, alertAction -> {
					Navigation.findNavController(fragmentView).navigateUp();
				}));
				optionsAlert.show((AppCompatActivity) requireActivity());
			}
		});
	}
}

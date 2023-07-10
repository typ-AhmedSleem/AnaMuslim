/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.setup.wizard;

import android.annotation.SuppressLint;
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

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.transition.MaterialSharedAxis;
import com.irozon.alertview.AlertActionStyle;
import com.irozon.alertview.AlertStyle;
import com.irozon.alertview.AlertView;
import com.irozon.alertview.objects.AlertAction;
import com.irozon.sneaker.Sneaker;
import com.typ.muslim.R;
import com.typ.muslim.app.Keys;
import com.typ.muslim.features.prays.PrayTimeCore;
import com.typ.muslim.interfaces.ResultCallback;
import com.typ.muslim.managers.AMSettings;
import com.typ.muslim.models.Location;
import com.typ.muslim.features.prays.models.PrayTimes;
import com.typ.muslim.profile.ProfileManager;
import com.typ.muslim.profile.models.Profile;
import com.typ.muslim.ui.prays.views.VerticalPrayView;
import com.typ.muslim.ui.home.MainActivity;
import com.typ.muslim.ui.setup.QuickSettingsActivity;

import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class PreviewPrayTimeFragment extends Fragment {

	// Static
	private static final String TAG = "PreviewPrayTimeFragment";

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Setup Enter/Exit Transitions
		setEnterTransition(new MaterialSharedAxis(MaterialSharedAxis.X, true));
		setExitTransition(new MaterialSharedAxis(MaterialSharedAxis.X, true));
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return LayoutInflater.from(requireContext()).inflate(R.layout.fragment_wizard_preview_prayers, container, false);
	}

	@Override
	@SuppressLint("ClickableViewAccessibility")
	public void onViewCreated(@NonNull View fragView, @Nullable Bundle savedInstanceState) {
		// Get current location
		final Location currLocation = AMSettings.getCurrentLocation(requireContext());
		// Calculate PrayTimes
		final PrayTimes prayTimes = PrayTimeCore.getNewInstance(requireContext(), currLocation).getPrayTimes(0);
		// Setup Views
		assert currLocation != null : "Current Location is null";
		final TextInputEditText tfLocation = (TextInputEditText) ((TextInputLayout) fragView.findViewById(R.id.til_preview_loc)).getEditText();
		Objects.requireNonNull(tfLocation).setText(String.format(Locale.getDefault(), "%s, %s", currLocation.getCityName(), currLocation.getCountryName()));

		assert currLocation.getConfig() != null : "Current Location is null";
		final TextInputEditText tfConfig = (TextInputEditText) ((TextInputLayout) fragView.findViewById(R.id.til_preview_config)).getEditText();
		Objects.requireNonNull(tfConfig).setText(currLocation.getConfig().toDisplayableString());

		final VerticalPrayView pvFajr = fragView.findViewById(R.id.vpv_fajr);
		pvFajr.setEnabled(false);
		pvFajr.setPray(prayTimes.getFajr());
		pvFajr.changeNotifMethodVisibility(false);

		final VerticalPrayView pvSunrise = fragView.findViewById(R.id.vpv_sunrise);
		pvSunrise.setEnabled(false);
		pvSunrise.setPray(prayTimes.getSunrise());
		pvSunrise.changeNotifMethodVisibility(false);

		final VerticalPrayView pvDhuhr = fragView.findViewById(R.id.vpv_dhuhr);
		pvDhuhr.setEnabled(false);
		pvDhuhr.setPray(prayTimes.getDhuhr());
		pvDhuhr.changeNotifMethodVisibility(false);

		final VerticalPrayView pvAsr = fragView.findViewById(R.id.vpv_asr);
		pvAsr.setEnabled(false);
		pvAsr.setPray(prayTimes.getAsr());
		pvAsr.changeNotifMethodVisibility(false);

		final VerticalPrayView pvMaghrib = fragView.findViewById(R.id.vpv_maghrib);
		pvMaghrib.setEnabled(false);
		pvMaghrib.setPray(prayTimes.getMaghrib());
		pvMaghrib.changeNotifMethodVisibility(false);

		final VerticalPrayView pvIsha = fragView.findViewById(R.id.vpv_isha);
		pvIsha.setEnabled(false);
		pvIsha.setPray(prayTimes.getIsha());
		pvIsha.changeNotifMethodVisibility(false);

		fragView.findViewById(R.id.btn_continue).setOnClickListener(v -> {
			final AlertView optionsAlert = new AlertView(getString(R.string.more_options), getString(R.string.all_is_set_you_are_free_to_choose), AlertStyle.BOTTOM_SHEET);
			optionsAlert.addAction(new AlertAction(getString(R.string.go_to_dashboard), AlertActionStyle.DEFAULT, alertAction -> {
				// Reset settings
				AMSettings.save(getContext(), Keys.IS_FIRST_RUN, false);
				// Create new profile
				ProfileManager.get(getContext()).resultsOn(new ResultCallback<Profile>() {
					@Override
					public void onResult(Profile createdProfile) {
						Toast.makeText(getContext(), "Welcome! " + createdProfile.getName(), Toast.LENGTH_SHORT).show();
						startActivity(new Intent(requireContext(), MainActivity.class));
						requireActivity().finish();
					}

					@Override
					public void onFailed() {
						Sneaker.with(PreviewPrayTimeFragment.this).setTitle("Wizard").setDuration(2000).setMessage("Can't create your profile.\nCheck your input and try again").sneakError();
					}
				}).createProfile(Profile.create(UUID.randomUUID().hashCode(), true, getString(R.string.ahmed_sleem), ""));
			}));
			optionsAlert.addAction(new AlertAction(getString(R.string.edit_quick_settings), AlertActionStyle.POSITIVE, alertAction -> {
				if (true) {
					Toast.makeText(requireContext(), "QuickSettings hasn't yet fully implemented.", Toast.LENGTH_SHORT).show();
					return;
				}
				startActivity(new Intent(requireContext(), QuickSettingsActivity.class));
				requireActivity().finish();
			}));
//			optionsAlert.addAction(new AlertAction(getString(R.string.go_back_to_config), AlertActionStyle.NEGATIVE, alertAction -> {
//				Navigation.findNavController(fragView).navigateUp();
//			}));
			optionsAlert.show((AppCompatActivity) requireActivity());
		});
	}
}

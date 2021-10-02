/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.fragments.wizard;

import static com.typ.muslim.core.praytime.enums.AsrMethod.SHAFII;
import static com.typ.muslim.core.praytime.enums.CalculationMethod.EGYPT;
import static com.typ.muslim.core.praytime.enums.HigherLatitudes.NONE;
import static com.typ.muslim.managers.ViewManager.ChoiceSelectorBottomSheet.Choice;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.transition.MaterialSharedAxis;
import com.irozon.alertview.AlertActionStyle;
import com.irozon.alertview.AlertStyle;
import com.irozon.alertview.AlertView;
import com.irozon.alertview.objects.AlertAction;
import com.typ.muslim.Extras;
import com.typ.muslim.R;
import com.typ.muslim.core.praytime.enums.AsrMethod;
import com.typ.muslim.core.praytime.enums.CalculationMethod;
import com.typ.muslim.core.praytime.enums.HigherLatitudes;
import com.typ.muslim.libs.EnhancedScaleTouchListener;
import com.typ.muslim.managers.AMSettings;
import com.typ.muslim.managers.ViewManager;
import com.typ.muslim.models.Location;
import com.typ.muslim.models.LocationConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectPrayConfigFragment extends Fragment {

	// Statics
	private static final String TAG = "SelectPrayConfigFragment";
	// Runtime
	private Location currLocation;
	private LocationConfig config;
	// Views
	private ImageButton ibtnHelpCalcMethod, ibtnHelpAsrMethod, ibtnHelpHighLat, ibtnBack, ibtnNext;
	private MaterialButton btnCalcMethod, btnAsrMethod, btnHighLat;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Setup Enter/Exit transitions
		setEnterTransition(new MaterialSharedAxis(MaterialSharedAxis.X, true));
		setExitTransition(new MaterialSharedAxis(MaterialSharedAxis.X, true));
		// Get config from args or from pref if any available
		if (getArguments() != null) currLocation = (Location) getArguments().getSerializable(Extras.EXTRA_LOCATION);
		if (currLocation == null) currLocation = AMSettings.getCurrentLocation(getContext());
		if (config == null) config = new LocationConfig(EGYPT, SHAFII, NONE); // null check is necessary to avoid runtime errors.
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return LayoutInflater.from(requireContext()).inflate(R.layout.fragment_wizard_pray_config, container, false);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public void onViewCreated(@NonNull View fragmentView, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(fragmentView, savedInstanceState);
		// Setup Views
		ibtnHelpCalcMethod = fragmentView.findViewById(R.id.ibtn_help_calc_method);
		ibtnHelpAsrMethod = fragmentView.findViewById(R.id.ibtn_help_asr_method);
		ibtnHelpHighLat = fragmentView.findViewById(R.id.ibtn_help_higher_lat);
		ibtnBack = fragmentView.findViewById(R.id.ibtn_go_back);
		ibtnNext = fragmentView.findViewById(R.id.ibtn_go_next);
		btnCalcMethod = fragmentView.findViewById(R.id.btn_calc_method);
		btnAsrMethod = fragmentView.findViewById(R.id.btn_asr_method);
		btnHighLat = fragmentView.findViewById(R.id.btn_higher_lat);
		// Listeners
		// TODO: 2/24/21 complete creating click listeners for ibtnHelpCalcMethod,ibtnHelpAsrJuristic,ibtnHelpHigherLat
		btnCalcMethod.setOnTouchListener(new EnhancedScaleTouchListener(100, 0.95f, 1f) {
			@Override
			public void onClick(View v, float x, float y) {
				new ViewManager.ChoiceSelectorBottomSheet(requireContext(), getString(R.string.calculation_method),
						getString(R.string.select_calc_method_your_loc_is_following),
						new Choice(String.valueOf(config.getCalculationMethod().ordinal()), null, null),
						getCalcMethodChoices(),
						choice -> {
							if (!(config.getCalculationMethod().ordinal() == Integer.parseInt(choice.getId()))) {
								// Calc method has changed
								SelectPrayConfigFragment.this.config.setCalculationMethod(CalculationMethod.values()[Integer.parseInt(choice.getId())]);
								btnCalcMethod.setText(choice.getDesc());
							}
						});
			}

			private List<Choice> getCalcMethodChoices() {
				String[] descs = requireContext().getResources().getStringArray(R.array.CalcMethodDescs);
				List<Choice> choices = new ArrayList<>();
				for (int i = 0; i < CalculationMethod.values().length; i++)
					choices.add(new Choice(String.valueOf(CalculationMethod.values()[i].ordinal()), CalculationMethod.values()[i].name(), descs[i]));
				return choices;
			}
		});
		btnAsrMethod.setOnTouchListener(new EnhancedScaleTouchListener(100, 0.95f, 1f) {
			@Override
			public void onClick(View v, float x, float y) {
				new ViewManager.ChoiceSelectorBottomSheet(requireContext(), getString(R.string.asr_juristic),
						getString(R.string.select_asr_method_your_loc_is_following),
						new Choice(String.valueOf(config.getAsrMethod().ordinal()), null, null), Arrays.asList(
						new Choice(String.valueOf(SHAFII.ordinal()), getString(R.string.shafii), getString(R.string.shafii_malikki_hanballi)),
						new Choice(String.valueOf(AsrMethod.HANAFI.ordinal()), getString(R.string.hanafi), getString(R.string.hanafi))),
						choice -> {
							if (!(config.getAsrMethod().ordinal() == Integer.parseInt(choice.getId()))) {
								// Asr method has changed
								SelectPrayConfigFragment.this.config.setAsrMethod(AsrMethod.values()[Integer.parseInt(choice.getId())]);
								btnAsrMethod.setText(choice.getTitle());
							}
						});
			}
		});
		btnHighLat.setOnTouchListener(new EnhancedScaleTouchListener(100, 0.95f, 1f) {
			@Override
			public void onClick(View v, float x, float y) {
				new ViewManager.ChoiceSelectorBottomSheet(requireContext(), getString(R.string.higher_latitudes),
						getString(R.string.select_high_lat_method_your_loc_is_following),
						new Choice(String.valueOf(config.getHigherLatitude().ordinal()), null, null), getHighLatMethodChoices(),
						choice -> {
							if (!(config.getHigherLatitude().ordinal() == Integer.parseInt(choice.getId()))) {
								// Calc method has changed
								SelectPrayConfigFragment.this.config.setHigherLatitude(HigherLatitudes.values()[Integer.parseInt(choice.getId())]);
								btnHighLat.setText(choice.getDesc());
							}
						});
			}

			private List<Choice> getHighLatMethodChoices() {
				String[] descs = requireContext().getResources().getStringArray(R.array.HigherLatMethodDescs);
				List<Choice> choices = new ArrayList<>();
				for (int i = 0; i < HigherLatitudes.values().length; i++)
					choices.add(new Choice(String.valueOf(HigherLatitudes.values()[i].ordinal()), HigherLatitudes.values()[i].name(), descs[i]));
				return choices;
			}
		});
		ibtnBack.setOnTouchListener(new EnhancedScaleTouchListener(100, 0.95f, 1f) {
			@Override
			public void onClick(View v, float x, float y) {
				Navigation.findNavController(fragmentView).navigateUp();
			}
		});
		ibtnNext.setOnTouchListener(new EnhancedScaleTouchListener(100, 0.95f, 1f) {
			@Override
			public void onClick(View v, float x, float y) {
				AlertView confirmationAlert = new AlertView(getString(R.string.confirm_configuration), getString(R.string.msg_confirm_config), AlertStyle.BOTTOM_SHEET);
				confirmationAlert.addAction(new AlertAction(getString(R.string.wait_to_take_a_look_on_it), AlertActionStyle.DEFAULT, alertAction -> {
				}));
				confirmationAlert.addAction(new AlertAction(getString(R.string.confirm_and_continue), AlertActionStyle.POSITIVE, alertAction -> {
					// Save current config
					AMSettings.saveConfiguration(requireContext(), config);
					// Navigate to next fragment
					Navigation.findNavController(fragmentView).navigate(R.id.action_selectPrayConfigFragment_to_previewPrayTimeFragment, currLocation != null ? currLocation.setConfig(config).toBundle() : null);
				}));
				confirmationAlert.show((AppCompatActivity) requireActivity());
			}
		});
	}
}

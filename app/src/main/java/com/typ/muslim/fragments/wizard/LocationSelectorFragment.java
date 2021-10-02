/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.fragments.wizard;

import static com.typ.muslim.core.praytime.enums.AsrMethod.SHAFII;
import static com.typ.muslim.core.praytime.enums.CalculationMethod.EGYPT;
import static com.typ.muslim.core.praytime.enums.HigherLatitudes.NONE;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.typ.muslim.Extras;
import com.typ.muslim.R;
import com.typ.muslim.core.praytime.enums.AsrMethod;
import com.typ.muslim.core.praytime.enums.CalculationMethod;
import com.typ.muslim.interfaces.IGetLocationListener;
import com.typ.muslim.libs.EnhancedScaleTouchListener;
import com.typ.muslim.managers.AMSettings;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.managers.LocationManager;
import com.typ.muslim.managers.ViewManager;
import com.typ.muslim.models.Location;
import com.typ.muslim.models.LocationConfig;

public class LocationSelectorFragment extends Fragment {

	// Statics
	private static final String TAG = "LocationSelectorFragment";

	// Runtime
	private Location selectedLocation;
	private LocationManager locationManager;
	// Views
	private ImageButton ibtnNext,
			ibtnBack;
	private MaterialButton
			btnGPS,
			btnOnlineLocate,
			btnOfflineLocate;
	private EditText inputSearchCity;
	private MaterialTextView tvNoIntent;
	// BottomSheets
	private EasyRecyclerView rvCities;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set Enter and Exit Transitions
		setEnterTransition(new MaterialSharedAxis(MaterialSharedAxis.X, true));
		setExitTransition(new MaterialSharedAxis(MaterialSharedAxis.X, true));
		// Setup LocationManager
		if (locationManager == null) locationManager = new LocationManager(requireActivity());
		// Get selected location from args if found
		if (getArguments() != null) this.selectedLocation = (Location) getArguments().getSerializable(Extras.EXTRA_LOCATION);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return LayoutInflater.from(requireContext()).inflate(R.layout.fragment_wizard_loc_selector, container, false);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public void onViewCreated(@NonNull View fragmentView, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(fragmentView, savedInstanceState);
		// Setup Views
		ibtnBack = fragmentView.findViewById(R.id.ibtn_go_back);
		ibtnNext = fragmentView.findViewById(R.id.ibtn_go_next);
		btnGPS = fragmentView.findViewById(R.id.btn_locate_gps);
		btnOnlineLocate = fragmentView.findViewById(R.id.btn_locate_online);
		btnOfflineLocate = fragmentView.findViewById(R.id.btn_locate_offline);
		inputSearchCity = fragmentView.findViewById(R.id.input_search_city);
		tvNoIntent = fragmentView.findViewById(R.id.tv_no_internet);
		// Init Views
		if (selectedLocation == null) animateHide(ibtnNext);
		if (!AManager.isNetworkEnabled(requireContext())) tvNoIntent.setVisibility(View.VISIBLE);
		// Bind views if previous location was selected
		if (selectedLocation != null) {
			ibtnNext.setVisibility(View.VISIBLE);
			tvNoIntent.setText(String.format("%s,%s", selectedLocation.getCityName(), selectedLocation.getCountryName()));
			inputSearchCity.setText(selectedLocation.getCityName());
		}
		// Listeners
		ibtnBack.setOnTouchListener(new EnhancedScaleTouchListener(100, 0.95f, 1f) {
			@Override
			public void onClick(View v, float x, float y) {
				Navigation.findNavController(fragmentView).navigateUp();
			}
		});
		ibtnNext.setOnTouchListener(new EnhancedScaleTouchListener(100, 0.95f, 1f) {
			@Override
			public void onClick(View v, float x, float y) {
				// Check if location was set
				if (selectedLocation == null) {
					Sneaker.with(requireActivity()).setTitle(getString(R.string.select_location)).setMessage(getString(R.string.location_not_selected_error_msg)).setDuration(2500).sneakError();
					return;
				}
				// Navigate to next destination
				Navigation.findNavController(fragmentView).navigate(R.id.action_locationSelectorFragment_to_selectPrayConfigFragment, selectedLocation.toBundle());
			}
		});
		btnGPS.setOnTouchListener(new EnhancedScaleTouchListener(100, 0.95f, 1f) {
			@Override
			public void onClick(View v, float x, float y) {
				// Show Loading
				// Get location from GPS
				locationManager.getGPSLocation(new IGetLocationListener() {
					@Override
					public void onGetLocationSucceed(Location location) {
						// Update selected location at runtime
						selectedLocation = location;
						AManager.log(TAG, selectedLocation);
						// Show LocationPreviewBottomSheet
						new ViewManager.PreviewLocationBottomSheet(requireContext(), selectedLocation, v -> {
							if (v.getId() == R.id.btn_continue) Toast.makeText(requireContext(), "Continue", Toast.LENGTH_SHORT).show();
							else if (v.getId() == R.id.btn_cancel) Toast.makeText(requireContext(), "Cancel", Toast.LENGTH_SHORT).show();
						});
						// Show Next Button
						if (selectedLocation != null) animateShow(ibtnNext);
					}

					@Override
					public void onPermissionDenied() {
						Toast.makeText(requireContext(), "Location Permissions Denied", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onGetLocationFailed() {
						Toast.makeText(requireContext(), "Failed to detect location", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
		btnOnlineLocate.setOnTouchListener(new EnhancedScaleTouchListener(100, 0.95f, 1f) {
			@Override
			public void onClick(View v, float x, float y) {
				// Check Internet Connection
				if (!AManager.isNetworkEnabled(requireContext())) {
					AlertView noInternetAlert = new AlertView(getString(R.string.no_internet), getString(R.string.internet_is_req_to_search_cities_online), AlertStyle.IOS);
					noInternetAlert.addAction(new AlertAction(getString(R.string.retry), AlertActionStyle.DEFAULT, alertAction -> {
						btnOnlineLocate.performClick(); /*Retry connecting*/
					}));
					noInternetAlert.addAction(new AlertAction(getString(R.string.open_wifi_settings), AlertActionStyle.DEFAULT, alertAction -> {
						startActivityForResult(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK), 0); /* Open WiFi setting*/
					}));
					noInternetAlert.addAction(new AlertAction(getString(R.string.search_offline), AlertActionStyle.POSITIVE, alertAction -> {
						new ViewManager.SearchCityBottomSheet(requireActivity(), locationManager) {
							@Override
							public void onItemClick(Location location) {
								// Update runtime
								if (location == null) return;
								LocationSelectorFragment.this.selectedLocation = location;
								// Show Location Preview
								new ViewManager.PreviewLocationBottomSheet(requireContext(), location, v -> {
									if (v.getId() == R.id.btn_continue) {
										// Show Next Button
										animateShow(ibtnNext);
										// Change bottom text
										tvNoIntent.setText(String.format("%s,%s", location.getCityName(), location.getCountryName()));
										// Save current location
										AMSettings.saveLocation(requireContext(), location.setConfig(new LocationConfig(EGYPT, SHAFII, NONE)));
										// Navigate to next fragment
										Navigation.findNavController(fragmentView).navigate(R.id.action_locationSelectorFragment_to_selectPrayConfigFragment, location.toBundle());
									}
								});
							}
						}.refresh(locationManager.searchForCities(inputSearchCity.getText().toString()));
					}));
					noInternetAlert.show((AppCompatActivity) requireActivity());
				} else {
					Toast.makeText(requireContext(), "Online Search", Toast.LENGTH_SHORT).show();
					return;
				}
				// Show Alert and exit method invocation if no internet was found
				// Start SearchCityBottomSheet
				// Show Next Button if location was selected
				if (selectedLocation != null) animateShow(ibtnNext);
			}
		});
	}

	/**
	 * Private method that animates alpha property given view to be hidden at the end
	 */
	private void animateHide(View target) {
		ObjectAnimator hideAnim = ObjectAnimator.ofFloat(target, "alpha", 1f, 0f).setDuration(300);
		hideAnim.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				target.setVisibility(View.GONE);
			}
		});
		hideAnim.start();
	}

	/**
	 * Private method that animates alpha property given view to be hidden at the end
	 */
	private void animateShow(View target) {
		ObjectAnimator showAnim = ObjectAnimator.ofFloat(target, "alpha", 0f, 1f).setDuration(300);
		showAnim.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationStart(Animator animation) {
				super.onAnimationStart(animation);
				target.setVisibility(View.VISIBLE);
			}
		});
		showAnim.start();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0) {
			if (AManager.isNetworkEnabled(requireContext())) tvNoIntent.setVisibility(View.GONE);
			else tvNoIntent.setVisibility(View.VISIBLE);
		}
	}
}

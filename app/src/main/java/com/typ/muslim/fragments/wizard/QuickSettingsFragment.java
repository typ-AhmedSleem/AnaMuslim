/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.fragments.wizard;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_NOTIFICATION_POLICY;
import static android.Manifest.permission.BIND_ACCESSIBILITY_SERVICE;
import static android.Manifest.permission.BIND_APPWIDGET;
import static android.Manifest.permission.BIND_DEVICE_ADMIN;
import static android.Manifest.permission.BIND_NFC_SERVICE;
import static android.Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE;
import static android.Manifest.permission.BIND_REMOTEVIEWS;
import static android.Manifest.permission.BODY_SENSORS;
import static android.Manifest.permission.CAPTURE_AUDIO_OUTPUT;
import static android.Manifest.permission.CHANGE_WIFI_STATE;
import static android.Manifest.permission.MODIFY_AUDIO_SETTINGS;
import static android.Manifest.permission.NFC;
import static android.Manifest.permission.READ_CALENDAR;
import static android.Manifest.permission.READ_CALL_LOG;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECEIVE_BOOT_COMPLETED;
import static android.Manifest.permission.SET_ALARM;
import static android.Manifest.permission.SET_WALLPAPER;
import static android.Manifest.permission.VIBRATE;
import static android.Manifest.permission.WAKE_LOCK;
import static android.Manifest.permission.WRITE_CALENDAR;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.typ.muslim.enums.PrayNotifyMethod.AZAN;
import static com.typ.muslim.enums.PrayNotifyMethod.NOTIFICATION_ONLY;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.textview.MaterialTextView;
import com.google.android.material.transition.MaterialSharedAxis;
import com.irozon.alertview.AlertActionStyle;
import com.irozon.alertview.AlertStyle;
import com.irozon.alertview.AlertView;
import com.irozon.alertview.objects.AlertAction;
import com.irozon.sneaker.Sneaker;
import com.typ.muslim.Keys;
import com.typ.muslim.R;
import com.typ.muslim.activities.MainActivity;
import com.typ.muslim.core.praytime.enums.Prays;
import com.typ.muslim.enums.OngoingNotificationStyle;
import com.typ.muslim.enums.PrayNotifyMethod;
import com.typ.muslim.interfaces.ResultCallback;
import com.typ.muslim.libs.EnhancedScaleTouchListener;
import com.typ.muslim.managers.AMRes;
import com.typ.muslim.managers.AMSettings;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.managers.ViewManager;
import com.typ.muslim.profile.ProfileManager;
import com.typ.muslim.profile.models.Profile;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import fr.arnaudguyon.perm.Perm;
import fr.arnaudguyon.perm.PermResult;
import kotlin.random.Random;

public class QuickSettingsFragment extends Fragment {

	// Statics
	private static final int RC_REQUEST_PERMS = 23;
	// Current settings
	private PrayNotifyMethod[] praysNotifMethods;
	private boolean isOngoingNotifEnabled = true;
	private OngoingNotificationStyle ongoingNotifStyle = OngoingNotificationStyle.STYLE1;
	private int[] notifyBeforePray = new int[6];
	private boolean[] iqamaAfterPrays = new boolean[6];
	private int[] muteDuringPrays = new int[6];
	private boolean isFlipToStopAzanEnabled = false;
	private boolean isVolumeToStopAzanEnabled = false;

	// Views
	private MaterialTextView tvPrayersNotifs,
			tvOngoingNotif,
			tvNotifyBeforePray,
			tvIqama,
			tvMuteDuringPray,
			tvFlipToStop,
			tvVolumeToStop,
			tvReqPerms;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Setup Enter/Exit Transition
		setEnterTransition(new MaterialSharedAxis(MaterialSharedAxis.X, true));
		setExitTransition(new MaterialSharedAxis(MaterialSharedAxis.X, true));
		// Prepare changed settings if any changes happened or keep default settings
		this.prepareSettings();
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return LayoutInflater.from(requireContext()).inflate(R.layout.fragment_wizard_quick_settings, container, false);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public void onViewCreated(@NonNull View fragmentView, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(fragmentView, savedInstanceState);
		// Setup Views
		tvPrayersNotifs = fragmentView.findViewById(R.id.tv_prayers_notif_config);
		tvOngoingNotif = fragmentView.findViewById(R.id.tv_ongoing_notif_config);
		tvNotifyBeforePray = fragmentView.findViewById(R.id.tv_min_before_azan);
		tvIqama = fragmentView.findViewById(R.id.tv_iqama_after_azan);
		tvMuteDuringPray = fragmentView.findViewById(R.id.tv_mute_during_pray);
		tvFlipToStop = fragmentView.findViewById(R.id.tv_flip_to_stop_azan);
		tvVolumeToStop = fragmentView.findViewById(R.id.tv_volume_to_stop_azan);
		tvReqPerms = fragmentView.findViewById(R.id.tv_perms_grant_status);
		// Refresh views content and style them according current settings
		this.refreshViews();
		// Listeners
		requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
			@Override
			public void handleOnBackPressed() {
				Toast.makeText(requireContext(), "Back is pressed", Toast.LENGTH_SHORT).show();
			}
		});
		fragmentView.findViewById(R.id.card_qs_configure_prays_notif).setOnTouchListener(new EnhancedScaleTouchListener(100, 0.97f, 0.9f) {
			@Override
			public void onClick(View v, float x, float y) {
				// TODO: 2/25/21 Show ChangePraysNotif (BottomSheet or Activity for result)
			}
		});
		fragmentView.findViewById(R.id.card_qs_ongoing_notif).setOnTouchListener(new EnhancedScaleTouchListener(100, 0.97f, 0.9f) {
			@Override
			public void onClick(View v, float x, float y) {

			}
		});
		fragmentView.findViewById(R.id.card_qs_notif_before_pray).setOnTouchListener(new EnhancedScaleTouchListener(100, 0.97f, 0.9f) {
			@Override
			public void onClick(View v, float x, float y) {
				new ViewManager.PrayMinutesPickerBottomSheet<Integer[]>(requireContext(), R.string.notify_before_pray, R.string.notify_before_pray, AMSettings.getNotifyTimeBeforePrays(requireContext()), new int[]{-60, 60}) {
					@Override
					public void onResult(Integer[] results) {
						for (Prays whatPray : Prays.values()) {
							if (whatPray == Prays.SUNRISE) continue;
							notifyBeforePray[whatPray.ordinal()] = results[whatPray.ordinal()];
						}
						// Refresh Views
						refreshViews();
					}
				};
			}
		});
		fragmentView.findViewById(R.id.card_qs_iqama_after_azan).setOnTouchListener(new EnhancedScaleTouchListener(100, 0.97f, 0.9f) {
			@Override
			public void onClick(View v, float x, float y) {
				new ViewManager.PraysSwitchBottomSheet<Boolean[]>(requireContext(), R.string.iqama_after_azan, R.string.subtitle_iqama_after_azan, AMSettings.isIqamaEnabledForPrays(requireContext())) {
					@Override
					public void onResult(Boolean[] results) {
						if (results != null) {
							for (Prays whatPray : Prays.values()) {
								if (whatPray == Prays.SUNRISE) continue;
								iqamaAfterPrays[whatPray.ordinal()] = results[whatPray.ordinal()];
							}
						}
					}
				};
			}
		});
		fragmentView.findViewById(R.id.card_qs_mute_in_pray).setOnTouchListener(new EnhancedScaleTouchListener(100, 0.97f, 0.9f) {
			@Override
			public void onClick(View v, float x, float y) {
				synchronized (this) {
					new ViewManager.PrayMinutesPickerBottomSheet<Integer[]>(requireContext(), R.string.mute_during_pray, R.string.mute_during_pray, AMSettings.getMuteTimeDuringPrays(requireContext()), new int[]{-60, 60}) {
						@Override
						public void onResult(Integer[] results) {
							for (Prays whatPray : Prays.values()) {
								if (whatPray == Prays.SUNRISE) continue;
								muteDuringPrays[whatPray.ordinal()] = results[whatPray.ordinal()];
							}
						}
					};
					// Refresh views
					AManager.log("mute", AMSettings.getMuteTimeDuringPrays(requireContext()));
					refreshViews();
				}
			}
		});
		fragmentView.findViewById(R.id.card_qs_flip_to_mute).setOnTouchListener(new EnhancedScaleTouchListener(100, 0.97f, 0.9f) {
			@Override
			public void onClick(View v, float x, float y) {
				// Change settings value at runtime
				isFlipToStopAzanEnabled = !isFlipToStopAzanEnabled;
				// Refresh layout
				if (isFlipToStopAzanEnabled) changeTextViewStatus(tvFlipToStop, R.string.enabled, 1);
				else changeTextViewStatus(tvFlipToStop, R.string.disabled, -1);
			}
		});
		fragmentView.findViewById(R.id.card_qs_volume_to_mute).setOnTouchListener(new EnhancedScaleTouchListener(100, 0.97f, 0.9f) {
			@Override
			public void onClick(View v, float x, float y) {
				// Change settings value at runtime
				isVolumeToStopAzanEnabled = !isVolumeToStopAzanEnabled;
				// Refresh layout
				if (isVolumeToStopAzanEnabled) changeTextViewStatus(tvVolumeToStop, R.string.enabled, 1);
				else changeTextViewStatus(tvVolumeToStop, R.string.disabled, -1);
			}
		});
		fragmentView.findViewById(R.id.card_qs_grant_perms).setOnTouchListener(new EnhancedScaleTouchListener(100, 0.97f, 0.9f) {
			@Override
			public void onClick(View v, float x, float y) {
				new Perm(QuickSettingsFragment.this, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, READ_CALENDAR,
						WRITE_CALENDAR, ACCESS_COARSE_LOCATION, VIBRATE, BIND_DEVICE_ADMIN, ACCESS_FINE_LOCATION, BODY_SENSORS,
						WAKE_LOCK, SET_WALLPAPER, RECEIVE_BOOT_COMPLETED, READ_CALL_LOG, READ_CONTACTS, NFC, MODIFY_AUDIO_SETTINGS,
						CHANGE_WIFI_STATE, CAPTURE_AUDIO_OUTPUT, BIND_REMOTEVIEWS, BIND_APPWIDGET, BIND_ACCESSIBILITY_SERVICE,
						BIND_NFC_SERVICE, ACCESS_NOTIFICATION_POLICY, BIND_NOTIFICATION_LISTENER_SERVICE).askPermissions(RC_REQUEST_PERMS); // Request permissions.
			}
		});
		fragmentView.findViewById(R.id.btn_finish).setOnTouchListener(new EnhancedScaleTouchListener(100, 0.97f, 0.9f) {
			@Override
			public void onClick(View v, float x, float y) {
				AlertView alertFinish = new AlertView(getString(R.string.finish), getString(R.string.msg_quick_settings_alert), AlertStyle.BOTTOM_SHEET);
				alertFinish.addAction(new AlertAction(getString(R.string.restore_defaults), AlertActionStyle.DEFAULT, alertAction -> {
					// Restore default settings then refresh runtime
					AMSettings.resetSettings(getContext());
					prepareSettings();
					// Refresh Views
					refreshViews();
				}));
				alertFinish.addAction(new AlertAction(getString(R.string.wait_to_take_a_look_on_it), AlertActionStyle.DEFAULT, alertAction -> {
				}));
				alertFinish.addAction(new AlertAction(getString(R.string.save_and_go), AlertActionStyle.POSITIVE, alertAction -> {
					// Save settings
					saveSettings();
					// Create Profile
					ProfileManager.get(getContext()).resultsOn(new ResultCallback<Profile>() {
						@Override
						public void onResult(Profile createdProfile) {
							Toast.makeText(getContext(), "Welcome! " + createdProfile.getName(), Toast.LENGTH_SHORT).show();
							startActivity(new Intent(requireContext(), MainActivity.class));
							requireActivity().finish();
						}

						@Override
						public void onFailed() {
							Sneaker.with(QuickSettingsFragment.this)
							       .setTitle("Wizard")
							       .setMessage("Can't create your profile.\nCheck your input and try again")
							       .sneakError();
						}
					}).createProfile(Profile.create(Random.Default.nextInt(1, 1000000),
							true,
							"Ahmed Sleem",
							""));
					// Finish Wizard and start AnaMuslim Dashboard
					startActivity(new Intent(requireActivity(), MainActivity.class));
					requireActivity().finish();
				}));
				alertFinish.show((AppCompatActivity) requireActivity());
			}

			private void saveSettings() {
				// Save app settings
				for (Prays pray : Prays.values()) {
					AMSettings.save(requireContext(), Keys.PRAY_NOTIFY_METHOD(pray), praysNotifMethods[pray.ordinal()]);
					if (pray == Prays.SUNRISE) continue;
					AMSettings.save(requireContext(), Keys.NOTIFY_BEFORE_PRAY(pray), notifyBeforePray[pray.ordinal()]);
					AMSettings.save(requireContext(), Keys.IQAMA_ENABLED_FOR(pray), iqamaAfterPrays[pray.ordinal()]);
					AMSettings.save(requireContext(), Keys.MUTE_DURING_PRAY(pray), muteDuringPrays[pray.ordinal()]);
				}
				AMSettings.save(requireContext(), Keys.ONGOING_NOTIFICATION_ENABLED, isOngoingNotifEnabled);
				AMSettings.save(requireContext(), Keys.ONGOING_NOTIFICATION_STYLE, ongoingNotifStyle);
				AMSettings.save(requireContext(), Keys.FLIP_TO_STOP_AZAN, isFlipToStopAzanEnabled);
				AMSettings.save(requireContext(), Keys.PRESS_VOLUME_TO_STOP_AZAN, isVolumeToStopAzanEnabled);
				// Save first run state
				AMSettings.save(getContext(), Keys.IS_FIRST_RUN, false);
			}
		});
	}

	private void prepareSettings() {
		this.praysNotifMethods = AMSettings.getPraysNotifyMethod(requireContext());
		this.isOngoingNotifEnabled = AMSettings.isOngoingNotificationEnabled(requireContext());
		this.ongoingNotifStyle = AMSettings.getOngoingNotificationStyle(requireContext());
		this.notifyBeforePray = AMSettings.getNotifyTimeBeforePrays(requireContext());
		this.iqamaAfterPrays = AMSettings.isIqamaEnabledForPrays(requireContext());
		this.muteDuringPrays = AMSettings.getMuteTimeDuringPrays(requireContext());
		this.isFlipToStopAzanEnabled = AMSettings.isFlipToStopAzanEnabled(requireContext());
		this.isVolumeToStopAzanEnabled = AMSettings.isPressVolumeToStopAzanEnabled(requireContext());
	}

	private void refreshViews() {
		if (isOngoingNotifEnabled) changeTextViewStatus(tvOngoingNotif, R.string.enabled, 1);
		else changeTextViewStatus(tvOngoingNotif, R.string.disabled, -1);
		if (isFlipToStopAzanEnabled) changeTextViewStatus(tvFlipToStop, R.string.enabled, 1);
		else changeTextViewStatus(tvFlipToStop, R.string.disabled, -1);
		if (isVolumeToStopAzanEnabled) changeTextViewStatus(tvVolumeToStop, R.string.enabled, 1);
		else changeTextViewStatus(tvVolumeToStop, R.string.disabled, -1);
		switch (getIdenticalPraysNotifMethodCount()) {
			case 5:
				changeTextViewStatus(tvPrayersNotifs, R.string.enabled, 1); // All enabled.
				break;
			case 0:
				changeTextViewStatus(tvPrayersNotifs, R.string.disabled, -1); // All disabled.
				break;
			default:
				changeTextViewStatus(tvPrayersNotifs, R.string.mixed, 0); // Mixed with enables and disables.
				break;
		}
		switch (getEnabledNotifyBeforePraysCount()) {
			case 5:
				changeTextViewStatus(tvNotifyBeforePray, R.string.enabled, 1); // All enabled.
				break;
			case 0:
				changeTextViewStatus(tvNotifyBeforePray, R.string.disabled, -1); // All disabled.
				break;
			default:
				changeTextViewStatus(tvNotifyBeforePray, R.string.mixed, 0); // Mixed
				break;
		}
		switch (getEnabledIqamaPraysCount()) {
			case 5:
				changeTextViewStatus(tvIqama, R.string.enabled, 1); // All enabled.
				break;
			case 0:
				changeTextViewStatus(tvIqama, R.string.disabled, -1); // All disabled.
				break;
			default:
				changeTextViewStatus(tvIqama, R.string.mixed, 0); // Mixed
				break;
		}
		switch (getMutedDuringPraysCount()) {
			case 5:
				changeTextViewStatus(tvMuteDuringPray, R.string.enabled, 1); // All enabled.
				break;
			case 0:
				changeTextViewStatus(tvMuteDuringPray, R.string.disabled, -1); // All disabled.
				break;
			default:
				changeTextViewStatus(tvMuteDuringPray, R.string.mixed, 0); // Mixed.
				break;
		}
	}

	@Contract(pure = true)
	private int getIdenticalPraysNotifMethodCount() {
		int count = 0;
		for (PrayNotifyMethod method : praysNotifMethods) if (method == AZAN || method == NOTIFICATION_ONLY) count++;
		return count;
	}

	@Contract(pure = true)
	private int getEnabledNotifyBeforePraysCount() {
		int count = 0;
		for (int minutes : notifyBeforePray) if (minutes > 0) count++;
		return count;
	}

	@Contract(pure = true)
	private int getEnabledIqamaPraysCount() {
		int count = 0;
		for (boolean isIqamaEnabled : iqamaAfterPrays) if (isIqamaEnabled) count++;
		return count;
	}

	@Contract(pure = true)
	private int getMutedDuringPraysCount() {
		int count = 0;
		for (int minutes : muteDuringPrays) if (minutes > 0) count++;
		return count;
	}

	/**
	 * -1 -> NONE
	 * 0 -> SOME
	 * 1 -> ALL
	 */
	private void changeTextViewStatus(@NotNull MaterialTextView tv, @StringRes int text, int status) {
		tv.setTextColor(AMRes.getColor(requireContext(), status == -1 ? R.color.red : status == 0 ? R.color.ef_colorPrimaryDark : R.color.green));
		if (text != -1) tv.setText(text);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == RC_REQUEST_PERMS) {
			PermResult permResult = new PermResult(permissions, grantResults);
			if (permResult.areGranted()) changeTextViewStatus(tvReqPerms, R.string.granted, 1); // All granted.
			else if (permResult.isGranted(WRITE_EXTERNAL_STORAGE) || permResult.isGranted(READ_EXTERNAL_STORAGE) ||
					permResult.isGranted(ACCESS_FINE_LOCATION) || permResult.isGranted(SET_ALARM) ||
					permResult.isGranted(WRITE_CALENDAR) || permResult.isGranted(READ_CALL_LOG))
				changeTextViewStatus(tvReqPerms, R.string.granted_some, 0);
			else if (permResult.areDenied()) changeTextViewStatus(tvReqPerms, R.string.denied, -1); // All denied.
			else if (permResult.areCancelled()) changeTextViewStatus(tvReqPerms, R.string.denied, -1); // All cancelled.
			else tvReqPerms.setVisibility(View.GONE);
			// Dummy code is below this comment line REMOVE IT
			for (String permission : permissions) AManager.log("onRequestPermissionsResult", permission + " -is-> " + (permResult.isGranted(permission) ? "Granted" : "Denied"));
		}
	}
}

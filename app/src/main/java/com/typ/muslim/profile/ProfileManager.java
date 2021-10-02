/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.profile;

import android.content.Context;

import androidx.annotation.NonNull;

import com.typ.muslim.R;
import com.typ.muslim.interfaces.ResultCallback;
import com.typ.muslim.managers.AMRes;
import com.typ.muslim.managers.AMSettings;
import com.typ.muslim.managers.PrefManager;
import com.typ.muslim.models.Timestamp;
import com.typ.muslim.profile.models.ExportableProfile;
import com.typ.muslim.profile.models.Profile;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileManager {

	// todo: Add method (isProfileCompleted) which returns true if the profile has fields to be completed on it

	// Profile pref keys
	public static final String KEY_PROFILE_ID = "profile_id";
	public static final String KEY_PROFILE_IS_MALE = "profile_is_male";
	public static final String KEY_PROFILE_NAME = "profile_name";
	public static final String KEY_PROFILE_PHOTO_PATH = "profile_photo";
	public static final String KEY_PROFILE_CREATED_IN = "profile_created_in";

	/**
	 * Get the current user profile as Profile model class.
	 *
	 * @param context The context used to access preferences
	 *
	 * @return The current user profile see {@link Profile} for details or null if not found.
	 *
	 * @implNote This method is annotated Nullable only for checking if user has profile or not
	 * and the app won't let user continue to it unless he create a profile
	 */
	@Contract(pure = true)
	@Nullable
	public static Profile getProfile(Context context) {
		return PrefManager.contains(context, KEY_PROFILE_ID) ?
		       Profile.of(PrefManager.get(context, KEY_PROFILE_ID, 1),
				       PrefManager.get(context, KEY_PROFILE_IS_MALE, true),
				       PrefManager.get(context, KEY_PROFILE_NAME, AMRes.getString(context, R.string.default_profile_name)),
				       PrefManager.get(context, KEY_PROFILE_PHOTO_PATH, ""),
				       new Timestamp(PrefManager.get(context, KEY_PROFILE_CREATED_IN, Timestamp.NOW().toMillis()))) : null;
	}

	@Contract(pure = true)
	public static boolean hasProfile(Context context) {
		return getProfile(context) != null;
	}

	/**
	 * Indicating whether the user is Male or Female.
	 *
	 * @apiNote returns {@code true} if no profile was found.
	 */
	public static boolean isMaleUser(Context context) {
		final Profile profile = getProfile(context);
		return profile == null || profile.isMale();
	}

	/**
	 * Starts a new ProfileManager instance to do operations like:
	 * (CREATE, IMPORT, EDIT, DELETE) a profile.
	 *
	 * @implNote ProfileManager.get(context).doBefore(a1, ...).doAfter(b1, ...).resultsOn(callback).create(profile)
	 */
	public static ManagerInstance get(Context context) {
		return new ManagerInstance(context);
	}

	static void createProfile(Context c, @Nullable Profile profile, @NonNull ResultCallback<Profile> callback) {
		// Check profile at first
		if (profile == null) {
			callback.onFailed();
			return;
		}
		// Save user profile
		PrefManager.set(c, KEY_PROFILE_ID, profile.getId());
		PrefManager.set(c, KEY_PROFILE_IS_MALE, profile.isMale());
		PrefManager.set(c, KEY_PROFILE_NAME, profile.getName());
		PrefManager.set(c, KEY_PROFILE_PHOTO_PATH, profile.getPhotoPath());
		PrefManager.set(c, KEY_PROFILE_CREATED_IN, profile.getCreatedIn().toMillis());
		// Notify callback
		callback.onResult(profile);
	}

	static void importProfile(Context c, @Nullable ExportableProfile exProfile, @NonNull ResultCallback<Profile> callback) {
		if (exProfile != null) {
			// Import profile
			PrefManager.set(c, KEY_PROFILE_ID, exProfile.getId());
			PrefManager.set(c, KEY_PROFILE_IS_MALE, exProfile.isMale());
			PrefManager.set(c, KEY_PROFILE_NAME, exProfile.getName());
			PrefManager.set(c, KEY_PROFILE_PHOTO_PATH, exProfile.getPhotoPath());
			PrefManager.set(c, KEY_PROFILE_CREATED_IN, exProfile.getCreatedIn().toMillis());
			// Import settings (if was exported)
			if (exProfile.hasSettings()) AMSettings.importSettings(c, exProfile.getSettings());
			// Notify callback
			callback.onResult(exProfile.getProfile());
		} else callback.onFailed();
	}

	static void editProfile(Context c, @Nullable Map<String, ?> editedProfileFields, @NonNull ResultCallback<Profile> callback) {
		if (editedProfileFields != null) {
			for (String key : editedProfileFields.keySet()) {
				Object value = editedProfileFields.get(key);
				if (value instanceof Integer) PrefManager.set(c, key, (int) value);
				else if (value instanceof Boolean) PrefManager.set(c, key, (boolean) value);
				else if (value instanceof String) PrefManager.set(c, key, (String) value);
				else if (value instanceof Long) PrefManager.set(c, key, (long) value);
			}
		}
	}

	static void deleteProfile(Context c, @Nullable Profile profile, boolean resetSettings, @NonNull ResultCallback<Profile> callback) {
		if (profile == null) {
			callback.onFailed();
			return;
		}
		// Delete profile
		PrefManager.remove(c, KEY_PROFILE_ID);
		PrefManager.remove(c, KEY_PROFILE_IS_MALE);
		PrefManager.remove(c, KEY_PROFILE_NAME);
		PrefManager.remove(c, KEY_PROFILE_PHOTO_PATH);
		PrefManager.remove(c, KEY_PROFILE_CREATED_IN);
		// Reset settings (if requested)
		Map<String, ?> exSettings = new HashMap<>();
		if (resetSettings) exSettings = AMSettings.resetSettings(c);
		// Notify callback
		callback.onResult(new ExportableProfile(profile, exSettings));
	}

	public static class ManagerInstance {

		// Runtime
		final Context context;
		List<Runnable> actionsBefore;
		List<Runnable> actionsAfter;
		ResultCallback<Profile> callback;

		ManagerInstance(Context context) {
			this.context = context;
			actionsBefore = new ArrayList<>();
			actionsAfter = new ArrayList<>();
		}

		public ManagerInstance doBefore(Runnable... actions) {
			if (actions != null && actions.length > 0) actionsBefore.addAll(actionsAfter);
			return this;
		}

		public ManagerInstance doAfter(Runnable... actions) {
			if (actions != null && actions.length > 0) actionsAfter.addAll(actionsAfter);
			return this;
		}

		public ManagerInstance resultsOn(ResultCallback<Profile> callback) {
			this.callback = callback;
			return this;
		}

		public void createProfile(Profile profile) {
			// Execute actions before
			executeActions(actionsBefore);
			// Execute target operation
			ProfileManager.createProfile(context, profile, callback);
			// Execute actions after
			executeActions(actionsAfter);
		}

		public void importProfile(@Nullable ExportableProfile exProfile) {
			// Execute actions before
			executeActions(actionsBefore);
			// Execute target operation
			ProfileManager.importProfile(context, exProfile, callback);
			// Execute actions after
			executeActions(actionsAfter);
		}

		public void editProfile(@Nullable Map<String, Object> editedProfileFields) {
			// Execute actions before
			executeActions(actionsBefore);
			// Execute target operation
			ProfileManager.editProfile(context, editedProfileFields, callback);
			// Execute actions after
			executeActions(actionsAfter);
		}

		/**
		 * @apiNote Callback on this operation returns an (ExportableProfile) not a (Profile)
		 */
		public void deleteProfile(@Nullable Profile profile, boolean resetSettings) {
			// Execute actions before
			executeActions(actionsBefore);
			// Execute target operation
			ProfileManager.deleteProfile(context, profile, resetSettings, callback);
			// Execute actions after
			executeActions(actionsAfter);
		}

		void executeActions(List<Runnable> actions) {
			if (actions == null || actions.isEmpty()) return;
			for (Runnable action : actions) if (action != null) action.run();
		}

	}

}

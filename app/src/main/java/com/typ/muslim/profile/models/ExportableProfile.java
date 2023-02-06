/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.profile.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.typ.muslim.models.Timestamp;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class ExportableProfile extends Profile {

	@Nullable
	private final Map<String, ?> settings;

	public ExportableProfile(Profile profile, @Nullable Map<String, ?> settings) {
		super(profile.getId(), profile.isMale(), profile.getName(), profile.getPhotoPath(), profile.getCreatedIn());
		this.settings = settings;
	}

	public ExportableProfile(int id, boolean isMale, @NonNull @NotNull String name, @Nullable String photoPath, Timestamp createdIn, Map<String, ?> settings) {
		super(id, isMale, name, photoPath, createdIn);
		this.settings = settings;
	}

	public boolean hasSettings() {
		return settings != null && !settings.isEmpty();
	}

	@Nullable
	public Map<String, ?> getSettings() {
		return settings;
	}

	public Profile getProfile() {
		return (Profile) this;
	}

	public String toJson() throws JSONException {
		JSONObject json = new JSONObject();
		// Write Profile info to json
		json.put("id", getId());
		json.put("is_male", isMale());
		json.put("name", getName());
		if (hasPhoto()) json.put("photo_path", getPhotoPath());
		json.put("created_in", getCreatedIn().toMillis());
		// Jsonify settings if included
		if (hasSettings()) {
			try {
				json.put("settings", new JSONObject(getSettings()));
			} catch (JSONException unusedExc) {
				unusedExc.printStackTrace();
			}
		}
		return json.toString();
	}

}

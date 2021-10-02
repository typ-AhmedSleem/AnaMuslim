/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.profile.models;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;
import com.typ.muslim.interfaces.Exportable;
import com.typ.muslim.managers.AMSettings;
import com.typ.muslim.models.Timestamp;
import com.typ.muslim.profile.ProfileHelper;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Model class representing Profile info
 */
public class Profile implements Cloneable {

	// Profile info fields
	private final int id;
	private final boolean isMale;
	private final @NonNull String name;
	private final @Nullable String photoPath;
	private final Timestamp createdIn;

	Profile(int id, boolean isMale, @NonNull String name, @Nullable String photoPath, Timestamp createdIn) {
		this.id = id;
		this.name = name;
		this.isMale = isMale;
		this.createdIn = createdIn != null ? createdIn : Timestamp.NOW();
		// Photo
		if (TextUtils.isEmpty(photoPath)) this.photoPath = null;
		else {
			final File photoFile = new File(photoPath);
			this.photoPath = photoFile.exists() ? photoPath : null;
		}
	}

	/**
	 * Builds a Profile model class with new info
	 */
	@NotNull
	@Contract("_, _, _, _ -> new")
	public static Profile create(int id, boolean isMale, @NonNull String name, @Nullable String photoPath) {
		return new Profile(id, isMale, name, photoPath, Timestamp.NOW());
	}

	/**
	 * Builds a Profile model class with existing info
	 */
	@NotNull
	@Contract("_, _, _, _, _ -> new")
	public static Profile of(int id, boolean isMale, @NonNull String name, @Nullable String photoPath, Timestamp createdIn) {
		return new Profile(id, isMale, name, photoPath, createdIn);
	}

	public int getId() {
		return id;
	}

	public boolean isMale() {
		return isMale;
	}

	@NonNull
	public String getName() {
		return name;
	}

	@Nullable
	public String getPhotoPath() {
		return photoPath;
	}

	public Timestamp getCreatedIn() {
		return createdIn;
	}

	/**
	 * Indicating whether user has set a photo or not
	 */
	public boolean hasPhoto() {
		return !TextUtils.isEmpty(this.photoPath);
	}

	/**
	 * Helper method used to load profile photo into the given ImageView or default photo if no photo was set
	 */
	public <IV extends ImageView> void loadPhotoIn(IV iv) {
		if (this.hasPhoto()) {
			final File photo = new File(this.photoPath);
			if (photo.exists()) Picasso.get().load(photo).into(iv);
			else loadDefaultPhotoIn(iv);
		} else loadDefaultPhotoIn(iv);
	}

	public void loadDefaultPhotoIn(ImageView iv) {
		Picasso.get().load(ProfileHelper.getDefaultProfileAvatar(this.isMale)).into(iv);
	}

	public ExportableProfile export(Context context) {
		return new ExportableProfile(
				this.id,
				this.isMale,
				this.name,
				this.photoPath,
				this.createdIn,
				AMSettings.exportSettings(context));
	}

	@Override
	public String toString() {
		return "Profile{" +
		       "id=" + id +
		       ", isMale=" + isMale +
		       ", name='" + name + '\'' +
		       ", photoUri=" + photoPath +
		       ", createdIn=" + createdIn +
		       '}';
	}

}

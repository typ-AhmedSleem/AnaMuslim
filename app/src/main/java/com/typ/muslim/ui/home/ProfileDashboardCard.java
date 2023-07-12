/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.home;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.typ.muslim.R;
import com.typ.muslim.enums.FormatPattern;
import com.typ.muslim.profile.ProfileManager;
import com.typ.muslim.profile.models.Profile;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileDashboardCard extends DashboardCard {

	// todo: Recode methods that getString from resources to return the text for female or male as provided in user Profile. e.g: AMRes.get(ctx).getString(R.string.maleText, R.string.femaleText);
	// todo: Add indicator in the card if any actions is needed to be completed by user in his profile

	// Statics
	private static final String TAG = "ProfileDashboardCard";
	// Runtime
	private Profile profile;
	private boolean hasLoadedProfilePic = false;
	// Views
	private MaterialTextView tvName, tvJoinedIn;
	private CircleImageView cimgUserPic;
	private MaterialButton btnManageProfile;

	public ProfileDashboardCard(Context context) {
		super(context);
	}

	public ProfileDashboardCard(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ProfileDashboardCard(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public void prepareRuntime(Context context) {
		if (isInEditMode()) return;
		// Get user profile
		this.profile = ProfileManager.getProfile(context);
	}

	@Override
	public void prepareCardView(Context context) {
		// Inflate card view
		inflate(context, R.layout.layout_profile_card, this);
		// Init views
		this.cimgUserPic = $(R.id.cimg_user_pic);
		this.tvName = $(R.id.tv_username);
		this.tvJoinedIn = $(R.id.tv_joined_in);
		this.btnManageProfile = $(R.id.btn_profile_options);
		// Listener
		this.btnManageProfile.setOnClickListener(this);
		// Perform UI refresh
		if (!isInEditMode()) this.refreshUI();
	}

	@Override
	public void refreshRuntime() {
		// Get user profile
		this.profile = ProfileManager.getProfile(getContext());
	}

	@Override
	public void refreshUI() {
		this.tvName.setText(this.profile.getName());
		if (!hasLoadedProfilePic) {
			profile.loadPhotoIn(this.cimgUserPic);
			hasLoadedProfilePic = true;
		}
		this.tvJoinedIn.setText(String.format(Locale.getDefault(), "%s: %s",
				getString(R.string.joined_in),
				this.profile.getCreatedIn().getFormatted(FormatPattern.DATE_NORMAL)));
	}

	@Override
	public void onClick(View v) {
		Toast.makeText(getContext(), R.string.planned_in_next_versions, Toast.LENGTH_LONG).show();
		// todo: Open ProfileBottomSheet in {half_expanded} state displaying profile info and show settings button and settings list is displayed when the bs is in {full_screen} state
	}

	@Override
	public boolean onLongClick(View v) {
		// todo: Open ProfileActionsBottomSheet that shows actions [exportProfile|importProfile|logout]
		return true;
	}
}

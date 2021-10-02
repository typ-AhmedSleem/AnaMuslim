/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityOptionsCompat;

import com.typ.muslim.R;
import com.typ.muslim.interfaces.OnTestCompletedListener;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.models.StageInfo;
import com.typ.muslim.profile.ProfileManager;
import com.typ.muslim.ui.StagesView;

import org.jetbrains.annotations.NotNull;

public class SplashActivity extends AppCompatActivity implements OnTestCompletedListener {

	// Statics
	private static final int RC_PERM = 21;
	private static final String TAG = "SplashActivity";
	// Views
	private StagesView stagesView;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// Always change Theme to Light to keep colors unchanged
		AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		// Hide default ActionBar
		if (getSupportActionBar() != null) getSupportActionBar().hide();
		// Check next destination then move to it
		if (ProfileManager.hasProfile(this)) this.onTestCompleted(new Class[]{MainActivity.class});
		else this.onTestCompleted(new Class[]{WizardActivity.class});
		// Setup stages view then execute stages async
//		stagesView = findViewById(R.id.splash_stages_view);
//		stagesView.setStages(EasyList.createList(
//				new Pair<>(new StageInfo(R.string.checking_perms, true, false), () -> {
//					AManager.log(TAG, "STAGE1: Checking Permissions...");
//					Perm perm = new Perm(SplashActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
//					if (perm.areGranted()) return SUCCEED;
//					else {
//						perm.askPermissions(RC_PERM);
//						return FAILED;
//					}
//				}),
//				new Pair<>(new StageInfo(R.string.checking_database, true, false), () -> {
//					return SUCCEED;
//				}),
//				new Pair<>(new StageInfo(R.string.checking_database, true, false), () -> {
//					AManager.log(TAG, "STAGE3: SUCCEED");
//					return StageResult.SUCCEED;
//				}),
//				new Pair<>(new StageInfo(R.string.checking_location, true, false), () -> {
//					AManager.log(TAG, "STAGE4: SUCCEED");
//					return StageResult.SUCCEED;
//				}),
//				new Pair<>(new StageInfo(R.string.checking_config, false, false), () -> {
//					AManager.log(TAG, "STAGE5: FAILED");
//					return FAILED;
//				}),
//				new Pair<>(new StageInfo(R.string.starting_anamuslim, true, true), null)
//		));//.executeAsync(this);
	}

	private void requestPermissions() {
		// TODO: 2021-05-27 code this
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		// TODO: 2021-05-27 code this
	}

	@Override
	public void onTestCompleted(Object[] payload) {
		// Payload contains 1 element which is class of the target activity
		new Handler().postDelayed(() -> {
			if (payload.length > 0) {
				Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, findViewById(R.id.splashTV), "headerTextViewTransition").toBundle();
				startActivity(new Intent(this, ((Class<?>) payload[0])));
				finish();
			} else {
				// TODO: 2021-05-27 handle this state
				AManager.log(TAG, "onTestCompleted: Payload contains no elements.");
			}
		}, 1500);
	}

	@Override
	public boolean onTestRequiresAction(@NonNull StageInfo stageInfo) {
		// TODO: 2021-05-27 handle this
		return false;
	}

	@Override
	public boolean onTestFacedError() {
		// TODO: 2021-05-27 handle this
		return false;
	}

	@Override
	public void onTestFailed(Exception reason) {
		// TODO: 2021-05-27 handle this

	}

}

/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui.activities.khatma;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.typ.muslim.ui.AnimatedTextView.Direction.NEXT;
import static com.typ.muslim.ui.AnimatedTextView.Direction.PREV;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.progresviews.ProgressWheel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.typ.muslim.R;
import com.typ.muslim.app.Consumers;
import com.typ.muslim.enums.FormatPatterns;
import com.typ.muslim.enums.KhatmaPlans;
import com.typ.muslim.interfaces.KhatmaManagerCallback;
import com.typ.muslim.interfaces.ResultCallback;
import com.typ.muslim.managers.KhatmaManager;
import com.typ.muslim.models.Khatma;
import com.typ.muslim.models.Timestamp;
import com.typ.muslim.ui.AnimatedTextView;
import com.typ.muslim.ui.BottomSheets;
import com.typ.muslim.ui.ViewContainer;
import com.typ.muslim.ui.activities.quran.QuranReaderActivity;
import com.zyyoona7.wheel.WheelView;

import java.util.Arrays;
import java.util.Locale;

public class KhatmaActivity extends AppCompatActivity implements KhatmaManagerCallback {

	// Statics
	private static final int RC_JOIN_KHATMA = 12;
	private static final int RC_CREATE_KHATMA = 21;
	private static final String TAG = "KhatmaActivity";
	// Runtime
	private KhatmaManager manager;
	// Views
	private FrameLayout flContainer;
	private ActiveKhatmaView akv;
	private NoKhatmaView nkv;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Hide default actionBar
		if (getSupportActionBar() != null) getSupportActionBar().hide();
		// Setup Khatma Manager instance
		manager = KhatmaManager.getInstance(this, this);
		// Set content to activity
		setContentView(flContainer);
	}

	@Override
	public void onPrepareManager() {
		// Setup container view
		flContainer = new FrameLayout(this);
		flContainer.setLayoutTransition(new LayoutTransition());
		// Setup khatma views
		akv = new ActiveKhatmaView(this);
		nkv = new NoKhatmaView(this);
	}

	@Override
	public void onPutUnderManagement(@NonNull Khatma khatma) {
		flContainer.removeAllViews();
		flContainer.addView(akv, new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
		akv.setKhatma(khatma);
	}

	@Override
	public void onNoActiveKhatmaFound() {
		flContainer.removeAllViews();
		flContainer.addView(nkv, new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
	}

	@Override
	public void onKhatmaProgressUpdated() {
		if (flContainer.getChildCount() > 0) {
			final ActiveKhatmaView akv = (ActiveKhatmaView) flContainer.getChildAt(0);
			akv.refreshUI();
		}
	}

	@Override
	public void onKhatmaReleased() {
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// todo: Check results came from JoinKhatmaActivity and CreateKhatmaActivity
	}

	private class NoKhatmaView extends ViewContainer {

		public NoKhatmaView(@NonNull Context context) {
			super(context);
		}

		@Override
		public void prepareView(Context context) {
			inflate(R.layout.layout_no_khatma_view);
			// Setup listeners of buttons
			$(R.id.btn_join_khatma).setOnClickListener(v -> {
				// todo: Open JoinKhatmaBottomSheet and listen for result
				Toast.makeText(context, "TODO: Join Khatma", Toast.LENGTH_SHORT).show();
			});
			$(R.id.btn_create_khatma).setOnClickListener(v -> {
				new CreateKhatmaBottomSheet(context, khatma -> {
					KhatmaManager.createNewKhatma(context, khatma, (succeed, data) -> {
						if (succeed) manager.putUnderManagement(khatma, true);
						else Toast.makeText(context, "Can't create Khatma", Toast.LENGTH_SHORT).show();
					});
				}, false).show();
			});
		}
	}

	private class ActiveKhatmaView extends ViewContainer {

		// Runtime
		private Khatma khatma;
		// Views
		private ProgressWheel pwPercentage;
		private MaterialTextView tvCompletedDays, tvName, tvPlanDays, tvRemDays, tvStartedIn, tvEndsIn;
		private MaterialButton btnOpenQuranReader;


		public ActiveKhatmaView(@NonNull Context context) {
			super(context);
		}

		@Override
		public void prepareView(Context context) {
			inflate(R.layout.layout_active_khatma_view);
			// Setup views
			pwPercentage = $(R.id.prw_khatma_percentage);
			tvCompletedDays = $(R.id.tv_khatma_completed_days);
			tvName = $(R.id.tv_khatma_num_name);
			tvPlanDays = $(R.id.tv_khatma_plan);
			tvRemDays = $(R.id.tv_khatma_rem_days);
			tvStartedIn = $(R.id.tv_khatma_start_date);
			tvEndsIn = $(R.id.tv_khatma_end_date);
			MaterialButton btnViewProgress = $(R.id.btn_view_khatma_progress);
			btnOpenQuranReader = $(R.id.btn_go_to_quran_reader);
			// Setup listeners
			btnViewProgress.setOnClickListener(v -> {
				manager.saveNewProgress();
			});
			btnOpenQuranReader.setOnClickListener(v -> {
				startActivity(new Intent(context, QuranReaderActivity.class));
			});
		}

		public void setKhatma(@NonNull Khatma khatma) {
			this.khatma = khatma;
			refreshUI();
		}

		@Override
		public void refreshUI() {
			tvCompletedDays.setText(String.format(Locale.getDefault(), "%d %s", khatma.getCompletedDays(), getString(khatma.getCompletedDays() == 1 ? R.string.day : R.string.days)));
			pwPercentage.setStepCountText(String.valueOf(khatma.getProgressPercentage()));
			pwPercentage.setPercentage((int) ((khatma.getProgressPercentage() / 100f) * 360));
			tvName.setText(Consumers.returnWhen(TextUtils.isEmpty(khatma.getName()), String.format(Locale.getDefault(), "%s %d", getString(R.string.khatma_number), khatma.getNumber()), khatma.getName()));
			tvRemDays.setText(String.valueOf(khatma.getPlan().getDuration() - khatma.getTodayNumber()));
			tvPlanDays.setText(String.format(Locale.getDefault(), "%d %s", khatma.getPlan().getDuration(), getString(R.string.days)));
			tvStartedIn.setText(khatma.getStartedIn().getFormatted(FormatPatterns.DATE_NORMAL));
			tvEndsIn.setText(khatma.getExpectedEnd().getFormatted(FormatPatterns.DATE_NORMAL));
			if (khatma.getProgressPercentage() >= 100) btnOpenQuranReader.setVisibility(GONE);
		}

		public void onProgressUpdate() {
			if (khatma == null) return;
			tvStartedIn.setText(khatma.getStartedIn().getFormatted(FormatPatterns.DATE_NORMAL));
			tvEndsIn.setText(khatma.getExpectedEnd().getFormatted(FormatPatterns.DATE_NORMAL));
		}

	}

	public static class CreateKhatmaBottomSheet extends BottomSheets.BaseBottomSheet {

		// Runtime
		private KhatmaPlans plan;
		// Views
		private TextInputLayout inputKhatmaName;
		private AnimatedTextView atvExpectedEnd;
		private WheelView<KhatmaPlans> wheelPlanPicker;
		// Callbacks
		private final ResultCallback<Khatma> callback;
		private MaterialButton btnCreate;

		public CreateKhatmaBottomSheet(Context context, ResultCallback<Khatma> khatmaResultCallback, boolean fitToContents) {
			super(context, null, fitToContents);
			callback = khatmaResultCallback;
			setupListeners();
			bindInnerViews();
		}

		@Override
		public void prepareRuntime() {
			if (plan == null) plan = KhatmaPlans.PLAN_30_DAYS;
		}

		@NonNull
		@Override
		public View onCreateView() {
			return inflate(R.layout.bs_create_khatma);
		}

		@Override
		public void prepareInnerViews() {
			inputKhatmaName = $(R.id.til_khatma_name);
			atvExpectedEnd = $(R.id.atv_plan_expected_end);
			wheelPlanPicker = $(R.id.wheel_plan_picker);
			btnCreate = $(R.id.btn_create_khatma);
		}

		@Override
		public void bindInnerViews() {
			inputKhatmaName.getEditText().setText(null);
			wheelPlanPicker.setSelectedItemPosition(KhatmaPlans.PLAN_30_DAYS.ordinal());
			wheelPlanPicker.setData(Arrays.asList(KhatmaPlans.values()));
		}

		@Override
		public void setupListeners() {
			btnCreate.setOnClickListener(v -> {
				callback.onResult(new Khatma(plan, String.valueOf(inputKhatmaName.getEditText().getText())));
				cancel();
			});
			wheelPlanPicker.setOnItemSelectedListener((wheelView, plan, position) -> {
				final AnimatedTextView.Direction animDir = plan.ordinal() > CreateKhatmaBottomSheet.this.plan.ordinal() ? NEXT : PREV;
				CreateKhatmaBottomSheet.this.plan = plan;
				atvExpectedEnd.setText(String.format(Locale.getDefault(), "%s:\n%s",
						getString(R.string.expected_end),
						plan.getExpectedEnd(Timestamp.NOW()).getFormatted(FormatPatterns.DATE_NORMAL)), animDir);
			});
		}
	}

}

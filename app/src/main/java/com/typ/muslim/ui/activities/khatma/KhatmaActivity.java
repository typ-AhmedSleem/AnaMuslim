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
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.progresviews.ProgressWheel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.android.material.transition.platform.MaterialContainerTransform;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;
import com.typ.muslim.R;
import com.typ.muslim.enums.FormatPatterns;
import com.typ.muslim.enums.KhatmaPlans;
import com.typ.muslim.features.quran.Quran;
import com.typ.muslim.interfaces.KhatmaManagerCallback;
import com.typ.muslim.interfaces.ResultCallback;
import com.typ.muslim.managers.KhatmaManager;
import com.typ.muslim.models.Khatma;
import com.typ.muslim.models.Timestamp;
import com.typ.muslim.ui.AnimatedTextView;
import com.typ.muslim.ui.BottomSheets;
import com.typ.muslim.ui.ViewContainer;
import com.zyyoona7.wheel.WheelView;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

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
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        findViewById(android.R.id.content).setTransitionName("transition_card_to_activity");
        // Setup transitions
        final MaterialContainerTransform transition = new MaterialContainerTransform();
        transition.addTarget(android.R.id.content);
        transition.setScrimColor(Color.TRANSPARENT);
        transition.setDrawingViewId(android.R.id.content);
        // Setup the activity
        getWindow().setSharedElementEnterTransition(transition);
        getWindow().setSharedElementReenterTransition(transition);
        setEnterSharedElementCallback(new MaterialContainerTransformSharedElementCallback());
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
        finishAfterTransition();
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
                Toast.makeText(context, "Under development.. will be available in the next update.", Toast.LENGTH_SHORT).show();
            });
            $(R.id.btn_create_khatma).setOnClickListener(v -> {
                new CreateKhatmaBottomSheet(context, khatma -> {
                    KhatmaManager.createNewKhatma(context, khatma, (succeed, data) -> {
                        if (succeed) manager.putUnderManagement(khatma, true);
                        else Toast.makeText(context, "Can't create Khatma", Toast.LENGTH_SHORT).show();
                    });
                }).show();
            });
        }
    }

    private class ActiveKhatmaView extends ViewContainer {

        // Runtime
        private Khatma khatma;
        // Views
        private ProgressWheel pwPercentage;
        private MaterialTextView tvCompletedDays,
                tvName,
                tvPlanDays,
                tvRemDays,
                tvStartedIn,
                tvEndsIn,
                tvWerdStartSurah,
                tvWerdStartAyah,
                tvWerdEndSurah,
                tvWerdEndAyah;

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
            tvWerdStartSurah = $(R.id.tv_werd_start_surah);
            tvWerdStartAyah = $(R.id.tv_werd_start_ayah);
            tvWerdEndSurah = $(R.id.tv_werd_end_surah);
            tvWerdEndAyah = $(R.id.tv_werd_end_ayah);
            MaterialButton btnViewProgress = $(R.id.btn_save_khatma_progress);
            MaterialButton btnDeleteKhatma = $(R.id.btn_delete_khatma);
            MaterialButton btnViewHistory = $(R.id.btn_khatma_history);
            // Setup listeners
            btnViewProgress.setOnClickListener(v -> {
                new Handler().postDelayed(() -> {
                    // Hide the msg after 10 seconds

                }, 10 * 1000);
                manager.saveProgress(); // Save progress.
            });
            btnDeleteKhatma.setOnClickListener(v -> manager.delete(true)); // todo: Ask user first for confirmation.
            btnViewHistory.setOnClickListener(v -> Toast.makeText(context, "Under development.. Will be available in the next version.", Toast.LENGTH_SHORT).show());
            ((MaterialToolbar) $(R.id.toolbar)).setNavigationOnClickListener(v -> manager.releaseKhatmaFromManagement(true));
        }

        public void setKhatma(@NonNull Khatma khatma) {
            this.khatma = khatma;
            refreshUI();
        }

        @Override
        public void refreshUI() {
            if (khatma == null) {
                onNoActiveKhatmaFound();
                return;
            } else if (!khatma.isActive()) {
                onNoActiveKhatmaFound();
                return;
            }
            // Khatma details
            tvCompletedDays.setText(khatma.getName(getContext()));
            pwPercentage.setStepCountText(khatma.getProgressPercentage() + "%");
            pwPercentage.setPercentage((int) ((khatma.getProgressPercentage() / 100f) * 360));
            tvName.setText(tvCompletedDays.getText());
            tvPlanDays.setText(String.format(Locale.getDefault(), "%d %s (%d/%s)", khatma.getPlan().getDuration(), getString(R.string.days), khatma.getWerdLength(), getString(R.string.day)));
            final int remParts = Quran.QURAN_JUZ2S_COUNT - (khatma.getCompletedWerds() * khatma.getWerdLength());
            tvRemDays.setText(String.format(Locale.getDefault(), "%d %s", remParts, getString(remParts == 1 ? R.string.part : R.string.parts)));
            tvStartedIn.setText(khatma.getStartedIn().getFormatted(FormatPatterns.DATE_NORMAL));
            tvEndsIn.setText(khatma.getExpectedEnd().getFormatted(FormatPatterns.DATE_NORMAL));
            // Werd
            if (khatma.getCurrentWerd() != null) {
                tvWerdStartSurah.setText(khatma.getCurrentWerd().getStart().getSurah().getName(getContext()));
                tvWerdStartAyah.setText(String.valueOf(khatma.getCurrentWerd().getStart().getNumber()));
                tvWerdEndSurah.setText(khatma.getCurrentWerd().getEnd().getSurah().getName(getContext()));
                tvWerdEndAyah.setText(String.valueOf(khatma.getCurrentWerd().getEnd().getNumber()));
            }
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

        public CreateKhatmaBottomSheet(Context context, ResultCallback<Khatma> khatmaResultCallback) {
            super(context, null, true);
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
            Objects.requireNonNull(inputKhatmaName.getEditText()).setText(null);
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

    public static class CongratulationBottomSheet extends BottomSheets.BaseBottomSheet {

        // Views
        private MaterialButton btnDismiss;

        public CongratulationBottomSheet(Context context) {
            super(context, null, true);
            setupListeners();
            bindInnerViews();
        }

        @Override
        public void prepareRuntime() {
        }

        @NonNull
        @Override
        public View onCreateView() {
            return inflate(R.layout.bs_congratulations_on_khatma);
        }

        @Override
        public void prepareInnerViews() {
        }

        @Override
        public void bindInnerViews() {
        }

        @Override
        public void setupListeners() {

        }
    }

}

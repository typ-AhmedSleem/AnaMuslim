/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.ui;

import static com.typ.muslim.enums.StageResult.SUCCEED;
import static com.typ.muslim.enums.StageStatus.CANCELLED;
import static com.typ.muslim.enums.StageStatus.DONE;
import static com.typ.muslim.enums.StageStatus.FAILED;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Pair;
import android.widget.ViewFlipper;

import com.typ.muslim.enums.StageResult;
import com.typ.muslim.enums.StageStatus;
import com.typ.muslim.interfaces.Executable;
import com.typ.muslim.interfaces.OnTestCompletedListener;
import com.typ.muslim.libs.easyjava.data.EasyList;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.models.StageInfo;
import com.typ.muslim.ui.activities.MainActivity;

import java.security.InvalidParameterException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class StagesView extends ViewFlipper {

	// Statics
	private static final String TAG = "StagesView";
	private final Handler handler = new Handler(Looper.getMainLooper());
	// Runtime
	private int currentStageNumber = 1;
	private EasyList<Pair<StageInfo, Executable>> stages;

	public StagesView(Context context) {
		super(context);
	}

	public StagesView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public StageItemView getStageItemViewAt(int stepIndex) {
		return (StageItemView) getChildAt(stepIndex);
	}

	public void showNext(int delay) {
		// Increase current stage
		if (currentStageNumber >= getChildCount()) currentStageNumber = 1;
		else currentStageNumber++;
		// Show next
		if (delay > 0) handler.postDelayed(super::showNext, delay);
		else handler.post(super::showNext);
	}

	public void showPrevious(int delay) {
		// Decrease current stage
		if (currentStageNumber <= 0) currentStageNumber = 1;
		else currentStageNumber--;
		// Show prev
		if (delay > 0) handler.postDelayed(super::showPrevious, delay);
		else super.showNext();
	}

	public int getCurrentStageNumber() {
		return currentStageNumber;
	}

	public StageItemView getCurrentStage() {
		return (StageItemView) getChildAt(currentStageNumber - 1);
	}

	public StagesView setStages(EasyList<Pair<StageInfo, Executable>> stages) {
		this.stages = stages;
		int childCount = getChildCount();
		if (stages == null) throw new NullPointerException("Stages can't be null");
		if (stages.size() == 0 || childCount == 0)
			throw new InvalidParameterException("Stages and children should be at least one");
		// Update runtime and views
		this.stages = stages;
		for (int i = 0; i < Math.min(childCount, stages.size()); i++)
			((StageItemView) getChildAt(i)).setStage(stages.get(i).first);
		return this;
	}

	public void updateStatus(StageItemView siv, StageStatus status) {
		if (siv == null || status == null) return;
		// Update status
		handler.post(() -> siv.updateStatus(status));
	}

	public void executeAsync(OnTestCompletedListener callback, Executable executable) {
		// STAGE 1: Check Permissions
		if (executable.call() == SUCCEED) {
			// Update SIV to success

			// Show next stage
			showNext(500);
		}
		// STAGE 2: Check Database
		// STAGE 3: Check Location
		// STAGE 4: Check Configuration
		// STAGE 5: Preparing Data
		// STAGE 6: Starting AnaMuslim
	}

	public void executeAsync(OnTestCompletedListener callback) {
		// TODO: 6/1/2021 Needs alot of work to be completed
		new Thread(() -> {
			if (stages == null) {
				if (callback != null)
					callback.onTestFailed(new Exception("There is no stages found"));
			} else {
				stages.iterate(((index, stage) -> {
					AManager.log(TAG, "\nWorking on %s", Thread.currentThread().toString());
					AManager.log(TAG, "Iterating the stage %d", index + 1);
					if (stage != null && stage.second != null) {
						AManager.log(TAG, "Scheduling stage %d for execution", index + 1);
						FutureTask<StageResult> task = new FutureTask<>(stage.second);
						try {
							StageResult result = task.get(4, TimeUnit.SECONDS);
							updateStatus((StageItemView) getChildAt(index), result == SUCCEED ? DONE : FAILED);
							showNext(0);
							AManager.log(TAG, "STAGE%d: Completed with Result => %s", index + 1, result);
						} catch (ExecutionException | InterruptedException | TimeoutException e) {
							// Change the SIV status to FAILED
							e.printStackTrace();
							updateStatus(((StageItemView) getChildAt(index)), task.isCancelled() ? CANCELLED : FAILED);
							showNext(0);
							AManager.log(TAG, "STAGE%d: Failed with Error=> %s", index + 1, e.getMessage());
						}
					} else AManager.log(TAG, "Either stage is null or the executable is null. STAGE[%s] EXECUTABLE[%s]", stage, stage != null ? stage.second : null);
					AManager.log(TAG, "Active Threads Count: " + Thread.activeCount());
				}));
				AManager.log(TAG, "Finished ASYNC");
				if (callback != null) callback.onTestCompleted(new Object[]{MainActivity.class});
			}
		}).start();
	}
}

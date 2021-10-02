/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.interfaces;

import androidx.annotation.NonNull;

import com.typ.muslim.models.StageInfo;

public interface OnTestCompletedListener {

    void onTestCompleted(Object[] payload);

    boolean onTestRequiresAction(@NonNull StageInfo stageInfo);

    boolean onTestFacedError();

    void onTestFailed(Exception reason);

}

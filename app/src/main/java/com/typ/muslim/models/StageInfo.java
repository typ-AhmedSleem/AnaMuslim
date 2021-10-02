/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.models;

import androidx.annotation.StringRes;

import com.typ.muslim.enums.StageStatus;

public class StageInfo {

    // Step info
    private @StringRes final int stepTitle;
    private final boolean isRequired;
    private StageStatus status;

    public StageInfo(int stepTitle, boolean isRequired, boolean isInfo) {
        this.stepTitle = stepTitle;
        this.isRequired = isRequired;
        this.status = isInfo ? StageStatus.INFO : StageStatus.WAITING;
    }

    public int getStepTitle() {
        return stepTitle;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public StageStatus getStatus() {
        return status;
    }

    public StageInfo updateStatus(StageStatus status) {
        this.status = status;
        return this;
    }
}

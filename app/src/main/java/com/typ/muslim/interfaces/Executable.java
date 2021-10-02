/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.interfaces;

import com.typ.muslim.enums.StageResult;

import java.util.concurrent.Callable;

public interface Executable extends Callable<StageResult> {

    @Override
    StageResult call();
}

/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.interfaces;

import com.typ.muslim.enums.ExpansionState;

public interface ExpansionListener {

	void onStateChanged(ExpansionState newState);

	void onStateUpdate(ExpansionState state, float progress);

}

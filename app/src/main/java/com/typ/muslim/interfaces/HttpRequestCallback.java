/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.interfaces;

import androidx.core.util.Pair;

public interface HttpRequestCallback {

	// todo: Add methods [onDownloading]

	void onSucceed();

	void onFailed(Pair<Integer, String> error);

	void onCancelled(boolean byUser);

}

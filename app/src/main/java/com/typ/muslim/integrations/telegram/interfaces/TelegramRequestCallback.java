/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.integrations.telegram.interfaces;

import com.typ.muslim.integrations.telegram.models.TelegramError;

import org.json.JSONObject;

public abstract class TelegramRequestCallback {

	public void onFailed(TelegramError error) {}

	public void onCancelled(boolean byUser) {}

	public void onResponse(JSONObject response) {}
}

/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.integrations.telegram.models;

import com.typ.muslim.managers.AManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TelegramRequestResult {

	private final boolean succeed;
	private final Map<String, Object> result;

	public TelegramRequestResult(boolean succeed, Map<String, Object> result) {
		this.succeed = succeed;
		this.result = result == null ? new HashMap<>() : result;
		AManager.log("TelegramRequestResult", toString());
	}

	public boolean hasSucceed() {
		return succeed;
	}

	public boolean hasResult() {
		return result != null && !result.isEmpty();
	}

	public int optInt(String key, int fallback) {
		if (result != null) {
			Object value = result.get(key);
			return value != null ? (int) value : fallback;
		}
		return fallback;
	}

	public boolean optBoolean(String key, boolean fallback) {
		if (result != null) {
			Object value = result.get(key);
			return value != null ? (boolean) value : fallback;
		}
		return fallback;
	}

	public String optString(String key, String fallback) {
		if (result != null) {
			Object value = result.get(key);
			return value != null ? (String) value : fallback;
		}
		return fallback;
	}

	public JSONObject optJSONObject(String key, JSONObject fallback) {
		if (result != null) {
			Object value = result.get(key);
			return value != null ? (JSONObject) value : fallback;
		}
		return fallback;
	}

	@Override
	public String toString() {
		return "TelegramRequestResult{" +
		       "hasSucceed=" + succeed +
		       ", result=" + result +
		       '}';
	}

}

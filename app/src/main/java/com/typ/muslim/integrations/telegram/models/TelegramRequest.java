/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.integrations.telegram.models;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.typ.muslim.integrations.telegram.interfaces.TelegramRequestCallback;
import com.typ.muslim.managers.AManager;

import org.json.JSONObject;

import java.util.Locale;

public class TelegramRequest {

	// todo: Add more fields for extra payload in the request

	private final String botToken;
	private final TelegramRequestCallback callback;
	// Urls
	private final String baseUrl;
	private String requestURL;

	public TelegramRequest(String botToken, TelegramRequestCallback callback) {
		// Set fields
		this.botToken = botToken;
		this.callback = callback;
		// Build base bot request url
		this.baseUrl = String.format(Locale.ENGLISH, "https://api.telegram.org/bot%s/", botToken);
		this.requestURL = baseUrl;
	}

	public String getBotToken() {
		return botToken;
	}

	/**
	 * todo: Create this better in code later
	 */
	public TelegramRequest addPayload(String payload) {
		// todo: Create this better in code later
		this.requestURL += payload;
		return this;
	}

	public TelegramRequest resetURL() {
		this.requestURL = baseUrl;
		return this;
	}

	public void send() {
		AManager.log("TelegramRequest", "send: URL[%s]", requestURL);
		AndroidNetworking.get(requestURL)
		                 .setPriority(Priority.HIGH)
		                 .build()
		                 .getAsJSONObject(new JSONObjectRequestListener() {
			                 @Override
			                 public void onResponse(JSONObject response) {
				                 notifyResponse(response);
			                 }

			                 @Override
			                 public void onError(ANError anError) {
				                 notifyFailed(new TelegramError(anError.getErrorCode(), anError.getErrorBody()));
			                 }
		                 });
	}

	void notifyResponse(JSONObject response) {
		if (callback != null) callback.onResponse(response);
	}

	void notifyFailed(TelegramError error) {
		if (callback != null) callback.onFailed(error);
	}

	void notifyCancelled(boolean byUser) {
		if (callback != null) callback.onCancelled(byUser);
	}

	@Override
	public String toString() {
		return "TelegramRequest{" +
		       "botToken='" + botToken + '\'' +
		       "baseURL='" + baseUrl + '\'' +
		       "requestURL='" + requestURL + '\'' +
		       '}';
	}
}

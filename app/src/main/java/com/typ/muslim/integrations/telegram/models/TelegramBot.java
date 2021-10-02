/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.integrations.telegram.models;

import android.database.Cursor;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.typ.muslim.integrations.telegram.TelegramBotsIntegration;
import com.typ.muslim.integrations.telegram.TelegramConstants;
import com.typ.muslim.integrations.telegram.interfaces.TelegramRequestCallback;
import com.typ.muslim.managers.AManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Model class that holds TelegramBot info
 */
public class TelegramBot extends TelegramUser {

	public static final TelegramBot UN_INITIALIZED_BOT = new TelegramBot(null);
	/**
	 * Bot's unique authentication
	 */
	public final @Nullable String token;
	/**
	 * Indicating whether the bot has its info fetched from (telegram or local) or not.
	 */
	public boolean isInitialized;

	public TelegramBot(@Nullable String token, int id, String firstName, String lastName, String username, boolean canJoinGroups, boolean canReadAllGroupMsgs, boolean supportsInlineQueries) {
		super(id, true, firstName, lastName, username, canJoinGroups, canReadAllGroupMsgs, supportsInlineQueries);
		this.token = token;
		this.isInitialized = !TextUtils.isEmpty(this.token);
	}

	public TelegramBot(@Nullable String token) {
		this(token, 0, null, null, null, false, false, false);
		this.isInitialized = false;
		// Send getMe request
		if (!TextUtils.isEmpty(token)) getMe();
	}

	public static List<TelegramBot> resolveAll(@Nullable Cursor c) {
		if (c == null) return new ArrayList<>();
		List<TelegramBot> bots = new ArrayList<>();
		c.moveToFirst();
		while (!c.isAfterLast()) {
			bots.add(resolve(c));
			c.moveToNext();
		}
		return bots;
	}

	public static TelegramBot resolve(@Nullable Cursor c) {
		if (c == null) return UN_INITIALIZED_BOT;
		return new TelegramBot(
				c.getString(0),
				c.getInt(1),
				c.getString(2),
				c.getString(3),
				c.getString(4),
				c.getString(5).equals("true"),
				c.getString(6).equals("true"),
				c.getString(7).equals("true"));
	}

	@NonNull
	public String getToken() {
		return token;
	}

	public TelegramBot setInitialized(boolean initialized) {
		isInitialized = initialized;
		return this;
	}

	public void getMe() {
		// Check if the bot token is null or empty
		if (TextUtils.isEmpty(token)) return;
		// Send GET request to Telegram to fetch bot info
		AndroidNetworking.get(TelegramConstants.ambBaseUrl + "\\getMe").setPriority(Priority.MEDIUM).build().getAsJSONObject(new JSONObjectRequestListener() {
			@Override
			public void onResponse(JSONObject rawResponse) {
				AManager.log("TelegramBot", "onResponse: " + rawResponse.toString());
				TelegramRequestResult response = TelegramBotsIntegration.parseResponse(rawResponse.toString());
				if (response.hasSucceed()) {
					id = response.optInt("id", 0);
					firstName = response.optString("first_name", "?");
					lastName = response.optString("last_name", "?");
					username = response.optString("username", "?");
					canJoinGroups = response.optBoolean("can_join_groups", false);
					canReadAllGroupMsgs = response.optBoolean("can_read_all_group_messages", false);
					supportsInlineQueries = response.optBoolean("supports_inline_queries", false);
					isInitialized = true;
					AManager.log("TelegramBot", "onParseResponse: " + TelegramBot.this.toString());
				} else AManager.log("TelegramBot", "onResponse: Failed with response: " + response);
			}

			@Override
			public void onError(ANError anError) {
				AManager.log("TelegramBot", "onError: CODE[%d] | BODY[%s] | DETAIL[%s] | RESPONSE[%s]", anError.getErrorCode(), anError.getErrorBody(), anError.getErrorDetail(), anError.getResponse());
			}
		});
	}

	public void getChat(long chatId, TelegramRequestCallback callback) {
		new TelegramRequest(token, callback)
				.addPayload(String.format(Locale.ENGLISH, "getChat?chat_id=%d", chatId)).send();
	}

	public void sendTextMessage(TelegramMessage msg, TelegramRequestCallback callback) {
		// Build request URL
		// Send it
		// Notify callbacks
	}

	public void sendPoll(TelegramMessage poll, TelegramRequestCallback callback) {
		// Build request URL
		// Send it
		// Notify callbacks
	}

	public void sendMedia(TelegramMessage media, TelegramRequestCallback callback) {
		// Build request URL
		// Prepare media
		// Send it
		// Notify callbacks
	}

	@Override
	public String toString() {
		return "TelegramBot{" +
		       "token='" + token + '\'' +
		       ", id=" + id +
		       ", firstName='" + firstName + '\'' +
		       ", lastName='" + lastName + '\'' +
		       ", username='" + username + '\'' +
		       ", canJoinGroups=" + canJoinGroups +
		       ", canReadAllGroupMsgs=" + canReadAllGroupMsgs +
		       ", supportsInlineQueries=" + supportsInlineQueries +
		       '}';
	}

	@Override
	public TelegramObject clone() {
		return new TelegramBot(token,
				id,
				firstName,
				lastName,
				username,
				canJoinGroups,
				canReadAllGroupMsgs,
				supportsInlineQueries).setInitialized(isInitialized);
	}

}

/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.integrations.telegram;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;

import com.typ.muslim.db.LocalDatabase;
import com.typ.muslim.integrations.telegram.models.TelegramBot;
import com.typ.muslim.integrations.telegram.models.TelegramRequestResult;
import com.typ.muslim.libs.easyjava.data.EasyList;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 * Contains all code that handles bots and its operations from storing bot info to handle network requests
 */
public class TelegramBotsIntegration {

	// todo: Add API methods like: [updateBot].

	public static EasyList<TelegramBot> getMyBots(Context context, boolean performOnlineRefresh) {
		EasyList<TelegramBot> myBots = new EasyList<>();
		myBots.add(new TelegramBot(TelegramConstants.AM_BOT_TOKEN));
		// Get my bots from local database
		myBots.addAll(TelegramBot.resolveAll(LocalDatabase.getInstance(context).query("SELECT * FROM telegram")));
		// Perform online refresh for every bot if it's requested
		if (performOnlineRefresh) {
			synchronized (myBots) {
				for (TelegramBot bot : myBots) bot.getMe();
			}
		}
		return myBots;
	}

	public static boolean persistBot(Context context, TelegramBot bot) {
		if (bot == null) return false;
		try {
			LocalDatabase.getInstance(context)
			             .execute("INSERT INTO telegram (` token `,` id `,` first_name `,` last_Name `,` username `,` can_join_groups `,` can_read_all_groups_msgs `,` support_inline_queries `) VALUES('" +
			                      bot.token + "','" +
			                      bot.id + "'," +
			                      bot.firstName + "','" +
			                      bot.lastName + "','" +
			                      bot.username + "','" +
			                      bot.canJoinGroups + "','" +
			                      bot.canReadAllGroupMsgs + "','" +
			                      bot.supportsInlineQueries + "')");
			return true;
		} catch (SQLiteException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static TelegramBot getBot(Context context, String botToken) {
		if (TextUtils.isEmpty(botToken)) return TelegramBot.UN_INITIALIZED_BOT;
		// Query db for the bot with given token
		Cursor c = LocalDatabase.getInstance(context).query(String.format(Locale.ENGLISH, "SELECT * FROM telegram where ` token ` = '%s' LIMIT 1", botToken));
		return TelegramBot.resolve(c);
	}

	public static boolean removeBot(Context context, String botToken) {
		if (TextUtils.isEmpty(botToken)) return false;
		try {
			LocalDatabase.getInstance(context)
			             .execute(String.format(Locale.ENGLISH, "DELETE FROM telegram where ` token ` = '%s'", botToken));
			return true;
		} catch (SQLiteException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean updateBot(Context context, TelegramBot updatedBot) {
		// todo: complete coding this method
		if (updatedBot == null) return false;
		try {
			LocalDatabase.getInstance(context)
			             .execute("");
			return true;
		} catch (SQLiteException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static TelegramRequestResult parseResponse(String rawJson) {
		// Do some checks
		if (TextUtils.isEmpty(rawJson)) return new TelegramRequestResult(false, null);
		try {
			return parseResponse(new JSONObject(rawJson));
		} catch (JSONException e) {
			// Error parsing json
			e.printStackTrace();
			return new TelegramRequestResult(false, null);
		}
	}

	public static TelegramRequestResult parseResponse(JSONObject json) {
		// Do some checks
		if (json == null) return new TelegramRequestResult(false, null);
		// Create the JSON object and result map
		final boolean hasSucceed = json.optBoolean("ok", false);
		JSONObject resultJson = json.optJSONObject("result");
		Map<String, Object> resultMap = new HashMap<>();
		// Parse json data and populate it to the map
		if (resultJson != null) {
			Iterator<String> ite = resultJson.keys();
			while (ite.hasNext()) {
				String key = ite.next();
				resultMap.put(key, json.opt(key));
			}
		}
		// Return the map
		return new TelegramRequestResult(hasSucceed, resultMap);
	}

//	static class Persistence extends SQLiteOpenHelper {
//
//		// Column Names
//		public static final String COLUMN_TOKEN = "bot_token";
//		public static final String COLUMN_ID = "id";
//		public static final String COLUMN_IS_BOT = "is_bot";
//		public static final String COLUMN_FIRST_NAME = "first_name";
//		public static final String COLUMN_LAST_NAME = "last_name";
//		public static final String COLUMN_USERNAME = "username";
//		public static final String COLUMN_CAN_JOIN_GROUPS = "can_join_groups";
//		public static final String COLUMN_CAN_READ_ALL_GROUP_MSGS = "can_read_all_group_msgs";
//		public static final String COLUMN_SUPPORT_INLINE_QUERIES = "support_inline_queries";
//
//		// Current Singleton Instance
//		protected static Persistence mInstance;
//
//		/**
//		 * Gets the current working singleton instance or create new one
//		 *
//		 * @return The current singleton instance
//		 */
//		public static Persistence getInstance(Context context) {
//			if (mInstance == null) mInstance = new Persistence(context);
//			return mInstance;
//		}
//
//		public Persistence(@Nullable Context context) {
//			super(context, "ti.db", null, 3);
//		}
//
//		@Override
//		public void onCreate(SQLiteDatabase db) {
//			db.execSQL("CREATE TABLE IF NOT EXISTS bots");
//		}
//
//		@Override
//		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		}
//	}

}

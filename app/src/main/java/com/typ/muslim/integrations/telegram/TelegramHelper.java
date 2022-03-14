/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.integrations.telegram;

import com.typ.muslim.integrations.telegram.models.TelegramChatPermissions;
import com.typ.muslim.integrations.telegram.models.TelegramChatPhoto;

import org.json.JSONObject;

public class TelegramHelper {

	public static TelegramChatPermissions optPermissions(JSONObject json) {
		final TelegramChatPermissions perms = new TelegramChatPermissions();
		if (json == null) return perms;
		else {
            // Add permissions
            perms.setCanSendMessages(json.optBoolean("can_send_messages", false));
            perms.setCanSendMedia(json.optBoolean("can_send_media_messages", false));
            perms.setCanSendPolls(json.optBoolean("can_send_polls", false));
            perms.setCanSendOtherMessages(json.optBoolean("can_send_other_messages", false));
            perms.setCanSendWebPagePreviews(json.optBoolean("can_add_web_page_previews", false));
            perms.setCanChangeInfo(json.optBoolean("can_change_info", false));
            perms.setCanInviteUsers(json.optBoolean("can_invite_users", false));
            perms.setCanPinMessages(json.optBoolean("can_pin_messages", false));
        }
		return perms;
	}

	public static TelegramChatPhoto optChatPhoto(JSONObject json) {
		final TelegramChatPhoto photo = new TelegramChatPhoto();
		if (json == null) return null;
		photo.setSmallPicUrl(json.optString("small_file_id"));
		photo.setSmallPicId(json.optString("small_file_unique_id"));
		photo.setBigPicUrl(json.optString("big_file_id"));
		photo.setBigPicId(json.optString("big_file_unique_id"));
		return photo;
	}

	// todo: Create method to build valid request url with given params

}

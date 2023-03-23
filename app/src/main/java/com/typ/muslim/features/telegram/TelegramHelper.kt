/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */
package com.typ.muslim.features.telegram

import com.typ.muslim.features.telegram.models.TelegramChatPermissions
import com.typ.muslim.features.telegram.models.TelegramChatPhoto
import org.json.JSONObject

object TelegramHelper {
    @JvmStatic
    fun optPermissions(json: JSONObject?): TelegramChatPermissions {
        val perms = TelegramChatPermissions()
        if (json == null) return perms else {
            // Add permissions
            perms.canSendMessages = json.optBoolean("can_send_messages", false)
            perms.canSendMedia = json.optBoolean("can_send_media_messages", false)
            perms.canSendPolls = json.optBoolean("can_send_polls", false)
            perms.canSendOtherMessages = json.optBoolean("can_send_other_messages", false)
            perms.canSendWebPagePreviews = json.optBoolean("can_add_web_page_previews", false)
            perms.canChangeInfo = json.optBoolean("can_change_info", false)
            perms.canInviteUsers = json.optBoolean("can_invite_users", false)
            perms.canPinMessages = json.optBoolean("can_pin_messages", false)
        }
        return perms
    }

    @JvmStatic
    fun optChatPhoto(json: JSONObject?): TelegramChatPhoto? {
        return json?.let {
            TelegramChatPhoto(
                smallPicUrl = json.optString("small_file_id"),
                smallPicId = json.optString("small_file_unique_id"),
                bigPicUrl = json.optString("big_file_id"),
                bigPicId = json.optString("big_file_unique_id")
            )
        }
    } // todo: Create method to build valid request url with given params
}
/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */
package com.typ.muslim.features.telegram.models

import android.database.Cursor
import android.text.TextUtils
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.typ.muslim.features.telegram.TelegramBotsIntegration
import com.typ.muslim.features.telegram.TelegramConstants
import com.typ.muslim.features.telegram.interfaces.TelegramRequestCallback
import com.typ.muslim.managers.AManager
import org.json.JSONObject
import java.util.*

/**
 * Model class that holds TelegramBot info
 */
class TelegramBot(
    /**
     * Bot unique authentication
     */
    val token: String?,
    id: Int,
    firstName: String?,
    lastName: String?,
    username: String?,
    canJoinGroups: Boolean,
    canReadAllGroupMsgs: Boolean,
    supportsInlineQueries: Boolean
) : TelegramUser(id, true, firstName, lastName, username, canJoinGroups, canReadAllGroupMsgs, supportsInlineQueries) {

    /**
     * Indicating whether the bot has its info fetched from (telegram or local) or not.
     */
    var isInitialized: Boolean = false

    constructor(token: String?) : this(token, 0, null, null, null, false, false, false) {
        // Send getMe request
        if (!TextUtils.isEmpty(token)) me
    }

    // Check if the bot token is null or empty
    // Send GET request to Telegram to fetch bot info
    val me: Unit
        get() {
            // Check if the bot token is null or empty
            if (TextUtils.isEmpty(token)) return
            // Send GET request to Telegram to fetch bot info
            isInitialized = false
            AndroidNetworking.get(TelegramConstants.ambBaseUrl + "\\getMe").setPriority(Priority.MEDIUM).build().getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(rawResponse: JSONObject) {
                    AManager.log("TelegramBot", "onResponse: $rawResponse")
                    val response = TelegramBotsIntegration.parseResponse(rawResponse.toString())
                    if (response.hasSucceed()) {
                        id = response.optInt("id", 0)
                        firstName = response.optString("first_name", "?")
                        lastName = response.optString("last_name", "?")
                        username = response.optString("username", "?")
                        canJoinGroups = response.optBoolean("can_join_groups", false)
                        canReadAllGroupMsgs = response.optBoolean("can_read_all_group_messages", false)
                        supportsInlineQueries = response.optBoolean("supports_inline_queries", false)
                        isInitialized = true
                        AManager.log("TelegramBot", "onParseResponse: " + this@TelegramBot.toString())
                    } else AManager.log("TelegramBot", "onResponse: Failed with response: $response")
                }

                override fun onError(anError: ANError) {
                    AManager.log("TelegramBot", "onError: CODE[%d] | BODY[%s] | DETAIL[%s] | RESPONSE[%s]", anError.errorCode, anError.errorBody, anError.errorDetail, anError.response)
                }
            })
        }

    fun getChat(chatId: Long, callback: TelegramRequestCallback?) {
        TelegramRequest(token, callback)
            .addPayload(String.format(Locale.ENGLISH, "getChat?chat_id=%d", chatId)).send()
    }

    fun sendTextMessage(msg: TelegramMessage?, callback: TelegramRequestCallback?) {
        // Build request URL
        // Send it
        // Notify callbacks
    }

    fun sendPoll(poll: TelegramMessage?, callback: TelegramRequestCallback?) {
        // Build request URL
        // Send it
        // Notify callbacks
    }

    fun sendMedia(media: TelegramMessage?, callback: TelegramRequestCallback?) {
        // Build request URL
        // Prepare media
        // Send it
        // Notify callbacks
    }

    override fun toString(): String {
        return "TelegramBot{" +
                "token='" + token + '\'' +
                ", id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", canJoinGroups=" + canJoinGroups +
                ", canReadAllGroupMsgs=" + canReadAllGroupMsgs +
                ", supportsInlineQueries=" + supportsInlineQueries +
                '}'
    }

    override fun clone(): TelegramObject {
        return TelegramBot(
            token,
            id,
            firstName,
            lastName,
            username,
            canJoinGroups,
            canReadAllGroupMsgs,
            supportsInlineQueries
        ).apply { isInitialized = true }
    }

    companion object {
        @JvmField
        val UN_INITIALIZED_BOT = TelegramBot(null)

        @JvmStatic
        fun resolveAll(c: Cursor?): List<TelegramBot> {
            if (c == null) return ArrayList()
            val bots: MutableList<TelegramBot> = ArrayList()
            c.moveToFirst()
            while (!c.isAfterLast) {
                bots.add(resolve(c))
                c.moveToNext()
            }
            return bots
        }

        @JvmStatic
        fun resolve(c: Cursor?): TelegramBot {
            return if (c == null) UN_INITIALIZED_BOT else TelegramBot(
                c.getString(0),
                c.getInt(1),
                c.getString(2),
                c.getString(3),
                c.getString(4), c.getString(5) == "true", c.getString(6) == "true", c.getString(7) == "true"
            )
        }
    }
}
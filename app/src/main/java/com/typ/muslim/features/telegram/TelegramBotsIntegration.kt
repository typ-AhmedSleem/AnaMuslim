/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */
package com.typ.muslim.features.telegram

import android.content.Context
import android.database.sqlite.SQLiteException
import android.text.TextUtils
import com.typ.muslim.db.LocalDatabase.Companion.getInstance
import com.typ.muslim.features.telegram.models.TelegramBot
import com.typ.muslim.features.telegram.models.TelegramBot.Companion.resolve
import com.typ.muslim.features.telegram.models.TelegramBot.Companion.resolveAll
import com.typ.muslim.features.telegram.models.TelegramRequestResult
import com.typ.muslim.libs.easyjava.data.EasyList
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 * Contains all code that handles bots and its operations from storing bot info to handle network requests
 */
object TelegramBotsIntegration {
    // todo: Add API methods like: [updateBot].
    private val block = Any()

    @JvmStatic
    fun getMyBots(context: Context?, performOnlineRefresh: Boolean): EasyList<TelegramBot> {
        val myBots = EasyList<TelegramBot>()
        myBots.add(TelegramBot(TelegramConstants.AM_BOT_TOKEN))
        // Get my bots from local database
        myBots.addAll(resolveAll(getInstance(context!!)!!.query("SELECT * FROM telegram")))
        // Perform online refresh for every bot if it's requested
        if (performOnlineRefresh) {
            synchronized(block) { for (bot in myBots) bot.me }
        }
        return myBots
    }

    fun persistBot(context: Context?, bot: TelegramBot?): Boolean {
        return if (bot == null) false else try {
            getInstance(context!!)?.execute(
                "INSERT INTO telegram (` token `,` id `,` first_name `,` last_Name `,` username `,` can_join_groups `,` can_read_all_groups_msgs `,` support_inline_queries `) VALUES('" +
                        bot.token + "','" +
                        bot.id + "'," +
                        bot.firstName + "','" +
                        bot.lastName + "','" +
                        bot.username + "','" +
                        bot.canJoinGroups + "','" +
                        bot.canReadAllGroupMsgs + "','" +
                        bot.supportsInlineQueries + "')"
            )
            true
        } catch (e: SQLiteException) {
            e.printStackTrace()
            false
        }
    }

    fun getBot(context: Context?, botToken: String?): TelegramBot {
        if (TextUtils.isEmpty(botToken)) return TelegramBot.UN_INITIALIZED_BOT
        // Query db for the bot with given token
        val c = getInstance(context!!)?.query(String.format(Locale.ENGLISH, "SELECT * FROM telegram where ` token ` = '%s' LIMIT 1", botToken)) ?: null
        return resolve(c)
    }

    fun removeBot(context: Context?, botToken: String?): Boolean {
        return if (TextUtils.isEmpty(botToken)) false else try {
            getInstance(context!!)?.execute(String.format(Locale.ENGLISH, "DELETE FROM telegram where ` token ` = '%s'", botToken))
            true
        } catch (e: SQLiteException) {
            e.printStackTrace()
            false
        }
    }

    fun updateBot(context: Context?, updatedBot: TelegramBot?): Boolean {
        // todo: complete coding this method
        return if (updatedBot == null) false else try {
            getInstance(context!!)?.execute("")
            true
        } catch (e: SQLiteException) {
            e.printStackTrace()
            false
        }
    }

    fun parseResponse(rawJson: String?): TelegramRequestResult {
        // Do some checks
        return if (TextUtils.isEmpty(rawJson)) TelegramRequestResult(false, null) else try {
            parseResponse(rawJson?.let { JSONObject(it) })
        } catch (e: JSONException) {
            // Error parsing json
            e.printStackTrace()
            TelegramRequestResult(false, null)
        }
    }

    @JvmStatic
    fun parseResponse(json: JSONObject?): TelegramRequestResult {
        // Do some checks
        if (json == null) return TelegramRequestResult(false, null)
        // Create the JSON object and result map
        val hasSucceed = json.optBoolean("ok", false)
        val resultJson = json.optJSONObject("result")
        val resultMap: MutableMap<String?, Any?> = HashMap()
        // Parse json data and populate it to the map
        if (resultJson != null) {
            val ite = resultJson.keys()
            while (ite.hasNext()) {
                val key = ite.next()
                resultMap[key] = json.opt(key)
            }
        }
        // Return the map
        return TelegramRequestResult(hasSucceed, resultMap)
    }
}
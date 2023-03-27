/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */
package com.typ.muslim.features.telegram.models

import org.json.JSONObject

class TelegramRequestResult(
    private val succeed: Boolean,
    result: Map<String?, Any?>?
) {
    private val result: Map<String, Any>? = null

    fun hasSucceed(): Boolean = succeed

    fun hasResult(): Boolean = result != null && result.isNotEmpty()


    fun optInt(key: String, fallback: Int): Int {
        if (result != null) {
            val value = result[key]
            return if (value != null) value as Int else fallback
        }
        return fallback
    }

    fun optBoolean(key: String, fallback: Boolean): Boolean {
        if (result != null) {
            val value = result[key]
            return if (value != null) value as Boolean else fallback
        }
        return fallback
    }

    fun optString(key: String, fallback: String): String {
        if (result != null) {
            val value = result[key]
            return if (value != null) value as String else fallback
        }
        return fallback
    }

    fun optJSONObject(key: String, fallback: JSONObject): JSONObject {
        if (result != null) {
            val value = result[key]
            return if (value != null) value as JSONObject else fallback
        }
        return fallback
    }

    override fun toString(): String {
        return "TelegramRequestResult{" +
                "hasSucceed=" + succeed +
                ", result=" + result +
                '}'
    }
}
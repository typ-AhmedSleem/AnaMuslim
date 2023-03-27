/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */
package com.typ.muslim.features.telegram.models

import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.typ.muslim.features.telegram.interfaces.TelegramRequestCallback
import com.typ.muslim.managers.AManager
import org.json.JSONObject

class TelegramRequest(// todo: Add more fields for extra payload in the request
    private val botToken: String?,
    private val callback: TelegramRequestCallback?
) {

    private val baseUrl: String = "https://api.telegram.org/bot$botToken/"
    private var requestURL: String = "https://api.telegram.org/bot$botToken/"

    init {
        requestURL = baseUrl
    }

    /**
     * todo: Better code this later
     */
    fun addPayload(payload: String): TelegramRequest {
        requestURL += payload
        return this
    }

    fun resetURL(): TelegramRequest {
        requestURL = baseUrl
        return this
    }

    fun send() {
        AManager.log("TelegramRequest", "send: URL[%s]", requestURL)
        AndroidNetworking.get(requestURL)
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    notifyResponse(response)
                }

                override fun onError(anError: ANError) {
                    notifyFailed(TelegramError(anError.errorCode, anError.errorBody))
                }
            })
    }

    fun notifyResponse(response: JSONObject?) {
        callback?.onResponse(response)
    }

    fun notifyFailed(error: TelegramError?) {
        callback?.onFailed(error)
    }

    fun notifyCancelled(byUser: Boolean) {
        callback?.onCancelled(byUser)
    }

    override fun toString(): String {
        return "TelegramRequest{" +
                "botToken='" + botToken + '\'' +
                "baseURL='" + baseUrl + '\'' +
                "requestURL='" + requestURL + '\'' +
                '}'
    }
}
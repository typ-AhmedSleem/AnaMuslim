/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */
package com.typ.muslim.features.telegram.interfaces

import com.typ.muslim.features.telegram.models.TelegramError
import org.json.JSONObject

abstract class TelegramRequestCallback {

    open fun onFailed(error: TelegramError?) {}
    open fun onCancelled(byUser: Boolean) {}
    open fun onResponse(response: JSONObject?) {}
    
}
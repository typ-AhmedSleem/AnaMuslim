/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */
package com.typ.muslim.features.telegram.models

class TelegramMessage(  // todo: add other types of messages like: Media, Poll, Stickers & ...
    val text: String
) {

    override fun toString(): String {
        return "TelegramMessage{" +
                "text='" + text + '\'' +
                '}'
    }
}
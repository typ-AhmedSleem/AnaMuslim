/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */
package com.typ.muslim.features.telegram.enums

enum class TelegramChatType {

    PRIVATE,
    GROUP,
    SUPERGROUP,
    CHANNEL;

    companion object {
        @JvmStatic
        fun of(type: String): TelegramChatType {
            return if (type.equals("group", ignoreCase = true)) GROUP
            else if (type.equals("supergroup", ignoreCase = true)) SUPERGROUP
            else if (type.equals("channel", ignoreCase = true)) CHANNEL
            else PRIVATE
        }
    }
}
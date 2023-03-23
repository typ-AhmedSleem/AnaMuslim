/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */
package com.typ.muslim.features.telegram.models

import com.typ.muslim.features.telegram.enums.TelegramChatType

class TelegramChannel(
    id: Int,
    photo: TelegramChatPhoto,
    title: String,
    description: String,
    botPermissions: TelegramChatPermissions
) : TelegramChat(
    id.toLong(),
    TelegramChatType.CHANNEL,
    photo,
    title,
    description,
    botPermissions
)

// todo: idk what to do here
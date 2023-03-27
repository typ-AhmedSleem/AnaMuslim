/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */
package com.typ.muslim.features.telegram.models

import com.typ.muslim.features.telegram.enums.TelegramChatType

/**
 * Model class for TelegramChat of type [Group | SuperGroup | Channel]
 */
open class TelegramChat(
    val id: Long,
    val type: TelegramChatType,
    val photo: TelegramChatPhoto,
    val title: String,
    val description: String,
    val botPermissions: TelegramChatPermissions
) : TelegramObject() {

    override fun toString(): String {
        return "TelegramChat{" +
                "id=" + id +
                ", type=" + type +
                ", photo=" + photo +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", botPermissions=" + botPermissions +
                '}'
    }
}
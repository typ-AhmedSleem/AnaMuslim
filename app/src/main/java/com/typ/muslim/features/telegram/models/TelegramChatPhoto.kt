/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */
package com.typ.muslim.features.telegram.models

class TelegramChatPhoto(
    val smallPicUrl: String? = null,
    val smallPicId: String? = null,
    val bigPicUrl: String? = null,
    val bigPicId: String? = null
) {

    override fun toString(): String {
        return "TelegramChatPhoto{" +
                "smallPicUrl='" + smallPicUrl + '\'' +
                ", smallPicId='" + smallPicId + '\'' +
                ", bigPicUrl='" + bigPicUrl + '\'' +
                ", bigPicId='" + bigPicId + '\'' +
                '}'
    }
}
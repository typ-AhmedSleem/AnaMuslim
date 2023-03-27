/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */
package com.typ.muslim.features.telegram.models

class TelegramError(
    val code: Int,
    val msg: String
) {

    override fun toString(): String {
        return "TelegramError{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}'
    }
}
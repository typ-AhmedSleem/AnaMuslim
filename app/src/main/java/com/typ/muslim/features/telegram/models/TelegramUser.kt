/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */
package com.typ.muslim.features.telegram.models

open class TelegramUser(
    var id: Int,
    var isBot: Boolean,
    var firstName: String?,
    var lastName: String?,
    var username: String?,
    var canJoinGroups: Boolean,
    var canReadAllGroupMsgs: Boolean,
    var supportsInlineQueries: Boolean
) : TelegramObject() {

    override fun toString(): String {
        return "TelegramUser{" +
                "id=" + id +
                ", isBot=" + isBot +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", canJoinGroups=" + canJoinGroups +
                ", canReadAllGroupMsgs=" + canReadAllGroupMsgs +
                ", supportsInlineQueries=" + supportsInlineQueries +
                '}'
    }
}
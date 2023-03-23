/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */
package com.typ.muslim.features.telegram.models

class TelegramChatPermissions(
    var canSendMessages: Boolean = false,
    var canSendMedia: Boolean= false,
    var canSendPolls: Boolean= false,
    var canSendOtherMessages: Boolean= false,
    var canSendWebPagePreviews: Boolean= false,
    var canChangeInfo: Boolean= false,
    var canInviteUsers: Boolean= false,
    var canPinMessages: Boolean= false
) : TelegramObject() {

    override fun toString(): String {
        return "TelegramChatPermission{" +
                "canSendMessages=" + canSendMessages +
                ", canSendMedia=" + canSendMedia +
                ", canSendPolls=" + canSendPolls +
                ", canSendOtherMessages=" + canSendOtherMessages +
                ", canSendWebPagePreviews=" + canSendWebPagePreviews +
                ", canChangeInfo=" + canChangeInfo +
                ", canInviteUsers=" + canInviteUsers +
                ", canPinMessages=" + canPinMessages +
                '}'
    }
}
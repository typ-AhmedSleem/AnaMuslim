/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.integrations.telegram.models;

import com.typ.muslim.integrations.telegram.enums.TelegramChatType;

import java.util.List;

public class TelegramChannel extends TelegramChat{

	public TelegramChannel(int id, TelegramChatPhoto photo, String title, String description, TelegramChatPermissions botPermissions) {
		super(id, TelegramChatType.CHANNEL, photo, title, description, botPermissions);
	}
	// todo: idk what to do here
}

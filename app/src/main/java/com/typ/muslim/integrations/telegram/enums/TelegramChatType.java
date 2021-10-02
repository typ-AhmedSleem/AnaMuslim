/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.integrations.telegram.enums;

public enum TelegramChatType {
	PRIVATE,
	GROUP,
	SUPERGROUP,
	CHANNEL;

	public static TelegramChatType of(String type) {
		if (type.equalsIgnoreCase("group")) return GROUP;
		else if (type.equalsIgnoreCase("supergroup")) return SUPERGROUP;
		else if (type.equalsIgnoreCase("channel")) return CHANNEL;
		else return PRIVATE;
	}

}

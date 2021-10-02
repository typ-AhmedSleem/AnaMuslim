/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.integrations.telegram.models;

public class TelegramMessage {

	// todo: add other types of messages like: Media, Poll, Stickers & ...

	private final String text;

	public TelegramMessage(String text) {this.text = text;}

	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		return "TelegramMessage{" +
		       "text='" + text + '\'' +
		       '}';
	}
}

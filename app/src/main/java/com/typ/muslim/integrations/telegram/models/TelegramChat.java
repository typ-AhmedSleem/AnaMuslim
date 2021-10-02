/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.integrations.telegram.models;

import com.typ.muslim.integrations.telegram.enums.TelegramChatType;

import java.util.List;

/**
 * Model class for TelegramChat of type [Group | SuperGroup | Channel]
 */
public class TelegramChat extends TelegramObject {

	private final long id;
	private final TelegramChatType type;
	private final TelegramChatPhoto photo;
	private final String title;
	private final String description;
	private final TelegramChatPermissions botPermissions;

	public TelegramChat(long id, TelegramChatType type, TelegramChatPhoto photo, String title, String description, TelegramChatPermissions botPermissions) {
		this.id = id;
		this.type = type;
		this.photo = photo;
		this.title = title;
		this.description = description;
		this.botPermissions = botPermissions;
	}

	public long getId() {
		return id;
	}

	public TelegramChatType getType() {
		return type;
	}

	public TelegramChatPhoto getPhoto() {
		return photo;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public TelegramChatPermissions getBotPermissions() {
		return botPermissions;
	}

	@Override
	public String toString() {
		return "TelegramChat{" +
		       "id=" + id +
		       ", type=" + type +
		       ", photo=" + photo +
		       ", title='" + title + '\'' +
		       ", description='" + description + '\'' +
		       ", botPermissions=" + botPermissions +
		       '}';
	}
}

/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.integrations.telegram.models;

public class TelegramChatPhoto {

	private String smallPicUrl;
	private String smallPicId;
	private String bigPicUrl;
	private String bigPicId;

	public TelegramChatPhoto() {
	}

	public TelegramChatPhoto(String smallPicUrl, String smallPicId, String bigPicUrl, String bigPicId) {
		this.smallPicUrl = smallPicUrl;
		this.smallPicId = smallPicId;
		this.bigPicUrl = bigPicUrl;
		this.bigPicId = bigPicId;
	}

	public String getSmallPicUrl() {
		return smallPicUrl;
	}

	public String getSmallPicId() {
		return smallPicId;
	}

	public String getBigPicUrl() {
		return bigPicUrl;
	}

	public String getBigPicId() {
		return bigPicId;
	}

	public TelegramChatPhoto setSmallPicUrl(String smallPicUrl) {
		this.smallPicUrl = smallPicUrl;
		return this;
	}

	public TelegramChatPhoto setSmallPicId(String smallPicId) {
		this.smallPicId = smallPicId;
		return this;
	}

	public TelegramChatPhoto setBigPicUrl(String bigPicUrl) {
		this.bigPicUrl = bigPicUrl;
		return this;
	}

	public TelegramChatPhoto setBigPicId(String bigPicId) {
		this.bigPicId = bigPicId;
		return this;
	}

	@Override
	public String toString() {
		return "TelegramChatPhoto{" +
		       "smallPicUrl='" + smallPicUrl + '\'' +
		       ", smallPicId='" + smallPicId + '\'' +
		       ", bigPicUrl='" + bigPicUrl + '\'' +
		       ", bigPicId='" + bigPicId + '\'' +
		       '}';
	}
}

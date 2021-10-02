/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.integrations.telegram.models;

public class TelegramError {

	private final int code;
	private final String msg;

	public TelegramError(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	@Override
	public String toString() {
		return "TelegramError{" +
		       "code=" + code +
		       ", msg='" + msg + '\'' +
		       '}';
	}

}

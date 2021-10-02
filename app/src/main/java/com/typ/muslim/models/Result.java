/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.models;

public class Result<T> {

	private final boolean succeed;
	private final T data;

	public Result(boolean succeed, T data) {
		this.succeed = succeed;
		this.data = data;
	}

	public boolean hasSucceed() {
		return succeed;
	}

	public T getData() {
		return data;
	}
}

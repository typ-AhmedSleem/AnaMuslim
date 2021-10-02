/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.models;

import java.io.Serializable;

public class TasbeehModel implements Serializable {

	private final int id;
	private int times;
	private final String content;

	public TasbeehModel(int id, int times, String content) {
		this.id = id;
		this.times = times;
		this.content = content;
	}

	public int getId() {
		return id;
	}

	public int howManyTimes() {
		return times;
	}

	public TasbeehModel setHowMany(int howMany) {
		this.times = howMany;
		return this;
	}

	public String getContent() {
		return content;
	}

}

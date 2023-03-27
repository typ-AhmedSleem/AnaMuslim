/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.features.ramadan.models;

import com.typ.muslim.models.Timestamp;

public class RamadanDay {

	// todo: Add getKhatmaStep method to return step to read today

	private final int number;
	private final Timestamp suhurIn;
	private final Timestamp iftarIn;

	public RamadanDay(int number, Timestamp suhurIn, Timestamp iftarIn) {
		this.number = number;
		this.suhurIn = suhurIn;
		this.iftarIn = iftarIn;
	}

	public int getNum() {
		return number;
	}

	public Timestamp getSuhurTime() {
		return suhurIn;
	}

	public Timestamp getIftarTime() {
		return iftarIn;
	}

	@Override
	public String toString() {
		return "RamadanDay{" +
		       "number=" + number +
		       ", suhurIn=" + suhurIn +
		       ", iftarIn=" + iftarIn +
		       '}';
	}
}

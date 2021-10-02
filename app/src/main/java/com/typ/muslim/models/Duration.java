/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.models;

import com.typ.muslim.managers.AManager;

public class Duration {

	private int seconds;
	private int minutes;
	private int hours;

	public Duration(long millis) {
		if (millis >= 1000) {
			this.seconds = (int) millis / 1000; // 7400
			this.minutes = this.seconds / 60; // 123
			this.seconds -= this.minutes * 60; // 20
			this.hours = this.minutes / 60; // 2
			this.minutes -= this.hours * 60; // 3
		} else this.seconds = 0;
		AManager.log("Duration", "H[%d] M[%d] S[%d]", hours, this.minutes, seconds);
	}

	public Duration(int minutes) {
		if (minutes >= 60) {
			this.hours = minutes / 60;
			this.minutes = minutes - this.hours * 60;
			this.seconds = (minutes * 60) - (this.hours * 3600 + this.minutes * 60);
		} else this.minutes = minutes;
		AManager.log("Duration", "H[%d] M[%d] S[%d] | RawM[%d]", hours, this.minutes, seconds, minutes);
	}

	public Duration(int seconds, int minutes, int hours) {
		this.seconds = seconds;
		this.minutes = minutes;
		this.hours = hours;
	}

	public int getSeconds() {
		return seconds;
	}

	public int getMinutes() {
		return minutes;
	}

	public int getHours() {
		return hours;
	}
}

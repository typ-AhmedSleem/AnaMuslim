/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.models;

import androidx.annotation.NonNull;

import com.typ.muslim.core.praytime.enums.Prays;
import com.typ.muslim.libs.easyjava.data.EasyList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PrayTimes implements Serializable {

	// PrayTimes
	private final List<Pray> prays;

	public PrayTimes(@NonNull List<Pray> prays) {
		this.prays = prays;
	}

	public PrayTimes(@NonNull Pray fajr,
	                 @NonNull Pray sunrise,
	                 @NonNull Pray dhuhr,
	                 @NonNull Pray asr,
	                 @NonNull Pray maghrib,
	                 @NonNull Pray isha) {
		this.prays = new ArrayList<>();
		this.prays.add(fajr);
		this.prays.add(sunrise);
		this.prays.add(dhuhr);
		this.prays.add(asr);
		this.prays.add(maghrib);
		this.prays.add(isha);
	}

	// Builder to easily use the class
	public static Builder newBuilder() {
		return new Builder();
	}

	@NonNull
	public Pray getFajr() {
		return prays.get(0);
	}

	@NonNull
	public Pray getSunrise() {
		return prays.get(1);
	}

	@NonNull
	public Pray getDhuhr() {
		return prays.get(2);
	}

	@NonNull
	public Pray getAsr() {
		return prays.get(3);
	}

	@NonNull
	public Pray getMaghrib() {
		return prays.get(4);
	}

	@NonNull
	public Pray getIsha() {
		return prays.get(5);
	}

	public List<Pray> asList() {
		return prays;
	}

	public EasyList<Pray> asEasyList() {
		return EasyList.createList(prays);
	}

	public Pray get(int index) {
		return this.prays.get(index);
	}

	public void remove(Prays whichPray) {
		prays.remove(whichPray.ordinal());
	}

	@Override
	public String toString() {
		return "PrayTimes{" +
		       "fajr=" + getFajr() +
		       "\nsunrise=" + getSunrise() +
		       "\ndhuhr=" + getDhuhr() +
		       "\nasr=" + getAsr() +
		       "\nmaghrib=" + getMaghrib() +
		       "\nisha=" + getIsha() +
		       '}';
	}

	public static class Builder {

		private Pray fajr;
		private Pray sunrise;
		private Pray dhuhr;
		private Pray asr;
		private Pray maghrib;
		private Pray isha;

		Builder() {}

		public Builder setFajr(Pray fajr) {
			this.fajr = fajr;
			return this;
		}

		public Builder setSunrise(Pray sunrise) {
			this.sunrise = sunrise;
			return this;
		}

		public Builder setDhuhr(Pray dhuhr) {
			this.dhuhr = dhuhr;
			return this;
		}

		public Builder setAsr(Pray asr) {
			this.asr = asr;
			return this;
		}

		public Builder setMaghrib(Pray maghrib) {
			this.maghrib = maghrib;
			return this;
		}

		public Builder setIsha(Pray isha) {
			this.isha = isha;
			return this;
		}

		public void add(Pray pray) {
			if (pray == null) return;
			if (pray.getType() == Prays.FAJR) setFajr(pray);
			else if (pray.getType() == Prays.SUNRISE) setSunrise(pray);
			else if (pray.getType() == Prays.DHUHR) setDhuhr(pray);
			else if (pray.getType() == Prays.ASR) setAsr(pray);
			else if (pray.getType() == Prays.MAGHRIB) setMaghrib(pray);
			else if (pray.getType() == Prays.ISHA) setIsha(pray);
		}

		public PrayTimes build() {
			return new PrayTimes(fajr, sunrise, dhuhr, asr, maghrib, isha);
		}

	}

}

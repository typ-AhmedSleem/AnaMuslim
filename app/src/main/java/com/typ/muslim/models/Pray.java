package com.typ.muslim.models;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import com.typ.muslim.app.AnaMuslimApp;
import com.typ.muslim.features.prays.enums.Prays;
import com.typ.muslim.enums.FormatPatterns;
import com.typ.muslim.managers.AManager;
import com.typ.muslim.managers.ResMan;

import java.io.Serializable;

/**
 * Model class of Pray which holds all Pray Item data
 */
public class Pray implements Serializable {

	/**
	 * Used only to indicate what this pray is by enum ordinal and name
	 */
	private final Prays type;
	/**
	 * PrayName returned from string resources
	 */
	private final String name;

	/**
	 * Timestamp of this pray time
	 */
	private final Timestamp in;

	public Pray(Prays type, String name, Timestamp in) {
		this.type = type;
		this.name = TextUtils.isEmpty(name) ? this.type.name() : name;
		this.in = in;
	}

	public Pray(Prays type, String name, long in) {
		this(type, name, new Timestamp(in));
	}

	public Prays getType() {
		return type;
	}

	public String getName() {
		// fixme: use context from method parameter not the global context
		return ResMan.getString(AnaMuslimApp.getContext().get(), getPrayNameRes());
	}

	@StringRes
	public int getPrayNameRes() {
		return type.getPrayNameRes();
	}

	@ColorRes
	public int getSurfaceColorRes() {
		return type.getSurfaceColorRes();
	}

	@DrawableRes
	public int getPrayIconRes() {
		return type.getIconRes();
	}

	@ColorRes
	public int getOnSurfaceColorRes() {
		return type.getOnSurfaceColorRes();
	}

	public Timestamp getIn() {
		return in;
	}

	public String getFormattedTime(Context context) {
		return AManager.getSelectedTimeFormat(context).format(this.in);
	}

	public boolean hasPassed() {
		return System.currentTimeMillis() > in.toMillis();
	}

	@Override
	public boolean equals(Object another) {
		if (another == null) return false;
		if (this == another) return true;
		if (!(another instanceof Pray)) return false;
		Pray pray = (Pray) another;
		return type == pray.getType() &&
		       in.matches(((Pray) another).in);
	}

	@Override
	public int hashCode() {
		return type.hashCode();
	}

	@Override
	public String toString() {
		return "Pray{" +
		       "ordinal=" + type +
		       ", name='" + name + '\'' +
		       ", in=" + in.getFormatted(FormatPatterns.DATETIME_FULL) +
		       ", hasPassed=" + hasPassed() +
		       '}';
	}
}

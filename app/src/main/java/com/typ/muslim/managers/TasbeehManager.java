/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.managers;

import android.content.Context;

import com.typ.muslim.enums.TasbeehatTimesTarget;
import com.typ.muslim.models.TasbeehModel;

import java.util.ArrayList;
import java.util.List;

public class TasbeehManager {

	public static TasbeehatTimesTarget howManyTimesForEachTasbeeh(Context context) {
		return TasbeehatTimesTarget.ofTimes(PrefManager.get(context, "howManyTimesForEachTasbeeh", TasbeehatTimesTarget.TIMES_33.howMany()));
	}

	public static void setTotalTimesForEachTasbeeh(Context context, int howMany) {
		PrefManager.set(context, "howManyTimesForEachTasbeeh", howMany);
	}

	public static List<TasbeehModel> getDailyTasbeehat(Context context, TasbeehatTimesTarget howManyTimes) {
		// todo: Get localized tasbeehat from external database
		if (howManyTimes.howMany() < 33 && howManyTimes.howMany() != -1) howManyTimes = TasbeehatTimesTarget.TIMES_33; // Use default
		List<TasbeehModel> tasbeehat = new ArrayList<>();
		tasbeehat.add(new TasbeehModel(1, howManyTimes.howMany(), "سبحان الله"));
		tasbeehat.add(new TasbeehModel(2, howManyTimes.howMany(), "الحمد لله"));
		tasbeehat.add(new TasbeehModel(3, howManyTimes.howMany(), "الله اكبر"));
		tasbeehat.add(new TasbeehModel(4, 1, "لا اله الا الله"));
		return tasbeehat;
	}
}

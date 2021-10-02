/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim;

import androidx.annotation.NonNull;

public final class Consumers {

	public static void doIf(@NonNull Runnable what, boolean when) {
		if (when) what.run();
	}

	public static void doIfNot(@NonNull Runnable what, boolean when) {
		if (!when) what.run();
	}

	public static void doWhen(boolean when, @NonNull Runnable whatIfTrue, @NonNull Runnable whatIfFalse) {
		if (when) whatIfTrue.run();
		else whatIfFalse.run();
	}

	public static <RT> RT returnWhen(boolean when, RT whatTrue, RT whatFalse) {
		return when ? whatTrue : whatFalse;
	}

}

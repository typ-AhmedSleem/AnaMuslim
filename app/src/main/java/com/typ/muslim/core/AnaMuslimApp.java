/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */

package com.typ.muslim.core;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

import java.lang.ref.WeakReference;

public class AnaMuslimApp extends MultiDexApplication {

	// Singleton instances
	private static WeakReference<Context> mContext;

	public static WeakReference<Context> getContext() {
		return mContext;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		synchronized (this) {
			if (mContext == null) {
				mContext = new WeakReference<>(getApplicationContext());
			}
		}
	}
}

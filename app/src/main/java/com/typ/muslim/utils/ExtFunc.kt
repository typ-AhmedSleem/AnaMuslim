package com.typ.muslim.utils

import android.content.Context
import com.typ.muslim.managers.LocaleManager

fun Int.toLocaleString(ctx: Context) = "%d".format(LocaleManager.getCurrLocale(ctx), this)
fun Long.toLocaleString(ctx: Context) = "%d".format(LocaleManager.getCurrLocale(ctx), this)
fun Float.toLocaleString(ctx: Context) = "%f".format(LocaleManager.getCurrLocale(ctx), this)

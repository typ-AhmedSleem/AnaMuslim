package com.typ.muslim.utils

import android.content.Context
import android.content.res.ColorStateList
import android.widget.Toast
import androidx.annotation.ArrayRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.typ.muslim.app.AnaMuslimApp
import com.typ.muslim.core.HelperMethod
import com.typ.muslim.managers.LocaleManager

@HelperMethod
fun Int.toLocaleString(ctx: Context = AnaMuslimApp.getContext().get()!!) = "%d".format(LocaleManager.getCurrLocale(ctx), this)

@HelperMethod
fun Long.toLocaleString(ctx: Context = AnaMuslimApp.getContext().get()!!) = "%d".format(LocaleManager.getCurrLocale(ctx), this)

@HelperMethod
fun Float.toLocaleString(ctx: Context = AnaMuslimApp.getContext().get()!!) = "%f".format(LocaleManager.getCurrLocale(ctx), this)

@HelperMethod
fun sp2px(ctx: Context = AnaMuslimApp.getContext().get()!!, @Dimension sp: Float) = DisplayUtils.sp2px(ctx, sp)

@HelperMethod
@Dimension
fun px2sp(ctx: Context = AnaMuslimApp.getContext().get()!!, px: Float) = DisplayUtils.px2sp(ctx, px)

@HelperMethod
fun dp2px(ctx: Context = AnaMuslimApp.getContext().get()!!, @Dimension dp: Float) = DisplayUtils.dp2px(ctx, dp)

@HelperMethod
@Dimension
fun px2dp(ctx: Context = AnaMuslimApp.getContext().get()!!, px: Float) = DisplayUtils.px2dp(ctx, px)

/* Kotlin functions to access different values within resources */

@HelperMethod
fun stringRes(ctx: Context = AnaMuslimApp.getContext().get()!!, @StringRes id: Int) = ctx.resources.getString(id)

@HelperMethod
fun stringRes(ctx: Context = AnaMuslimApp.getContext().get()!!, @StringRes id: Int, vararg formatArgs: Any): String {
    return ctx.resources.getString(id, formatArgs)
}

@HelperMethod
fun pluralStringRes(ctx: Context = AnaMuslimApp.getContext().get()!!, @PluralsRes id: Int, count: Int) = ctx.resources.getQuantityString(id, count)

@HelperMethod
@ColorInt
fun colorRes(ctx: Context = AnaMuslimApp.getContext().get()!!, @ColorRes id: Int) = ContextCompat.getColor(ctx, id)

@HelperMethod
fun fontRes(ctx: Context = AnaMuslimApp.getContext().get()!!, @FontRes id: Int) = ResourcesCompat.getFont(ctx, id)!!

@HelperMethod
fun stringArrayRes(ctx: Context = AnaMuslimApp.getContext().get()!!, @ArrayRes id: Int) = ctx.resources.getStringArray(id)

@HelperMethod
fun intArrayRes(ctx: Context = AnaMuslimApp.getContext().get()!!, @ArrayRes id: Int) = ctx.resources.getIntArray(id)

@HelperMethod
fun drawableRes(ctx: Context = AnaMuslimApp.getContext().get()!!, @DrawableRes id: Int) = ResourcesCompat.getDrawable(ctx.resources, id, null)

@HelperMethod
fun colorStateList(color: Int) = ColorStateList.valueOf(color)

@HelperMethod
fun colorStateList(ctx: Context, id: Int) = ColorStateList.valueOf(colorRes(ctx, id))

@HelperMethod
fun todo(ctx: Context, msg: String) {
    Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
}

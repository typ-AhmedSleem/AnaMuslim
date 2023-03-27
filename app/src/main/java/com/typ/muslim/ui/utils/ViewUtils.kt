package com.typ.muslim.ui.utils

import android.content.Context
import com.typ.muslim.managers.ResMan
import com.typ.muslim.utils.DisplayUtils

object ViewUtils {

    fun Int.toColor(context: Context): Int = ResMan.getColor(context, this)

    fun Float.dp(context: Context): Int = DisplayUtils.dp2px(context, this)

    fun Float.sp(context: Context): Int = DisplayUtils.sp2px(context, this)

}
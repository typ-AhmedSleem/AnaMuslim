/*
 * This app is developed by AHMED SLEEM
 *
 * Copyright (c) 2021.  TYP INC. All Rights Reserved
 */
package com.typ.muslim.profile

import androidx.annotation.DrawableRes
import com.typ.muslim.R

object ProfileHelper {
    /**
     * @return The right default profile avatar photo caring of user gender
     */
    @JvmStatic
    @DrawableRes
    fun getDefaultProfileAvatar(isUserMale: Boolean): Int {
        return if (isUserMale) R.drawable.bg_header1 else R.drawable.bg_header2
    }
}
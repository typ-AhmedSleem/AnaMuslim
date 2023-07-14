package com.typ.muslim.managers

import android.content.Context
import android.text.TextUtils
import java.util.Locale

object LocaleManager {

    // Keys
    private const val KEY_CURR_LOCALE = "currentLocale"

    object Locales {
        // Predefined locales that app supports till now
        val ARABIC = Locale("ar")
        val ENGLISH: Locale = Locale.ENGLISH
    }

    @JvmStatic
    fun isArabic(context: Context) = getCurrLocale(context).language == Locales.ARABIC.language

    @JvmStatic
    fun getCurrLocale(context: Context?): Locale {
        return if (context == null) Locale.getDefault()
        else {
            val code = PrefManager.get(context, KEY_CURR_LOCALE, "ar")
            if (TextUtils.isEmpty(code)) Locale.getDefault() else Locale(code)
        }
    }

    @JvmStatic
    fun setCurrentLocale(context: Context?, langCode: String?) {
        if (TextUtils.isEmpty(langCode)) {
            PrefManager.remove(context, KEY_CURR_LOCALE)
            return
        }
        PrefManager.set(context, KEY_CURR_LOCALE, langCode)
    }

}
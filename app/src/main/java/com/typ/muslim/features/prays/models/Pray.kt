package com.typ.muslim.features.prays.models

import android.content.Context
import androidx.annotation.StringRes
import com.typ.muslim.app.AnaMuslimApp
import com.typ.muslim.enums.FormatPattern
import com.typ.muslim.features.prays.enums.PrayType
import com.typ.muslim.managers.LocaleManager
import com.typ.muslim.managers.ResMan
import com.typ.muslim.models.Timestamp
import java.io.Serializable
import java.util.Locale

/**
 * Model class of Pray which holds all Pray Item data
 */
class Pray(
    /**
     * Used only to indicate what this pray is by enum ordinal and name
     */
    @JvmField val type: PrayType,
    /**
     * Localized string name of this pray
     */
    @JvmField val name: String,
    /**
     * Timestamp of this pray time
     */
    @JvmField val time: Timestamp
) : Serializable {

    constructor(type: PrayType, name: String, timestamp: Long) : this(type, name, Timestamp(timestamp))

    fun getName() = getName(AnaMuslimApp.getContext().get()!!)

    fun getName(ctx: Context) = ResMan.getString(ctx, prayNameRes)

    @get:StringRes
    val prayNameRes: Int
        get() = type.prayNameRes

    @get:StringRes
    val prayDefRes: Int
        get() = type.prayDefRes

    val passed: Boolean
        get() = System.currentTimeMillis() > time.toMillis()

    fun getFormattedTime(ctx: Context) = getFormattedTime(ctx, LocaleManager.getCurrLocale(ctx))

    fun getFormattedTime(ctx: Context, lcl: Locale) = FormatPattern.getDefault(ctx  ).format(time, lcl)

    fun getFormattedTime(ctx: Context, format: FormatPattern) = getFormattedTime(format, LocaleManager.getCurrLocale(ctx))
    fun getFormattedTime(formatter: FormatPattern, lcl: Locale) = formatter.format(time, lcl)

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (this === other) return true
        return if (other !is Pray) false else type === other.type && time.matches(other.time)
    }

    override fun hashCode(): Int {
        return type.hashCode()
    }

    override fun toString(): String {
        return "Pray{" +
                "ordinal=" + type +
                ", name='" + name + '\'' +
                ", in=" + time.getFormatted(FormatPattern.DATETIME_FULL) +
                ", hasPassed=" + passed +
                '}'
    }
}

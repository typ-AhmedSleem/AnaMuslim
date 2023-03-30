package com.typ.muslim.features.khatma.models

import com.typ.muslim.features.quran.models.QuranAyah
import com.typ.muslim.interfaces.Exportable

class KhatmaWerd(
    val start: QuranAyah,
    val end: QuranAyah
) : java.io.Serializable, Exportable<String> {

    constructor(fromAyah: Int, fromSurah: Int, toAyah: Int, toSurah: Int) : this(
        QuranAyah(fromAyah, fromSurah, ""),
        QuranAyah(toAyah, toSurah, "")
    )

    /**
     * @return JSON-like
     */
    override fun export(): String = "${start.export()}:${end.export()}"

    override fun toString(): String {
        return "KhatmaWerd{" +
                "start=" + start +
                ", end=" + end +
                '}'
    }
}
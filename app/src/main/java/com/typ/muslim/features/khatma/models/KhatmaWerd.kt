package com.typ.muslim.features.khatma.models

import com.typ.muslim.features.quran.models.QuranAyah

class KhatmaWerd(
    val start: QuranAyah,
    val end: QuranAyah
) : java.io.Serializable {

    constructor(fromAyah: Int, fromSurah: Int, toAyah: Int, toSurah: Int) : this(
        QuranAyah(fromAyah, fromSurah, ""),
        QuranAyah(toAyah, toSurah, "")
    )

    override fun toString(): String {
        return "KhatmaWerd{" +
                "start=" + start +
                ", end=" + end +
                '}'
    }
}
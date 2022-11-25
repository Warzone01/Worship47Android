package com.kirdevelopment.worship47andorid2.models

data class SongParams(
    var categories: ArrayList<String> = ArrayList(),
    var chords: ArrayList<String> = ArrayList(),
    var isTranslated: Boolean = false,
    var level: ArrayList<String> = ArrayList(),
    val isNeedSortForAlphabet: Boolean = true
)
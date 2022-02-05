package com.kirdevelopment.worship47andorid2.models

data class Result(
    val author: String? = null,
    val category: List<Category> = emptyList(),
    val chordKey1: String? = null,
    val chordKey2: String? = null,
    val chords: String? = null,
    val chordsFile1: String? = null,
    val chordsFile2: Any? = null,
    val created: String? = null,
    val difficult: String? = null,
    val id: Int = 0,
    val is_translated: Boolean = false,
    val main_key: String? = null,
    val modified: String? = null,
    val presentation: String? = null,
    val text: String = "",
    val text_eng: String? = null,
    val text_file: String? = null,
    val title: String = "",
    val title_eng: String? = null,
    val translator: List<Translator> = emptyList(),
    val user: Any? = null,
    val ytb_id1: String? = null,
    val ytb_id2: String? = null,
    val ytb_id3: String? = null,
    var songExpanded: Boolean = false
)
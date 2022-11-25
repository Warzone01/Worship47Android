package com.kirdevelopment.worship47andorid2.filterCore

import com.kirdevelopment.worship47andorid2.models.SongParams
import com.kirdevelopment.worship47andorid2.utils.Constants.ALL_SONGS
import com.kirdevelopment.worship47andorid2.models.Result as res

/**
 * Основная логика фильтрации песен
 * Фильтрует основной список песен, который приходит из модели и передаётся в list,
 * который передаётся непосредственно в ресайклер
 */
object FilterCore {

    private val regex = "[^\\p{L}\\p{N}]+".toRegex()

    // основная функция соритровки
    fun filterByList(
        songListForSort: ArrayList<res>,
        songListForAdd: ArrayList<res>,
        params: SongParams?,
        category: String
    ) {

        // фильтрует основной список песен
        songListForAdd.addAll(
            songListForSort
                .filter {
                    if (!params?.level.isNullOrEmpty()) params?.level?.contains(it.difficult)
                        ?: true else true
                }
                .filter { if (params?.isTranslated == true) !it.text_eng.isNullOrEmpty() else true }
                .filter {
                    if (!params?.chords.isNullOrEmpty()) params?.chords?.contains(it.chordKey1)
                        ?: true else true
                }
                .filter {
                    if (category != ALL_SONGS) {
                        it.category.map { categ -> categ.title }.contains(category)
                    } else true
                }
                .sortedBy { it.title }
        )
    }

    // основная функция соритровки для поиска
    fun filterByListToSearch(
        songListForSort: ArrayList<res>,
        songListForAdd: ArrayList<res>,
        params: SongParams?,
        category: String,
        text: String
    ) {
        val replacedText = text.replace(regex, "")

        // фильтрует основной список песен
        songListForAdd.addAll(
            songListForSort
                .filter {
                    if (!params?.level.isNullOrEmpty()) params?.level?.contains(it.difficult)
                        ?: true else true
                }
                .filter { if (params?.isTranslated == true) !it.text_eng.isNullOrEmpty() else true }
                .filter {
                    if (!params?.chords.isNullOrEmpty()) params?.chords?.contains(it.chordKey1)
                        ?: true else true
                }
                .filter {
                    if (category != ALL_SONGS) {
                        it.category.map { categ -> categ.title }.contains(category)
                    } else true
                }
                .filter {
                    (it.title + it.title_eng + it.text + it.text_eng).lowercase()
                        .replace(regex, "")
                        .contains(replacedText.lowercase())
                }
                .sortedBy { it.title }
        )
    }
}
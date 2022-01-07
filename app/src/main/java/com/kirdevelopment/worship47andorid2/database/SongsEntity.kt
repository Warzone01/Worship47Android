package com.kirdevelopment.worship47andorid2.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.io.Serializable

@Entity(tableName = "songs")
data class SongsEntity(
    @PrimaryKey var songId: Int?,
    @ColumnInfo(name = "song_name_ru") var songNameRu: String,
    @ColumnInfo(name = "song_name_eng") var songNameEng: String,
    @ColumnInfo(name = "song_text_ru") var songTextRu: String,
    @ColumnInfo(name = "song_text_eng") var songTextEng: String,
    @ColumnInfo(name = "song_chords") var songChords: String,
    @ColumnInfo(name = "author") var author: String,
    @ColumnInfo(name = "difficult") var difficult: String,
    @ColumnInfo(name = "presentation_download_link") var presentation: String,
    @ColumnInfo(name = "text_file_link") var textFileLink: String,
    @ColumnInfo(name = "chords_files_links") var chordsFilesLinks: List<String>,
    @ColumnInfo(name = "song_keys") var songKeys: List<String>,
    @ColumnInfo(name = "song_video_ids") var songVideoIds: List<String>,
    @ColumnInfo(name = "song_category_slugs") var songCategorySlug: List<String>,
    @ColumnInfo(name = "song_translators") var songTranslators: List<String>,
    var songExpanded: Boolean
) : Serializable {
    constructor() : this(
        null,
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        emptyList(),
        emptyList(),
        emptyList(),
        emptyList(),
        emptyList(),
        false
    )
}
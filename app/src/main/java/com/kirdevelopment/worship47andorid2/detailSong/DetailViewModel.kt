package com.kirdevelopment.worship47andorid2.detailSong

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kirdevelopment.worship47andorid2.database.SongDatabase
import com.kirdevelopment.worship47andorid2.database.SongsEntity
import com.kirdevelopment.worship47andorid2.database.StringConverter
import com.kirdevelopment.worship47andorid2.models.Result
import kotlinx.coroutines.*

class DetailViewModel: ViewModel() {

    val songId: MutableLiveData<Int> = MutableLiveData()
    val song: MutableLiveData<Result> = MutableLiveData()

    fun getSong(context: Context) {
        val database: SongDatabase = SongDatabase.getDatabase(context)
        MainScope().launch(Dispatchers.IO) {
            val songFromDb = database.songsDao().getSong(songId.value ?: 0)
            withContext(Dispatchers.Main) {
                song.value = getSongFromDb(songFromDb)
            }
        }
    }

    // конвертирует песню из базы данных в лайвдату
    private fun getSongFromDb(songsEntity: SongsEntity): Result {
        return Result(
            author = songsEntity.author,
            category = emptyList(),
            chordKey1 = StringConverter.fromStringToList(songsEntity.songKeys)[0],
            chordKey2 = StringConverter.fromStringToList(songsEntity.songKeys)[1],
            chords = songsEntity.songChords,
            chordsFile1 = StringConverter.fromStringToList(songsEntity.chordsFilesLinks)[0],
            chordsFile2 = StringConverter.fromStringToList(songsEntity.chordsFilesLinks)[1],
            difficult = songsEntity.difficult,
            id = songsEntity.songId ?: 0,
            main_key = StringConverter.fromStringToList(songsEntity.songKeys)[0],
            presentation = songsEntity.presentation,
            text = songsEntity.songTextRu,
            text_eng = songsEntity.songTextEng,
            text_file = songsEntity.textFileLink,
            title = songsEntity.songNameRu,
            title_eng = songsEntity.songNameEng,
            translator = emptyList(),
            ytb_id1 = StringConverter.fromStringToList(songsEntity.songVideoIds)[0],
            ytb_id2 = StringConverter.fromStringToList(songsEntity.songVideoIds)[1],
            ytb_id3 = StringConverter.fromStringToList(songsEntity.songVideoIds)[2],
            songExpanded = songsEntity.songExpanded
        )
    }
}
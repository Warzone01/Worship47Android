package com.kirdevelopment.worship47andorid2.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kirdevelopment.worship47andorid2.database.SongDatabase
import com.kirdevelopment.worship47andorid2.database.SongsEntity
import com.kirdevelopment.worship47andorid2.database.StringConverter
import com.kirdevelopment.worship47andorid2.interactors.MainInteractor
import com.kirdevelopment.worship47andorid2.models.Category
import com.kirdevelopment.worship47andorid2.models.Result
import com.kirdevelopment.worship47andorid2.models.SongParams
import com.kirdevelopment.worship47andorid2.models.Translator
import com.kirdevelopment.worship47andorid2.utils.Events
import kotlinx.coroutines.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomeViewModel : ViewModel() {
    val songsList: MutableLiveData<ArrayList<Result>> = MutableLiveData()
    private val mainInteractor = MainInteractor()
    var songs: ArrayList<Result> = ArrayList()
    var songsUpdate: ArrayList<Result> = ArrayList()
    var nextSongs: ArrayList<Result> = ArrayList()
    val filterParms: MutableLiveData<SongParams> = MutableLiveData()
    private val lastPage = 0

    // получить первые песни
    fun getFirstSongs(token: String, events: Events, context: Context) {
        val database: SongDatabase = SongDatabase.getDatabase(context)
        MainScope().launch(Dispatchers.IO) {
            val songsDb = database.songsDao().getAllSongs()
            if (songsDb.isEmpty()) {
                mainInteractor.getAllSongs(token)?.let { songs.addAll(it) }
                for (i in songs) {
                    database.songsDao().insertSongs(parseSongsToDb(i))
                }
                songs.clear()
                getNextSongs(token, context)
            } else {
                withContext(Dispatchers.Main) {
                    for (i in songsDb) {
                        songs.add(parseDBSongsToStore(i))
                    }
                    songsList.value = songs
                    events.changeLastUpdateTime()
                    songsList.notifyObserver()
                }
            }
        }
    }

    // обновляет песни
    fun updateSongs(token: String, date: String, context: Context, events: Events) {
        val database: SongDatabase = SongDatabase.getDatabase(context)
        MainScope().launch(Dispatchers.IO) {
            while (mainInteractor.updatePage != null && mainInteractor.updatePage != lastPage) {
                mainInteractor.getUpdatedSongs(
                    token = token,
                    lastUpdateDate = date
                )?.let { songsUpdate.addAll(it) }
            }
            mainInteractor.updatePage = 1
            for (i in songsUpdate) {
                database.songsDao().insertSongs(parseSongsToDb(i))
            }
            val dbSong = database.songsDao().getAllSongs()
            withContext(Dispatchers.Main) {
                songs.clear()
                for (i in dbSong) {
                    songs.add(parseDBSongsToStore(i))
                }
                songsList.value = songs
            }
            events.changeLastUpdateTime()
         }
    }

    // получить пагинацию песен
    private fun getNextSongs(token: String, context: Context) {
        val database: SongDatabase = SongDatabase.getDatabase(context)
        MainScope().launch(Dispatchers.IO) {
            while (mainInteractor.page != null && mainInteractor.page != lastPage) {
                mainInteractor.getNextSongs(token)?.let { nextSongs.addAll(it) }

                // добавляет в базу данных песни
                for (i in nextSongs) {
                    database.songsDao().insertSongs(parseSongsToDb(i))
                }
                nextSongs.clear()
            }
            val dbSong = database.songsDao().getAllSongs()
            withContext(Dispatchers.Main) {
                for (i in dbSong) {
                    songs.add(parseDBSongsToStore(i))
                }
                songsList.value = songs
                songsList.notifyObserver()
            }
        }
    }

    // конвертирует полученные песни для базы данных
    fun parseSongsToDb(song: Result): SongsEntity {
        return SongsEntity(
            songId = song.id,
            songChords = song.chords ?: "",
            songTextEng = song.text_eng ?: "",
            songTextRu = song.text,
            songNameEng = song.title_eng ?: "",
            songNameRu = song.title,
            songCategorySlug = StringConverter.fromListToString(song.category.map { it.title }
                ?: emptyList()),
            author = song.author ?: "",
            songKeys = StringConverter.fromListToString(
                listOf(
                    song.chordKey1 ?: "",
                    song.chordKey2 ?: ""
                )
            ),
            songTranslators = StringConverter.fromListToString(song.translator.map { it.name }
                ?: emptyList()),
            songVideoIds = StringConverter.fromListToString(
                listOf(
                    song.ytb_id1 ?: "",
                    song.ytb_id2 ?: "",
                    song.ytb_id3 ?: ""
                )
            ),
            songExpanded = song.songExpanded,
            presentation = song.presentation ?: "",
            textFileLink = song.text_file ?: "",
            difficult = song.difficult ?: "",
            chordsFilesLinks = StringConverter.fromListToString(
                listOf(
                    song.chordsFile1 ?: "",
                    song.chordsFile2 ?: ""
                )
            ),
        )
    }

    // конвертирует базу данныйх в лайвдату
    private fun parseDBSongsToStore(songsEntity: SongsEntity): Result {

        val categoryList: MutableList<Category> = mutableListOf()
        for (i in StringConverter.fromStringToList(songsEntity.songCategorySlug)) {
            categoryList.add(Category(title = i))
        }

        val translator: MutableList<Translator> = mutableListOf()
        for (i in StringConverter.fromStringToList(songsEntity.songTranslators)) {
            translator.add(Translator(name = i))
        }

        return Result(
            author = songsEntity.author,
            category = categoryList as List<Category>,
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

    // сообщает обзёрверу, что список песен обновился
    private fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }
}
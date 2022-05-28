package com.kirdevelopment.worship47andorid2.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kirdevelopment.worship47andorid2.interactors.MainInteractor
import com.kirdevelopment.worship47andorid2.models.Result
import kotlinx.coroutines.*

class HomeViewModel: ViewModel() {
    val songsList: MutableLiveData<ArrayList<Result>> = MutableLiveData()
    private val mainInteractor = MainInteractor()
    var songs: ArrayList<Result> = ArrayList()
    var nextSongs: ArrayList<Result> = ArrayList()

    // получить первые песни
    fun getFirstSongs(token: String) {
        GlobalScope.launch(Dispatchers.IO) {
            songs.addAll(mainInteractor.getAllSongs(token))
            withContext(Dispatchers.Main) {
                songsList.value = songs
            }
        }
    }

    // получить пагинацию песен
    fun getNextSongs(token: String) {
        GlobalScope.launch(Dispatchers.IO) {
            while (!mainInteractor.page.isNullOrBlank()) {
                nextSongs.addAll(mainInteractor.getNextSongs(token))
                withContext(Dispatchers.Main) {
                    songsList.value?.addAll(nextSongs)
                    Log.d("Вызвалось", "Список новых песен ${nextSongs.size}, глобальный список ${songsList.value?.size}")
                }
                nextSongs.clear()
            }
        }
    }
}
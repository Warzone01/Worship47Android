package com.kirdevelopment.worship47andorid2.interactors

import android.util.Log
import com.kirdevelopment.worship47andorid2.models.Result
import com.kirdevelopment.worship47andorid2.retrofit.Common
import retrofit2.await

class MainInteractor {
    private val services = Common.retrofitServices
    private val pattern = "(https://worship47\\.tk/api/songs\\-ro/\\?page=)".toRegex()
    var page: String? = null

    // получает первые 10 песен
    suspend fun getAllSongs(token: String): ArrayList<Result> {
        kotlin.runCatching {
            page = services.getSongsList(
                token = "Token $token"
            ).await().next

            return services.getSongsList(
                token = "Token $token"
            ).await().results
        }.onFailure {
            Log.e("Ошибка", it.stackTraceToString())
            return ArrayList()
        }
        return ArrayList()
    }

    // получает пагинацию песен
    suspend fun getNextSongs(token: String): ArrayList<Result> {
        kotlin.runCatching {
            page = services.getNextSongsList(
                token = "Token $token",
                page = page?.replace(pattern, "")?.toInt() ?: 0
            ).await().next

            return services.getNextSongsList(
                token = "Token $token",
                page = page?.replace(pattern, "")?.toInt() ?: 0
            ).await().results

        }.onFailure {
            Log.e("Ошибка", it.stackTraceToString())
            return ArrayList()
        }
        return ArrayList()
    }
}
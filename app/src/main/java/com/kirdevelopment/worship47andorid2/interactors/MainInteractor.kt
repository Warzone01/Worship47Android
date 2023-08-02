package com.kirdevelopment.worship47andorid2.interactors

import android.util.Log
import androidx.compose.animation.core.updateTransition
import com.kirdevelopment.worship47andorid2.models.Result
import com.kirdevelopment.worship47andorid2.retrofit.Common

class MainInteractor {
    private val services = Common.retrofitServices
    var page: Int? = null
    var updatePage: Int? = 1

    // получает первые 10 песен
    suspend fun getAllSongs(token: String): ArrayList<Result>? {
        kotlin.runCatching {
            page = services.getSongsList(
                token = "Token $token"
            ).execute().body()?.next

            return services.getSongsList(
                token = "Token $token"
            ).execute().body()?.results
        }.onFailure {
            Thread.sleep(1000)
            Log.e("Ошибка", it.stackTraceToString())
            return ArrayList()
        }
        return ArrayList()
    }

    // получает пагинацию песен
    suspend fun getNextSongs(token: String): ArrayList<Result>? {
        kotlin.runCatching {

            if (page!=0) {
                val result = services.getNextSongsList(
                    token = "Token $token",
                    page = page?.toInt() ?: 0
                ).execute().body()?.results

                page = services.getNextSongsList(
                    token = "Token $token",
                    page = page?.toInt() ?: 0
                ).execute().body()?.next

                return result
            } else {
                page = 1
                return ArrayList()
            }

        }.onFailure {
            Thread.sleep(1000)
            Log.e("Ошибка", it.localizedMessage)
            return ArrayList()
        }
        return ArrayList()
    }

    // получает обновления песен с даты последнего обновления
    suspend fun getUpdatedSongs(token: String, lastUpdateDate: String): ArrayList<Result>? {
        kotlin.runCatching {
                 val results = ArrayList<com.kirdevelopment.worship47andorid2.models.Result>()

                // получаем результаты запроса
                results += services.updateSongsFrom(
                    token = "Token $token",
                    date = lastUpdateDate,
                    page = updatePage?.toInt() ?: 0
                ).execute().body()?.results!!

                // получает следущую страницу через запрос обновлённых песен
                updatePage = services.updateSongsFrom(
                    token = "Token $token",
                    page = updatePage?.toInt() ?: 0,
                    date = lastUpdateDate
                ).execute().body()?.next

                return results
        }.onFailure {
            Thread.sleep(1000)
            Log.e("Ошибка", it.stackTraceToString())
            return ArrayList()
        }
        return ArrayList()
    }
}
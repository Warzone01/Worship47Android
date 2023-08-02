package com.kirdevelopment.worship47andorid2.interactors

import android.util.Log
import com.kirdevelopment.worship47andorid2.retrofit.Common

class AuthInteractor {
    private val services = Common.retrofitServices

    // получает ключ авторизации
    suspend fun getAuthToken(login: String, password: String): String? {
        kotlin.runCatching {
            return services.getAuthToken(
                username = login,
                password = password
            ).execute().body()?.token
        }.onFailure {
            Thread.sleep(1000)
            Log.e("Ошибка", it.stackTraceToString())
            return null
        }
        return null
    }
}
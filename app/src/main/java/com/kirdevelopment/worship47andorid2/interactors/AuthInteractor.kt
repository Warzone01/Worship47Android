package com.kirdevelopment.worship47andorid2.interactors

import android.util.Log
import com.kirdevelopment.worship47andorid2.retrofit.Common
import retrofit2.await

class AuthInteractor {
    private val services = Common.retrofitServices

    // получает ключ авторизации
    suspend fun getAuthToken(login: String, password: String): String? {
        kotlin.runCatching {
            return services.getAuthToken(
                username = login,
                password = password
            ).await().token
        }.onFailure {
            Log.e("Ошибка", it.stackTraceToString())
            return null
        }
        return null
    }
}
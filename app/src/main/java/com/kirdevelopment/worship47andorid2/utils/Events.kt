package com.kirdevelopment.worship47andorid2.utils

import android.util.Log

interface Events {

    // изменяет дату последнего обновления
    fun changeLastUpdateTime() {
    }

    // эвент ошибки соединения
    fun connectionError(message: String) {

        // сделать обработку в том числе и когда нет песен
        when {
            message.contains(Constants.FAILED_TO_CONNECT) -> Log.d(
                "Ошибка",
                "Failed to connect to worship47.tk"
            )
        }
    }
}


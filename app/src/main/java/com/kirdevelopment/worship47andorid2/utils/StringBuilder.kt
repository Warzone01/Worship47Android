package com.kirdevelopment.worship47andorid2.utils

object StringBuilder {

    // строит строку из списка
    fun buildFromListToString(list: ArrayList<String>): String {
        val result: String = buildString {
            for (i in list) {
                this.append("$i${if (list.size > 1 && list[list.lastIndex] != i)", " else ""}")
            }
        }
        return result
    }
}
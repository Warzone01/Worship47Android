package com.kirdevelopment.worship47andorid2.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.kirdevelopment.worship47andorid2.utils.Constants.DELIMITERS
import java.util.stream.Collectors

/**
 * конвертирует списки в строки и на оборот
 * */
@ProvidedTypeConverter
class StringConverter {
    companion object {

        @TypeConverter
        @JvmStatic
        fun fromListToString(list: List<String>): String {
            return list.stream().collect(Collectors.joining(DELIMITERS))
        }

        @TypeConverter
        @JvmStatic
        fun fromStringToList(string: String): List<String> {
            return string.split(DELIMITERS)
        }
    }
}
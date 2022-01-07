package com.kirdevelopment.worship47andorid2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [SongsEntity::class], version = 1, exportSchema = false)
@TypeConverters(StringConverter::class)
abstract class SongDatabase : RoomDatabase() {
    abstract fun songsDao(): SongsDao

    companion object {
        var songsDatabase: SongDatabase? = null

        // получить базу данных
        fun getDatabase(context: Context): SongDatabase {
            if (songsDatabase == null) {
                songsDatabase = Room.databaseBuilder(
                    context,
                    SongDatabase::class.java,
                    "songsDB"
                ).build()
            }
            return songsDatabase as SongDatabase
        }
    }
}
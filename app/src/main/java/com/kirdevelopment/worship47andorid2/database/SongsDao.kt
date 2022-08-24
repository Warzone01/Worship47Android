package com.kirdevelopment.worship47andorid2.database

import androidx.room.*

@Dao
interface SongsDao {

    // получить все песни из базы данных
    @Query("SELECT * FROM songs ORDER BY songId ASC")
    fun getAllSongs(): List<SongsEntity>

    // получить конкретную песню из списка по id
    @Query("SELECT * FROM songs WHERE songId = :id")
    fun getSong(id: Int): SongsEntity

    // записать песни в базу данных
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSongs(songs: SongsEntity)

    // обновить песни
    @Update
    fun updateSongs(songs: SongsEntity)
}
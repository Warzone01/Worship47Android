package com.kirdevelopment.worship47andorid2.retrofit

import com.kirdevelopment.worship47andorid2.models.AuthKey
import com.kirdevelopment.worship47andorid2.models.SongObject
import retrofit2.Call
import retrofit2.http.*

interface RetrofitServices {

    // запрос на авторизацию, а в ответ получем token
    @FormUrlEncoded
    @POST("/api/get-token/")
    fun getAuthToken(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<AuthKey>

    // получить полный список песен
    @GET("/api/songs-ro/")
    fun getSongsList(@Header("Authorization") token: String): Call<SongObject>

    // получить список следующих песен
    @GET("/api/songs-ro/?page=")
    fun getNextSongsList(@Header("Authorization") token: String,
                         @Query("page") page: Int): Call<SongObject>

    // обновляет песни, передавая дату последнего сохранения
    @GET("/api/songs-ro/")
    fun updateSongsFrom(@Header("Authorization") token: String,
                        @Query("page") page: Int,
                        @Query("update_from") date: String): Call<SongObject>
}
package com.kirdevelopment.worship47andorid2.retrofit

object Common {
    private const val BASE_URL = "https://worship47.tk"
    val retrofitServices: RetrofitServices
        get() = RetrofitClient.getClient(BASE_URL)!!.create(RetrofitServices::class.java)
}
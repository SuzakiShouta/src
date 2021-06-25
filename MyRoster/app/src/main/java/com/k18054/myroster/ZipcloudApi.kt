package com.k18054.myroster

import android.os.Bundle
import android.os.Handler
import android.telecom.Call
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url
import kotlin.concurrent.thread

data class Address(val message: String = "", val results: List<Any> = listOf())

interface AddressService {

    @GET("search")
    fun fetchAddress(
        @Query("zipcode") page: Int,
    ): Call<Address>
    fun getProfile(@Url url: String?)
}

class ZipcloudApi {
}
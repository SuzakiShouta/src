package com.k18054.myroster

import android.os.Handler
import android.util.Log
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

    val zipcode = 7830060

    val retrofit = Retrofit.Builder()
        .baseUrl("https://zipcloud.ibsnet.co.jp/api/")
        .addConverterFactory(GsonConverterFactory.create()).build()
//           ↑↑↑↑↑↑↑↑
//             Gsonのファクトリーメソッド必須！
    val handler = Handler()

    fun getAddress(zipCode: Int) {
        thread { // Retrofitはメインスレッドで処理できない
            try {
                Log.d("MainActivity", "try")
                val service:AddressService = retrofit.create(AddressService::class.java)
                val address = service.fetchAddress(zipcode).execute().body() ?: throw IllegalStateException("bodyがnullだよ！")

                handler.post(Runnable {
                    // メインスレッドで処理
                    // UIはここで
                    println(address)
                })

            } catch (e: Exception) {
                Log.d("mopi", "debug $e")
            }
        }
    }

}
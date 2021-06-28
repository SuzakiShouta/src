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

data class Address(val message: String = "",
                   val results: List<Result> = listOf())
data class Result(val address1: String = "",
                  val address2: String = "",
                  val address3: String = "",
                  val kana1: String = "",
                  val kana2: String = "",
                  val kana3: String = "",
                  val prefcode: Int = 0,
                  val zipcode: Int = 0)

interface AddressService {
    @GET("search")
    fun fetchAddress(
        @Query("zipcode") page: Int,
    ): Call<Address>
}

class ZipcloudApi {

    //MainActivityにStringを渡したい。
    //↓callback(関数型)の定義。
    private var callback: ((String?) -> Unit)? = null
    fun setTextApi(api:(String?) -> Unit) {
        this.callback = api
    }

    val retrofit = Retrofit.Builder()
        .baseUrl("https://zipcloud.ibsnet.co.jp/api/")
        .addConverterFactory(GsonConverterFactory.create()).build()
//           ↑↑↑↑↑↑↑↑
//             Gsonのファクトリーメソッド必須！
    val handler = Handler()

    fun getAddress (postalCode : Int) {
        thread { // Retrofitはメインスレッドで処理できない
            try {
                val service:AddressService = retrofit.create(AddressService::class.java)
                val address = service.fetchAddress(postalCode).execute().body() ?: throw IllegalStateException("bodyがnullだよ！")
                val result = address.results[0].address1.plus(address.results[0].address2).plus(address.results[0].address3)
                println(result)
                handler.post(Runnable {
                    // メインスレッドで処理
                    // UIはここで
                    callback?.invoke(result)
                })
            } catch (e: Exception) {
                Log.d("mopi", "debug $e")
            }
        }
    }

}
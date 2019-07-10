package com.example.dmmitemsearchsample.common.api

import android.os.Build
import com.example.dmmitemsearchsample.common.Constants
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitServiceGenerator {

    inline fun <reified T: Any> createApiService(): T {
        val apiUrl = Constants.BASE_URL
        val client = builderHttpClient()
        val gson = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()
        val retrofit = Retrofit.Builder()
            .baseUrl(apiUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return retrofit.create(T::class.java)
    }

    fun builderHttpClient(): OkHttpClient =
        OkHttpClient.Builder().apply {
            //if (Constants.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                addInterceptor(logging)
            //}

            addInterceptor(createInterceptor())
            connectTimeout(Constants.TIME_INTERVAL, TimeUnit.SECONDS)
            readTimeout(Constants.TIME_INTERVAL, TimeUnit.SECONDS)
        }.build()

    // ヘッダ生成
    private fun createInterceptor(): Interceptor {
        return object : Interceptor {
            override fun intercept(chain: Interceptor.Chain?): Response? {
                chain ?: return null

                val request = chain.request().newBuilder()
                    .header("x-os", "android")
                    .header("x-os-version", Build.VERSION.RELEASE)
                    .build()
                return chain.proceed(request)
            }
        }
    }
}
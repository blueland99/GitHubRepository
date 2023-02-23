package com.blueland.githubrepository.data.network

import com.blueland.githubrepository.global.Constant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constant.API_BASE_URL)
            .client(okHttpClient())                                     // 통신시 로그 찍음
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())  // Rxjava + Retrofit2 연결 (Observable 형태로 return 받기 위함)
            .addConverterFactory(GsonConverterFactory.create())         // xml 형태 파싱
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    private fun okHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = if (Constant.IS_DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.BASIC

        val builder = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)

        builder.addInterceptor(interceptor)
        builder.addInterceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
            chain.proceed(requestBuilder.build())
        }
        return builder.build()
    }
}
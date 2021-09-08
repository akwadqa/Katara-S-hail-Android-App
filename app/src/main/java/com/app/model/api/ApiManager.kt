package com.app.model.api

import android.util.Log
import com.app.utils.AppConstants
import com.app.utils.SharedPreferencesManager
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit


object ApiManager {

    val apiClient: ApiInterface
        get() {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(AppConstants.BASE_DEV_URL)
                .client(getAuthHttpClient().build())
                .build()

            return retrofit.create(ApiInterface::class.java)
        }

    val apiUpdateClient: ApiInterface
        get() {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(AppConstants.BASE_DEV_URL)
                .client(getHttpClient().build())
                .build()

            return retrofit.create(ApiInterface::class.java)
        }

    /**
     * Method to create [OkHttpClient] builder by adding required headers in the [Request]
     *
     * @return OkHttpClient object
     */
    private fun getAuthHttpClient(): OkHttpClient.Builder {
        val accessToken = SharedPreferencesManager.getString(AppConstants.TOKEN)
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder: Request.Builder = original.newBuilder()
                if(!accessToken.isNullOrEmpty())
                    requestBuilder.header("Authorization", "Bearer $accessToken")
//                requestBuilder.header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1bmlxdWVfbmFtZSI6ImZlN2NhZDU2LWVjMWUtNDkxZC04ZTExLTg3M2RlNTMxOTlmZiIsIm5iZiI6MTYyNzQ5NTg2MiwiZXhwIjoxNjI3NTM5MDYyLCJpYXQiOjE2Mjc0OTU4NjJ9.6O1O-nB6UlyQCbkC8kGJc7qmAcbeVm_stXnKQDAcYEs")
                requestBuilder.method(original.method, original.body)
                val request = requestBuilder.build()
                val response = try {
                    chain.proceed(request)
                } catch (e: SocketTimeoutException) {
                    throw IOException()
                }
                Log.e("Response =", response.message)
                response
            }
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(100, TimeUnit.SECONDS)
            .connectionPool(ConnectionPool(0, 5, TimeUnit.MINUTES))
            .readTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
    }

    private fun getHttpClient(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder: Request.Builder = original.newBuilder()
               requestBuilder.method(original.method, original.body)
                val request = requestBuilder.build()
                val response = try {
                    chain.proceed(request)
                } catch (e: SocketTimeoutException) {
                    throw IOException()
                }
                Log.e("Response =", response.message)
                response
            }
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .readTimeout(100, TimeUnit.SECONDS)
            .connectionPool(ConnectionPool(0, 5, TimeUnit.MINUTES))
            .writeTimeout(100, TimeUnit.SECONDS)
            .connectTimeout(100, TimeUnit.SECONDS)
    }

}
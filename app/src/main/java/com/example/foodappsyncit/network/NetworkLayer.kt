package com.example.foodappsyncit.network

import com.example.foodappsyncit.utils.Constants.Companion.BASE_URL
import com.example.foodappsyncit.utils.UserPreferences
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkLayer {

    private fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    private val unAuthorizedClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .addInterceptor {
            it.proceed(
                it.request().newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")
                    .build()
            )
        }.build()

    private val authorizedClient = OkHttpClient.Builder()
        .addInterceptor {
            it.proceed(
                UserPreferences.retrieveToken("token")?.let { token ->
                    it.request().newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json")
                        .build()
                }!!
            )
        }.build()

    private fun provideAuthService(): AuthFoodAppService {
        return provideRetrofit(authorizedClient).create(AuthFoodAppService::class.java)
    }

    private fun provideUnAuthService(): UnAuthFoodAppService {
        return provideRetrofit(unAuthorizedClient).create(UnAuthFoodAppService::class.java)
    }

    val apiAuthorizedClient = ApiAuthorizedClient(provideAuthService())
    val apiUnAuthorizedClient = ApiUnAuthorizedClient(provideUnAuthService())

}
package com.example.myapplication.di

import android.content.Context
import com.example.myapplication.BaseApplication
import com.example.myapplication.model.db.UserDatabase
import com.example.myapplication.model.remote.RestApi
import com.example.myapplication.repository.DataRepository
import com.example.myapplication.repository.DataRepositoryImplementation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun getBaseUrl(): String = "https://reqres.in/api/"

    @Provides
    @Singleton
    fun okHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    @Provides
    @Singleton
    fun getRetrofit(okHttpClient: OkHttpClient, baseUrl: String): Retrofit =
        Retrofit.Builder().client(okHttpClient).baseUrl(baseUrl).addConverterFactory(
            GsonConverterFactory.create()
        ).build()

    @Provides
    @Singleton
    fun getApi(retrofit: Retrofit): RestApi = retrofit.create(RestApi::class.java)

    @Provides
    @Singleton
    fun getMyContext():Context=BaseApplication.appContext

    @Provides
    @Singleton
    fun getMyDb():UserDatabase=UserDatabase.getInstance(getMyContext())

    @Provides
    @Singleton
    fun repository(api: RestApi,db:UserDatabase): DataRepository = DataRepositoryImplementation(api,db)
}
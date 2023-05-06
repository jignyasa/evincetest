package com.example.myapplication.model.remote

import com.example.myapplication.model.UserDetailModel
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface RestApi {
    @GET("users?page=1")
    suspend fun getData(): Response<UserDetailModel>

    @DELETE("users/{user_id}")
    suspend fun removeUser(@Path("user_id") id: Int): Response<UserDetailModel>

    @PATCH("users/{user_id}/{first_name}/{email}")
    suspend fun editUser(
        @Path("user_id") id: Int,
        @Path("first_name") firstName: String,
        @Path("email") email: String
    ): Response<UserDetailModel>
}
package com.example.myapplication.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName
data class UserDetailModel(@SerializedName("per_page")
                           val perPage: Int? = 0,
                           @SerializedName("total")
                           val total: Int? = 0,
                           @SerializedName("data")
                           val data: List<DataItem>?,
                           @SerializedName("page")
                           val page: Int? = 0,
                           @SerializedName("total_pages")
                           val totalPages: Int? = 0,
                           @SerializedName("support")
                           val support: Support?)
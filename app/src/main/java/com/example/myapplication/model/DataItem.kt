package com.example.myapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
@Entity(tableName = "userTable")
data class DataItem(@SerializedName("last_name")
                    val lastName: String? = "",
                    @SerializedName("id")
                    @PrimaryKey
                    val id: Int? = 0,
                    @SerializedName("avatar")
                    val avatar: String? = "",
                    @SerializedName("first_name")
                    var firstName: String? = "",
                    @SerializedName("email")
                    var email: String? = ""):java.io.Serializable
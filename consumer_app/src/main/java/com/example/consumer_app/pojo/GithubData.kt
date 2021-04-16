package com.example.consumer_app.pojo


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GithubData(
    @SerializedName("login")
    val username: String? = null,
    val avatar_url: String? = null,
    val name: String? = null,
    val followers: String? = null,
    val following: String? = null,
    val public_repos: String? = null,
    val id: String? = null,
) : Parcelable

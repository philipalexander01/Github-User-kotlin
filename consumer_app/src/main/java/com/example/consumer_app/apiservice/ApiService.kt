package com.example.consumer_app.apiservice

import com.example.consumer_app.pojo.GithubData
import com.example.consumer_app.pojo.GithubSearch
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    //for search data
    @GET("search/users")
    @Headers("Authorization: token 50523cba1a605acb396e4f5170266b2d7f00366d")
    suspend fun getDataBySearch(@Query("q") username:String):Response<GithubSearch>

    //for detail profile
    @GET("users/{username}")
    @Headers("Authorization: token 50523cba1a605acb396e4f5170266b2d7f00366d")
    suspend fun getDataByUsername(@Path("username") username:String):Response<GithubData>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 50523cba1a605acb396e4f5170266b2d7f00366d")
    suspend fun getFollowersData(@Path("username") username:String):Response<ArrayList<GithubData>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 50523cba1a605acb396e4f5170266b2d7f00366d")
    suspend fun getFollowingData(@Path("username") username:String):Response<ArrayList<GithubData>>

}
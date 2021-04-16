package com.example.githubusers2.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubusers2.apiservice.ApiService
import com.example.githubusers2.pojo.GithubData
import com.example.githubusers2.utility.Utility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FollowingViewModel:ViewModel() {
    private val dataFollowing = MutableLiveData<ArrayList<GithubData>>()
    fun setDataFollowing(username:String){
        val apiService = Utility().getRetrofit().create(ApiService::class.java)
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val response = apiService.getFollowingData(username)
                withContext(Dispatchers.Main) {
                    if (response.code() == 200) {
                        if (response.body() != null) {
                            dataFollowing.postValue(response.body())
                        }
                    }
                }
            }
        } catch (e: Throwable) {
            Log.d("retrofit error: ",e.message.toString())
        }
    }
    fun getDataFollowing(): LiveData<ArrayList<GithubData>> {
        return dataFollowing
    }
}
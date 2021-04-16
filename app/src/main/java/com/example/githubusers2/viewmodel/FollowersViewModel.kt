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

class FollowersViewModel:ViewModel() {
    private val dataFollowers = MutableLiveData<ArrayList<GithubData>>()

    fun setDataFollowers(username:String){
        val apiService = Utility().getRetrofit().create(ApiService::class.java)
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val response = apiService.getFollowersData(username)
                withContext(Dispatchers.Main) {
                    if (response.code() == 200) {
                        if (response.body() != null) {
                            dataFollowers.postValue(response.body())
                        }
                    }
                }
            }
        } catch (e: Throwable) {
            Log.d("retrofit error: ",e.message.toString())
        }
    }
    fun getDataFollowers(): LiveData<ArrayList<GithubData>> {
        return dataFollowers
    }
}
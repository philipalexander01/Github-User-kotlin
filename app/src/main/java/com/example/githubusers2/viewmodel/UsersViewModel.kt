package com.example.githubusers2.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubusers2.apiservice.ApiService
import com.example.githubusers2.pojo.GithubData
import com.example.githubusers2.pojo.GithubSearch
import com.example.githubusers2.utility.Utility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UsersViewModel : ViewModel(){
    private val dataBySearch = MutableLiveData<GithubSearch>()
    private val dataByUsername = MutableLiveData<GithubData>()

    fun setDataBySearch(username:String = "philip"){
        val apiService = Utility().getRetrofit().create(ApiService::class.java)
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val response = apiService.getDataBySearch(username)
                withContext(Dispatchers.Main) {
                    if (response.code() == 200) {
                        if (response.body() != null) {
                            dataBySearch.postValue(response.body())
                        }
                    }
                }
            }
        } catch (e: Throwable) {
            Log.d("retrofit error: ",e.message.toString())
        }
    }
    fun getDataBySearch():LiveData<GithubSearch>{
        return dataBySearch
    }

    fun setDataByUsername(username: String){
        val apiService = Utility().getRetrofit().create(ApiService::class.java)
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val response = apiService.getDataByUsername(username)
                withContext(Dispatchers.Main) {
                    if (response.code() == 200) {
                        if (response.body() != null) {
                            dataByUsername.postValue(response.body())
                        }
                    }
                }
            }
        } catch (e: Throwable) {
            Log.d("retrofit error: ",e.message.toString())
        }
    }
    fun getDataByUsername():LiveData<GithubData>{
        return dataByUsername
    }




}
package com.example.githubusers2.utility

import android.content.Context

class UserPreference(context: Context) {
    companion object{
        const val NOTIF = "notif"
        const val ACTIVE = "active"
    }
    private val PREFS_NAME = "user_pref"
    private var sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    //setPref
    fun setPref( key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    //getPref
    fun getPref(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    //clear pref
    fun clearPref(key:String){
        val editor = sharedPreferences.edit()
        editor.putString(key, null)
        editor.apply()
    }

}
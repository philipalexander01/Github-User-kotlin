package com.example.githubusers2.helper

import android.database.Cursor
import androidx.lifecycle.MutableLiveData
import com.example.githubusers2.db.DatabaseContract
import com.example.githubusers2.pojo.GithubData

object MappingHelper {
    fun mapCursorToArrayList(githubCursor: Cursor?): ArrayList<GithubData> {
        val userList = ArrayList<GithubData>()
        githubCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.GithubColumns._ID))
                val username =
                    getString(getColumnIndexOrThrow(DatabaseContract.GithubColumns.USERNAME))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.GithubColumns.NAME))
                val following =
                    getString(getColumnIndexOrThrow(DatabaseContract.GithubColumns.FOLLOWING))
                val followers =
                    getString(getColumnIndexOrThrow(DatabaseContract.GithubColumns.FOLLOWERS))
                val repos =
                    getString(getColumnIndexOrThrow(DatabaseContract.GithubColumns.PUBLIC_REPOS))
                val avatar_url =
                    getString(getColumnIndexOrThrow(DatabaseContract.GithubColumns.AVATAR_URL))

                userList.add(
                    GithubData(
                        username,
                        avatar_url,
                        name,
                        followers,
                        following,
                        repos,
                        id.toString()
                    )
                )
            }
        }
        return userList
    }


}
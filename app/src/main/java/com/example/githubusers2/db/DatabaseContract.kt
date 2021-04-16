package com.example.githubusers2.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {
    const val AUTHORITY = "com.example.githubusers2"
    const val SCHEME = "content"

    internal  class GithubColumns:BaseColumns{
        companion object{
            const val TABLE_NAME = "users"
            const val _ID = "_id"
            const val USERNAME = "username"
            const val NAME = "name"
            const val AVATAR_URL = "avatar_url"
            const val FOLLOWERS = "followers"
            const val FOLLOWING = "following"
            const val PUBLIC_REPOS = "public_repos"

            val CONTENT_URI = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()

        }
    }
}
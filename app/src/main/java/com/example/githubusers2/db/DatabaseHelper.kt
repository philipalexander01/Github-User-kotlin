package com.example.githubusers2.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.githubusers2.db.DatabaseContract.GithubColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "dbgithubapp"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_USER = "CREATE TABLE $TABLE_NAME" +
                " (${DatabaseContract.GithubColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${DatabaseContract.GithubColumns.USERNAME} TEXT NOT NULL," +
                " ${DatabaseContract.GithubColumns.NAME} TEXT NOT NULL," +
                " ${DatabaseContract.GithubColumns.AVATAR_URL} TEXT NOT NULL," +
                " ${DatabaseContract.GithubColumns.FOLLOWERS} TEXT NOT NULL," +
                " ${DatabaseContract.GithubColumns.FOLLOWING} TEXT NOT NULL," +
                " ${DatabaseContract.GithubColumns.PUBLIC_REPOS} TEXT NOT NULL )"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_USER)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}


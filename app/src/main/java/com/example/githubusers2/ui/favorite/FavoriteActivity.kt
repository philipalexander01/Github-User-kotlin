package com.example.githubusers2.ui.favorite

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusers2.R
import com.example.githubusers2.adapter.ListUsersViewAdapter
import com.example.githubusers2.databinding.ActivityFavoriteBinding
import com.example.githubusers2.db.DatabaseContract.GithubColumns.Companion.CONTENT_URI
import com.example.githubusers2.helper.MappingHelper
import com.example.githubusers2.pojo.GithubData
import com.example.githubusers2.ui.DetailActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {
    private var binding: ActivityFavoriteBinding? = null
    private lateinit var listUsersViewAdapter: ListUsersViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.title = getString(R.string.list_favorite)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        observeData()
        binding?.apply {
            listUsersViewAdapter = ListUsersViewAdapter(getString(R.string.favorite))
            rvFavorite.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            rvFavorite.adapter = listUsersViewAdapter
            listUsersViewAdapter.setOnItemClickCallback(object :
                ListUsersViewAdapter.OnItemClickCallback {
                override fun onItemClick(data: GithubData) {
                    val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
                    intent.putExtra(getString(R.string.username), data.username)
                    startActivity(intent)
                }
            })
        }
        loadDataAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    fun observeData() {
        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadDataAsync()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)
    }

    private fun loadDataAsync() {
        lifecycleScope.launch(Dispatchers.Main) {
            val deferred = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favData = deferred.await()
            if (favData.size > 0) {
                listUsersViewAdapter.setDataUser(favData)
            } else {
                binding?.rvFavorite?.visibility = View.GONE
                binding?.ivNoData?.visibility = View.VISIBLE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }


}
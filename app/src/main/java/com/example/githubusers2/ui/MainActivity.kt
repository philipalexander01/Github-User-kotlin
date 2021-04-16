package com.example.githubusers2.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusers2.R
import com.example.githubusers2.adapter.ListUsersViewAdapter
import com.example.githubusers2.databinding.ActivityMainBinding
import com.example.githubusers2.databinding.NoInternetLayoutBinding
import com.example.githubusers2.pojo.GithubData
import com.example.githubusers2.ui.favorite.FavoriteActivity
import com.example.githubusers2.ui.settings.SettingsActivity
import com.example.githubusers2.utility.Utility
import com.example.githubusers2.viewmodel.UsersViewModel
import es.dmoral.toasty.Toasty

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private var binding1: NoInternetLayoutBinding? = null
    private lateinit var usersViewModel: UsersViewModel
    private lateinit var listUsersViewAdapter: ListUsersViewAdapter

    private var actNw: NetworkCapabilities? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.title = getString(R.string.list_user)

        //check internet connection
        actNw = Utility().checkInternetConnection(this)

        if (actNw == null) {
            binding1 = NoInternetLayoutBinding.inflate(layoutInflater)
            setContentView(binding1?.root)
            binding1?.btnCobaLagi?.setOnClickListener {
                actNw = Utility().checkInternetConnection(this)
                if (actNw != null) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        } else {
            usersViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
                .get(UsersViewModel::class.java)
            usersViewModel.setDataBySearch()
            listUsersViewAdapter =
                ListUsersViewAdapter(this@MainActivity.getString(R.string.search))
            binding?.apply {
                binding?.progressBar?.visibility = View.VISIBLE
                rvUsers.adapter = listUsersViewAdapter
                rvUsers.layoutManager = LinearLayoutManager(this@MainActivity)
            }

            usersViewModel.getDataBySearch().observe(this, {
                if (it.items.isNotEmpty()) {
                    binding?.apply {
                        rvUsers.visibility = View.VISIBLE
                        ivNoData.visibility = View.GONE
                        listUsersViewAdapter.setDataUser(it.items)
                        binding?.progressBar?.visibility = View.GONE
                    }
                }else{
                    binding?.apply {
                        rvUsers.visibility = View.GONE
                        ivNoData.visibility = View.VISIBLE
                    }

                }
            })

            listUsersViewAdapter.setOnItemClickCallback(object :
                ListUsersViewAdapter.OnItemClickCallback {
                override fun onItemClick(data: GithubData) {
                    val intent = Intent(this@MainActivity, DetailActivity::class.java).apply {
                        putExtra(getString(R.string.username), data.username)
                    }
                    startActivity(intent)
                }
            })
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting -> {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
            R.id.favorite -> {
                startActivity(Intent(this, FavoriteActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.length == 0) binding?.progressBar?.visibility = View.GONE
                else {
                    actNw = Utility().checkInternetConnection(this@MainActivity)
                    if (actNw == null) {
                        Toasty.error(
                            this@MainActivity,
                            this@MainActivity.getString(R.string.internet_connection),
                            Toast.LENGTH_SHORT,
                            true
                        ).show()
                    } else {
                        binding?.progressBar?.visibility = View.VISIBLE
                        usersViewModel.setDataBySearch(newText.toString())
                    }
                }
                return true
            }

        })

        return true

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        binding1 = null
    }
}
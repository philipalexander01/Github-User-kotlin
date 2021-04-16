package com.example.consumer_app.ui

import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.consumer_app.R
import com.example.consumer_app.adapter.TabViewPagerAdapter
import com.example.consumer_app.databinding.ActivityDetailBinding
import com.example.consumer_app.databinding.NoInternetLayoutBinding
import com.example.consumer_app.db.DatabaseContract
import com.example.consumer_app.db.DatabaseContract.GithubColumns.Companion.CONTENT_URI
import com.example.consumer_app.helper.MappingHelper
import com.example.consumer_app.pojo.GithubData
import com.example.consumer_app.utility.Utility
import com.example.consumer_app.viewmodel.UsersViewModel

import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class DetailActivity : AppCompatActivity() {
    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }

    private var binding: ActivityDetailBinding? = null
    private var binding1: NoInternetLayoutBinding? = null
    private var actNw: NetworkCapabilities? = null
    private lateinit var username: String

    private lateinit var usersViewModel: UsersViewModel

    //sqlite
    private var githubData: GithubData? = null

    private lateinit var uriWithUsername: Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        //view model
        initViewModel()
        username = intent.getStringExtra(getString(R.string.username)).toString()
        //check internet connection
        actNw = Utility().checkInternetConnection(this)
        //if there is no an internet connection
        if (actNw == null) {
            binding1 = NoInternetLayoutBinding.inflate(layoutInflater)
            setContentView(binding1?.root)
            binding1?.btnCobaLagi?.setOnClickListener {
                actNw = Utility().checkInternetConnection(this)
                if (actNw != null) {
                    val intent = Intent(this, DetailActivity::class.java)
                    intent.putExtra(getString(R.string.username), username)
                    startActivity(intent)
                    finish()
                }
            }

        }
        //if there is an internet connection
        else {
            usersViewModel.setDataByUsername(username)


            //observe data
            observeData()

            //set up tab layout
            initAdapter()

            var isShow = true
            var scrollRange = -1
            binding?.apply {
                appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
                    if (scrollRange == -1) {
                        scrollRange = barLayout.totalScrollRange
                    }
                    if (scrollRange + verticalOffset == 0) {
                        collapsingToolbar.title = username
                        collapsingToolbar.isTitleEnabled = true
                        isShow = true
                    } else if (isShow) {
                        collapsingToolbar.isTitleEnabled = false
                        collapsingToolbar.title =
                            username //careful there should a space between double quote otherwise it wont work
                        isShow = false
                    }
                })
                var isActive = false
                uriWithUsername = Uri.parse("$CONTENT_URI/$username")
                GlobalScope.launch(Dispatchers.Main) {
                    val deferred = async(Dispatchers.IO) {
                        val cursor = contentResolver.query(uriWithUsername, null, null, null, null)
                        MappingHelper.mapCursorToArrayList(cursor)
                    }
                    val favData = deferred.await()
                    if (favData.isNotEmpty()) {
                        isActive = true
                        setStatus(isActive)
                    }
                }

                favButton.setOnClickListener {
                    uriWithUsername = Uri.parse("$CONTENT_URI/$username")
                    val values = ContentValues()

                    //if already add to favorite
                    if (isActive) {
                        isActive = false
                        setStatus(isActive)

                        //delete data
                        contentResolver.delete(uriWithUsername, null, null)
                        Toasty.success(
                            this@DetailActivity,
                            getString(R.string.success_remove_favorite),
                            Toast.LENGTH_SHORT,
                            true
                        ).show()
                    } else {
                        isActive = true
                        setStatus(isActive)
                        //insert data to database
                        values.put(DatabaseContract.GithubColumns.USERNAME, githubData?.username)
                        values.put(DatabaseContract.GithubColumns.NAME, githubData?.name)
                        values.put(
                            DatabaseContract.GithubColumns.AVATAR_URL,
                            githubData?.avatar_url
                        )
                        values.put(DatabaseContract.GithubColumns.FOLLOWERS, githubData?.followers)
                        values.put(DatabaseContract.GithubColumns.FOLLOWING, githubData?.following)
                        values.put(
                            DatabaseContract.GithubColumns.PUBLIC_REPOS,
                            githubData?.public_repos
                        )
                        contentResolver.insert(CONTENT_URI, values)
                        Toasty.success(
                            this@DetailActivity,
                            getString(R.string.succes_favorite),
                            Toast.LENGTH_SHORT,
                            true
                        ).show()
                    }
                }
            }
        }
        binding?.ivBack?.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        binding1 = null
    }

    private fun initAdapter() {
        val tabViewPagerAdapter = TabViewPagerAdapter(this, username)
        binding?.apply {
            viewPager.adapter = tabViewPagerAdapter
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }
    }

    private fun initViewModel() {
        usersViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(UsersViewModel::class.java)
    }

    private fun observeData() {
        usersViewModel.getDataByUsername().observe(this, {
            if (it != null) {
                binding?.apply {
                    tvUsername.text = it.username
                    tvName.text = it.name
                    Glide.with(this@DetailActivity).load(it.avatar_url)
                        .apply(RequestOptions.circleCropTransform())
                        .into(ivAvatar)
                    tvFollowers.text = it.followers
                    tvFollowing.text = it.following
                    tvRepo.text = it.public_repos
                    //set data to object
                    githubData = it
                }
            }
        })
    }

    //change favorite icon
    private fun setStatus(isActive: Boolean) {
        val myFabSrc = ResourcesCompat.getDrawable(
            resources,
            R.drawable.ic_baseline_favorite_24_white,
            null
        )
        val illBeWhite = myFabSrc?.constantState?.newDrawable()
        if (isActive) {
            illBeWhite?.mutate()?.colorFilter =
                BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                    Color.RED,
                    BlendModeCompat.SRC_ATOP
                )
            binding?.favButton?.setImageDrawable(illBeWhite)
        } else {
            illBeWhite?.mutate()?.colorFilter =
                BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                    Color.WHITE,
                    BlendModeCompat.SRC_ATOP
                )
            binding?.favButton?.setImageDrawable(illBeWhite)
        }
    }
}
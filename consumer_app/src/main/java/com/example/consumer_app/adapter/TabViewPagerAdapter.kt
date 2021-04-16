package com.example.consumer_app.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.consumer_app.R
import com.example.consumer_app.ui.fragment.FollowersFragment
import com.example.consumer_app.ui.fragment.FollowingFragment

class TabViewPagerAdapter(private val activity: AppCompatActivity, val username: String) :
    FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowersFragment()
            1 -> fragment = FollowingFragment()
        }
        val bundle = Bundle()
        bundle.putString(activity.getString(R.string.username), username)
        fragment?.arguments = bundle
        return fragment as Fragment
    }
}
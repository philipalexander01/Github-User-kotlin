package com.example.githubusers2.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusers2.R
import com.example.githubusers2.adapter.ListUsersViewAdapter
import com.example.githubusers2.databinding.FragmentFollowersBinding
import com.example.githubusers2.viewmodel.FollowersViewModel

class FollowersFragment : Fragment() {
    private var binding: FragmentFollowersBinding? = null
    private lateinit var followersViewModel: FollowersViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFollowersBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        followersViewModel =
            ViewModelProvider(requireActivity()).get(FollowersViewModel::class.java)
        val adapter = ListUsersViewAdapter()
        followersViewModel.setDataFollowers(
            arguments?.getString(this.getString(R.string.username)).toString()
        )
        binding?.apply {
            progressBar.visibility = View.VISIBLE
            rvFollowers.adapter = adapter
            rvFollowers.layoutManager = LinearLayoutManager(requireActivity())
        }
        followersViewModel.getDataFollowers().observe(requireActivity(), {
            if (it.isNotEmpty()) {
                adapter.setDataUser(it)
                binding?.progressBar?.visibility = View.GONE
            } else {
                binding?.progressBar?.visibility = View.GONE
                binding?.ivNoData?.visibility = View.VISIBLE
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
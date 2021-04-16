package com.example.consumer_app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consumer_app.R
import com.example.consumer_app.adapter.ListUsersViewAdapter
import com.example.consumer_app.databinding.FragmentFollowingBinding
import com.example.consumer_app.viewmodel.FollowingViewModel

class FollowingFragment : Fragment() {

    private var binding: FragmentFollowingBinding? = null
    private lateinit var followingViewModel: FollowingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFollowingBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        followingViewModel =
            ViewModelProvider(requireActivity()).get(FollowingViewModel::class.java)
        val adapter = ListUsersViewAdapter()
        followingViewModel.setDataFollowing(
            arguments?.getString(this.getString(R.string.username)).toString()
        )
        binding?.apply {
            binding?.progressBar?.visibility = View.VISIBLE
            rvFollowing.adapter = adapter
            rvFollowing.layoutManager = LinearLayoutManager(requireActivity())
        }
        followingViewModel.getDataFollowing().observe(requireActivity(), {
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
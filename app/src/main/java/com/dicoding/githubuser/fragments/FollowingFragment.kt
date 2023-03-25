package com.dicoding.githubuser.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.R
import com.dicoding.githubuser.adapters.UserDetailAdapter
import com.dicoding.githubuser.databinding.FragmentFollowingBinding
import com.dicoding.githubuser.viewmodels.MainViewModel

class FollowingFragment : Fragment() {

    lateinit var binding: FragmentFollowingBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var followingAdapter: UserDetailAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followingAdapter = UserDetailAdapter()

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = followingAdapter

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get()

        mainViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        mainViewModel.isEmptyFollowing.observe(viewLifecycleOwner) {
            binding.llEmpty.visibility = if(it) View.VISIBLE else View.GONE
        }

        mainViewModel.following.observe(viewLifecycleOwner) { response ->
            followingAdapter.mUsers = response
            followingAdapter.notifyDataSetChanged()
        }

        mainViewModel.getFollowing(activity?.intent?.getStringExtra("login")!!)

    }
}
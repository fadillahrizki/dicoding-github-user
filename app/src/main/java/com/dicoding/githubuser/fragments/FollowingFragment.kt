package com.dicoding.githubuser.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.adapters.UserDetailAdapter
import com.dicoding.githubuser.databinding.FragmentFollowingBinding
import com.dicoding.githubuser.viewmodels.MainViewModel
import com.dicoding.githubuser.viewmodels.ViewModelFactory

class FollowingFragment : Fragment() {

    private lateinit var binding: FragmentFollowingBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var followingAdapter: UserDetailAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followingAdapter = UserDetailAdapter()

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = followingAdapter

        val factory = ViewModelFactory.getInstance(requireActivity().application)
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        mainViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        mainViewModel.isEmptyFollowing.observe(viewLifecycleOwner) {
            binding.llEmpty.visibility = if (it) View.VISIBLE else View.GONE
        }

        mainViewModel.following.observe(viewLifecycleOwner) { response ->
            followingAdapter.mUsers = response
            followingAdapter.notifyDataSetChanged()
        }

        if (savedInstanceState == null) {
            mainViewModel.getFollowing(activity?.intent?.getStringExtra("login")!!)
        }

    }

    override fun onResume() {
        super.onResume()
        setProperHeightOfView()
    }

    private fun setProperHeightOfView() {
        val layoutView = binding.root
        if (layoutView != null) {
            val layoutParams = layoutView.layoutParams
            if (layoutParams != null) {
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                layoutView.requestLayout()
            }
        }
    }
}
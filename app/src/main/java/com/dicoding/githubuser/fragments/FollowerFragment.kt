package com.dicoding.githubuser.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.R
import com.dicoding.githubuser.adapters.UserDetailAdapter
import com.dicoding.githubuser.databinding.FragmentFollowerBinding
import com.dicoding.githubuser.viewmodels.MainViewModel
import com.dicoding.githubuser.viewmodels.ViewModelFactory


class FollowerFragment : Fragment() {

    private lateinit var binding: FragmentFollowerBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var followerAdapter: UserDetailAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowerBinding.bind(
            inflater.inflate(
                R.layout.fragment_follower,
                container,
                false
            )
        )
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followerAdapter = UserDetailAdapter()

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = followerAdapter
        binding.recyclerView.setHasFixedSize(true)

        val factory = ViewModelFactory.getInstance(requireActivity().application)
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        mainViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        mainViewModel.isEmptyFollowers.observe(viewLifecycleOwner) {
            binding.llEmpty.visibility = if (it) View.VISIBLE else View.GONE
        }

        mainViewModel.followers.observe(viewLifecycleOwner) { response ->
            followerAdapter.mUsers = response
            followerAdapter.notifyDataSetChanged()
        }

        if (savedInstanceState == null) {
            mainViewModel.getFollowers(activity?.intent?.getStringExtra("login")!!)
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
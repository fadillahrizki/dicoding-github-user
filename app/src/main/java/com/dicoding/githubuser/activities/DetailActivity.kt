package com.dicoding.githubuser.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Adapter
import android.widget.LinearLayout
import androidx.annotation.StringRes
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.githubuser.R
import com.dicoding.githubuser.adapters.DetailPagerAdapter
import com.dicoding.githubuser.databinding.ActivityDetailBinding
import com.dicoding.githubuser.databinding.ActivityMainBinding
import com.dicoding.githubuser.models.User
import com.dicoding.githubuser.viewmodels.MainViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailBinding
    lateinit var mainViewModel: MainViewModel

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val detailPagerAdapter = DetailPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = detailPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get()

        mainViewModel.isLoadingDetail.observe(this) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        mainViewModel.isErrorDetail.observe(this) {
            binding.llError.visibility = if(it) View.VISIBLE else View.GONE
            binding.llData.visibility = if (it) View.GONE else View.VISIBLE
        }

        mainViewModel.user.observe(this) {
            setDetail(it)
        }

        mainViewModel.getDetailUser(intent.getStringExtra("login")!!)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setDetail(user: User){
        Glide
            .with(this)
            .load(user.avatarUrl)
            .circleCrop()
            .placeholder(R.drawable.placeholder)
            .into(binding.avatar)

        binding.name.text = user.name
        binding.login.text = user.login
        binding.followers.text = user.followers.toString()
        binding.following.text = user.following.toString()
        binding.company.text = user.company ?: "-"
        binding.location.text = user.location ?: "-"
    }
}
package com.dicoding.githubuser.activities

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.githubuser.R
import com.dicoding.githubuser.adapters.DetailPagerAdapter
import com.dicoding.githubuser.databinding.ActivityDetailBinding
import com.dicoding.githubuser.models.User
import com.dicoding.githubuser.utils.SettingPreferences
import com.dicoding.githubuser.viewmodels.MainViewModel
import com.dicoding.githubuser.viewmodels.UserViewModel
import com.dicoding.githubuser.viewmodels.ViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var pref: SettingPreferences
    private var isDarkMode: Boolean =  false
    private lateinit var userViewModel: UserViewModel
    private lateinit var user: User
    private var isFavorite: Boolean = false

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

        pref = SettingPreferences.getInstance(dataStore)

        val detailPagerAdapter = DetailPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = detailPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager, true, true) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        val factory = ViewModelFactory.getInstance(this.application)
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        userViewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]

        mainViewModel.isLoadingDetail.observe(this) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE

            if (it) {
                binding.llError.visibility = View.GONE
            }
        }

        mainViewModel.isError.observe(this) {
            binding.llError.visibility = if (it) View.VISIBLE else View.GONE
            binding.llData.visibility = if (it) View.GONE else View.VISIBLE
        }

        if (savedInstanceState == null) {
            mainViewModel.getDetailUser(intent.getStringExtra("login")!!)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.change_theme -> {
                mainViewModel.saveThemeSetting(pref, !isDarkMode)
                return true
            }
            R.id.favorite -> {
//                val i = Intent(this, MenuActivity::class.java)
//                startActivity(i)

                if(isFavorite) {
                    userViewModel.delete(user)
                    Toast.makeText(applicationContext, getString(R.string.favorite_delete), Toast.LENGTH_LONG).show()
                } else {
                    userViewModel.insert(user)
                    Toast.makeText(applicationContext, getString(R.string.favorite_insert), Toast.LENGTH_LONG).show()
                }

                return true
            }
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.detail_option_menu, menu)

        mainViewModel.getThemeSettings(pref).observe(this) { isDarkModeActive: Boolean ->

            isDarkMode = isDarkModeActive

            if (isDarkModeActive) {
                menu.findItem(R.id.change_theme).setIcon(R.drawable.ic_light_mode)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                menu.findItem(R.id.change_theme).setIcon(R.drawable.ic_dark_mode)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        mainViewModel.user.observe(this) {
            setDetail(it)
            user = it

            mainViewModel.getFavoriteUser(user.id!!).observe(this) {favorite ->

                isFavorite = favorite != null

                if(favorite != null) {
                    menu.findItem(R.id.favorite).setIcon(R.drawable.ic_favorite)
                } else {
                    menu.findItem(R.id.favorite).setIcon(R.drawable.ic_favorite_border)
                }
            }
        }

        return true
    }

    private fun setDetail(user: User) {
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
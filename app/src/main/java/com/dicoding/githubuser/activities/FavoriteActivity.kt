package com.dicoding.githubuser.activities

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.R
import com.dicoding.githubuser.adapters.FavoriteAdapter
import com.dicoding.githubuser.databinding.ActivityFavoriteBinding
import com.dicoding.githubuser.utils.SettingPreferences
import com.dicoding.githubuser.viewmodels.MainViewModel
import com.dicoding.githubuser.viewmodels.UserViewModel
import com.dicoding.githubuser.viewmodels.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var pref: SettingPreferences
    private var isDarkMode: Boolean =  false
    private lateinit var userViewModel: UserViewModel
    private lateinit var favoriteAdapter: FavoriteAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val factory = ViewModelFactory.getInstance(this.application)
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        userViewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]
        pref = SettingPreferences.getInstance(dataStore)
        favoriteAdapter = FavoriteAdapter()

        binding.rvData.layoutManager = LinearLayoutManager(this)
        binding.rvData.adapter = favoriteAdapter

        mainViewModel.getFavoriteUsers().observe(this) {
            favoriteAdapter.mUsers = it
            favoriteAdapter.notifyDataSetChanged()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.change_theme -> {
                mainViewModel.saveThemeSetting(pref, !isDarkMode)
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
        inflater.inflate(R.menu.favorite_option_menu, menu)

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

        return true
    }
}
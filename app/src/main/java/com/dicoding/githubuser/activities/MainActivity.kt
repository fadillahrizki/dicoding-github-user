package com.dicoding.githubuser.activities

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.R
import com.dicoding.githubuser.adapters.SearchAdapter
import com.dicoding.githubuser.databinding.ActivityMainBinding
import com.dicoding.githubuser.utils.SettingPreferences
import com.dicoding.githubuser.viewmodels.MainViewModel
import com.dicoding.githubuser.viewmodels.ViewModelFactory


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var pref: SettingPreferences
    private var isDarkMode: Boolean =  false

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        searchAdapter = SearchAdapter()
        pref = SettingPreferences.getInstance(dataStore)

        binding.rvSearch.layoutManager = LinearLayoutManager(this)
        binding.rvSearch.adapter = searchAdapter

        val factory = ViewModelFactory.getInstance(this.application)
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        mainViewModel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE

            if (it) {
                binding.llError.visibility = View.GONE
                binding.llEmpty.visibility = View.GONE
                binding.rvSearch.visibility = View.GONE
            }
        }

        mainViewModel.isSearch.observe(this) {
            binding.llSearch.visibility = if (it) View.GONE else View.VISIBLE
        }

        mainViewModel.isEmpty.observe(this) {
            binding.llEmpty.visibility = if (it) View.VISIBLE else View.GONE
        }

        mainViewModel.search.observe(this) { response ->
            searchAdapter.mUsers = response.items!!
            searchAdapter.notifyDataSetChanged()
        }

        mainViewModel.isError.observe(this) {
            binding.llError.visibility = if (it) View.VISIBLE else View.GONE
            binding.rvSearch.visibility = if (it) View.GONE else View.VISIBLE
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.change_theme -> {
                mainViewModel.saveThemeSetting(pref, !isDarkMode)
                return true
            }
            R.id.favorite -> {
                startActivity(Intent(this, FavoriteActivity::class.java))
                return true
            }
            else -> return true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

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

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.searchUsers(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }
}
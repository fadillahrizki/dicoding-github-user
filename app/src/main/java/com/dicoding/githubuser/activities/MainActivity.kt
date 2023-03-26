package com.dicoding.githubuser.activities

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.R
import com.dicoding.githubuser.adapters.SearchAdapter
import com.dicoding.githubuser.databinding.ActivityMainBinding
import com.dicoding.githubuser.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var searchAdapter: SearchAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        searchAdapter = SearchAdapter()

        binding.rvSearch.layoutManager = LinearLayoutManager(this)
        binding.rvSearch.adapter = searchAdapter

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get()

        mainViewModel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE

            if (it) {
                binding.llError.visibility = View.GONE
                binding.llEmpty.visibility = View.GONE
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

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
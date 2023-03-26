package com.dicoding.githubuser.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.models.SearchResponse
import com.dicoding.githubuser.models.User
import com.dicoding.githubuser.models.UserDetail
import com.dicoding.githubuser.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _searchResponse = MutableLiveData<SearchResponse>()
    private val _user = MutableLiveData<User>()
    private val _followers = MutableLiveData<ArrayList<UserDetail>>()
    private val _following = MutableLiveData<ArrayList<UserDetail>>()
    private val _isLoading = MutableLiveData<Boolean>()
    private val _isEmpty = MutableLiveData<Boolean>()
    private val _isSearch = MutableLiveData<Boolean>()
    private val _isLoadingDetail = MutableLiveData<Boolean>()
    private val _isError = MutableLiveData<Boolean>()

    private val _isEmptyFollowers = MutableLiveData<Boolean>()
    private val _isEmptyFollowing = MutableLiveData<Boolean>()

    val search: LiveData<SearchResponse> = _searchResponse
    val user: LiveData<User> = _user
    val followers: LiveData<ArrayList<UserDetail>> = _followers
    val following: LiveData<ArrayList<UserDetail>> = _following
    val isLoading: LiveData<Boolean> = _isLoading
    val isEmpty: LiveData<Boolean> = _isEmpty
    val isSearch: LiveData<Boolean> = _isSearch

    val isLoadingDetail: LiveData<Boolean> = _isLoadingDetail
    val isError: LiveData<Boolean> = _isError

    val isEmptyFollowers: LiveData<Boolean> = _isEmptyFollowers
    val isEmptyFollowing: LiveData<Boolean> = _isEmptyFollowing

    fun searchUsers(query: String) {
        _isSearch.value = true
        _isLoading.value = true
        val client = ApiConfig.getApiService().search(query)
        client.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _searchResponse.value = response.body()

                    _isEmpty.value = _searchResponse.value!!.items!!.isEmpty()
                    _isError.value = false

                    Log.d("Search Users", "onSuccess: ${response.body()}")
                } else {
                    _isError.value = true
                    Log.e("Search Users", "onFailure: ${response.raw()}")
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.e("Search Users", "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun getDetailUser(username: String) {
        _isLoadingDetail.value = true
        val client = ApiConfig.getApiService().detail(username)
        client.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                _isLoadingDetail.value = false

                if (response.isSuccessful) {
                    _user.value = response.body()
                    _isError.value = false
                    Log.d("Detail User", "onSuccess: ${response.body()}")
                } else {
                    _isError.value = true
                    Log.e("Detail User", "onFailure: ${response.raw()}")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                _isLoadingDetail.value = false
                _isError.value = true
                Log.e("Detail User", "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun getFollowers(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().followers(username)
        client.enqueue(object : Callback<ArrayList<UserDetail>> {
            override fun onResponse(
                call: Call<ArrayList<UserDetail>>,
                response: Response<ArrayList<UserDetail>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _followers.value = response.body()

                    _isEmptyFollowers.value = _followers.value!!.isEmpty()

                    Log.d("Get Followers", "onSuccess: ${response.body()}")
                } else {
                    Log.e("Get Followers", "onFailure: ${response.raw()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<UserDetail>>, t: Throwable) {
                _isLoading.value = false
                Log.e("Get Followers", "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun getFollowing(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().following(username)
        client.enqueue(object : Callback<ArrayList<UserDetail>> {
            override fun onResponse(
                call: Call<ArrayList<UserDetail>>,
                response: Response<ArrayList<UserDetail>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _following.value = response.body()

                    _isEmptyFollowing.value = _following.value!!.isEmpty()

                    Log.d("Get Following", "onSuccess: ${response.body()}")
                } else {
                    Log.e("Get Following", "onFailure: ${response.raw()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<UserDetail>>, t: Throwable) {
                _isLoading.value = false
                Log.e("Get Followings", "onFailure: ${t.message.toString()}")
            }

        })
    }
}
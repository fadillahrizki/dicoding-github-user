package com.dicoding.githubuser.network

import com.dicoding.githubuser.models.UserDetail
import com.dicoding.githubuser.models.SearchResponse
import com.dicoding.githubuser.models.User
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("/search/users")
    fun search(@Query("q") query:String): Call<SearchResponse>

    @GET("/users/{username}")
    fun detail(@Path("username") username:String): Call<User>

    @GET("/users/{username}/followers")
    fun followers(@Path("username") username:String): Call<ArrayList<UserDetail>>

    @GET("/users/{username}/following")
    fun following(@Path("username") username:String): Call<ArrayList<UserDetail>>
}
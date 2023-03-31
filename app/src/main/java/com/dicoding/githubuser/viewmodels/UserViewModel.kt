package com.dicoding.githubuser.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.models.User
import com.dicoding.githubuser.repositories.UserRepository

class UserViewModel(application: Application) : ViewModel() {
    private val mUserRepository: UserRepository = UserRepository(application)
    fun insert(user: User) {
        mUserRepository.insert(user)
    }
    fun delete(user: User) {
        mUserRepository.delete(user)
    }
}

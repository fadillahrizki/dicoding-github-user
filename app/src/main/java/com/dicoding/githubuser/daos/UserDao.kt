package com.dicoding.githubuser.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dicoding.githubuser.models.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)

    @Query("SELECT * from user ORDER BY id ASC")
    fun getAllUsers(): LiveData<List<User>>

    @Query("SELECT * from user where id=:id")
    fun getUser(id: Int): LiveData<User>
}
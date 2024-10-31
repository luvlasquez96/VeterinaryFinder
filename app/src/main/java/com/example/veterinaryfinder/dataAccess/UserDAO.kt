package com.example.veterinaryfinder.dataAccess

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.veterinaryfinder.model.User

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    suspend fun login(username: String, password: String): User?
}
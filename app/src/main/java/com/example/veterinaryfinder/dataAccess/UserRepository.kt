package com.example.veterinaryfinder.dataAccess

import com.example.veterinaryfinder.model.User

class UserRepository(private val userDao: UserDao) {
    suspend fun login(username: String, password: String) {
        userDao.login(username, password)
    }

    suspend fun register(username: String, password: String) {
        userDao.insertUser(User(username = username, password = password))
    }
}
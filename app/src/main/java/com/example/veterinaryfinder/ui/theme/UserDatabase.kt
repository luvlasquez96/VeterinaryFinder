package com.example.veterinaryfinder.ui.theme

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.veterinaryfinder.dataAccess.UserDao
import com.example.veterinaryfinder.model.User

@Database(entities = [User::class], version = 1)
abstract class UserDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
}
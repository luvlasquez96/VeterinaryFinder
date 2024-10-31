package com.example.veterinaryfinder

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.veterinaryfinder.dataAccess.VeterinaryDao
import com.example.veterinaryfinder.model.Veterinary

@Database(entities = [Veterinary::class], version = 1)
abstract class VeterinaryDatabase : RoomDatabase() {
    abstract fun veterinaryDao(): VeterinaryDao
}
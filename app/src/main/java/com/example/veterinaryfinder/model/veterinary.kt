package com.example.veterinaryfinder.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "veterinaries")
data class Veterinary(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val address: String,
    val phone: String,
    val image: String?,
    val webSite: String
)
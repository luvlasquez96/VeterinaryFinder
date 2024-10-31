package com.example.veterinaryfinder.dataAccess

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.veterinaryfinder.model.Veterinary
import kotlinx.coroutines.flow.Flow

@Dao
interface VeterinaryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVeterinary(veterinary: Veterinary)

    @Query("SELECT * FROM veterinaries WHERE name LIKE '%' || :query || '%'")
    fun getVeterinaries(query: String): Flow<List<Veterinary>>

    @Delete
    suspend fun delete(veterinary: Veterinary)

    @Update
    suspend fun updateVeterinary(veterinary: Veterinary)

    @Query("SELECT * FROM veterinaries WHERE id = :veterinaryId LIMIT 1")
    suspend fun getVeterinaryById(veterinaryId: Int): Veterinary
}
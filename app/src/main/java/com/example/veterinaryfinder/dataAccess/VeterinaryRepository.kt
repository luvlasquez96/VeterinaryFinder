package com.example.veterinaryfinder.dataAccess

import com.example.veterinaryfinder.model.Veterinary
import kotlinx.coroutines.flow.Flow

class VeterinaryRepository(private val veterinaryDao: VeterinaryDao) {

    suspend fun insertVeterinary(veterinary: Veterinary) {
        veterinaryDao.insertVeterinary(veterinary)
    }

    fun searchVeterinaries(query: String): Flow<List<Veterinary>> {
        return veterinaryDao.getVeterinaries(query)
    }

    suspend fun deleteVeterinary(veterinary: Veterinary) {
        veterinaryDao.delete(veterinary)
    }

    suspend fun updateVeterinary(veterinary: Veterinary) {
        veterinaryDao.updateVeterinary(veterinary)
    }

    suspend fun getVeterinaryById(veterinaryId: Int): Veterinary {
        return veterinaryDao.getVeterinaryById(veterinaryId)
    }
}
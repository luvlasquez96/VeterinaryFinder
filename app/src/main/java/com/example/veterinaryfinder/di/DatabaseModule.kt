package com.example.veterinaryfinder.di

import android.content.Context
import androidx.room.Room
import com.example.veterinaryfinder.VeterinaryDatabase
import com.example.veterinaryfinder.dataAccess.UserDao
import com.example.veterinaryfinder.dataAccess.UserRepository
import com.example.veterinaryfinder.dataAccess.VeterinaryDao
import com.example.veterinaryfinder.dataAccess.VeterinaryRepository
import com.example.veterinaryfinder.ui.theme.UserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): VeterinaryDatabase {
        return Room.databaseBuilder(
            context,
            VeterinaryDatabase::class.java,
            "veterinaria_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDatabase(@ApplicationContext context: Context): UserDatabase {
        return Room.databaseBuilder(
            context,
            UserDatabase::class.java,
            "user_db"
        ).build()
    }

    @Provides
    fun provideVeterinaryDao(database: VeterinaryDatabase): VeterinaryDao {
        return database.veterinaryDao()
    }

    @Provides
    fun provideUserDao(database: UserDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    fun provideVeterinaryRepository(veterinaryDao: VeterinaryDao): VeterinaryRepository {
        return VeterinaryRepository(veterinaryDao)
    }

    @Provides
    fun provideUserRepository(userDao: UserDao): UserRepository {
        return UserRepository(userDao)
    }
}
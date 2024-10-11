package com.accessment.task.di

import android.content.Context
import androidx.room.Room
import com.accessment.task.data.database.AppDatabase
import com.accessment.task.data.database.MedResponseDao
import com.accessment.task.data.network.ApiService
import com.accessment.task.data.network.RetrofitInstance
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiService = RetrofitInstance.api

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    // Provide Room Database (AppDatabase)
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "medicine_database"
        ).build()
    }

    // Provide MedResponseDao from AppDatabase
    @Provides
    @Singleton
    fun provideMedResponseDao(appDatabase: AppDatabase): MedResponseDao {
        return appDatabase.medicineResponseDao()
    }
}
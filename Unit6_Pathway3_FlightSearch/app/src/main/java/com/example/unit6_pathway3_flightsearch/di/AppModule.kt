package com.example.unit6_pathway3_flightsearch.di

import android.content.Context
import androidx.room.Room
import com.example.unit6_pathway3_flightsearch.data.AppDatabase
import com.example.unit6_pathway3_flightsearch.data.FlightDao
import com.example.unit6_pathway3_flightsearch.data.FlightRepository
import com.example.unit6_pathway3_flightsearch.data.FlightRepositoryImpl
import com.example.unit6_pathway3_flightsearch.data.UserPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindFlightRepository(
        flightRepositoryImpl: FlightRepositoryImpl
    ): FlightRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "flight_search"
        )
            .createFromAsset("database/flight_search.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideFlightDao(appDatabase: AppDatabase): FlightDao {
        return appDatabase.flightDao()
    }

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(
        @ApplicationContext context: Context
    ): UserPreferencesRepository {
        return UserPreferencesRepository(context)
    }
}


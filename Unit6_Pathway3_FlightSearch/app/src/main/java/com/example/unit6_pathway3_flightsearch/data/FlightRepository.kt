package com.example.unit6_pathway3_flightsearch.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface FlightRepository {
    fun getAirportSuggestions(query: String): Flow<List<Airport>>
    fun getAllAirports(): Flow<List<Airport>>
    suspend fun getFavoriteFlight(departureCode: String, destinationCode: String): Favorite?
    fun getFavoriteFlights(): Flow<List<FavoriteFlight>>
    suspend fun insertFavoriteFlight(favorite: Favorite)
    suspend fun deleteFavoriteFlight(favorite: Favorite)
}

class FlightRepositoryImpl @Inject constructor(
    private val flightDao: FlightDao
) : FlightRepository {
    override fun getAirportSuggestions(query: String): Flow<List<Airport>> = flightDao.getAirportSuggestions(query)
    override fun getAllAirports(): Flow<List<Airport>> = flightDao.getAllAirports()
    override suspend fun getFavoriteFlight(departureCode: String, destinationCode: String): Favorite? = flightDao.getFavoriteFlight(departureCode, destinationCode)
    override fun getFavoriteFlights(): Flow<List<FavoriteFlight>> = flightDao.getFavoriteFlights()
    override suspend fun insertFavoriteFlight(favorite: Favorite) = flightDao.insertFavoriteFlight(favorite)
    override suspend fun deleteFavoriteFlight(favorite: Favorite) = flightDao.deleteFavoriteFlight(favorite)
}

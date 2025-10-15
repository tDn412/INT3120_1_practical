package com.example.unit6_pathway3_flightsearch.data

import kotlinx.coroutines.flow.Flow

interface FlightRepository {
    fun getAirportSuggestions(query: String): Flow<List<Airport>>
    fun getAllFavoriteFlights(): Flow<List<FavoriteFlight>>
    fun getAirportByCode(iataCode: String): Flow<Airport>
    fun getAllOtherAirports(iataCode: String): Flow<List<Airport>>
    suspend fun insertFavorite(favorite: Favorite)
    suspend fun deleteFavorite(favorite: Favorite)
    fun getFavorite(departureCode: String, destinationCode: String): Flow<Favorite?>
}

class OfflineFlightRepository(private val flightDao: FlightDao) : FlightRepository {
    override fun getAirportSuggestions(query: String): Flow<List<Airport>> =
        flightDao.getAirportSuggestions(query)

    override fun getAllFavoriteFlights(): Flow<List<FavoriteFlight>> =
        flightDao.getAllFavoriteFlights()

    override fun getAirportByCode(iataCode: String): Flow<Airport> =
        flightDao.getAirportByCode(iataCode)

    override fun getAllOtherAirports(iataCode: String): Flow<List<Airport>> =
        flightDao.getAllOtherAirports(iataCode)

    override suspend fun insertFavorite(favorite: Favorite) =
        flightDao.insertFavorite(favorite)

    override suspend fun deleteFavorite(favorite: Favorite) =
        flightDao.deleteFavorite(favorite)

    override fun getFavorite(departureCode: String, destinationCode: String): Flow<Favorite?> =
        flightDao.getFavorite(departureCode, destinationCode)
}
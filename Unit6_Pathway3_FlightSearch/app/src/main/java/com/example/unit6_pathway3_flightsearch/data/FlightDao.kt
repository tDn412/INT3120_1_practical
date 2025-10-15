package com.example.unit6_pathway3_flightsearch.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FlightDao {
    @Query(
        """
        SELECT * FROM airport 
        WHERE name LIKE '%' || :query || '%' OR iata_code LIKE '%' || :query || '%' 
        ORDER BY passengers DESC
        """
    )
    fun getAirportSuggestions(query: String): Flow<List<Airport>>

    @Query("SELECT * FROM airport ORDER BY passengers DESC")
    fun getAllAirports(): Flow<List<Airport>>

    @Query("SELECT * FROM favorite WHERE departure_code = :departureCode AND destination_code = :destinationCode")
    suspend fun getFavoriteFlight(departureCode: String, destinationCode: String): Favorite?

    @Query(
        """
        SELECT f.id, 
               f.departure_code AS departureCode,
               dep.name AS departureName,
               f.destination_code AS destinationCode,
               dest.name AS destinationName
        FROM favorite AS f
        JOIN airport AS dep ON f.departure_code = dep.iata_code
        JOIN airport AS dest ON f.destination_code = dest.iata_code
        ORDER BY f.id DESC
        """
    )
    fun getFavoriteFlights(): Flow<List<FavoriteFlight>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteFlight(favorite: Favorite)

    @Delete
    suspend fun deleteFavoriteFlight(favorite: Favorite)
}

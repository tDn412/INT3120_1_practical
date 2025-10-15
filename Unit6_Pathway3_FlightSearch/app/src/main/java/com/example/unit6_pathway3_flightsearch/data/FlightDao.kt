package com.example.unit6_pathway3_flightsearch.data

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FlightDao {
    // Lấy danh sách sân bay gợi ý dựa trên truy vấn
    @Query(
        """
        SELECT * FROM airport 
        WHERE name LIKE '%' || :query || '%' OR iata_code LIKE '%' || :query || '%' 
        ORDER BY passengers DESC
        """
    )
    fun getAirportSuggestions(query: String): Flow<List<Airport>>

    // Lấy tất cả các chuyến bay yêu thích, kết hợp thông tin tên sân bay
    @Query(
        """
        SELECT 
            f.id, 
            f.departure_code, 
            dep.name as departure_name, 
            f.destination_code, 
            dest.name as destination_name
        FROM favorite AS f
        JOIN airport AS dep ON f.departure_code = dep.iata_code
        JOIN airport AS dest ON f.destination_code = dest.iata_code
        ORDER BY f.id DESC
        """
    )
    fun getAllFavoriteFlights(): Flow<List<FavoriteFlight>>

    // Lấy một sân bay cụ thể bằng mã IATA
    @Query("SELECT * from airport WHERE iata_code = :iataCode")
    fun getAirportByCode(iataCode: String): Flow<Airport>

    // Lấy tất cả sân bay TRỪ sân bay đã chọn (để làm danh sách điểm đến)
    @Query("SELECT * from airport WHERE iata_code != :iataCode ORDER BY name ASC")
    fun getAllOtherAirports(iataCode: String): Flow<List<Airport>>

    // Thêm một chuyến bay vào danh sách yêu thích
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(favorite: Favorite)

    // Xóa một chuyến bay khỏi danh sách yêu thích
    @Delete
    suspend fun deleteFavorite(favorite: Favorite)

    // Lấy một chuyến bay yêu thích cụ thể
    @Query("SELECT * FROM favorite WHERE departure_code = :departureCode AND destination_code = :destinationCode")
    fun getFavorite(departureCode: String, destinationCode: String): Flow<Favorite?>
}

// Data class tùy chỉnh để chứa kết quả từ câu lệnh JOIN
data class FavoriteFlight(
    val id: Int,
    @ColumnInfo(name = "departure_code")
    val departureCode: String,
    @ColumnInfo(name = "departure_name")
    val departureName: String,
    @ColumnInfo(name = "destination_code")
    val destinationCode: String,
    @ColumnInfo(name = "destination_name")
    val destinationName: String
)
package com.example.unit6_pathway3_flightsearch.data

/**
 * Lớp dữ liệu để chứa kết quả từ câu lệnh JOIN
 * giữa bảng 'favorite' và 'airport'.
 */
data class FavoriteFlight(
    val id: Int,
    val departureCode: String,
    val departureName: String,
    val destinationCode: String,
    val destinationName: String
)

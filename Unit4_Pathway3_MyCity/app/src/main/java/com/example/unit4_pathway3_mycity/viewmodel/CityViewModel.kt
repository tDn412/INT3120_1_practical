package com.example.unit4_pathway3_mycity.viewmodel

import androidx.lifecycle.ViewModel
import com.example.unit4_pathway3_mycity.model.Place

class CityViewModel : ViewModel() {
    private val places = listOf(
        Place(1, "Café A", "Cà phê view đẹp", "Cafe"),
        Place(2, "Café B", "Cà phê sách", "Cafe"),
        Place(3, "Nhà hàng X", "Ẩm thực Việt Nam", "Restaurant"),
        Place(4, "Nhà hàng Y", "Ẩm thực Nhật", "Restaurant"),
        Place(5, "Công viên Hoa", "Công viên nhiều hoa", "Park"),
        Place(6, "Công viên Xanh", "Rộng rãi, thoáng mát", "Park")
    )

    fun getCategories(): List<String> =
        places.map { it.category }.distinct()

    fun getPlacesByCategory(category: String): List<Place> =
        places.filter { it.category == category }

    fun getPlaceById(id: Int?): Place? {
        return places.find { it.id == id }
    }
}

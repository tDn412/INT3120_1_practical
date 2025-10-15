package com.example.unit4_pathway3_mycity.navigation

sealed class Screen(val route: String) {
    object Categories : Screen("categories")
    object Recommendations : Screen("recommendations/{category}") {
        fun createRoute(category: String) = "recommendations/$category"
    }
    object Detail : Screen("detail/{id}") {
        fun createRoute(id: Int) = "detail/$id"
    }
}

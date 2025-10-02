package com.example.unit3_pathway1_higherorderfunctionswcollections

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.unit3_pathway1_higherorderfunctionswcollections.ui.theme.Unit3_Pathway1_HigherOrderFunctionsWCollectionsTheme

class Cookie(
    val name: String,
    val softBaked: Boolean,
    val hasFilling: Boolean,
    val price: Double
)

val cookies = listOf(
    Cookie(
        name = "Chocolate Chip",
        softBaked = false,
        hasFilling = false,
        price = 1.69
    ),
    Cookie(
        name = "Banana Walnut",
        softBaked = true,
        hasFilling = false,
        price = 1.49
    ),
    Cookie(
        name = "Vanilla Creme",
        softBaked = false,
        hasFilling = true,
        price = 1.59
    ),
    Cookie(
        name = "Chocolate Peanut Butter",
        softBaked = false,
        hasFilling = true,
        price = 1.49
    ),
    Cookie(
        name = "Snickerdoodle",
        softBaked = true,
        hasFilling = false,
        price = 1.39
    ),
    Cookie(
        name = "Blueberry Tart",
        softBaked = true,
        hasFilling = true,
        price = 1.79
    ),
    Cookie(
        name = "Sugar and Sprinkles",
        softBaked = false,
        hasFilling = false,
        price = 1.39
    )
)


fun main() {
//    val fullMenu = cookies.map {
//        "${it.name} - $${it.price}"
//    }
//    val softBakedMenu = cookies.filter {
//        it.softBaked
//    }
//
//    cookies.forEach {
//        println("Menu item: ${it.name}")
//    }
//    fullMenu.forEach {
//        println(it)
//    }
//    softBakedMenu.forEach {
//        println("${it.name} - $${it.price}")
//    }

//    val groupedMenu = cookies.groupBy { it.softBaked }
//    val softBakedMenu = groupedMenu[true] ?: listOf()
//    val crunchyMenu = groupedMenu[false] ?: listOf()
//    println(groupedMenu.size)
//    println("Soft cookies:")
//    softBakedMenu.forEach {
//        println("${it.name} - $${it.price}")
//    }
//    println("Crunchy cookies:")
//    crunchyMenu.forEach {
//        println("${it.name} - $${it.price}")
//    }

//    val totalPrice = cookies.fold(0.0) {total, phantu ->
//        total + phantu.price
//    }
//    println("Total price: $${totalPrice}")

    val alphabeticalMenu = cookies.sortedBy {
        it.name
    }
    println("Alphabetical menu:")
    alphabeticalMenu.forEach {
        println(it.name)
    }
}
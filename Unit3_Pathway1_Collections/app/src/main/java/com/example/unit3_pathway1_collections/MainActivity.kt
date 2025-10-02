package com.example.unit3_pathway1_collections

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
import com.example.unit3_pathway1_collections.ui.theme.Unit3_Pathway1_CollectionsTheme

fun main() {
//    val rockPlanets = arrayOf<String>("Mercury", "Venus", "Earth", "Mars")
//    for (i in rockPlanets.indices) {
//        println(rockPlanets[i])
//    }

//    val solarSystem = mutableListOf("Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune")
//    solarSystem.add("Pluto")
//    solarSystem.add(3, "Theia")
//    for (planet in solarSystem) {
//        println(planet)
//    }

//    val solarSystem = mutableSetOf("Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune")
//    println(solarSystem.size)
//    solarSystem.add("Pluto")
//    println(solarSystem.size)
//    println(solarSystem.contains("Pluto"))

    val solarSystem = mutableMapOf(
        "Mercury" to 0,
        "Venus" to 0,
        "Earth" to 1,
        "Mars" to 2,
        "Jupiter" to 79,
        "Saturn" to 82,
        "Uranus" to 27,
        "Neptune" to 14
    )
    println(solarSystem.size)
    solarSystem["Pluto"] = 5
    println(solarSystem.size)
    println(solarSystem["Pluto"])
    println(solarSystem.get("Theia"))
}


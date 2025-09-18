package com.example.unit1_pathway1_helloworld

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.unit1_pathway1_helloworld.ui.theme.Unit1_Pathway1_HelloWorldTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Unit1_Pathway1_HelloWorldTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    Greeting()
                }
            }
        }
    }
}

fun main() {
    println("I'm")
    println("learning")
    println("Kotlin!")
    println()
    println("Monday")
    println("Tuesday")
    println("Wednesday")
    println("Thursday")
    println("Friday")
    println()
    println("Tomorrow is rainy")
    println()
    println("There is a chance of snow")
    println()
    println("Cloudy")
    println("Partly Cloudy")
    println("Windy")
    println()
    println("How's the weather today?")
}

@Composable
fun Greeting() {
    Text(text = "Hello World!")
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Unit1_Pathway1_HelloWorldTheme {
        Greeting()
    }
}

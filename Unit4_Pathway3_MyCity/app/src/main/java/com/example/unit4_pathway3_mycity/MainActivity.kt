package com.example.unit4_pathway3_mycity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.unit4_pathway3_mycity.ui.theme.Unit4_Pathway3_MyCityTheme
import com.example.unit4_pathway3_mycity.MyCityApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Unit4_Pathway3_MyCityTheme {
                val navController = rememberNavController()
                MyCityApp(navController)
            }
        }
    }
}

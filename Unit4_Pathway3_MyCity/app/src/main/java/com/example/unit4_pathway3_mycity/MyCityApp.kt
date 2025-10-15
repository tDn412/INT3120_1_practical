package com.example.unit4_pathway3_mycity

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.unit4_pathway3_mycity.model.Place
import com.example.unit4_pathway3_mycity.viewmodel.CityViewModel
import com.example.unit4_pathway3_mycity.navigation.Screen

@Composable
fun MyCityApp(navController: NavHostController, viewModel: CityViewModel = viewModel()) {
    NavHost(navController = navController, startDestination = Screen.Categories.route) {
        composable(Screen.Categories.route) {
            CategoryScreen(viewModel.getCategories()) { category ->
                navController.navigate(Screen.Recommendations.createRoute(category))
            }
        }
        composable(Screen.Recommendations.route) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            RecommendationListScreen(
                places = viewModel.getPlacesByCategory(category),
                onPlaceClick = { navController.navigate(Screen.Detail.createRoute(it.id)) }
            )
        }
        composable(Screen.Detail.route) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            val place = id?.let { viewModel.getPlaceById(it) }
            place?.let {
                DetailScreen(it)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(categories: List<String>, onCategoryClick: (String) -> Unit) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("My City") }) }
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            items(categories) { category ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { onCategoryClick(category) }
                ) {
                    Text(category, modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendationListScreen(places: List<Place>, onPlaceClick: (Place) -> Unit) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Recommendations") }) }
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            items(places) { place ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { onPlaceClick(place) }
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(place.name, style = MaterialTheme.typography.titleLarge)
                        Text(place.description, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(place: Place) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(place.name) }) }
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            Text(place.name, style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(8.dp))
            Text(place.description)
        }
    }
}




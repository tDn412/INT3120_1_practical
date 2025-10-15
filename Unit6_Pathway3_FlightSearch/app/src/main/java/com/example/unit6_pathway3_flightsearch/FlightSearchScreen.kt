package com.example.unit6_pathway3_flightsearch.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unit6_pathway3_flightsearch.FlightSearchViewModel
import com.example.unit6_pathway3_flightsearch.data.Airport
import com.example.unit6_pathway3_flightsearch.data.FavoriteFlight
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightSearchScreen(
    viewModel: FlightSearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Flight Search") })
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
            SearchBar(
                query = uiState.searchQuery,
                onQueryChange = viewModel::onQueryChange
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Hiển thị nội dung dựa trên trạng thái
            if (uiState.searchQuery.isBlank()) {
                FavoriteRoutesList(
                    favoriteFlights = uiState.favoriteFlights,
                    onToggleFavorite = viewModel::toggleFavorite
                )
            } else if (uiState.selectedAirport == null) {
                SuggestionsList(
                    suggestions = uiState.suggestions,
                    onAirportSelected = viewModel::onAirportSelected
                )
            } else {
                FlightResultsList(
                    departureAirport = uiState.selectedAirport!!,
                    flights = uiState.flights,
                    favoriteFlights = uiState.favoriteFlights, // Thêm danh sách yêu thích
                    onToggleFavorite = viewModel::toggleFavorite
                    // Xóa viewModel khỏi đây
                )
            }
        }
    }
}

@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        label = { Text("Enter departure airport") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}

@Composable
fun SuggestionsList(
    suggestions: List<Airport>,
    onAirportSelected: (Airport) -> Unit
) {
    LazyColumn {
        items(suggestions) { airport ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAirportSelected(airport) }
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = airport.iataCode,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.width(60.dp)
                )
                Text(text = airport.name)
            }
        }
    }
}

@Composable
fun FlightResultsList(
    departureAirport: Airport,
    flights: List<Airport>,
    favoriteFlights: List<FavoriteFlight>, // Thêm tham số này
    onToggleFavorite: (String, String) -> Unit
    // Xóa viewModel khỏi đây
) {
    Column {
        Text("Flights from ${departureAirport.iataCode}", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn {
            items(flights) { destinationAirport ->
                FlightRow(
                    departureAirport = departureAirport,
                    destinationAirport = destinationAirport,
                    isFavorite = favoriteFlights.any {
                        it.departureCode == departureAirport.iataCode && it.destinationCode == destinationAirport.iataCode
                    },
                    onToggleFavorite = onToggleFavorite
                )
            }
        }
    }
}

@Composable
fun FavoriteRoutesList(
    favoriteFlights: List<FavoriteFlight>,
    onToggleFavorite: (String, String) -> Unit
) {
    Column {
        Text(
            "Favorite routes",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (favoriteFlights.isEmpty()) {
            Text("You have no favorite routes yet.")
        } else {
            LazyColumn {
                items(favoriteFlights) { flight ->
                    FavoriteFlightCard(flight = flight, onToggleFavorite = onToggleFavorite)
                }
            }
        }
    }
}


@Composable
fun FlightRow(
    departureAirport: Airport,
    destinationAirport: Airport,
    isFavorite: Boolean, // Nhận trạng thái yêu thích trực tiếp
    onToggleFavorite: (String, String) -> Unit
    // Xóa viewModel khỏi đây
) {
    // Xóa toàn bộ khối LaunchedEffect ở đây

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("DEPART", style = MaterialTheme.typography.labelMedium)
                AirportInfo(airport = departureAirport)
                Spacer(modifier = Modifier.height(8.dp))
                Text("ARRIVE", style = MaterialTheme.typography.labelMedium)
                AirportInfo(airport = destinationAirport)
            }
            IconButton(
                onClick = {
                    onToggleFavorite(departureAirport.iataCode, destinationAirport.iataCode)
                    // Không cần cập nhật isFavorite ở đây nữa
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) Color(0xFFFFC107) else Color.LightGray
                )
            }
        }
    }
}

@Composable
fun FavoriteFlightCard(
    flight: FavoriteFlight,
    onToggleFavorite: (String, String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("DEPART", style = MaterialTheme.typography.labelMedium)
                AirportInfo(code = flight.departureCode, name = flight.departureName)
                Spacer(modifier = Modifier.height(8.dp))
                Text("ARRIVE", style = MaterialTheme.typography.labelMedium)
                AirportInfo(code = flight.destinationCode, name = flight.destinationName)
            }
            IconButton(
                onClick = { onToggleFavorite(flight.departureCode, flight.destinationCode) }
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Remove from Favorite",
                    tint = Color(0xFFFFC107)
                )
            }
        }
    }
}

@Composable
fun AirportInfo(airport: Airport) {
    AirportInfo(code = airport.iataCode, name = airport.name)
}

@Composable
fun AirportInfo(code: String, name: String) {
    Row {
        Text(text = code, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = name)
    }
}


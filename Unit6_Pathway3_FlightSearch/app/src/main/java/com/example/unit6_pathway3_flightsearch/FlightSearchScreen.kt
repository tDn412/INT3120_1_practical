package com.example.unit6_pathway3_flightsearch

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.unit6_pathway3_flightsearch.data.Airport
import com.example.unit6_pathway3_flightsearch.data.FavoriteFlight
import com.example.unit6_pathway3_flightsearch.ui.FlightSearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightSearchScreen(
    modifier: Modifier = Modifier,
    viewModel: FlightSearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val suggestions by viewModel.suggestions.collectAsState()
    val favoriteFlights by viewModel.favoriteFlights.collectAsState()
    val destinationFlights by viewModel.destinationFlights.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Flight Search") }
            )
        }
    ) { innerPadding ->
        Column(modifier = modifier.padding(innerPadding)) {
            SearchTextField(
                query = uiState.searchQuery,
                onQueryChange = viewModel::onQueryChange,
                modifier = Modifier.padding(16.dp)
            )

            if (uiState.searchQuery.isBlank()) {
                if (uiState.selectedAirport == null) {
                    if (favoriteFlights.isNotEmpty()) {
                        FavoriteList(
                            favoriteFlights = favoriteFlights,
                            onFavoriteClick = viewModel::onFavoriteClick,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No favorite routes yet.")
                        }
                    }
                } else {
                    FlightResultsList(
                        departureAirport = uiState.selectedAirport!!,
                        destinationAirports = destinationFlights,
                        favoriteFlights = favoriteFlights.map { it.destinationCode },
                        onFavoriteClick = viewModel::onFavoriteClick,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            } else {
                SuggestionList(
                    suggestions = suggestions,
                    onSuggestionClick = viewModel::onAirportSelected,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun SearchTextField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Enter departure airport") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
        )
    )
}

@Composable
fun SuggestionList(
    suggestions: List<Airport>,
    onSuggestionClick: (Airport) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(suggestions, key = { it.id }) { airport ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSuggestionClick(airport) }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = airport.iataCode,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(text = airport.name)
            }
        }
    }
}

@Composable
fun FavoriteList(
    favoriteFlights: List<FavoriteFlight>,
    onFavoriteClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.padding(horizontal = 16.dp)) {
        item {
            Text(
                text = "Favorite Routes",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        items(favoriteFlights, key = { it.id }) { flight ->
            FavoriteRow(
                favoriteFlight = flight,
                onFavoriteClick = onFavoriteClick,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
fun FavoriteRow(
    favoriteFlight: FavoriteFlight,
    onFavoriteClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("DEPART", style = MaterialTheme.typography.labelMedium)
                Text(
                    text = "${favoriteFlight.departureCode} ${favoriteFlight.departureName}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(Modifier.height(8.dp))
                Text("ARRIVE", style = MaterialTheme.typography.labelMedium)
                Text(
                    text = "${favoriteFlight.destinationCode} ${favoriteFlight.destinationName}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            IconButton(onClick = { onFavoriteClick(favoriteFlight.departureCode, favoriteFlight.destinationCode) }) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Remove from favorites",
                    tint = Color(0xFFFFC107) // Gold color for favorite
                )
            }
        }
    }
}


@Composable
fun FlightResultsList(
    departureAirport: Airport,
    destinationAirports: List<Airport>,
    favoriteFlights: List<String>,
    onFavoriteClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.padding(horizontal = 16.dp)) {
        item {
            Text(
                text = "Flights from ${departureAirport.iataCode}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        items(destinationAirports, key = { it.id }) { destination ->
            FlightRow(
                departureAirport = departureAirport,
                destinationAirport = destination,
                isFavorite = destination.iataCode in favoriteFlights,
                onFavoriteClick = onFavoriteClick
            )
        }
    }
}

@Composable
fun FlightRow(
    departureAirport: Airport,
    destinationAirport: Airport,
    isFavorite: Boolean,
    onFavoriteClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("DEPART", style = MaterialTheme.typography.labelMedium)
                Text(
                    text = "${departureAirport.iataCode} ${departureAirport.name}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(Modifier.height(8.dp))
                Text("ARRIVE", style = MaterialTheme.typography.labelMedium)
                Text(
                    text = "${destinationAirport.iataCode} ${destinationAirport.name}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            IconButton(onClick = { onFavoriteClick(departureAirport.iataCode, destinationAirport.iataCode) }) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                    tint = if (isFavorite) Color(0xFFFFC107) else Color.LightGray
                )
            }
        }
    }
}

package com.example.unit6_pathway3_flightsearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unit6_pathway3_flightsearch.data.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlightSearchViewModel @Inject constructor(
    private val flightRepository: FlightRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FlightSearchUiState())
    val uiState: StateFlow<FlightSearchUiState> = _uiState.asStateFlow()

    init {
        // Lắng nghe thay đổi từ DataStore và cập nhật truy vấn tìm kiếm
        viewModelScope.launch {
            userPreferencesRepository.searchQuery
                .collect { query ->
                    _uiState.update { it.copy(searchQuery = query) }
                    // Khi có query, thực hiện tìm kiếm gợi ý
                    if (query.isNotBlank()) {
                        flightRepository.getAirportSuggestions(query).collect { suggestions ->
                            _uiState.update { it.copy(suggestions = suggestions) }
                        }
                    } else {
                        // Nếu không có query, xóa gợi ý và danh sách chuyến bay, hiển thị favorite
                        _uiState.update {
                            it.copy(
                                suggestions = emptyList(),
                                flights = emptyList(),
                                selectedAirport = null
                            )
                        }
                    }
                }
        }

        viewModelScope.launch {
            flightRepository.getAllFavoriteFlights().collect { favorites ->
                _uiState.update { it.copy(favoriteFlights = favorites) }
            }
        }
    }

    fun onQueryChange(query: String) {
        viewModelScope.launch {
            userPreferencesRepository.saveSearchQuery(query)
        }
    }

    fun onAirportSelected(airport: Airport) {
        _uiState.update { it.copy(selectedAirport = airport, searchQuery = airport.iataCode) }
        viewModelScope.launch {
            userPreferencesRepository.saveSearchQuery(airport.iataCode) // Lưu lựa chọn mới
            flightRepository.getAllOtherAirports(airport.iataCode).collect { flights ->
                _uiState.update { it.copy(flights = flights, suggestions = emptyList()) }
            }
        }
    }

    fun toggleFavorite(departureCode: String, destinationCode: String) {
        viewModelScope.launch {
            val favorite = flightRepository.getFavorite(departureCode, destinationCode).first()
            if (favorite == null) {
                flightRepository.insertFavorite(
                    Favorite(
                        departureCode = departureCode,
                        destinationCode = destinationCode
                    )
                )
            } else {
                flightRepository.deleteFavorite(favorite)
            }
        }
    }
}

data class FlightSearchUiState(
    val searchQuery: String = "",
    val suggestions: List<Airport> = emptyList(),
    val selectedAirport: Airport? = null,
    val flights: List<Airport> = emptyList(),
    val favoriteFlights: List<FavoriteFlight> = emptyList()
)
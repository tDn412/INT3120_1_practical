package com.example.unit6_pathway3_flightsearch.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unit6_pathway3_flightsearch.data.Airport
import com.example.unit6_pathway3_flightsearch.data.Favorite
import com.example.unit6_pathway3_flightsearch.data.FavoriteFlight
import com.example.unit6_pathway3_flightsearch.data.FlightRepository
import com.example.unit6_pathway3_flightsearch.data.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
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
        // Tải truy vấn tìm kiếm đã lưu khi ViewModel được khởi tạo
        viewModelScope.launch {
            _uiState.update {
                it.copy(searchQuery = userPreferencesRepository.searchQuery.first())
            }
        }
    }

    // Flow này sẽ tự động cập nhật danh sách gợi ý mỗi khi searchQuery thay đổi
    val suggestions: StateFlow<List<Airport>> = uiState
        .flatMapLatest { state ->
            if (state.searchQuery.isBlank()) {
                flowOf(emptyList())
            } else {
                flightRepository.getAirportSuggestions(state.searchQuery)
            }
        }
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val favoriteFlights: StateFlow<List<FavoriteFlight>> =
        flightRepository.getFavoriteFlights()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val destinationFlights: StateFlow<List<Airport>> =
        combine(
            uiState,
            flightRepository.getAllAirports()
        ) { state, allAirports ->
            state.selectedAirport?.let { selected ->
                allAirports.filterNot { it.id == selected.id }
            } ?: emptyList()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )


    fun onQueryChange(query: String) {
        // Cập nhật UI state ngay lập tức
        _uiState.update { it.copy(searchQuery = query) }
        // Lưu vào DataStore như một tác vụ nền
        viewModelScope.launch {
            userPreferencesRepository.saveSearchQuery(query)
        }
    }

    fun onAirportSelected(airport: Airport) {
        // Cập nhật sân bay đã chọn và xóa ô tìm kiếm
        _uiState.update {
            it.copy(
                selectedAirport = airport,
                searchQuery = ""
            )
        }
        // Lưu chuỗi tìm kiếm rỗng vào DataStore
        viewModelScope.launch {
            userPreferencesRepository.saveSearchQuery("")
        }
    }

    fun onFavoriteClick(departureCode: String, destinationCode: String) {
        viewModelScope.launch {
            val favorite = Favorite(
                departureCode = departureCode,
                destinationCode = destinationCode
            )
            val existingFavorite = flightRepository.getFavoriteFlight(departureCode, destinationCode)
            if (existingFavorite == null) {
                flightRepository.insertFavoriteFlight(favorite)
            } else {
                flightRepository.deleteFavoriteFlight(existingFavorite)
            }
        }
    }
}

data class FlightSearchUiState(
    val searchQuery: String = "",
    val selectedAirport: Airport? = null,
)


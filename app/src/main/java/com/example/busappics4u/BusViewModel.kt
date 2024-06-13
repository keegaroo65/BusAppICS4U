package com.example.busappics4u

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.busappics4u.data.Trip
import com.example.busappics4u.ui.history.HistoryViewModel
import com.example.busappics4u.ui.home.HomeViewModel
import com.example.busappics4u.ui.trip.TripViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class BusViewModel(
    _mainActivity: MainActivity,
    _navController: NavHostController
) : ViewModel() {
    val mainActivity = _mainActivity

    private val _busState = MutableStateFlow(
        BusUiState(
            TripViewModel(this),
            HomeViewModel(this),
            HistoryViewModel(this)
        )
    )
    val busState = _busState.asStateFlow()

    val navController = _navController

    fun trip() {
        if (!_busState.value.tripViewModel.tripState.value.tripActive) {
            _busState.value.tripViewModel.startTrip()
        }
        navController.navigate("trip")
    }

    fun tripDetails(trip: Trip) {
        navController.navigate("trip/${trip.id}")
    }
}

data class BusUiState(
    val tripViewModel: TripViewModel,
    val homeViewModel: HomeViewModel,
    val historyViewModel: HistoryViewModel
)

enum class TripStatus {
    None,
    Active,
    Cancelled
}
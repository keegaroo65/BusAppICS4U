package com.example.busappics4u

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.busappics4u.data.Trip
import com.example.busappics4u.ui.history.HistoryViewModel
import com.example.busappics4u.ui.home.HomeViewModel
import com.example.busappics4u.ui.trip.TripDetailViewModel
import com.example.busappics4u.ui.trip.TripViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class BusViewModel(
    val mainActivity: MainActivity,
    val navController: NavHostController
) : ViewModel() {

    private val _appState = MutableStateFlow(
        BusUiState(
            TripViewModel(this),
            TripDetailViewModel(
                mainActivity.container.tripsRepository, 0
            ),
            HomeViewModel(this),
            HistoryViewModel(this)
        )
    )
    val appState = _appState.asStateFlow()

    fun trip() {
        if (!_appState.value.tripViewModel.uiState.value.tripActive) {
            _appState.value.tripViewModel.startTrip()
        }
        navController.navigate("trip")
    }

    fun tripDetails(trip: Trip) {
        navController.navigate("trip/${trip.id}")
    }
}

data class BusUiState(
    val tripViewModel: TripViewModel,
    val tripDetailViewModel: TripDetailViewModel,
    val homeViewModel: HomeViewModel,
    val historyViewModel: HistoryViewModel
)
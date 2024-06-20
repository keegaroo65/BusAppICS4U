package com.example.busappics4u.ui.trip

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.busappics4u.BusViewModel
import com.example.busappics4u.data.Trip
import com.example.busappics4u.utility.Utility
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

private const val TAG = "TripViewModel.kt"

class TripViewModel(
    val busViewModel: BusViewModel
): ViewModel() {
    private val _uiState = MutableStateFlow(TripState())
    val uiState = _uiState.asStateFlow()

    val tripsRepository = busViewModel.mainActivity.container.tripsRepository

    // Trip detail updating methods wrappers (called directly from onClick events then passed on)
    fun updateBusId(_busId: String) {
        val digits = _busId.filter { it.isDigit() }

        updateBusId(if (digits.isNotEmpty()) Integer.parseInt(digits) else 0)
    }

    fun updateRouteId(_routeId: String) {
        val digits = _routeId.filter { it.isDigit() }

        updateRouteId(if (digits.isNotEmpty()) Integer.parseInt(digits) else 0)
    }

    fun updateStartStopId(_startStop: String) {
        val digits = _startStop.filter { it.isDigit() }

        updateStartStopId(if (digits.length > 0) Integer.parseInt(digits) else 0)
    }

    fun updateEndStopId(_endStop: String) {
        val digits = _endStop.filter { it.isDigit() }

        updateEndStopId(if (digits.isNotEmpty()) Integer.parseInt(digits) else 0)
    }

    // Trip actions
    fun startTrip() {
        _uiState.value = TripState(tripActive = true)
    }

    suspend fun endTrip() {
        Log.d(TAG, "endTrip()")
        updateTripActive(false)

        busViewModel.navController.popBackStack()

        updateEndTime(Utility.time())

        val trip = _uiState.value.trip
        if (!trip.isEmpty()) {
            tripsRepository.insertTrip(trip)
        }
    }

    fun abortTrip() {
        Log.d(TAG, "cancelTrip()")
        updateTripActive(false)

        busViewModel.navController.popBackStack()
    }

    // Trip detail updating methods (operate directly with trip state)
    fun updateBusId(_busId: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                trip = currentState.trip.copy(
                    busId = _busId
                )
            )
        }
    }

    fun updateRouteId(_routeId: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                trip = currentState.trip.copy(
                    routeId = _routeId
                )
            )
        }
    }

    fun updateEndTime(_endTime: Long) {
        _uiState.update { currentState ->
            currentState.copy(
                trip = currentState.trip.copy(
                    endTime = _endTime
                )
            )
        }
    }

    fun updateRouteHeadsign(_routeHeadsign: String) {
        _uiState.update { currentState ->
            currentState.copy(
                trip = currentState.trip.copy(
                    routeHeadsign = _routeHeadsign
                )
            )
        }
    }

    fun updateStartStopId(_startStop: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                trip = currentState.trip.copy(
                    startStop = _startStop
                )
            )
        }
    }

    fun updateEndStopId(_endStop: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                trip = currentState.trip.copy(
                    endStop = _endStop
                )
            )
        }
    }

    fun updatePromptCancel(_promptCancel: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                promptCancel = _promptCancel
            )
        }
    }

    fun updateTripActive(_tripActive: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                tripActive = _tripActive
            )
        }
    }
}

data class TripState(
    val trip: Trip = Trip(startTime = Utility.time()),
    val tripActive: Boolean = false,
    val promptCancel: Boolean = false
)
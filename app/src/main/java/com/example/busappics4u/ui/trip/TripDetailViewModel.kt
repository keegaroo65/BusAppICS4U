package com.example.busappics4u.ui.trip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.busappics4u.BusViewModel
import com.example.busappics4u.data.Trip
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


class TripDetailViewModel(
    busViewModel: BusViewModel,
    tripId: Int
) : ViewModel() {
    val tripsRepository = busViewModel.mainActivity.container.tripsRepository

    val uiState: StateFlow<TripDetailState> =
        tripsRepository.getTripStream(tripId)
            .filterNotNull()
            .map {
                TripDetailState(trip = it)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = TripDetailState()
            )

}

data class TripDetailState(
    val trip: Trip = Trip(startTime = 0)
)
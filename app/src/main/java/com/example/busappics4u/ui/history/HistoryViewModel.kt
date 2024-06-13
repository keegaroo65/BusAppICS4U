package com.example.busappics4u.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.busappics4u.BusViewModel
import com.example.busappics4u.data.Trip
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HistoryViewModel(
    busViewModel: BusViewModel
) : ViewModel() {
    val historyUiState: StateFlow<HistoryUiState> =
        busViewModel.mainActivity.container.tripsRepository.getAllTripsStream()
            .map { HistoryUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = HistoryUiState()
            )
}

data class HistoryUiState(
    val tripList: List<Trip> = listOf()
)
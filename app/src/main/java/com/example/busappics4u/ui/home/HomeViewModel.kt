package com.example.busappics4u.ui.home

import androidx.lifecycle.ViewModel
import com.example.busappics4u.BusViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel(
    busViewModel: BusViewModel
): ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun input(text: String) {
        _uiState.update { currentState ->
            currentState.copy(
                inputText = text
            )
        }
    }

    fun output(
        text: String,
        routeId: Int = 0
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                outputText = text,
                routeId = routeId
            )
        }
    }
}

data class HomeUiState(
    val inputText: String = "",
    val outputText: String = "trip info here",
    val routeId: Int = 0
)
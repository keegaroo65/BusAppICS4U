package com.example.busappics4u.ui.home

import androidx.lifecycle.ViewModel
import com.example.busappics4u.BusViewModel
import com.google.transit.realtime.GtfsRealtime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel(
    busViewModel: BusViewModel
): ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun getNumTextFields(): Int {
        return when (uiState.value.busSearchType) {
            0 -> 1
            1 -> 2
            else -> 0
        }
    }

    fun hideOutput() {
        _uiState.update { currentState ->
            currentState.copy(
                outputText = ""
            )
        }
    }

    fun setSearchType(searchType: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                busSearchType = searchType
            )
        }
    }

    fun setBusList(busList: List<GtfsRealtime.FeedEntity>) {
        _uiState.update { currentState ->
            currentState.copy(
                busList = busList
            )
        }
    }

    fun showBusList() {
        _uiState.update { currentState ->
            currentState.copy(
                showingBusList = true
            )
        }
    }

    fun hideBusList() {
        _uiState.update { currentState ->
            currentState.copy(
                showingBusList = false,
                busList = null // To make sure loading screen shows up if popup opened consecutively
            )
        }
    }

    fun input1(text: String) {
        _uiState.update { currentState ->
            currentState.copy(
                inputText1 = text
            )
        }
    }

    fun input2(text: String) {
        _uiState.update { currentState ->
            currentState.copy(
                inputText2 = text
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
    val busSearchType: Int = 0,
    val showingBusList: Boolean = false,
    val busList: List<GtfsRealtime.FeedEntity>? = null,
    val inputText1: String = "",
    val inputText2: String = "",
    val outputText: String = "",
    val routeId: Int = 0
)
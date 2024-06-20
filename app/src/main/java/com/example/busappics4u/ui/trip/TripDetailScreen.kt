package com.example.busappics4u.ui.trip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.busappics4u.data.TripsRepository
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@Composable
fun TripDetailScreen(
    tripsRepository: TripsRepository,
    tripId: Int,
    uiState: TripDetailState
//    viewModel: TripDetailViewModel = TripDetailViewModel(tripsRepository, tripId)
) {
//    val uiState = viewModel.uiState.collectAsState().value

    val dtf = DateTimeFormatter.ofPattern("EEE MMM dd, uu 'at' hh:mm a")

    val instant = Instant.now() //can be LocalDateTime
    val systemZone = ZoneId.systemDefault() // my timezone
    val currentOffsetForMyZone = systemZone.rules.getOffset(instant)

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        TripDetail (
            "Bus ID:",
            uiState.trip.busId.toString()
        )
        TripDetail (
            "Route ID:",
            uiState.trip.routeId.toString()
        )
        TripDetail (
            "Route Headsign:",
            uiState.trip.routeHeadsign
        )
        TripDetail(
            "Trip Start:",
             LocalDateTime.ofEpochSecond(
                 uiState.trip.startTime,
                 0,
                 currentOffsetForMyZone
                 )
                 .format(dtf)
        )
    }
}

@Composable
fun TripDetail(
    key: String,
    value: String
) {
    Row(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text("$key $value")
    }
}
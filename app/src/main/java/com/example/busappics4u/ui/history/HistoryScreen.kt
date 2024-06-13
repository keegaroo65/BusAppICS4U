package com.example.busappics4u.ui.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.busappics4u.BusViewModel
import com.example.busappics4u.data.Trip
import com.example.busappics4u.utility.Utility

@Composable
fun HistoryScreen(
    busViewModel: BusViewModel
) {
    val extraPadding = 10.dp

    val viewModel = busViewModel.busState.collectAsState().value.historyViewModel
    val allTrips = viewModel.historyUiState.collectAsState().value.tripList

    Column {
        Text(
            text = "All history",
            style = MaterialTheme.typography.displayMedium
        )
        Column(
            verticalArrangement = Arrangement.Top,
        ) {
            allTrips.forEach {
                HistoryChit(
                    it,
                    extraPadding,
                    Modifier
                        .clickable {
                            busViewModel.tripDetails(it)
                        }
                )
            }
        }
    }
}

@Composable
fun HistoryChit(
    trip: Trip,
    extraPadding: Dp,
    modifier: Modifier
) {
    val extraPx = with(LocalDensity.current) { extraPadding.toPx() }
    val limitedColor = MaterialTheme.colorScheme.surface

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start//spacedBy(alignment = Alignment.Start)
    ) {
        Text(
            modifier = Modifier
                .padding(5.dp + extraPadding)
                .drawBehind {
                    drawRoundRect(
                        color = Utility.ServiceColor(trip.routeId),
                        cornerRadius = CornerRadius(extraPx * 0.75F,extraPx * 0.75F),
                        size = this.size.copy(
                            width = this.size.width + extraPx,
                            height = this.size.height + extraPx
                        ),
                        topLeft = Offset(-extraPx/2F, -extraPx/2F)
                        //radius = this.size.maxDimension
                    )
                    if (Utility.limited.contains(trip.routeId)) {
                        drawRoundRect(
                            color = limitedColor,
                            cornerRadius = CornerRadius(extraPx * 0.5F, extraPx * 0.5F)
                        )
                    }
                },
            text = trip.routeId.toString()
        )
        Text(
            text = trip.routeHeadsign + " (${trip.busId})"
        )
    }
}
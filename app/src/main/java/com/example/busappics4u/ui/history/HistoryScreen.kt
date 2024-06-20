package com.example.busappics4u.ui.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.busappics4u.data.Trip
import com.example.busappics4u.utility.Utility

@Composable
fun HistoryScreen(
    uiState: HistoryUiState,
    tripDetails: (Trip) -> Unit
) {
    val extraPadding = 10.dp

    Column {
        Text(
            text = "All history",
            style = MaterialTheme.typography.displayMedium
        )
        Column(
            verticalArrangement = Arrangement.Top,
        ) {
            uiState.tripList.forEach {
                HistoryChit(
                    it,
                    extraPadding,
                    Modifier
                        .clickable {
                            tripDetails(it)
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

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start//spacedBy(alignment = Alignment.Start)
    ) {
        RouteIdIcon(
            trip.routeId,
            extraPadding
        )
        Text(
            text = trip.routeHeadsign + " (${trip.busId})"
        )
    }
}

@Composable
fun RouteIdIcon(
    routeId: String,
    extraPadding: Dp = 10.dp,
) {
    val extraPx = with(LocalDensity.current) { extraPadding.toPx() }
    val limitedColor = MaterialTheme.colorScheme.surface

    Text(
        modifier = Modifier
            .padding(5.dp + extraPadding)
            .drawBehind {
                drawRoundRect(
                    color = Utility.ServiceColor(routeId.toInt()),
                    cornerRadius = CornerRadius(extraPx * 0.75F, extraPx * 0.75F),
                    size = this.size.copy(
                        width = this.size.width + extraPx,
                        height = this.size.height + extraPx
                    ),
                    topLeft = Offset(-extraPx / 2F, -extraPx / 2F)
                    //radius = this.size.maxDimension
                )
                if (Utility.limited.contains(routeId.toInt())) {
                    drawRoundRect(
                        color = limitedColor,
                        cornerRadius = CornerRadius(extraPx * 0.5F, extraPx * 0.5F)
                    )
                }
            },
        text = routeId
    )
}

@Composable
fun RouteIdIcon(
    routeId: Int,
    extraPadding: Dp = 10.dp,
) {
    RouteIdIcon(
        routeId.toString(),
        extraPadding
    )
}
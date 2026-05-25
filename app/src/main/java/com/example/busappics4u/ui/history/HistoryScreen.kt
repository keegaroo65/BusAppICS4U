package com.example.busappics4u.ui.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.Modifier.Companion.then
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.busappics4u.R
import com.example.busappics4u.data.Trip
import com.example.busappics4u.ui.theme.ServiceTypeColors
import com.example.busappics4u.utility.Utility
import com.example.busappics4u.utility.Utility.Companion.IsRouteLimited

val busFontFamily = //TextStyle(
    /*fontFamily = */FontFamily(
        Font(
            R.font.frutiger67boldcondensed
        )
    )
//)

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
    routeString: String,
    extraPadding: Dp = 10.dp,
    backgroundColor: Color = Color.White,
) {
    val extraPx = with(LocalDensity.current) { extraPadding.toPx() }
    val limitedColor = backgroundColor//Color.White//0xFF1F1A17)// MaterialTheme.colorScheme.surface

    val routeId = routeString.toInt()
    val isLimited = Utility.IsRouteLimited(routeId)
    val routeColor = Utility.ServiceColor(routeId)

    Text(
        text =
            if (routeId > 4)
                routeString
            else
                "R$routeString",
        style = LocalTextStyle.current.copy(fontFamily = busFontFamily),
        color = if (isLimited) ServiceTypeColors.Local else Color.White,
        modifier = Modifier
            .padding(5.dp + extraPadding)
            .drawBehind {
                val borderSize = Utility.MinAspectRatio(this.size.copy(
                    width = this.size.width + extraPx,
                    height = this.size.height + extraPx
                ), 1f)
                val widthDiff = this.size.width + extraPx - borderSize.width
                drawRect(
                    color = routeColor,
                    //cornerRadius = CornerRadius(extraPx * 0.75F, extraPx * 0.75F),
                    size = borderSize,
                    topLeft = Offset(-(extraPx - widthDiff) / 2F, -extraPx / 2F)
                    //radius = this.size.maxDimension
                )
                if (IsRouteLimited(routeId)) {
                    drawRect(
                        color = limitedColor,
                        //cornerRadius = CornerRadius(extraPx * 0.5F, extraPx * 0.5F),
                        size = Utility.MinAspectRatio(this.size, 1f),
                        topLeft = Offset(widthDiff / 2f, 0f)
                    )
                }
            }
    )
}

@Composable
fun RouteIdIcon(
    routeId: Int,
    extraPadding: Dp = 10.dp,
    backgroundColor: Color = Color.White
) {
    RouteIdIcon(
        routeId.toString(),
        extraPadding
    )
}
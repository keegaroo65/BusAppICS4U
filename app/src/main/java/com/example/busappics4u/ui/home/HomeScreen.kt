package com.example.busappics4u.ui.home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.DirectionsBus
import androidx.compose.material.icons.outlined.FormatListNumbered
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.compose.rememberNavController
import com.example.busappics4u.BusUiState
import com.example.busappics4u.BusViewModel
import com.example.busappics4u.MainActivity
import com.example.busappics4u.data.WebReqHandler
import com.example.busappics4u.ui.history.RouteIdIcon
import com.google.transit.realtime.GtfsRealtime

private const val TAG = "HomeScreen"

@Composable
fun HomeScreen(
    busViewModel: BusViewModel
) {
    val busState = busViewModel.appState.collectAsState().value
    val viewModel = busState.homeViewModel
    val uiState = viewModel.uiState.collectAsState().value

    val focusManager = LocalFocusManager.current

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(7.dp)
    ) {
        HomePinger(busViewModel, busState, viewModel, uiState, focusManager)
    }
}


val typeOptions = listOf(
    Icons.Outlined.DirectionsBus,
    Icons.Outlined.FormatListNumbered,
    Icons.Outlined.Bolt
)

@Composable
fun HomePinger(
    busViewModel: BusViewModel,
    busState: BusUiState,
    viewModel: HomeViewModel,
    uiState: HomeUiState,
    focusManager: FocusManager
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (uiState.showingBusList) {
                BusListPopup(
                    viewModel,
                    uiState,
                    if (uiState.busSearchType == 1)
                        "No buses in the given range are currently active."
                    else
                        "No ZEBs are currently active."
                )
            }

            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp, 0.dp, 0.dp, 55.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {// Title text
                Text(
//                        modifier = Modifier
//                            .align(Alignment.TopCenter),
                    text = "Bus app!",
                    style = MaterialTheme.typography.displayLarge
                )

                // Segmented button to pick between 4 transaction types
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SingleChoiceSegmentedButtonRow {
                        typeOptions.forEachIndexed { index, icon ->
                            SegmentedButton(
                                shape = SegmentedButtonDefaults.itemShape(index = index, count = typeOptions.size),
                                onClick = { viewModel.setSearchType(index) },
                                /*icon = {
                                    SegmentedButtonDefaults.Icon(active = index == tranType) {
                                        Icon(
                                            imageVector = icon,
                                            contentDescription = "",
                                            tint = Color.Cyan,
                                            modifier = Modifier.size(SegmentedButtonDefaults.IconSize)
                                        )
                                    }
                                },*/
                                selected = index == uiState.busSearchType
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = "",
                                    //tint = Color.Cyan,
                                    modifier = Modifier.size(SegmentedButtonDefaults.IconSize)
                                )
                            }
                        }
                    }
                    Text(
                        modifier = Modifier
                            .padding(5.dp),
                        textAlign = TextAlign.Center,
                        text = when (uiState.busSearchType) {
                            0 -> "Single bus"
                            1 -> "Range of buses"
                            else -> "Zero-Emission buses"
                        }
                    )
                }

                val textFieldNum = viewModel.getNumTextFields()

                if (textFieldNum > 0) {
                    // First text field
                    TextField(
                        value = uiState.inputText1,
                        onValueChange = { viewModel.input1(it) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = if (textFieldNum == 1) ImeAction.Done else ImeAction.Next
                        ),
                        placeholder = { Text(
                            if (textFieldNum == 1) "Bus id (eg. 4810)" else "Minimum bus id"
                        ) }
                    )

                    if (textFieldNum > 1) {
                        // Second text field
                        TextField(
                            value = uiState.inputText2,
                            onValueChange = { viewModel.input2(it) },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            placeholder = { Text("Maximum bus id") }
                        )
                    }
                }


                Button(
                    modifier = Modifier
                        .padding(20.dp),
                    onClick = {
                        viewModel.output("loading...")

                        focusManager.clearFocus()

                        when(uiState.busSearchType) {
                            0 -> WebReqHandler.SearchSingleBus(
                                uiState.inputText1
                            ) { outputText, routeId ->
                                viewModel.output(
                                    outputText,
                                    routeId
                                )
                            }
                            1 -> {
                                viewModel.showBusList()

                                WebReqHandler.SearchBusRange(
                                    uiState.inputText1,
                                    uiState.inputText2
                                ) { outputText, busList ->
                                    if (outputText.isNotEmpty()) {
                                        viewModel.output(
                                            outputText
                                        )
                                    }

                                    viewModel.setBusList(busList)
                                }
                            }
                            else -> {
                                Log.d(TAG, "showing list?? :3")
                                viewModel.showBusList()

                                WebReqHandler.SearchZEBs({
                                    Log.d(TAG, "Calling callback setBusList :D")
                                    viewModel.setBusList(it)
                                })
                                Log.d(TAG, "s earching zebs")
                            }
                        }
                    }
                ) {
                    Text(
                        text = "Ping",
                        style = MaterialTheme.typography.headlineLarge,
                    )
                }

//                Row (
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        text = uiState.outputText
//                    )
//                    if (uiState.routeId != 0) {
//                        RouteIdIcon(uiState.routeId)
//                    }
//                }

            }


            LargeFloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(15.dp),
                onClick = { busViewModel.trip() }
            ) {
                Icon(
                    if (busState.tripViewModel.uiState.collectAsState().value.tripActive)
                        Icons.Filled.PlayCircleOutline
                    else
                        Icons.Filled.AddCircleOutline,
                    "Begin new trip",
                    Modifier
                        .size(50.dp),
                )
            }
        }
    }
}


@Composable
fun BusListPopup(
    viewModel: HomeViewModel,
    uiState: HomeUiState,
    warningMessage: String
) {
    Dialog(
        onDismissRequest = {
            viewModel.hideBusList()
        }
    ) {
        var _cardModifier = Modifier.padding(15.dp)

        if (uiState.busList != null)
            _cardModifier = _cardModifier.fillMaxWidth()

        Card(
            modifier = _cardModifier
        ) {
            Column(
                modifier = if (uiState.busList != null) Modifier.fillMaxWidth() else Modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                uiState.busList.let {
                    if (it == null) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(10.dp)
                        )
                    } else if (it.isEmpty()) {
                        Text(
                            modifier = Modifier.padding(5.dp),
                            text = warningMessage
                        )
                    } else {
                        Column(
                            modifier = Modifier
                                .padding(5.dp)
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            Log.d(TAG, "num buses for dialog: ${uiState.busList!!.size}")
                            uiState.busList.forEachIndexed { index, bus ->
                                BusListCard(index, bus)
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun BusListCard(index: Int, bus: GtfsRealtime.FeedEntity) {
    val routeId = bus.vehicle.trip.routeId

    if (isValidRoute(routeId)) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Bus #${bus.vehicle.vehicle.id} on route")
            RouteIdIcon(routeId)
        }
    }
    else
        Text("Bus #${bus.vehicle.vehicle.id} is on the road")
}

fun isValidRoute(routeId: String): Boolean {
    return routeId.toIntOrNull() != null
}

@Preview
@Composable
fun HsPreview() {
    HomeScreen(BusViewModel(MainActivity(), rememberNavController()))
}
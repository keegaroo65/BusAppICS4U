package com.example.busappics4u.ui.trip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.busappics4u.BusViewModel
import kotlinx.coroutines.launch
import org.w3c.dom.Text

@Composable
fun TripScreen(
    busViewModel: BusViewModel,
) {
    val viewModel = busViewModel.appState.collectAsState().value.tripViewModel
    val uiState = viewModel.uiState.collectAsState().value

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "Current Trip",
                style = MaterialTheme.typography.titleLarge
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                TripFields(viewModel)
            }

            TripActions(viewModel)

            if (uiState.promptCancel) {
                AbortConfirmationDialog(
                    viewModel,
                    {
                        viewModel.abortTrip()
                    }
                )
            }
        }
    }
}

@Composable
fun TripFields(
    viewModel: TripViewModel
) {
    val uiState = viewModel.uiState.collectAsState().value

    TripField(
        "Bus ID",
        uiState.trip.busId.toString()
    ) {
        viewModel.updateBusId(it)
    }

    TripField(
        "Route ID",
        uiState.trip.routeId.toString()
    ) {
        viewModel.updateRouteId(it)
    }

    TripField(
        "Route Headsign",
        uiState.trip.routeHeadsign,
        false
    ) {
        viewModel.updateRouteHeadsign(it)
    }

    TripField(
        "Start Stop ID",
        uiState.trip.startStop.toString()
    ) {
        viewModel.updateStartStopId(it)
    }

    TripField(
        "End Stop ID",
        uiState.trip.endStop.toString(),
        lastField = true
    ) {
        viewModel.updateEndStopId(it)
    }
}

@Composable
fun TripActions(
    viewModel: TripViewModel
) {
    val uiState = viewModel.uiState.collectAsState().value

    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally)
    ) {
        OutlinedButton(
            onClick = {
                viewModel.updatePromptCancel(true)
            }
        ) {
            Text("Abort Trip")
        }
        Button(
            onClick = {
                coroutineScope.launch {
                    viewModel.endTrip()
                }
            }
        ) {
            Text("End Trip")
        }
    }
}

@Composable
fun TripField(
    name: String,
    _value: String,
    numOnly: Boolean = true,
    lastField: Boolean = false,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = if (name == "Route Headsign") Modifier
            else Modifier, // Add autofilling here somehow..
        value = if (_value == "0") "" else _value,
        label = { Text(name) },
        placeholder = { Text(name) },
        onValueChange = onChange,
        keyboardOptions = KeyboardOptions(
            imeAction = if (lastField) ImeAction.Done else ImeAction.Next,
            keyboardType = if (numOnly) KeyboardType.Number else KeyboardType.Text
        )
    )
}

@Composable
fun AbortConfirmationDialog(
    viewModel: TripViewModel,
    onAbortConfirm: () -> Unit,
    onAbortCancel: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = {
            viewModel.updatePromptCancel(false)
            onAbortCancel()
        },
        title = {
            Text("Confirm")
        },
        text = {
            Text("Are you sure you want to abort this active trip?")
        },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = {
                viewModel.updatePromptCancel(false)
                onAbortCancel()
            }) {
                Text("No")
            }
        },
        confirmButton = {
            TextButton(onClick = {
                viewModel.updatePromptCancel(false)
                onAbortConfirm()
            }) {
                Text("Yes")
            }
        }
    )
}

//id, hs, id, ss, es

//@Preview
//@Composable
//fun TsPreview() {
//    TripScreen({})
//}
package com.example.busappics4u.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.busappics4u.BusViewModel
import com.example.busappics4u.MainActivity
import com.example.busappics4u.data.WebReqHandler
import com.example.busappics4u.ui.history.RouteIdIcon

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

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
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

                    TextField(
                        value = uiState.inputText,
                        onValueChange = { viewModel.input(it) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        placeholder = { Text("Bus id (eg. 4810)") }
                    )

                    Button(
                        modifier = Modifier
                            .padding(20.dp),
                        onClick = {
                            viewModel.output("loading...")

                            focusManager.clearFocus()

                            WebReqHandler.test(
                                uiState.inputText
                            ) { outputText, routeId ->
                                viewModel.output(
                                    outputText,
                                    routeId
                                )
                            }
                        }
                    ) {
                        Text(
                            text = "Ping",
                            style = MaterialTheme.typography.headlineLarge,
                        )
                    }

                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = uiState.outputText
                        )
                        if (uiState.routeId != 0) {
                            RouteIdIcon(uiState.routeId)
                        }
                    }

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
}



@Preview
@Composable
fun HsPreview() {
    HomeScreen(BusViewModel(MainActivity(), rememberNavController()))
}
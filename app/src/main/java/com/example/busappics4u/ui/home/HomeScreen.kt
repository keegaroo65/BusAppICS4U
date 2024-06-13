package com.example.busappics4u.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.busappics4u.BusViewModel

@Composable
fun HomeScreen(
    busViewModel: BusViewModel
) {
    val busState = busViewModel.busState.collectAsState().value
    val viewModel = busState.homeViewModel
    val uiState = viewModel.uiState.collectAsState().value

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
                // Title text
                Text(
                    modifier = Modifier
                        .align(Alignment.TopCenter),
                    text = "Bus app!",
                    style = MaterialTheme.typography.displayLarge
                )

                LargeFloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(15.dp),
                    onClick = { busViewModel.trip() }
                ) {
                    Icon(
                        if (busState.tripViewModel.tripState.collectAsState().value.tripActive)
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
//            TextField(
//                value = uiState.inputText,
//                onValueChange = { viewModel.input(it) },
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                placeholder = { Text("Bus id (eg. 4810)") }
//            )
//
//            Button(
//                modifier = Modifier,
//                onClick = {
//                    busViewModel.trip()
//                }
//            ) {
//                Text(
//                    text = "Ping",
//                    style = MaterialTheme.typography.headlineLarge,
//                )
//            }
//
//            Text(
//                text = uiState.outputText
//            )
//        }
    }
}

//                    viewModel.output("loading...")
//
//                    WebReqHandler.test(
//                        uiState.inputText
//                    ) {
//                        viewModel.output(it)
//                    }
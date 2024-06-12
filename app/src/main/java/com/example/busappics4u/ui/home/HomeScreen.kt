package com.example.busappics4u.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.busappics4u.data.WebReqHandler
import com.example.busappics4u.ui.AppViewModelProvider

@Composable
fun Home(
    navController: NavController,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.uiState.collectAsState().value

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            TextField(
                value = uiState.inputText,
                onValueChange = { viewModel.input(it) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = { Text("Bus id (eg. 4810)") }
            )

            Button(
                modifier = Modifier,
                onClick = {
                    viewModel.output("loading...")

                    WebReqHandler.test(
                        uiState.inputText
                    ) {
                        viewModel.output(it)
                    }
                }
            ) {
                Text(
                    text = "Ping",
                    style = MaterialTheme.typography.headlineLarge,
                )
            }

            Text(
                text = uiState.outputText
            )
        }
    }
}
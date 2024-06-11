package com.example.busappics4u

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun Home(
    navController: NavController
) {
    var idState by rememberSaveable { mutableStateOf("") }
    var tripId by rememberSaveable { mutableStateOf("trip info here") }

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
                value = idState,
                onValueChange = {
                    idState = it
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = { Text("Bus id (eg. 4810)") }
            )

            Button(
                modifier = Modifier,
                onClick = {
                    tripId = "loading..."

                    WebReqHandler.test(
                        idState
                    ) {
                        tripId = it
                    }
                }
            ) {
                Text(
                    text = "Ping",
                    style = MaterialTheme.typography.headlineLarge,
                )
            }

            Text(
                text = tripId
            )
        }
    }
}
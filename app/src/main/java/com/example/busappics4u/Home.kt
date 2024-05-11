package com.example.busappics4u

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun Home(
    navController: NavController
) {
    Button(
        modifier = Modifier
            .fillMaxSize(),
        onClick = { WebReqHandler.test() }
    ) {

    }
}
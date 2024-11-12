package com.example.busappics4u.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.busappics4u.BusViewModel
import com.example.busappics4u.R
import com.example.busappics4u.ui.history.HistoryScreen
import com.example.busappics4u.ui.home.HomeScreen
import com.example.busappics4u.ui.settings.SettingsScreen
import com.example.busappics4u.ui.trip.TripDetailScreen
import com.example.busappics4u.ui.trip.TripScreen


@Composable
fun BusNavGraph(
    busViewModel: BusViewModel,
    navController: NavHostController,
    innerPadding: PaddingValues
) {
    val NAV_HOME = stringResource(NavItem.Home.navRoute)
    val NAV_HISTORY = stringResource(NavItem.History.navRoute)
    val NAV_SETTINGS = stringResource(NavItem.Settings.navRoute)
    val NAV_TRIP = stringResource(R.string.tripRoute)
    val NAV_TRIP_DETAIL = stringResource(R.string.tripDetailRoute)

    val tripsRepository = busViewModel.mainActivity.container.tripsRepository

    NavHost(
        modifier = Modifier.padding(innerPadding),
        navController = navController,
        startDestination = NAV_HOME
    ) {
        composable(
            route = NAV_HOME
        ) {
            HomeScreen(
                busViewModel,
                innerPadding
            )
        }

        composable(
            route = NAV_HISTORY
        ) {
            val historyViewModel = busViewModel.appState.collectAsState().value.historyViewModel
            val historyViewState = historyViewModel.uiState.collectAsState()

            HistoryScreen(
                historyViewState.value
            ) { busViewModel.tripDetails(it) }
        }

        composable(
            route = NAV_SETTINGS
        ) {
            SettingsScreen(busViewModel)
        }

        composable(
            route = NAV_TRIP
        ) {
            TripScreen(busViewModel)
        }

        composable(
            route = NAV_TRIP_DETAIL
        ) {
            val tripDetailViewModel = busViewModel.appState.collectAsState().value.tripDetailViewModel
            val tripDetailViewState = tripDetailViewModel.uiState.collectAsState().value
            TripDetailScreen(
                tripsRepository,
                (it.arguments?.getString("id") ?: "0").toInt(),
                tripDetailViewState
            )
        }
    }
}
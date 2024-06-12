package com.example.busappics4u.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.busappics4u.ui.history.History
import com.example.busappics4u.ui.home.Home
import com.example.busappics4u.ui.settings.Settings



@Composable
fun BusNavGraph(
    navController: NavHostController,
    innerPadding: PaddingValues
) {
    val NAV_HOME = stringResource(NavItem.Home.navRoute)
    val NAV_HISTORY = stringResource(NavItem.History.navRoute)
    val NAV_SETTINGS = stringResource(NavItem.Settings.navRoute)

    NavHost(
        modifier = Modifier.padding(innerPadding),
        navController = navController,
        startDestination = NAV_HOME
    ) {
        composable(
            route = NAV_HOME
        ) {
            Home(navController)
        }
        composable(
            route = NAV_HISTORY
        ) {
            History()
        }
        composable(
            route = NAV_SETTINGS
        ) {
            Settings()
        }
    }
}
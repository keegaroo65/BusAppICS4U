package com.example.busappics4u

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.busappics4u.data.FileHandler
import com.example.busappics4u.ui.navigation.BusNavGraph
import com.example.busappics4u.ui.navigation.NavItem
import com.example.busappics4u.ui.theme.BusAppICS4UTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context: Context = applicationContext

        FileHandler.load(context)

        setContent {
            BusAppICS4UTheme {
                BusApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusApp() {
    val navController = rememberNavController()

    // A surface container using the 'background' color from the theme
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    titleContentColor = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.surfaceVariant)
                ),
                title = {
                    Text(
                        text = "Bus Tool <3",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        //style = MaterialTheme.typography.titleLarge
                    )
                },
            )
        },
        bottomBar = { AppBottomNavigation(navController = navController) }
    ) { innerPadding ->
        BusNavGraph(navController, innerPadding)
    }
}

@Composable
fun AppBottomNavigation(
    navController: NavController
) {
    val navItems = listOf(NavItem.Home, NavItem.History, NavItem.Settings)

    val containerColor = MaterialTheme.colorScheme.surface

    NavigationBar(
        containerColor = containerColor,
        contentColor = MaterialTheme.colorScheme.contentColorFor(containerColor)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        navItems.forEach{ item ->
            val navRoute: String = stringResource(item.navRoute)
            val title: String = stringResource(id = item.title)

            val selected = currentRoute == navRoute

            val icon: ImageVector =
                if (selected) {
                    item.selectedIcon
                } else {
                    item.unselectedIcon
                }

            NavigationBarItem(
                icon = { Icon(icon, "") },
                selected = selected,
                label = { Text(text = title) },
                onClick = {
                    navController.navigate(navRoute)
                }
            )
        }
    }
}

@Preview
@Composable
fun MainPreview() {
    BusApp()
}

@Preview
@Composable
fun AppBottomNavPreview() {
    AppBottomNavigation(rememberNavController())
}
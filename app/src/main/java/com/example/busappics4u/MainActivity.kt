package com.example.busappics4u

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.busappics4u.data.AppContainer
import com.example.busappics4u.data.AppDataContainer
import com.example.busappics4u.data.FileHandler
import com.example.busappics4u.ui.navigation.BusNavGraph
import com.example.busappics4u.ui.navigation.NavItem
import com.example.busappics4u.ui.theme.BusAppICS4UTheme

class MainActivity : ComponentActivity() {
    lateinit var container: AppContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context: Context = applicationContext

        Log.d("BusApplication", "onCreate called wahoo")
        container = AppDataContainer(context)

        FileHandler.load(context)

        setContent {
            BusAppICS4UTheme {
                BusApp(this)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusApp(
    mainActivity: MainActivity
) {
    val navController = rememberNavController()
    val busViewModel = BusViewModel(mainActivity, navController)

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
        bottomBar = { AppBottomNavigation(busViewModel) }
    ) { innerPadding ->
        BusNavGraph(busViewModel, navController, innerPadding)
    }
}

@Composable
fun AppBottomNavigation(
    busViewModel: BusViewModel
) {
    val navItems = listOf(NavItem.Home, NavItem.History, NavItem.Settings)

    val containerColor = MaterialTheme.colorScheme.surface

    NavigationBar(
        containerColor = containerColor,
        contentColor = MaterialTheme.colorScheme.contentColorFor(containerColor)
    ) {
        val navBackStackEntry by busViewModel.navController.currentBackStackEntryAsState()
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
                    busViewModel.navController.navigate(navRoute)
                }
            )
        }
    }
}

//@Preview
//@Composable
//fun MainPreview() {
//    BusApp()
//}
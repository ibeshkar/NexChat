package com.artofelectronic.nexchat.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.artofelectronic.nexchat.ui.navigation.AppNavigation
import com.artofelectronic.nexchat.ui.navigation.Screens
import com.artofelectronic.nexchat.utils.NavigationUtil.navigateToUserList

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppScaffold(startDestination: String) {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    val bottomBarScreens = listOf(Screens.Chats, Screens.Users, Screens.Profile)
    val showBottomBar = bottomBarScreens.any { currentRoute?.startsWith(it.route) == true }

    val showFab = currentRoute == Screens.Chats.route

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .systemBarsPadding(),
        contentWindowInsets = WindowInsets(0,0,0,0),
        topBar = {
            TopBarLayout(navController)
        },
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController)
            }
        },
        floatingActionButton = {
            AnimatedVisibility(showFab) {
                FloatingActionButton(
                    onClick = { navController.navigateToUserList() },
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "User List")
                }
            }
        }
    ) { padding ->
        AppNavigation(
            navController = navController,
            modifier = Modifier.padding(padding),
            startDestination = startDestination
        )
    }
}
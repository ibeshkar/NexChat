package com.artofelectronic.nexchat.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.artofelectronic.nexchat.ui.navigation.AppNavigation
import com.artofelectronic.nexchat.ui.navigation.Screens
import com.artofelectronic.nexchat.ui.theme.LightMintGreen
import com.artofelectronic.nexchat.utils.navigateToUserList

@Composable
fun AppScaffold(startDestination: String) {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    val bottomBarScreens = listOf(Screens.Chats, Screens.Users, Screens.Profile)
    val showBottomBar = bottomBarScreens.any { currentRoute?.startsWith(it.route) == true }

    val showFab = currentRoute == Screens.Chats.route || currentRoute == Screens.Users.route

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController)
            }
        },
        floatingActionButton = {
            AnimatedVisibility(showFab) {
                FloatingActionButton(
                    onClick = { navController.navigateToUserList() }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "New Chat")
                }
            }
        },
        containerColor = LightMintGreen
    ) { padding ->
        AppNavigation(
            navController = navController,
            modifier = Modifier.padding(padding),
            startDestination = startDestination
        )
    }
}
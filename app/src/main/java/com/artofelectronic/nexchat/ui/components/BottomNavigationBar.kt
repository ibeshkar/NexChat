package com.artofelectronic.nexchat.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.artofelectronic.nexchat.ui.navigation.Screens
import java.util.Locale

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(Screens.Chats, Screens.Users, Screens.Profile)
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                icon = {
                    when (screen) {
                        Screens.Chats -> Icon(Icons.AutoMirrored.Filled.Chat, null)
                        Screens.Users -> Icon(Icons.Default.Group, null)
                        Screens.Profile -> Icon(Icons.Default.Person, null)
                        else -> Icon(Icons.Default.QuestionMark, null)
                    }
                },
                label = {
                    Text(screen.route.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
                    })
                },
                selected = currentRoute?.startsWith(screen.route) == true,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            // Avoid multiple copies of the same destination
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}
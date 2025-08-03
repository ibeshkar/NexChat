package com.artofelectronic.nexchat.ui.components

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.artofelectronic.nexchat.ui.navigation.Screens
import java.util.Locale

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(Screens.Chats, Screens.Users, Screens.Profile)
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route
    var selectedIndex by remember { mutableIntStateOf(0) }

    NavigationBar(
        modifier = Modifier.navigationBarsPadding(),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        items.forEachIndexed { index, screen ->
            NavigationBarItem(
                selected = selectedIndex == index,
                icon = {
                    when (screen) {
                        Screens.Chats -> Icon(Icons.AutoMirrored.Filled.Chat, "Chats")
                        Screens.Users -> Icon(Icons.Default.Group, "Users")
                        Screens.Profile -> Icon(Icons.Default.Person, "Profile")
                        else -> Icon(Icons.Default.QuestionMark, null)
                    }
                },
                label = {
                    Text(
                        text = screen.route.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
                        },
                        fontWeight = if (selectedIndex == index) FontWeight.Bold else FontWeight.Normal,
                        style = MaterialTheme.typography.labelLarge
                    )
                },

                onClick = {
                    selectedIndex = index
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
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.onBackground,
                    indicatorColor = MaterialTheme.colorScheme.surfaceContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}
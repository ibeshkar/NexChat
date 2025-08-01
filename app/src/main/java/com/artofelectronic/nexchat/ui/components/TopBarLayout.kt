package com.artofelectronic.nexchat.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.artofelectronic.nexchat.R
import com.artofelectronic.nexchat.ui.navigation.Screens
import com.artofelectronic.nexchat.ui.state.UiState
import com.artofelectronic.nexchat.ui.viewmodels.ProfileViewModel
import com.artofelectronic.nexchat.utils.NavigationUtil.navigateToStart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarLayout(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val profile by profileViewModel.userProfile.collectAsState()
    val logoutState by profileViewModel.signOutState.collectAsState()

    val showMenu = currentRoute == Screens.Profile.route
    var expanded by remember { mutableStateOf(false) }

    val routes = listOf(
        Screens.Start,
        Screens.SignUp,
        Screens.SignIn,
        Screens.Chats,
        Screens.Users,
        Screens.Profile
    )


    when (val state = logoutState) {
        is UiState.Loading -> FullScreenLoadingDialog()
        is UiState.Error -> Toast.makeText(
            LocalContext.current,
            state.message,
            Toast.LENGTH_LONG
        ).show()

        else -> Unit
    }


    LaunchedEffect(logoutState) {
        if (logoutState is UiState.Success) {
            navController.navigateToStart()
        }
    }

    TopAppBar(
        title = {
            when (currentRoute) {
                Screens.Chats.route -> {
                    Text(
                        text = stringResource(R.string.app_name),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }

                Screens.Users.route -> {
                    Text(
                        text = stringResource(R.string.user_list),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }

                Screens.Profile.route -> {
                    Text(
                        text = stringResource(R.string.profile_settings),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }

                else -> {
                    if (currentRoute?.startsWith(Screens.Chat.route) == true) {
                        val otherUserId =
                            navController.currentBackStackEntry?.arguments?.getString(Screens.Chat.ARG_CHAT_ID)
                                ?.split("_")
                                ?.firstOrNull { it != profileViewModel.currentUserId }
                        profileViewModel.fetchUserProfile(otherUserId.orEmpty())

                        val avatarUrl = profile?.avatarUrl.orEmpty()
                        val displayName = profile?.displayName ?: "User"

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (avatarUrl.isNotEmpty()) {
                                CircleAvatar(imageUrl = avatarUrl, size = 36.dp)
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            Text(text = displayName)
                        }
                    }
                }
            }
        },
        navigationIcon = {
            if (currentRoute !in routes.map { it.route }) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions = {
            if (showMenu) {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More options")
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(stringResource(R.string.signout_caption))
                        },
                        onClick = {
                            expanded = false
                            profileViewModel.signOut()
                        }
                    )
                }
            }
        }
    )
}
package com.artofelectronic.nexchat.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.artofelectronic.nexchat.R
import com.artofelectronic.nexchat.ui.components.FullScreenLoadingDialog
import com.artofelectronic.nexchat.ui.components.RetryLayout
import com.artofelectronic.nexchat.ui.components.UserListItem
import com.artofelectronic.nexchat.ui.viewmodels.UserViewModel
import com.artofelectronic.nexchat.utils.NavigationUtil.navigateToChat
import com.artofelectronic.nexchat.utils.Resource
import kotlinx.coroutines.flow.collectLatest

@Composable
fun UserListScreen(
    navController: NavController,
    viewModel: UserViewModel = hiltViewModel()
) {

    val state by viewModel.userList.collectAsState()
    val currentUserId by viewModel.currentUserId.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigation.collectLatest { chatId ->
            navController.navigateToChat(chatId)
        }
    }

    when (val result = state) {
        is Resource.Loading -> {
            FullScreenLoadingDialog()
        }

        is Resource.Error -> {
            RetryLayout(
                errorMessage = result.throwable.message ?: stringResource(R.string.unknown_error),
                onClick = viewModel.observeUsers()
            )
        }

        is Resource.Success -> {
            if (result.data.isEmpty()) {
                RetryLayout(
                    errorMessage = stringResource(R.string.no_users_found),
                    onClick = viewModel.observeUsers()
                )
                return
            }

            LazyColumn {
                items(result.data.filter { it.userId != currentUserId }) { user ->
                    UserListItem(user) {
                        viewModel.onUserSelected(currentUserId, user.userId)
                    }
                }
            }
        }
    }
}
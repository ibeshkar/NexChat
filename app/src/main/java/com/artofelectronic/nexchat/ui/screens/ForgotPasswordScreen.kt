package com.artofelectronic.nexchat.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.artofelectronic.nexchat.R
import com.artofelectronic.nexchat.ui.components.AuthContainer
import com.artofelectronic.nexchat.ui.components.EmailField
import com.artofelectronic.nexchat.ui.viewmodels.AuthViewModel
import com.artofelectronic.nexchat.ui.components.FullScreenLoadingDialog
import com.artofelectronic.nexchat.ui.state.UiState

@Composable
fun ForgotPasswordScreen(
    navController: NavHostController,
    viewModel: AuthViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()
    val formData by viewModel.authFormData.collectAsState()
    val context = navController.context


    when (val state = uiState) {
        is UiState.Loading -> {
            FullScreenLoadingDialog()
        }

        is UiState.Error -> {
            Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
        }

        is UiState.Success -> {
            Toast.makeText(
                context,
                context.getString(R.string.password_reset_email_sent), Toast.LENGTH_LONG
            ).show()

            navController.navigateUp()
        }

        UiState.Idle -> Unit
    }

    AuthContainer {
        EmailField(
            value = formData.email,
            label = stringResource(R.string.forgot_password_screen_caption),
            error = formData.emailError,
            onValueChange = viewModel::onEmailChanged
        )

        Spacer(modifier = Modifier.height(50.dp))

        Button(
            onClick = viewModel::sendPasswordResetEmail,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                stringResource(R.string.submit_caption),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
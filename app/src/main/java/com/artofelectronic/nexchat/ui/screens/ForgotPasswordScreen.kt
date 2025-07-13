package com.artofelectronic.nexchat.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.artofelectronic.nexchat.R
import com.artofelectronic.nexchat.ui.viewmodels.AuthViewModel
import com.artofelectronic.nexchat.ui.components.FullScreenLoadingDialog
import com.artofelectronic.nexchat.ui.state.AuthState
import com.artofelectronic.nexchat.ui.theme.AlmostWhite
import com.artofelectronic.nexchat.ui.theme.DarkerGreen
import com.artofelectronic.nexchat.ui.theme.LightMintGreen

@Composable
fun ForgotPasswordScreen(
    navController: NavHostController,
    viewModel: AuthViewModel = hiltViewModel()
) {

    val uiState by viewModel.authUiState.collectAsState()
    val resultState by viewModel.authState.collectAsState()
    val context = navController.context

    LaunchedEffect(true) {
        if (resultState is AuthState.Success) {
            Toast.makeText(
                context,
                context.getString(R.string.password_reset_email_sent), Toast.LENGTH_LONG
            ).show()

            navController.navigateUp()
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LightMintGreen)
        ) {
            Image(
                painter = painterResource(id = R.drawable.start_screen),
                contentDescription = "Forgot Password Illustration",
                modifier = Modifier
                    .size(250.dp)
                    .align(Alignment.CenterHorizontally),
                alignment = Alignment.BottomEnd,
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = AlmostWhite,
                        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                ) {
                    Spacer(modifier = Modifier.height(30.dp))

                    Text(
                        text = "Forgot\n\nPassword?",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkerGreen
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Enter your account email. We'll send you a reset link.",
                        fontSize = 14.sp,
                        color = DarkerGreen
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    OutlinedTextField(
                        value = uiState.email,
                        onValueChange = viewModel::onEmailChanged,
                        isError = uiState.emailError != null,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Email") },
                        singleLine = true,
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = "Email Icon")
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        shape = RoundedCornerShape(30.dp),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = DarkerGreen,
                            unfocusedIndicatorColor = Color.LightGray,
                            focusedTextColor = DarkerGreen,
                            unfocusedTextColor = DarkerGreen,
                            focusedLeadingIconColor = DarkerGreen,
                            unfocusedLeadingIconColor = DarkerGreen,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        )
                    )

                    uiState.emailError?.let {
                        Text(
                            it,
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(top = 2.dp, end = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = viewModel::sendPasswordResetEmail,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DarkerGreen)
                    ) {
                        Text("Submit", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (resultState is AuthState.Loading) {
            FullScreenLoadingDialog()
        }
    }
}
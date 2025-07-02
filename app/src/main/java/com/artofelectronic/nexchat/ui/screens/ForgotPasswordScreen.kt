package com.artofelectronic.nexchat.ui.screens

import android.annotation.SuppressLint
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.artofelectronic.nexchat.R
import com.artofelectronic.nexchat.data.repository.FirebaseAuthRepository
import com.artofelectronic.nexchat.data.repository.SignupRepositoryImpl
import com.artofelectronic.nexchat.domain.usecases.CheckUserSignInStatusUseCase
import com.artofelectronic.nexchat.domain.usecases.SendPasswordResetEmailUseCase
import com.artofelectronic.nexchat.domain.usecases.SignInWithEmailUseCase
import com.artofelectronic.nexchat.domain.usecases.SignInWithFacebookUseCase
import com.artofelectronic.nexchat.domain.usecases.SignInWithGoogleUseCase
import com.artofelectronic.nexchat.domain.usecases.SignInWithTwitterUseCase
import com.artofelectronic.nexchat.domain.usecases.SignupWithEmailUseCase
import com.artofelectronic.nexchat.ui.viewmodels.AuthViewModel
import com.artofelectronic.nexchat.ui.components.FullScreenLoadingDialog
import com.artofelectronic.nexchat.ui.state.SignupState
import com.artofelectronic.nexchat.ui.theme.AlmostWhite
import com.artofelectronic.nexchat.ui.theme.DarkerGreen
import com.artofelectronic.nexchat.ui.theme.LightMintGreen
import com.artofelectronic.nexchat.utils.InputValidator
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ForgotPasswordScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    var email by rememberSaveable { mutableStateOf("") }

    var emailError by remember { mutableStateOf<String?>(null) }

    var isLoading by remember { mutableStateOf(false) }


    // Track lifecycle state
    val lifecycleOwner = LocalLifecycleOwner.current
    var isVisible by remember { mutableStateOf(true) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            isVisible = event == Lifecycle.Event.ON_RESUME
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(key1 = authViewModel.resetPasswordState, key2 = isVisible) {
        authViewModel.resetPasswordState.collect { state ->
            when (state) {
                is SignupState.Idle -> {
                    isLoading = false
                }

                is SignupState.Loading -> {
                    isLoading = true
                }

                is SignupState.Success -> {
                    isLoading = false
                    if (!isVisible) return@collect

                    Toast.makeText(
                        navController.context,
                        "Password reset email sent",
                        Toast.LENGTH_LONG
                    ).show()

                    navController.navigateUp()
                }

                is SignupState.Error -> {
                    isLoading = false
                    if (!isVisible) return@collect

                    Toast.makeText(navController.context, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    val onEmailChange = remember {
        { newEmail: String ->
            email = newEmail
            emailError = null
        }
    }

    val onSubmitClickListener = remember {
        {
            emailError = InputValidator.validateEmail(email)
            if (emailError == null) {
                isLoading = true
                authViewModel.sendPasswordResetEmail(email)
            }
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
                contentDescription = "Top Image",
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
                        shape = RoundedCornerShape(
                            topStart = 30.dp,
                            topEnd = 30.dp
                        )
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

                    Spacer(modifier = Modifier.height(50.dp))

                    Text(
                        text = "Please enter the email address associated with your account, and we'll send you a link to reset your password.",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkerGreen
                    )

                    Spacer(modifier = Modifier.height(50.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = onEmailChange,
                        isError = emailError != null,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholder = { Text(text = "Email") },
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = "Email Icon")
                        },
                        shape = RoundedCornerShape(30.dp),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = DarkerGreen,
                            unfocusedIndicatorColor = Color.LightGray,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedTextColor = DarkerGreen,
                            unfocusedTextColor = DarkerGreen,
                            focusedLeadingIconColor = DarkerGreen,
                            unfocusedLeadingIconColor = DarkerGreen,
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        )
                    )

                    emailError?.let {
                        Text(
                            it,
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(top = 2.dp, end = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    Button(
                        onClick = onSubmitClickListener,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DarkerGreen
                        )
                    ) {
                        Text(
                            text = "Submit",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        if (isLoading) {
            FullScreenLoadingDialog()
        }
    }

}

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun ForgotPasswordScreenPreview() {
    ForgotPasswordScreen(
        navController = rememberNavController(),
        authViewModel = AuthViewModel(
            CheckUserSignInStatusUseCase(
                FirebaseAuthRepository()
            ),
            SignupWithEmailUseCase(
                SignupRepositoryImpl(
                    FirebaseAuth.getInstance()
                )
            ),
            SignInWithGoogleUseCase(
                SignupRepositoryImpl(
                    FirebaseAuth.getInstance()
                )
            ),
            SignInWithTwitterUseCase(
                SignupRepositoryImpl(
                    FirebaseAuth.getInstance()
                )
            ),
            SignInWithFacebookUseCase(
                SignupRepositoryImpl(
                    FirebaseAuth.getInstance()
                )
            ),
            SignInWithEmailUseCase(
                FirebaseAuthRepository()
            ),
            SendPasswordResetEmailUseCase(
                FirebaseAuthRepository()
            )
        )
    )
}
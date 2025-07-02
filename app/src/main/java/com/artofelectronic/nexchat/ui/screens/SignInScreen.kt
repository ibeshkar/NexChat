package com.artofelectronic.nexchat.ui.screens

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
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
import com.artofelectronic.nexchat.ui.activities.AuthActivity
import com.artofelectronic.nexchat.ui.activities.MainActivity
import com.artofelectronic.nexchat.ui.viewmodels.AuthViewModel
import com.artofelectronic.nexchat.ui.components.AuthProvider
import com.artofelectronic.nexchat.ui.components.FullScreenLoadingDialog
import com.artofelectronic.nexchat.ui.components.OrDivider
import com.artofelectronic.nexchat.ui.components.SocialButton
import com.artofelectronic.nexchat.ui.navigation.Screens
import com.artofelectronic.nexchat.ui.state.SignupState
import com.artofelectronic.nexchat.ui.theme.AlmostWhite
import com.artofelectronic.nexchat.ui.theme.DarkerGreen
import com.artofelectronic.nexchat.ui.theme.LightMintGreen
import com.artofelectronic.nexchat.utils.InputValidator
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SignInScreen(
    navController: NavController,
    viewModel: AuthViewModel
) {

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

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


    LaunchedEffect(key1 = viewModel.signInState, key2 = isVisible) {
        viewModel.signInState.collect { state ->
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

                    val context = navController.context
                    Intent(context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        context.startActivity(this)
                        (context as? AuthActivity)?.finish()
                    }
                }

                is SignupState.Error -> {
                    isLoading = false
                    if (!isVisible) return@collect

                    Toast.makeText(navController.context, state.message, Toast.LENGTH_LONG)
                        .show()
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

    val onPasswordChange = remember {
        { pass: String ->
            password = pass
            passwordError = null
        }
    }

    val onSignInClickListener = remember {
        {
            emailError = InputValidator.validateEmail(email)
            passwordError = InputValidator.validatePassword(password)

            if (emailError == null && passwordError == null) {
                isLoading = true
                viewModel.signInWithEmail(email, password)
            }
        }
    }

    val onGoogleSignInClickListener = remember {
        {
            val context = navController.context
            viewModel.signInWithGoogle(context)
        }
    }

    val onFacebookSignInClickListener = remember {
        {
            LoginManager.getInstance().logInWithReadPermissions(
                navController.context as Activity,
                listOf("email", "public_profile")
            )
        }
    }

    val onTwitterSignInClickListener = remember {
        {
            viewModel.signInWithTwitter(navController.context as Activity)
        }
    }

    val onSignUpClickListener = remember {
        Modifier.clickable {
            navController.navigate(Screens.SignUp.route) {
                popUpTo(Screens.SignIn.route) {
                    inclusive = true
                }
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
                    .size(200.dp)
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

                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = onPasswordChange,
                        isError = passwordError != null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("PasswordField"),
                        singleLine = true,
                        placeholder = { Text(text = "Password") },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = "Lock Icon")
                        },
                        trailingIcon = {
                            val icon =
                                if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                            IconButton(
                                onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = icon, contentDescription = "Visibility Icon")
                            }
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
                            focusedTrailingIconColor = DarkerGreen,
                            unfocusedTrailingIconColor = DarkerGreen
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
                    )

                    passwordError?.let {
                        Text(
                            it,
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(top = 2.dp, end = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = AnnotatedString("Forgot password?"),
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(end = 5.dp)
                            .clickable {
                                navController.navigate(Screens.ForgotPassword.route)
                            },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkerGreen
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = onSignInClickListener,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DarkerGreen
                        )
                    ) {
                        Text(
                            text = "Sign In",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(100.dp))

                    OrDivider()

                    Spacer(modifier = Modifier.height(50.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        SocialButton(
                            provider = AuthProvider.Google,
                            onClick = onGoogleSignInClickListener
                        )

                        SocialButton(
                            provider = AuthProvider.Facebook,
                            onClick = onFacebookSignInClickListener
                        )

                        SocialButton(
                            provider = AuthProvider.Twitter,
                            onClick = onTwitterSignInClickListener
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {

                        Row(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Have no account?",
                                fontSize = 14.sp,
                            )

                            Text(
                                text = AnnotatedString(" Sign up"),
                                modifier = onSignUpClickListener,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkerGreen
                            )
                        }
                    }
                }
            }
        }

        if (isLoading) {
            FullScreenLoadingDialog()
        }
    }

}

@Preview
@Composable
fun SignInScreenPreview() {
    SignInScreen(
        navController = rememberNavController(),
        viewModel = AuthViewModel(
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
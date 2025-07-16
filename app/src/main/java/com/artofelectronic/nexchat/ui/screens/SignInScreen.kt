package com.artofelectronic.nexchat.ui.screens

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.artofelectronic.nexchat.R
import com.artofelectronic.nexchat.ui.viewmodels.AuthViewModel
import com.artofelectronic.nexchat.ui.components.EmailField
import com.artofelectronic.nexchat.ui.components.FullScreenLoadingDialog
import com.artofelectronic.nexchat.ui.components.OrDivider
import com.artofelectronic.nexchat.ui.components.PasswordField
import com.artofelectronic.nexchat.ui.components.SocialAuthRow
import com.artofelectronic.nexchat.ui.state.AuthState
import com.artofelectronic.nexchat.ui.state.AuthUiState
import com.artofelectronic.nexchat.ui.theme.AlmostWhite
import com.artofelectronic.nexchat.ui.theme.DarkerGreen
import com.artofelectronic.nexchat.ui.theme.LightMintGreen
import com.artofelectronic.nexchat.utils.navigateToChats
import com.artofelectronic.nexchat.utils.navigateToForgotPassword
import com.artofelectronic.nexchat.utils.navigateToSignup
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult

@Composable
fun SignInScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {

    val uiState by viewModel.authUiState.collectAsState()
    val resultState by viewModel.authState.collectAsState()
    val context = navController.context

    // Initialize Facebook SDK for signup
    val activity = LocalActivity.current as Activity
    val callbackManager = viewModel.facebookCallbackManager

    val onFacebookSignInClick = {
        LoginManager.getInstance().logInWithReadPermissions(
            activity,
            listOf("email", "public_profile")
        )
        LoginManager.getInstance().registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    viewModel.handleFacebookAccessToken(result.accessToken)
                }

                override fun onCancel() {}
                override fun onError(error: FacebookException) {
                    Toast.makeText(context, "Facebook sign-in failed-> ${error.message}", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    SignInContent(
        uiState,
        onEmailChange = viewModel::onEmailChanged,
        onPasswordChange = viewModel::onPasswordChanged,
        onSignInClick = viewModel::signInWithEmail,
        onGoogleClick = viewModel::signupWithGoogle,
        onFacebookClick = onFacebookSignInClick,
        onTwitterClick = { viewModel.signInWithTwitter(context as Activity) },
        onSignUpClick = { navController.navigateToSignup() },
        onForgotPasswordClick = { navController.navigateToForgotPassword() }
    )


    when (resultState) {
        is AuthState.Loading -> {
            FullScreenLoadingDialog()
        }

        is AuthState.Success -> {
            navController.navigateToChats()
        }

        is AuthState.Error -> {
            Toast.makeText(
                context,
                (resultState as AuthState.Error).message,
                Toast.LENGTH_SHORT
            ).show()

            viewModel.resetAuthState()
        }

        else -> {}
    }
}

@Composable
private fun SignInContent(
    uiState: AuthUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignInClick: () -> Unit,
    onGoogleClick: () -> Unit,
    onFacebookClick: () -> Unit,
    onTwitterClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
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
                    .align(Alignment.CenterHorizontally)
            )

            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .background(
                        color = AlmostWhite,
                        shape = RoundedCornerShape(30.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                ) {

                    EmailField(
                        uiState.email,
                        uiState.emailError,
                        onEmailChange
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    PasswordField(
                        label = "Password",
                        password = uiState.password,
                        error = uiState.passwordError,
                        onPasswordChange = onPasswordChange
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Forgot password?",
                        modifier = Modifier
                            .align(Alignment.End)
                            .clickable { onForgotPasswordClick() },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkerGreen
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = onSignInClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DarkerGreen)
                    ) {
                        Text("Sign In", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(50.dp))

                    OrDivider()

                    Spacer(modifier = Modifier.height(30.dp))

                    SocialAuthRow(
                        onGoogleClick = onGoogleClick,
                        onFacebookClick = onFacebookClick,
                        onTwitterClick = onTwitterClick
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        Text("Have no account?", fontSize = 14.sp)
                        Text(
                            text = " Sign up",
                            fontWeight = FontWeight.Bold,
                            color = DarkerGreen,
                            fontSize = 14.sp,
                            modifier = Modifier.clickable { onSignUpClick() }
                        )
                    }
                }
            }
        }
    }
}
package com.artofelectronic.nexchat.ui.screens

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.artofelectronic.nexchat.R
import com.artofelectronic.nexchat.ui.components.AuthContainer
import com.artofelectronic.nexchat.ui.viewmodels.AuthViewModel
import com.artofelectronic.nexchat.ui.components.EmailField
import com.artofelectronic.nexchat.ui.components.FullScreenLoadingDialog
import com.artofelectronic.nexchat.ui.components.OrDivider
import com.artofelectronic.nexchat.ui.components.PasswordField
import com.artofelectronic.nexchat.ui.components.SocialAuthRow
import com.artofelectronic.nexchat.ui.state.UiState
import com.artofelectronic.nexchat.ui.state.AuthFormData
import com.artofelectronic.nexchat.utils.NavigationUtil.navigateToChats
import com.artofelectronic.nexchat.utils.NavigationUtil.navigateToForgotPassword
import com.artofelectronic.nexchat.utils.NavigationUtil.navigateToSignup
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult

@Composable
fun SignInScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {

    val formData by viewModel.authFormData.collectAsState()
    val resultState by viewModel.uiState.collectAsState()
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
                    Toast.makeText(
                        context,
                        "${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }

    when (resultState) {
        is UiState.Loading -> {
            FullScreenLoadingDialog()
        }

        is UiState.Success -> {
            navController.navigateToChats()
        }

        is UiState.Error -> {
            Toast.makeText(
                context,
                (resultState as UiState.Error).message,
                Toast.LENGTH_LONG
            ).show()

            viewModel.resetAuthState()
        }

        else -> Unit
    }



    SignInContent(
        formData,
        onEmailChange = viewModel::onEmailChanged,
        onPasswordChange = viewModel::onPasswordChanged,
        onSignInClick = viewModel::signInWithEmail,
        onGoogleClick = viewModel::signupWithGoogle,
        onFacebookClick = onFacebookSignInClick,
        onTwitterClick = { viewModel.signInWithTwitter(context as Activity) },
        onSignUpClick = { navController.navigateToSignup() },
        onForgotPasswordClick = { navController.navigateToForgotPassword() }
    )

}

@Composable
private fun SignInContent(
    authFormData: AuthFormData,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignInClick: () -> Unit,
    onGoogleClick: () -> Unit,
    onFacebookClick: () -> Unit,
    onTwitterClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {

    AuthContainer {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                EmailField(
                    value = authFormData.email,
                    label = stringResource(R.string.email_caption),
                    error = authFormData.emailError,
                    onValueChange = onEmailChange
                )

                Spacer(Modifier.height(8.dp))

                PasswordField(
                    label = stringResource(R.string.password_caption),
                    password = authFormData.password,
                    error = authFormData.passwordError,
                    onPasswordChange = onPasswordChange
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = stringResource(R.string.forgot_password_caption),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.End)
                        .clickable { onForgotPasswordClick() },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = onSignInClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = stringResource(R.string.sign_in_caption),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(32.dp))

                OrDivider()

                Spacer(Modifier.height(24.dp))

                SocialAuthRow(
                    onGoogleClick = onGoogleClick,
                    onFacebookClick = onFacebookClick,
                    onTwitterClick = onTwitterClick
                )
            }

            Spacer(Modifier.height(32.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
            ) {
                Text(
                    stringResource(R.string.have_no_account_caption),
                    fontSize = 14.sp
                )
                Text(
                    text = stringResource(R.string.sign_up_caption2),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { onSignUpClick() }
                )
            }
        }
    }
}

@Preview
@Composable
private fun SignInContentPreview() {
    SignInContent(
        authFormData = AuthFormData(),
        onEmailChange = {},
        onPasswordChange = {},
        onSignInClick = {},
        onGoogleClick = {},
        onFacebookClick = {},
        onTwitterClick = {},
        onSignUpClick = {},
        onForgotPasswordClick = {}
    )
}
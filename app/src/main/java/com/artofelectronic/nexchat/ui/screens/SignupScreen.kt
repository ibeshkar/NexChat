package com.artofelectronic.nexchat.ui.screens

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.artofelectronic.nexchat.ui.navigation.Screens
import com.artofelectronic.nexchat.ui.state.UiState
import com.artofelectronic.nexchat.ui.state.AuthFormData
import com.artofelectronic.nexchat.utils.NavigationUtil.navigateToChats
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult

@Composable
fun SignupScreen(
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
                Toast.LENGTH_SHORT
            ).show()

            viewModel.resetAuthState()
        }

        else -> Unit
    }



    SignupContent(
        authFormData = formData,
        onEmailChange = viewModel::onEmailChanged,
        onPasswordChange = viewModel::onPasswordChanged,
        onConfirmPasswordChange = viewModel::onConfirmPasswordChanged,
        onSignupClick = viewModel::signupWithEmail,
        onGoogleClick = viewModel::signupWithGoogle,
        onFacebookClick = onFacebookSignInClick,
        onTwitterClick = { viewModel.signInWithTwitter(context as Activity) },
        onNavigateToSignIn = { navController.navigate(Screens.SignIn.route) }
    )

}

@Composable
private fun SignupContent(
    authFormData: AuthFormData,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onSignupClick: () -> Unit,
    onGoogleClick: () -> Unit,
    onFacebookClick: () -> Unit,
    onTwitterClick: () -> Unit,
    onNavigateToSignIn: () -> Unit
) {

    AuthContainer {
        EmailField(
            value = authFormData.email,
            label = stringResource(R.string.email_caption),
            error = authFormData.emailError,
            onValueChange = onEmailChange
        )

        Spacer(Modifier.height(6.dp))

        PasswordField(
            label = stringResource(R.string.password_caption),
            password = authFormData.password,
            error = authFormData.passwordError,
            onPasswordChange = onPasswordChange
        )

        Spacer(Modifier.height(6.dp))

        PasswordField(
            label = stringResource(R.string.confirm_password_caption),
            password = authFormData.confirmPassword,
            error = authFormData.confirmPasswordError,
            onPasswordChange = onConfirmPasswordChange
        )

        Spacer(Modifier.height(30.dp))

        Button(
            onClick = onSignupClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                stringResource(R.string.sign_up_caption),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(50.dp))

        OrDivider()

        Spacer(Modifier.height(30.dp))

        SocialAuthRow(
            onGoogleClick = onGoogleClick,
            onFacebookClick = onFacebookClick,
            onTwitterClick = onTwitterClick
        )

        Spacer(Modifier.height(60.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(R.string.already_have_an_account_caption),
                fontSize = 14.sp
            )
            Text(
                stringResource(R.string.sign_in_caption2),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onNavigateToSignIn() }
            )
        }
    }
}
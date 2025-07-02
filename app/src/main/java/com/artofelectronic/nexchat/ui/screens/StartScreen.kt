package com.artofelectronic.nexchat.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.artofelectronic.nexchat.R
import com.artofelectronic.nexchat.ui.navigation.Screens
import com.artofelectronic.nexchat.ui.theme.AlmostWhite
import com.artofelectronic.nexchat.ui.theme.DarkerGreen
import com.artofelectronic.nexchat.ui.theme.LightMintGreen

@Composable
fun StartScreen(navController: NavController) {

    val onSignInClickListener = remember {
        {
            navController.navigate(Screens.SignIn.route)
        }
    }

    val onSignUpClickListener = remember {
        {
            navController.navigate(Screens.SignUp.route)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightMintGreen)
            .padding(horizontal = 32.dp)
    ) {

        Spacer(modifier = Modifier.height(100.dp))

        Text(
            text = "Welcome to NEXChat",
            color = DarkerGreen,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(100.dp))

        Image(
            painter = painterResource(id = R.drawable.start_screen),
            contentDescription = "Start Screen Image",
            modifier = Modifier
                .fillMaxWidth()
                .size(200.dp)
        )

        Text(
            text = "Connect with friends and family anytime, anywhere.",
            color = DarkerGreen,
            fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(150.dp)
        )

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

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Button(
            onClick = onSignUpClickListener,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AlmostWhite
            )
        ) {
            Text(
                text = "Sign Up",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DarkerGreen
            )
        }

    }
}

@Preview
@Composable
fun StartScreenPreview() {
    StartScreen(navController = rememberNavController())
}
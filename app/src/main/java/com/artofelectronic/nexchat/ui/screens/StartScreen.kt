package com.artofelectronic.nexchat.ui.screens

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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.artofelectronic.nexchat.R
import com.artofelectronic.nexchat.utils.NavigationUtil.navigateToSignIn
import com.artofelectronic.nexchat.utils.NavigationUtil.navigateToSignup

@Composable
fun StartScreen(navController: NavController) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.retro_top_image),
                contentDescription = "Start Screen Image",
                modifier = Modifier.size(250.dp)
            )

            Text(
                text = stringResource(R.string.welcome_to_nexchat_title),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.connect_with_friends_and_family_anytime_anywhere_text),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { navController.navigateToSignIn() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 320.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    )
                ) {
                    Text(stringResource(R.string.sign_in_caption), fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { navController.navigateToSignup() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 320.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    Text(stringResource(R.string.sign_up_caption), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Preview
@Composable
fun StartScreenPreview() {
    StartScreen(navController = NavController(LocalContext.current))
}
package com.artofelectronic.nexchat.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.artofelectronic.nexchat.R
import com.artofelectronic.nexchat.ui.components.CircleAvatar
import com.artofelectronic.nexchat.ui.components.FullScreenLoadingDialog
import com.artofelectronic.nexchat.ui.components.PhotoSourceDialog
import com.artofelectronic.nexchat.ui.components.RetryLayout
import com.artofelectronic.nexchat.ui.state.UiState
import com.artofelectronic.nexchat.ui.viewmodels.ProfileViewModel
import java.io.File
import java.util.UUID

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier, viewModel: ProfileViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    var galleryImageUri by remember { mutableStateOf<Uri?>(null) }
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }
    var isAvatarChanged by remember { mutableStateOf(false) }

    var displayName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var avatarUrl by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }

    var showSheet by remember { mutableStateOf(false) }


    LaunchedEffect(userProfile) {
        userProfile?.let {
            displayName = it.displayName
            email = it.email
            avatarUrl = it.avatarUrl
            bio = it.bio.orEmpty()
        }
    }


    // Init gallery Photo Picker launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(), onResult = { uri: Uri? ->
            if (uri != null) {
                galleryImageUri = uri
                avatarUrl = uri.toString()
                isAvatarChanged = true
            }
        })

    // Init Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && cameraImageUri != null) {
            avatarUrl = cameraImageUri.toString()
            isAvatarChanged = true
        }
    }

    // Camera permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val uri = createImageUri(context)
            cameraImageUri = uri
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(
                context, context.getString(R.string.camera_permission_denied), Toast.LENGTH_SHORT
            ).show()
        }
    }


    // Open bottom sheet menu to choose image source
    if (showSheet) {
        PhotoSourceDialog(
            onOpenGallery = {
                showSheet = false
                galleryLauncher.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }, onOpenCamera = {
                showSheet = false
                cameraImageUri =
                    openCamera(context, cameraImageUri, cameraLauncher, permissionLauncher)
            }, onDismiss = {
                showSheet = false
            })
    }


    when (val state = uiState) {
        is UiState.Loading -> {
            FullScreenLoadingDialog()
        }

        is UiState.Error -> {
            RetryLayout(
                onClick = { viewModel.getUserProfile() }
            )
        }

        else -> Unit
    }


    // Main Content
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        CircleAvatar(
            imageUrl = avatarUrl,
            size = 200.dp,
            userName = displayName,
            contentDescription = "Profile picture",
            onPhotoPick = { showSheet = true }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = email,
            fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = displayName,
            onValueChange = { displayName = it },
            label = { Text(stringResource(R.string.display_name_caption)) },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = bio,
            onValueChange = { bio = it },
            modifier = Modifier.height(200.dp),
            label = { Text(stringResource(R.string.bio_caption)) },
            maxLines = 3
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.updateUserProfile(
                    user = userProfile?.copy(displayName = displayName, bio = bio),
                    isAvatarChanged = isAvatarChanged,
                    avatarUri = galleryImageUri ?: cameraImageUri
                )
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        ) {
            Text(stringResource(R.string.save_changes_caption))
        }
    }
}

private fun openCamera(
    context: Context,
    cameraImageUri: Uri?,
    cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>,
    permissionLauncher: ManagedActivityResultLauncher<String, Boolean>
): Uri? {
    var imageUri = cameraImageUri
    when (PackageManager.PERMISSION_GRANTED) {
        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) -> {
            val uri = createImageUri(context)
            imageUri = uri
            cameraLauncher.launch(uri)
        }

        else -> {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }
    return imageUri
}

private fun createImageUri(context: Context): Uri {
    val imageFile = File(
        context.cacheDir, "images/${UUID.randomUUID()}.jpg"
    ).apply { parentFile?.mkdirs() }

    val imageUri = FileProvider.getUriForFile(
        context, "${context.packageName}.fileprovider", imageFile
    )

    return imageUri
}
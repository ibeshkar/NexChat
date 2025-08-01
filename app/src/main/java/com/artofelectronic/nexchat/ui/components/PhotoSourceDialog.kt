package com.artofelectronic.nexchat.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.artofelectronic.nexchat.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoSourceDialog(
    modifier: Modifier = Modifier,
    onOpenCamera: () -> Unit,
    onOpenGallery: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isSheetVisible by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    // Launch bottom sheet when isSheetOpen becomes true
    LaunchedEffect(isSheetVisible) {
        if (isSheetVisible) {
            coroutineScope.launch {
                sheetState.show()
            }
        }
    }


    ModalBottomSheet(
        onDismissRequest = {
            isSheetVisible = false
            onDismiss()
        },
        sheetState = sheetState
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.choose_option_caption),
                style = MaterialTheme.typography.titleMedium,
                modifier = modifier.padding(bottom = 16.dp)
            )

            Spacer(modifier = modifier.height(12.dp))

            Button(
                onClick = {
                    isSheetVisible = false
                    onOpenCamera()
                },
                modifier = modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.open_camera_caption))
            }

            Spacer(modifier = modifier.height(8.dp))

            Button(
                onClick = {
                    isSheetVisible = false
                    onOpenGallery()
                },
                modifier = modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.select_from_gallery_caption))
            }
        }
    }
}
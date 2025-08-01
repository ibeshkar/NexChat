package com.artofelectronic.nexchat.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp

@Composable
fun PasswordField(
    label: String,
    password: String,
    error: String?,
    onPasswordChange: (String) -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { Text(text = label, fontSize = 14.sp) },
        isError = error != null,
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Lead lock icon") },
        trailingIcon = {
            val icon = if (visible) Icons.Default.Visibility else Icons.Default.VisibilityOff
            IconButton(onClick = { visible = !visible }) {
                Icon(imageVector = icon, contentDescription = "Visibility Icon $label")
            }
        },
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        shape = MaterialTheme.shapes.extraLarge
    )
    error?.let {
        Text(it, color = Color.Red, style = MaterialTheme.typography.bodySmall)
    }
}
package com.artofelectronic.nexchat.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    OutlinedTextField(
        value = query,
        onValueChange = { onQueryChanged(it) },
        modifier = modifier,
        textStyle = TextStyle(color = Color.Black),
        singleLine = true,
        placeholder = { Text(text = "Search", color = Color.Gray) },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Search")
        },
        shape = RoundedCornerShape(30.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Gray.copy(alpha = 0.5f),
            unfocusedIndicatorColor = Color.Gray.copy(alpha = 0.5f),
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            focusedLeadingIconColor = Color.Gray,
            unfocusedLeadingIconColor = Color.Gray,
        )
    )
}


@Preview
@Composable
private fun PreviewSearchBar() {
    SearchBar(
        query = "",
        onQueryChanged = {}
    )
}
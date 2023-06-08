package com.interview.profilecreator.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun OutlinedTextFieldBackground(
    content: @Composable () -> Unit,
) {
    // This box just wraps the background and the OutlinedTextField
    Box {
        // This box works as background
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(top = 10.dp) // adding some space to the label
                .background(
                    Color.White,
                    // rounded corner to match with the OutlinedTextField
                    shape = RoundedCornerShape(30.dp)
                )
        )
        // OutlineTextField will be the content...
        content()
    }
}
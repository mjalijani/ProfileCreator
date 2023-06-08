package com.interview.profilecreator.ui.screens.signIn

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.interview.profilecreator.R
import com.interview.profilecreator.data.User
import com.interview.profilecreator.utils.decodeFromBase64
import com.skydoves.landscapist.glide.GlideImage

@ExperimentalComposeUiApi
@Composable
fun SignIn(user: User) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Hello ${user.name}",
            style = TextStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Text(
            text = stringResource(R.string.signInDescription),
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Light
            )
        )

        GlideImage(
            imageModel = { user.image.decodeFromBase64() },
            modifier = Modifier
                .size(150.dp)
                .padding(20.dp)
        )

        Text(
            text = user.website,
            modifier = Modifier
                .padding(10.dp),
            textDecoration = TextDecoration.Underline,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Light,
                color = Color.Blue
            )
        )

        Text(
            text = user.name,
            modifier = Modifier
                .padding(10.dp),
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Light,
                color = Color.Black
            )
        )

        Text(
            text = user.email,
            modifier = Modifier
                .padding(10.dp),
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Light,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.size(20.dp))

        Button(onClick = {
            // do nothing
        }) {
            Text(
                text = stringResource(R.string.signin), modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                textAlign = TextAlign.Center
            )
        }

    }
}


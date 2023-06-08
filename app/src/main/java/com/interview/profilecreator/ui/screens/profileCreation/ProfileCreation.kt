package com.interview.profilecreator.ui.screens.profileCreation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.interview.profilecreator.R
import com.interview.profilecreator.data.User
import com.interview.profilecreator.navigation.Screen
import com.interview.profilecreator.ui.component.ChooseImageFromCameraOrGalleryDialog
import com.interview.profilecreator.ui.component.OutlinedTextFieldBackground
import com.interview.profilecreator.utils.StorageFileUtil
import com.interview.profilecreator.utils.encodeToBae64
import com.interview.profilecreator.utils.showToast
import com.skydoves.landscapist.glide.GlideImage
import java.util.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProfileCreation(navController: NavController) {
    val viewModel = hiltViewModel<ProfileCreationViewModel>()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val passwordVisible = rememberSaveable { mutableStateOf(false) }

    val showDialog = remember {
        mutableStateOf(false)
    }
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { resStatus ->
            if (resStatus && viewModel.cameraUri.value != null) viewModel.image.value =
                viewModel.cameraUri.value!!
        }
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { imageUri ->
            imageUri?.let {
                viewModel.image.value = imageUri.toString()
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(R.string.profileCreation),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start),
            style = TextStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Text(
            text = stringResource(R.string.profileDesctription),
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Light
            )
        )

        GlideImage(
            imageModel = { viewModel.image.value },
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .padding(20.dp)
                .clickable {
                    showDialog.value = true
                }
        )

        ChooseImageFromCameraOrGalleryDialog(
            onCameraClick = {
                StorageFileUtil.createFileInPublicImageDirectory(
                    coroutineScope = coroutineScope,
                    context = context,
                    fileName = "img-${System.currentTimeMillis()}.jpg",
                    mimeType = "image/jpeg",
                    parentPath = null,
                    true
                ) { uri, _ ->
                    viewModel.cameraUri.value = uri
                    cameraLauncher.launch(uri)
                }
            },
            onGalleryClick = { galleryLauncher.launch("image/*") },
            bottomSheetVisible = showDialog
        )

        OutlinedTextFieldBackground {
            OutlinedTextField(
                value = viewModel.name.value,
                singleLine = true,
                onValueChange = { viewModel.name.value = it },
                label = {
                    Text(
                        text = stringResource(R.string.firstName),
                        style = TextStyle(fontSize = 15.sp)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            )
        }

        OutlinedTextFieldBackground {
            OutlinedTextField(
                value = viewModel.email.value,
                singleLine = true,
                onValueChange = { viewModel.email.value = it },
                label = {
                    Text(
                        text = stringResource(R.string.email),
                        style = TextStyle(fontSize = 15.sp)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            )
        }

        OutlinedTextFieldBackground {
            OutlinedTextField(
                value = viewModel.pass.value,
                onValueChange = { viewModel.pass.value = it },
                singleLine = true,
                visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                label = {
                    Text(
                        text = stringResource(R.string.pass),
                        style = TextStyle(fontSize = 15.sp)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisible.value)
                        R.drawable.visibility
                    else R.drawable.visibility_off

                    val description =
                        if (passwordVisible.value) stringResource(R.string.hidePass) else stringResource(
                            R.string.showPass
                        )

                    IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                        Icon(painter = painterResource(image), description)
                    }
                }
            )
        }

        OutlinedTextFieldBackground {
            OutlinedTextField(
                value = viewModel.website.value,
                singleLine = true,
                onValueChange = { viewModel.website.value = it },
                label = {
                    Text(
                        text = stringResource(R.string.website),
                        style = TextStyle(fontSize = 15.sp)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            )
        }

        Spacer(modifier = Modifier.size(20.dp))

        Button(onClick = {

            val user = User(
                viewModel.name.value,
                viewModel.email.value,
                viewModel.image.value.toString()
                    .encodeToBae64(), // I'm encoding the image uri to base64 cause navigation component make mistake and thinks that image uri is part of route uri address but it's just argument
                viewModel.pass.value,
                viewModel.website.value
            )
            if (viewModel.inputsAreValid()) {
                navigateToSignInScreen(navController, user, viewModel)
            } else {
                context.showToast(viewModel.findAppropriateError())
            }

        }) {
            Text(
                text = stringResource(R.string.submit), modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                textAlign = TextAlign.Center
            )
        }

    }
}

private fun navigateToSignInScreen(
    navController: NavController,
    user: User,
    viewModel: ProfileCreationViewModel
) {
    navController.navigate(
        Screen.SignIn.route.plus(
            "/${viewModel.mapUserGsonToJson(user)}"
        )
    )
}




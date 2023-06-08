package com.interview.profilecreator.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.interview.profilecreator.R

@ExperimentalComposeUiApi
@Composable
fun ChooseImageFromCameraOrGalleryDialog(
    onCameraClick: (() -> Unit),
    onGalleryClick: (() -> Unit),
    bottomSheetVisible: MutableState<Boolean>
) {

    if (bottomSheetVisible.value) {
        Dialog(
            onDismissRequest = {
                bottomSheetVisible.value = false
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                usePlatformDefaultWidth = false,
                dismissOnClickOutside = true
            ),
            content = {

                Box(modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures {
                            bottomSheetVisible.value = false
                        }
                    }) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .pointerInput(Unit) {
                                detectTapGestures {
                                }
                            },
                        shape = RoundedCornerShape(30.dp, 30.dp, 0.dp, 0.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Spacer(modifier = Modifier.padding(10.dp))

                            Text(
                                text = stringResource(R.string.chooseOne),
                                color = Color.Black,
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                            )

                            Spacer(modifier = Modifier.padding(5.dp))


                            Row {


                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.camera),
                                        contentDescription = "Logo",
                                        modifier = Modifier
                                            .width(140.dp)
                                            .height(40.dp)
                                            .align(Alignment.Center)
                                            .clickable {
                                                onCameraClick()
                                                bottomSheetVisible.value = false
                                            },
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.gallery),
                                        contentDescription = "Logo",
                                        modifier = Modifier
                                            .width(140.dp)
                                            .height(40.dp)
                                            .align(Alignment.Center)
                                            .clickable {
                                                onGalleryClick()
                                                bottomSheetVisible.value = false
                                            },
                                    )
                                }

                            }

                            Spacer(modifier = Modifier.padding(10.dp))

                        }

                    }
                }
            })
    }

}
package com.interview.profilecreator.ui.screens.profileCreation

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.interview.profilecreator.R
import com.interview.profilecreator.data.InputErrors
import com.interview.profilecreator.data.User
import com.interview.profilecreator.utils.isEmailValid
import com.interview.profilecreator.utils.isUrlValid
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileCreationViewModel @Inject constructor() : ViewModel() {
    val name: MutableState<String> = mutableStateOf("")
    val email: MutableState<String> = mutableStateOf("")
    val pass: MutableState<String> = mutableStateOf("")
    val website: MutableState<String> = mutableStateOf("")
    val image: MutableState<Any> = mutableStateOf(R.drawable.person)
    val cameraUri: MutableState<Uri?> = mutableStateOf(null)


    fun inputsAreValid() =
        email.value.isEmailValid() &&
                website.value.isUrlValid() &&
                pass.value.length > 3 &&
                name.value.isNotEmpty() &&
                image.value != R.drawable.person


    fun findAppropriateError(): String {
        return if (image.value == R.drawable.person) {
            InputErrors.ImageError.errorMessage
        } else if (name.value.isEmailValid()) {
            InputErrors.FirstNameError.errorMessage
        } else if (email.value.isEmailValid().not()) {
            InputErrors.EmailError.errorMessage
        } else if (pass.value.length < 4) {
            InputErrors.PasswordError.errorMessage
        } else if (website.value.isUrlValid().not()) {
            InputErrors.WebsiteError.errorMessage
        } else {
            InputErrors.UnknownError.errorMessage
        }
    }


    fun mapUserGsonToJson(user: User): String = Gson().toJson(
        user,
        User::class.java
    )
}
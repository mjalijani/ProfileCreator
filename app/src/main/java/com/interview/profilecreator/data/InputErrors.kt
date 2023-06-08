package com.interview.profilecreator.data

sealed class InputErrors(
    val errorMessage : String
){
    object ImageError : InputErrors("please choose Image")
    object FirstNameError : InputErrors("please choose name for your account")
    object EmailError : InputErrors("please write valid email for your account")
    object PasswordError : InputErrors("password length must be greater than 3")
    object WebsiteError : InputErrors("please write valid website")
    object UnknownError : InputErrors("Unknown Error occurred, please try again few later")
}

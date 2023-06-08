package com.interview.profilecreator.navigation

sealed class Screen(
    val route: String,
    val objectName: String = "",
    val objectPath: String = ""
) {

    object ProfileCreation : Screen("profile_creation_screen")
    object SignIn : Screen(route = "sign_in_screen", objectName = "user", objectPath = "/{user}")
}
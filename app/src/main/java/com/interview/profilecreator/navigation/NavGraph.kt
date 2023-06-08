package com.interview.profilecreator.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.interview.profilecreator.data.User
import com.interview.profilecreator.data.UserArgType
import com.interview.profilecreator.ui.screens.profileCreation.ProfileCreation
import com.interview.profilecreator.ui.screens.signIn.SignIn

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Navigation(
    navController: NavHostController, modifier: Modifier,
) {
    NavHost(navController, startDestination = Screen.ProfileCreation.route, modifier) {

        composable(Screen.ProfileCreation.route) { ProfileCreation(navController) }
        composable(
            route = Screen.SignIn.route.plus(Screen.SignIn.objectPath),
            arguments = listOf(navArgument(Screen.SignIn.objectName) { type = UserArgType() })
        ) { navBackStackEntry ->
            val user = navBackStackEntry.arguments?.getString("user")
                ?.let { Gson().fromJson(it, User::class.java) }
            user?.let {
                SignIn(it)
            }
        }

    }
}


@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route?.substringBeforeLast("/")
}
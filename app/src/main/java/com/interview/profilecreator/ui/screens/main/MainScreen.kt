package com.interview.profilecreator.ui.screens.main

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.interview.profilecreator.navigation.Navigation

@Composable
fun MainScreen() {
    Navigation(rememberNavController(), Modifier.padding(10.dp))
}
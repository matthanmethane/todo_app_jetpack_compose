package com.dongwoo.testnavigation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.dongwoo.testnavigation.navigation.destinations.listComposable
import com.dongwoo.testnavigation.navigation.destinations.splashComposable
import com.dongwoo.testnavigation.navigation.destinations.taskComposable
import com.dongwoo.testnavigation.ui.viewmodels.SharedViewModel
import com.dongwoo.testnavigation.util.Constants.SPLASH_SCREEN

@Composable
fun SetupNavigation(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
){
    val screen = remember(navController){
        Screens(navController = navController)
    }

    NavHost(
        navController = navController,
        startDestination = SPLASH_SCREEN
    ){
        splashComposable(
            navigateToListScreen = screen.splash
        )
        listComposable(
            navigateToTaskScreen = screen.list,
            sharedViewModel = sharedViewModel
        )
        taskComposable(
            navigateToListScreen = screen.task,
            sharedViewModel = sharedViewModel
        )
    }
}
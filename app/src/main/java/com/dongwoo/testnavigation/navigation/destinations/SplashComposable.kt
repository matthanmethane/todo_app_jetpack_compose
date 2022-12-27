package com.dongwoo.testnavigation.navigation.destinations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dongwoo.testnavigation.ui.screens.splash.SplashScreen
import com.dongwoo.testnavigation.util.Constants
import com.dongwoo.testnavigation.util.Constants.SPLASH_SCREEN

fun NavGraphBuilder.splashComposable(
    navigateToListScreen: () -> Unit,
){
    composable(
        route = SPLASH_SCREEN,
        arguments = listOf(navArgument(Constants.LIST_ARGUMENT_KEY){
            type = NavType.StringType
        })
    ){
        SplashScreen(navigateToListScreen)
    }
}
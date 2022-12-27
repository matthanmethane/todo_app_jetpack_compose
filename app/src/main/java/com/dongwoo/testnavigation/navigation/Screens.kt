package com.dongwoo.testnavigation.navigation

import androidx.navigation.NavHostController
import com.dongwoo.testnavigation.util.Action
import com.dongwoo.testnavigation.util.Constants.LIST_SCREEN
import com.dongwoo.testnavigation.util.Constants.SPLASH_SCREEN

class Screens(navController: NavHostController) {

    val splash: () -> Unit = {
        navController.navigate(route = "list/${Action.NO_ACTION.name}"){
            popUpTo(SPLASH_SCREEN) { inclusive = true}
        }
    }

    val task: (Action) -> Unit = { action ->
        navController.navigate(route = "list/${action.name}"){
            popUpTo(LIST_SCREEN) { inclusive = true }
        }
    }

    val list: (taskId: Int) -> Unit = { taskId ->
        navController.navigate(route = "task/$taskId")
    }
}
package com.dongwoo.testnavigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dongwoo.testnavigation.navigation.SetupNavigation
import com.dongwoo.testnavigation.ui.theme.TestNavigationTheme
import com.dongwoo.testnavigation.ui.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestNavigationTheme {
                    navController = rememberNavController()
                    SetupNavigation(
                        navController = navController,
                        sharedViewModel = sharedViewModel
                    )
            }
        }
    }
}



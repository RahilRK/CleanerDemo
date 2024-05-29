package com.base.hilt.ui.composable

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.base.hilt.ui.screen.settings.Settings
import com.base.hilt.ui.screen.SplashScreen
import com.base.hilt.utils.NavControllerConstant.SETTINGS
import com.base.hilt.utils.NavControllerConstant.SPLASH_SCREEN
import com.base.hilt.utils.NavControllerConstant.START_DESTINATION


@Composable
fun MyNavController() {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = START_DESTINATION, builder = {

        /*todo splash_screen*/
        composable(
            route = SPLASH_SCREEN
        ) {
            SplashScreen(onButtonClick = {

                navController.navigate(SETTINGS)
            })
        }

        /*todo settings*/
        composable(
            route = SETTINGS
        ) {
            Settings()
        }
    })
}
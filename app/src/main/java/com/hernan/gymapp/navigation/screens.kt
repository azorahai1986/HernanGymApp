package com.hernan.gymapp.navigation

sealed class Screens(val routes:String){

    data object SplashScreen: Screens("SplashScreen")
    data object MainScreen: Screens("MainScreen")
    data object TurnsScreen: Screens("TurnsScreen")
    data object UsersTypeScreen: Screens("UsersTypeScreen")
    data object UsersScreen: Screens("UsersScreen")
}

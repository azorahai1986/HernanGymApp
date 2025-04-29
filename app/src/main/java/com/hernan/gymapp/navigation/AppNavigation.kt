package com.hernan.gymapp.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hernan.gymapp.account.presentation.ui.AccountScreen
import com.hernan.gymapp.account.presentation.ui.AdditionalDatesScreen
import com.hernan.gymapp.account.presentation.viewmodel.AdditionalDateViewModel
import com.hernan.gymapp.account.presentation.viewmodel.RegisterViewModel
import com.hernan.gymapp.main_screen.presentation.ui.MainScreen
import com.hernan.gymapp.user_type.presentation.viewmodel.ClientViewModel
import com.hernan.gymapp.main_screen.presentation.viewmodel.StateViewModel
import com.hernan.gymapp.common.ui_app.SplashScreen
import com.hernan.gymapp.common.ui_app.TurnsScreen
import com.hernan.gymapp.qr_classes_control.QRScannerScreen
import com.hernan.gymapp.qr_classes_control.QRScannerViewModel
import com.hernan.gymapp.qr_classes_control.QrSuccessRegistration
import com.hernan.gymapp.routins_and_excercises.presentation.ui.screens.RoutineScreen
import com.hernan.gymapp.routins_and_excercises.presentation.ui.screens.VideoPlayer
import com.hernan.gymapp.routins_and_excercises.presentation.viewmodel.RoutineViewModel
import com.hernan.gymapp.user_type_selected.presentation.ui.screens.screen.UsersScreen
import com.hernan.gymapp.user_type.presentation.ui.screens.UsersTypeScreen
import com.hernan.gymapp.user_type_selected.presentation.viewmodel.UserTypeSelectedVM

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, widthDp = 320)

@Composable
fun AppNavigation(){
    var isdarkTheme = true


    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.SplashScreen.routes){

        composable(route = Screens.SplashScreen.routes){
            SplashScreen(navController)
        }

        composable(route = Screens.MainScreen.routes){
            val firstViewModel: StateViewModel = hiltViewModel()
            MainScreen(isdarkTheme, navController, firstViewModel)
        }

        composable(route = Screens.TurnsScreen.routes + "/{text}",
            arguments = listOf(navArgument(name = "text") {
                type = NavType.StringType
            })){
            TurnsScreen(isdarkTheme, navController, it.arguments?.getString("text"))
        }

        composable(route = Screens.UsersTypeScreen.routes + "/{text}",
            arguments = listOf(navArgument(name = "text") {
                type = NavType.StringType
            })){
            val clientViewModel: ClientViewModel = hiltViewModel()
            UsersTypeScreen(
                isdarkTheme,
                navController,
                it.arguments?.getString("text"),
                clientViewModel
            )
        }

        composable(route = Screens.UsersScreen.routes + "/{text}",
            arguments = listOf(navArgument(name = "text") {
                type = NavType.StringType
            })){
            val userTypeSelectedVM: UserTypeSelectedVM = hiltViewModel()
            val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
            val coroutineScope = rememberCoroutineScope()
            UsersScreen(
                isdarkTheme,
                navController,
                it.arguments?.getString("text"),
                userTypeSelectedVM,
                bottomSheetScaffoldState,
                coroutineScope)
        }

        composable(route = Screens.RoutineScreen.routes + "/{text}",
            arguments = listOf(navArgument(name = "text") {
                type = NavType.StringType
            })){
            val routineViewModel: RoutineViewModel= hiltViewModel()
            RoutineScreen(
                navController,
                it.arguments?.getString("text"),
                routineViewModel
            )
        }

        composable(route = Screens.VideoPlayer.routes + "/{text}/{text2}",
            arguments = listOf(navArgument(name = "text") {
                type = NavType.StringType
            })){
            VideoPlayer(
                it.arguments?.getString("text"),
                it.arguments?.getString("text2"),
            )
        }

        composable(route = Screens.QRScannerScreen.routes + "/{text}",
            arguments = listOf(navArgument(name = "text") {
                type = NavType.StringType
            })){
            val qrScannerViewModel: QRScannerViewModel = hiltViewModel()
            QRScannerScreen(
                navController,
                qrScannerViewModel
            )
        }

        composable(
            route = Screens.QrSuccessRegistration.routes + "/{isCorrectQr}",
            arguments = listOf(
                navArgument(name = "isCorrectQr") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) { it: NavBackStackEntry ->
            val isCorrectQr = it.arguments?.getBoolean("isCorrectQr") ?: false

            QrSuccessRegistration(
                navController,
                isCorrectQr
            )
        }

        composable(route = Screens.AccountScreen.routes){
            val accountViewModel: RegisterViewModel = hiltViewModel()
            AccountScreen(navController, accountViewModel)
        }

        composable(
            route = Screens.AdditionalDateScreen.routes + "/{name}/{phoneNumber}/{email}",
            arguments = listOf(
                navArgument(name = "name") { type = NavType.StringType },
                navArgument(name = "phoneNumber") { type = NavType.StringType },
                navArgument(name = "email") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val additionalDateViewModel: AdditionalDateViewModel = hiltViewModel()
                AdditionalDatesScreen(
                    additionalDateViewModel = additionalDateViewModel,
                    navController = navController,
                    name = backStackEntry.arguments?.getString("name"),
                    phoneNumber = backStackEntry.arguments?.getString("phoneNumber"),
                    email = backStackEntry.arguments?.getString("email")
                )
            }
        }


}
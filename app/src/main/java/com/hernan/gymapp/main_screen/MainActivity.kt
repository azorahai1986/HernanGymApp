package com.hernan.gymapp.main_screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hernan.gymapp.navigation.AppNavigation
import com.hernan.gymapp.common.ui.theme.GymAppTheme
import dagger.hilt.android.AndroidEntryPoint


private const val isdarkTheme = false
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {

            GymAppTheme(isdarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //ContainerViewApp()

                    AppNavigation()
                }
            }

        }
    }
}

@Preview
@Composable
fun MyAppPreview() {
    GymAppTheme(isdarkTheme) {
        AppNavigation()
    }
}


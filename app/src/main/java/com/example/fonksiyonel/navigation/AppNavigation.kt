package com.example.fonksiyonel.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fonksiyonel.ui.auth.AuthViewModel
import com.example.fonksiyonel.ui.auth.LoginScreen
import com.example.fonksiyonel.ui.auth.RegisterScreen
import com.example.fonksiyonel.ui.profile.ProfileScreen
import com.example.fonksiyonel.ui.profile.ProfileViewModel

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val PROFILE = "profile"
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Routes.LOGIN
) {
    val authViewModel: AuthViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()
    
    // Check if user is already logged in
    val initialRoute = remember {
        if (authViewModel.currentUser != null) {
            Routes.PROFILE
        } else {
            startDestination
        }
    }
    
    NavHost(
        navController = navController,
        startDestination = initialRoute
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                },
                onNavigateToProfile = {
                    navController.navigate(Routes.PROFILE) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Routes.REGISTER) {
            RegisterScreen(
                viewModel = authViewModel,
                onNavigateToLogin = {
                    navController.navigateUp()
                },
                onNavigateToProfile = {
                    navController.navigate(Routes.PROFILE) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Routes.PROFILE) {
            ProfileScreen(
                viewModel = profileViewModel,
                onNavigateToLogin = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.PROFILE) { inclusive = true }
                    }
                }
            )
        }
    }
} 
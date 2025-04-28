package com.example.fonksiyonel.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fonksiyonel.ui.auth.AuthViewModel
import com.example.fonksiyonel.ui.auth.LoginScreen
import com.example.fonksiyonel.ui.auth.RegisterScreen
import com.example.fonksiyonel.ui.components.BottomNavBar
import com.example.fonksiyonel.ui.diagnose.DiagnoseResultScreen
import com.example.fonksiyonel.ui.diagnose.DiagnoseViewModel
import com.example.fonksiyonel.ui.diagnose.ImageSelectionScreen
import com.example.fonksiyonel.ui.home.HomeScreen
import com.example.fonksiyonel.ui.profile.ProfileScreen
import com.example.fonksiyonel.ui.profile.ProfileViewModel

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val PROFILE = "profile"
    const val IMAGE_SELECTION = "image_selection"
    const val DIAGNOSE_RESULT = "diagnose_result"
    const val HOME = "home"
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Routes.LOGIN
) {
    val authViewModel: AuthViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()
    val diagnoseViewModel: DiagnoseViewModel = viewModel()
    
    // Check if user is already logged in
    val initialRoute = remember {
        if (authViewModel.currentUser != null) {
            Routes.HOME
        } else {
            startDestination
        }
    }
    
    // Current route for bottom navigation
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    // Bottom navigation visibility
    val showBottomBar = remember(currentRoute) {
        when (currentRoute) {
            Routes.LOGIN, Routes.REGISTER, Routes.DIAGNOSE_RESULT -> false
            else -> true
        }
    }
    
    // Scaffold with bottom navigation
    androidx.compose.material3.Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = currentRoute ?: Routes.HOME,
                visible = showBottomBar,
                onNavItemClick = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = initialRoute,
            modifier = androidx.compose.ui.Modifier.padding(innerPadding)
        ) {
            composable(Routes.LOGIN) {
                LoginScreen(
                    viewModel = authViewModel,
                    onNavigateToRegister = {
                        navController.navigate(Routes.REGISTER)
                    },
                    onNavigateToProfile = {
                        navController.navigate(Routes.HOME) {
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
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.REGISTER) { inclusive = true }
                        }
                    }
                )
            }
            
            composable(Routes.HOME) {
                HomeScreen(
                    onNavigateToImageSelection = {
                        navController.navigate(Routes.IMAGE_SELECTION)
                    },
                    onNavigateToProfile = {
                        navController.navigate(Routes.PROFILE)
                    }
                )
            }
            
            composable(Routes.PROFILE) {
                ProfileScreen(
                    viewModel = profileViewModel,
                    onNavigateToImageSelection = {
                        navController.navigate(Routes.IMAGE_SELECTION)
                    },
                    onNavigateToLogin = {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.HOME) { inclusive = true }
                        }
                    }
                )
            }
            
            composable(Routes.IMAGE_SELECTION) {
                ImageSelectionScreen(
                    viewModel = diagnoseViewModel,
                    onNavigateToResults = {
                        navController.navigate(Routes.DIAGNOSE_RESULT)
                    },
                    onBackToProfile = {
                        navController.navigateUp()
                    }
                )
            }
            
            composable(Routes.DIAGNOSE_RESULT) {
                DiagnoseResultScreen(
                    viewModel = diagnoseViewModel,
                    onBackToImageSelection = {
                        navController.navigateUp()
                    },
                    onNewAnalysis = {
                        navController.navigate(Routes.IMAGE_SELECTION) {
                            popUpTo(Routes.IMAGE_SELECTION) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
} 
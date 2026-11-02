package com.example.anvux.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.anvux.domain.model.SessionState
import com.example.anvux.feature.auth.LoginScreen
import com.example.anvux.feature.auth.RegisterScreen
import com.example.anvux.feature.auth.SessionViewModel

@Composable
fun AnVuxNavHost(
    modifier: Modifier = Modifier,
    sessionViewModel: SessionViewModel = hiltViewModel(),
) {
    val sessionState by sessionViewModel.sessionState.collectAsState()
    val navController = rememberNavController()

    val isLoaded = sessionState != SessionState.Loading
    // Chốt màn hình đầu tiên một lần khi đã đọc xong phiên đăng nhập đã lưu.
    val startDestination = remember(isLoaded) {
        if (sessionState == SessionState.LoggedIn) Routes.MAIN else Routes.LOGIN
    }

    // Khi phiên kết thúc (đăng xuất hoặc refresh token bị từ chối) mà đang ở màn chính: về Đăng nhập.
    LaunchedEffect(sessionState) {
        if (sessionState == SessionState.LoggedOut &&
            navController.currentDestination?.route == Routes.MAIN
        ) {
            navController.navigate(Routes.LOGIN) {
                popUpTo(Routes.MAIN) { inclusive = true }
            }
        }
    }

    if (!isLoaded) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        )
    } else {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier,
        ) {
            composable(Routes.LOGIN) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Routes.MAIN) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = { navController.navigate(Routes.REGISTER) },
                )
            }
            composable(Routes.REGISTER) {
                RegisterScreen(
                    onRegisterSuccess = {
                        navController.navigate(Routes.MAIN) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    },
                    onNavigateBack = { navController.popBackStack() },
                )
            }
            composable(Routes.MAIN) {
                MainScreen(onLogout = { sessionViewModel.logout() })
            }
        }
    }
}
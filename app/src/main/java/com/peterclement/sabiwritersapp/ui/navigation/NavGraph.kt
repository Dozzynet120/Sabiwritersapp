package com.peterclement.sabiwritersapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.peterclement.sabiwritersapp.ui.screens.*
import com.peterclement.sabiwritersapp.ui.viewmodel.BookingViewModel

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val bookingViewModel: BookingViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {
        // Splash (Entry Point with Video)
        composable(Routes.SPLASH) {
            SplashVideoScreen(navController = navController)
        }

        // Auth
        composable(Routes.LOGIN) {
            LoginScreen(navController = navController)
        }

        composable(Routes.SIGNUP) {
            SignupScreen(navController = navController)
        }

        // Home
        composable(Routes.HOME) {
            HomeScreen(navController = navController)
        }

        // Booking Flow
        composable(Routes.BOOKING) {
            BookingFormScreen(
                navController = navController,
                nextRoute = Routes.BOOKING_SUCCESS,
                bookingViewModel = bookingViewModel
            )
        }

        composable(Routes.BOOKING_SUCCESS) {
            BookingSuccessScreen(
                navController = navController,
                bookingViewModel = bookingViewModel,
                nextRoute = Routes.BOOKING_DETAILS
            )
        }

        composable(
            route = "${Routes.BOOKING_DETAILS}?nextRoute={nextRoute}",
            arguments = listOf(
                navArgument("nextRoute") {
                    type = NavType.StringType
                    defaultValue = Routes.PAYMENT
                }
            )
        ) { backStackEntry ->
            val nextRoute = backStackEntry.arguments?.getString("nextRoute") ?: Routes.PAYMENT
            BookingDetailsScreen(
                navController = navController,
                bookingViewModel = bookingViewModel,
                nextRoute = nextRoute
            )
        }

        // Payment
        composable(
            route = "${Routes.PAYMENT}?amount={amount}",
            arguments = listOf(
                navArgument("amount") {
                    type = NavType.FloatType
                    defaultValue = 100.0f
                }
            )
        ) { backStackEntry ->
            val amount = backStackEntry.arguments?.getFloat("amount")?.toDouble() ?: 100.0
            PaymentScreen(
                navController = navController,
                bookingViewModel = bookingViewModel,
                amount = amount
            )
        }

        // Admin
        composable(Routes.ADMIN_DASHBOARD) {
            AdminDashboardScreen()
        }
    }
}
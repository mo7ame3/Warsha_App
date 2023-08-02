package com.example.warshaapp.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.warshaapp.screens.client.home.ClientHomeScreen
import com.example.warshaapp.screens.client.home.ClientHomeViewModel
import com.example.warshaapp.screens.client.order.OrderViewModel
import com.example.warshaapp.screens.client.order.details.ClientOrdersInCraftScreen
import com.example.warshaapp.screens.client.order.details.offers.ClientOrderOffersScreen
import com.example.warshaapp.screens.client.postScreen.ClientPostScreen
import com.example.warshaapp.screens.client.postScreen.ClientPostViewModel
import com.example.warshaapp.screens.sharedScreens.authentication.login.AuthenticationViewModel
import com.example.warshaapp.screens.sharedScreens.authentication.login.LoginScreen
import com.example.warshaapp.screens.sharedScreens.splash.SplashScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AllScreens.SplashScreen.name
    ) {

        composable(route = AllScreens.SplashScreen.name) {
            SplashScreen(navController = navController)
        }
        composable(route = AllScreens.LoginScreen.name) {
            val authenticationViewModel = hiltViewModel<AuthenticationViewModel>()
            LoginScreen(
                navController = navController,
                authenticationViewModel = authenticationViewModel
            )
        }

        //Client

        //Home
        composable(
            route = AllScreens.ClientHomeScreen.name + "/{route}",
            arguments = listOf(navArgument(name = "route") {
                type = NavType.StringType
            }
            )
        ) { data ->
            val clientHomeViewModel = hiltViewModel<ClientHomeViewModel>()
            val orderViewModel = hiltViewModel<OrderViewModel>()
            data.arguments!!.getString("route")?.let {
                ClientHomeScreen(
                    navController = navController,
                    route = it,
                    clientHomeViewModel = clientHomeViewModel,
                    orderViewModel = orderViewModel
                )
            }
        }

        //Post Order
        composable(
            route = AllScreens.ClientPostScreen.name + "/{craftId}/{craftName}",
            arguments = listOf(
                navArgument(name = "craftId") {
                    type = NavType.StringType
                },
                navArgument(name = "craftName") {
                    type = NavType.StringType
                },
            )
        ) { data ->
            val clientPostViewModel = hiltViewModel<ClientPostViewModel>()
            ClientPostScreen(
                navController = navController,
                craftId = data.arguments!!.getString("craftId").toString(),
                craftName = data.arguments!!.getString("craftName").toString(),
                clientPostViewModel = clientPostViewModel,
            )
        }


        //Order Details
        composable(
            route = AllScreens.ClientOrdersInCraftScreen.name + "/{craftId}/{craftName}",
            arguments = listOf(navArgument(name = "craftId") {
                type = NavType.StringType
            }, navArgument(name = "craftName") {
                type = NavType.StringType
            })
        ) { data ->
            val orderViewModel = hiltViewModel<OrderViewModel>()
            ClientOrdersInCraftScreen(
                navController = navController,
                craftId = data.arguments!!.getString("craftId").toString(),
                name = data.arguments!!.getString("craftName").toString(),
                orderViewModel = orderViewModel
            )

        }
        //OrderOffers
        composable(route = AllScreens.ClientOrderOffersScreen.name + "/{problemTitle}/{orderDescription}/{orderId}/{craftId}",
            arguments = listOf(
                navArgument(name = "problemTitle") {
                    type = NavType.StringType
                },
                navArgument(name = "orderDescription") {
                    type = NavType.StringType
                },
                navArgument(name = "orderId") {
                    type = NavType.StringType
                },
                navArgument(name = "craftId") {
                    type = NavType.StringType
                }
            )) { data ->
            val orderViewModel = hiltViewModel<OrderViewModel>()
            ClientOrderOffersScreen(
                navController = navController,
                orderViewModel = orderViewModel,
                orderTitle = data.arguments?.getString("problemTitle")!!,
                orderDescription = data.arguments?.getString("orderDescription")!!,
                orderId = data.arguments?.getString("orderId")!!,
                craftId = data.arguments?.getString("craftId")!!,
            )
        }


    }
}

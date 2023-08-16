package com.example.warshaapp.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.warshaapp.screens.admin.home.AdminHomeScreen
import com.example.warshaapp.screens.admin.query.blocks.AdminBlockedUsers
import com.example.warshaapp.screens.admin.query.clients.AdminAllClients
import com.example.warshaapp.screens.client.home.ClientHomeScreen
import com.example.warshaapp.screens.client.home.ClientHomeViewModel
import com.example.warshaapp.screens.client.order.OrderViewModel
import com.example.warshaapp.screens.client.order.details.ClientOrdersInCraftScreen
import com.example.warshaapp.screens.client.order.details.offers.ClientOrderOffersScreen
import com.example.warshaapp.screens.client.postScreen.ClientPostScreen
import com.example.warshaapp.screens.client.postScreen.ClientPostViewModel
import com.example.warshaapp.screens.client.profile.ClientMyProfileScreen
import com.example.warshaapp.screens.client.profile.ClientProfileScreen
import com.example.warshaapp.screens.client.profile.ClientProfileViewModel
import com.example.warshaapp.screens.sharedScreens.authentication.login.AuthenticationViewModel
import com.example.warshaapp.screens.sharedScreens.authentication.login.LoginScreen
import com.example.warshaapp.screens.sharedScreens.setting.SettingScreen
import com.example.warshaapp.screens.sharedScreens.setting.SettingViewModel
import com.example.warshaapp.screens.sharedScreens.splash.SplashScreen
import com.example.warshaapp.screens.worker.home.WorkerHomeScreen
import com.example.warshaapp.screens.worker.home.WorkerHomeViewModel
import com.example.warshaapp.screens.worker.myOffers.MyOfferProblemDetails
import com.example.warshaapp.screens.worker.myOffers.MyOffersViewModel
import com.example.warshaapp.screens.worker.problemDetails.WorkerProblemDetails
import com.example.warshaapp.screens.worker.problemDetails.WorkerProblemDetailsViewModel
import com.example.warshaapp.screens.worker.profile.WorkerMyProfileScreen
import com.example.warshaapp.screens.worker.profile.WorkerProfileScreen
import com.example.warshaapp.screens.worker.profile.WorkerProfileViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController, startDestination = AllScreens.SplashScreen.name
    ) {

        composable(route = AllScreens.SplashScreen.name) {
            SplashScreen(navController = navController)
        }
        composable(route = AllScreens.LoginScreen.name) {
            val authenticationViewModel = hiltViewModel<AuthenticationViewModel>()
            LoginScreen(
                navController = navController, authenticationViewModel = authenticationViewModel
            )
        }


        //Client

        //Home
        composable(
            route = AllScreens.ClientHomeScreen.name + "/{route}",
            arguments = listOf(navArgument(name = "route") {
                type = NavType.StringType
            })
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
            route = AllScreens.ClientPostScreen.name + "/{craftId}/{craftName}", arguments = listOf(
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
        composable(
            route = AllScreens.ClientOrderOffersScreen.name + "/{problemTitle}/{orderDescription}/{orderId}/{craftId}",
            arguments = listOf(navArgument(name = "problemTitle") {
                type = NavType.StringType
            }, navArgument(name = "orderDescription") {
                type = NavType.StringType
            }, navArgument(name = "orderId") {
                type = NavType.StringType
            }, navArgument(name = "craftId") {
                type = NavType.StringType
            })
        ) { data ->
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

        //Client Profile
        composable(
            route = "${AllScreens.ClientMyProfileScreen.name}/{completeProject}",
            arguments = listOf(navArgument(name = "completeProject") {
                type = NavType.BoolType
            })
        ) { data ->
            val clientProfileViewModel = hiltViewModel<ClientProfileViewModel>()
            ClientMyProfileScreen(
                navController = navController,
                completeProject = data.arguments!!.getBoolean("completeProject"),
                clientProfileViewModel = clientProfileViewModel
            )
        }

        composable(
            route = AllScreens.ClientProfileScreen.name + "/{clientId}", arguments = listOf(
                navArgument(name = "clientId") {
                    type = NavType.StringType
                },
            )
        ) {
            val clientProfileViewModel = hiltViewModel<ClientProfileViewModel>()
            ClientProfileScreen(
                navController = navController,
                clientId = it.arguments?.getString("clientId").toString(),
                clientProfileViewModel = clientProfileViewModel
            )
        }
        composable(
            route = AllScreens.SettingScreen.name + "/{client}",
            arguments = listOf(navArgument(name = "client") {
                type = NavType.BoolType
            })
        ) {
            val settingViewModel = hiltViewModel<SettingViewModel>()
            SettingScreen(
                navController = navController,
                client = it.arguments!!.getBoolean("client"),
                settingViewModel = settingViewModel
            )
        }


        //worker

        composable(
            route = AllScreens.WorkerHomeScreen.name + "/{route}",
            arguments = listOf(navArgument(name = "route") {
                type = NavType.StringType
            })
        ) { data ->
            val viewModel = hiltViewModel<WorkerHomeViewModel>()
            val myOfferViewModel = hiltViewModel<MyOffersViewModel>()
            data.arguments!!.getString("route")?.let {
                WorkerHomeScreen(
                    navController = navController,
                    route = it,
                    workerHomeViewModel = viewModel,
                    myOffersViewModel = myOfferViewModel
                )
            }
        }


        composable(route = AllScreens.WorkerMyProfileScreen.name) {
            val workerProfileViewModel = hiltViewModel<WorkerProfileViewModel>()
            WorkerMyProfileScreen(
                navController = navController, workerProfileViewModel = workerProfileViewModel
            )
        }

        composable(
            route = AllScreens.WorkerProfileScreen.name + "/{workerId}",
            arguments = listOf(navArgument(name = "workerId") {
                type = NavType.StringType
            })
        ) {
            val workerProfileViewModel = hiltViewModel<WorkerProfileViewModel>()
            WorkerProfileScreen(
                navController = navController,
                workerId = it.arguments?.getString("workerId").toString(),
                workerProfileViewModel = workerProfileViewModel
            )
        }

        composable(
            route = AllScreens.MyProjectProblemDetails.name + "/{orderId}",
            arguments = listOf(navArgument(name = "orderId") {
                type = NavType.StringType
            })
        ) {
            val myOffersViewModel = hiltViewModel<MyOffersViewModel>()
            MyOfferProblemDetails(
                navController = navController,
                myOffersViewModel = myOffersViewModel,
                orderId = it.arguments!!.getString("orderId").toString()
            )
        }

        composable(
            route = AllScreens.WorkerProblemDetails.name + "/{orderId}",
            arguments = listOf(navArgument(name = "orderId") {
                type = NavType.StringType
            })
        ) {
            val viewModel = hiltViewModel<WorkerProblemDetailsViewModel>()

            WorkerProblemDetails(
                navController = navController,
                orderID = it.arguments?.getString("orderId").toString(),
                workerProblemDetailsViewModel = viewModel
            )
        }


        //Admin Screen


        composable(route = AllScreens.AdminHomeScreen.name) {
            AdminHomeScreen(navController = navController)
        }

        composable(route = AllScreens.AdminBlockedUsers.name) {
            AdminBlockedUsers(navController)
        }

        composable(route = AllScreens.AdminAllClients.name) {
            AdminAllClients(navController)
        }

    }
}

package com.example.warshaapp.screens.admin.home

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.warshaapp.components.TopBar
import com.example.warshaapp.navigation.AllScreens
import com.example.warshaapp.sharedpreference.SharedPreference
import com.example.warshaapp.ui.theme.AdminSecondaryColor
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun AdminHomeScreen(navController: NavController) {
    val context = LocalContext.current
    val sharedPreference = SharedPreference(context)
    val scope = rememberCoroutineScope()

    Scaffold(topBar = {
        TopBar(title = "") {
            scope.launch {
                sharedPreference.removeUser()
                navController.navigate(route = AllScreens.LoginScreen.name) {
                    navController.popBackStack()
                    navController.popBackStack()
                    navController.popBackStack()
                }
            }
        }
    }) {
        Surface(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.padding(top = 50.dp, end = 15.dp, start = 15.dp)) {
                items(items = query) {
                    QueryRow(title = it) { query ->
                        when (query) {
                            "المهن" -> {
                                navController.navigate(route = AllScreens.AdminJobsScreen.name)
                            }

                            "العمال" -> {
                                navController.navigate(route = AllScreens.AdminAllWorkers.name)
                            }

                            "العملاء" -> {
                                navController.navigate(route = AllScreens.AdminAllClients.name)
                            }

                            "الحظر" -> {
                                navController.navigate(route = AllScreens.AdminBlockedUsers.name)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(85.dp))

                }
            }
        }
    }
}


@Composable
fun QueryRow(
    title: String,
    onAction: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .height(100.dp)
            .clickable { onAction.invoke(title) },
        border = BorderStroke(width = 1.dp, color = AdminSecondaryColor),
        shape = RoundedCornerShape(35.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = title, style = TextStyle(
                    color = AdminSecondaryColor,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}


val query = listOf(
    "العمال",
    "العملاء",
    "الحظر",
    "المهن",
)
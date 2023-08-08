package com.example.warshaapp.screens.sharedScreens.report

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.warshaapp.components.LoginButton
import com.example.warshaapp.components.TopBar
import com.example.warshaapp.constant.Constant.reportList
import com.example.warshaapp.navigation.AllScreens
import com.example.warshaapp.ui.theme.MainColor

@ExperimentalMaterial3Api
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReportScreen(
    navController: NavController,
    client: Boolean = true,
    status: String = ""
) {
    Scaffold(topBar = {
        TopBar(title = "بلاغ") {
            navController.popBackStack()
        }
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 100.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            val checkedList = remember {
                mutableStateListOf<String>()
            }
            //list
            Column {

                reportList.forEach { element ->
                    var checked by remember {
                        mutableStateOf(false)
                    }
                    if ((status == "client" && element != "وصف غير دقيق للمشكلة")
                        || (status == "worker" && (element != "عامل غير كفء" && element != "عمل غير متقن"))
                    )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    checked = !checked
                                    if (checked) {
                                        checkedList.add(element)
                                    } else {
                                        checkedList.remove(element)
                                    }
                                }) {
                            Checkbox(
                                checked = checked,
                                onCheckedChange = {
                                    checked = it
                                    if (checked) {
                                        checkedList.add(element)
                                    } else {
                                        checkedList.remove(element)
                                    }
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = MainColor,
                                    uncheckedColor = MainColor
                                )
                            )
                            Text(text = element)
                        }
                    Spacer(modifier = Modifier.height(15.dp))
                }
            }

            // Spacer(modifier = Modifier.height(50.dp))
            //Buttons
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {

                LoginButton(
                    isLogin = true,
                    label = "تأكيد",
                    modifier = Modifier
                        .width(120.dp)
                        .height(45.dp)
                ) {
                    //if condition between screens "Client or Worker"
                    if (checkedList.isNotEmpty()) {
                        if (client) {
                            navController.navigate(route = AllScreens.ClientHomeScreen.name + "/{}") {
                                navController.popBackStack()
                                navController.popBackStack()
                                navController.popBackStack()
                            }
                        } else {
                            navController.navigate(route = AllScreens.WorkerHomeScreen.name + "/{}") {
                                navController.popBackStack()
                                navController.popBackStack()
                                navController.popBackStack()
                            }
                        }
                    }
                    // update report to database
                }
                LoginButton(
                    isLogin = false,
                    label = "تراجع",
                    modifier = Modifier
                        .width(120.dp)
                        .height(45.dp)
                ) {
                    //if condition between screens "Client or Worker"
                    if (client) {
                        navController.navigate(route = AllScreens.ClientHomeScreen.name + "/{}") {
                            navController.popBackStack()
                            navController.popBackStack()
                            navController.popBackStack()
                        }
                    } else {
                        navController.navigate(route = AllScreens.WorkerHomeScreen.name + "/{}") {
                            navController.popBackStack()
                            navController.popBackStack()
                            navController.popBackStack()
                        }
                    }
                }
            }
        }
    }
}
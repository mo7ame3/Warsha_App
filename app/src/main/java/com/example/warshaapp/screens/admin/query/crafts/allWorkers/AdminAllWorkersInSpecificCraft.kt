package com.example.warshaapp.screens.admin.query.crafts.allWorkers

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.warshaapp.components.TopBar

@ExperimentalMaterial3Api
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AdminAllWorkersInSpecificCraft(navController: NavController) {
    Scaffold(topBar = {
        TopBar(title = "") {
            navController.popBackStack()
        }
    }) {

    }
}
package com.example.warshaapp.screens.admin.query.clients

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.warshaapp.components.TopBar

@ExperimentalMaterial3Api
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AdminAllClients(navController: NavController) {
    Scaffold(topBar = {
        TopBar(title = "") {
            navController.popBackStack()
        }
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp, start = 15.dp, end = 15.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "تفاصيل جميع العملاء", style = TextStyle(
                        fontSize = 25.sp
                    )
                )
            }
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}
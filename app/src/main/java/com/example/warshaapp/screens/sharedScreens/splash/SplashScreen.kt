package com.example.warshaapp.screens.sharedScreens.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.warshaapp.constant.Constant
import com.example.warshaapp.navigation.AllScreens
import com.example.warshaapp.sharedpreference.SharedPreference
import com.example.warshaapp.ui.theme.MainColor
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navController: NavController) {
    val scale = remember {
        Animatable(0f)
    }

    val context = LocalContext.current
    val sharedPreference = SharedPreference(context)
    val getState = sharedPreference.getState.collectAsState(initial = "")
    val getToken = sharedPreference.getToken.collectAsState(initial = "")

    LaunchedEffect(key1 = true, block = {
        scale.animateTo(
            targetValue = 0.6f,
            animationSpec = tween(
                durationMillis = 800,
                easing = { OvershootInterpolator(8f).getInterpolation(it) })
        )
        delay(500L)
        if (getToken.value.toString().isNotEmpty()) {
            Constant.token = getToken.value.toString()
            when (getState.value) {
                "client" -> {
                    navController.navigate(AllScreens.ClientHomeScreen.name + "/{}") {
                        navController.popBackStack()
                    }
                }

                "worker" -> {
                    navController.navigate(AllScreens.WorkerHomeScreen.name + "/login") {
                        navController.popBackStack()
                    }
                }

                "admin" -> {
                    navController.navigate(AllScreens.AdminHomeScreen.name) {
                        navController.popBackStack()
                    }
                }
            }
        } else {
            navController.navigate(AllScreens.LoginScreen.name) {
                navController.popBackStack()
            }
        }
    })

    Surface(modifier = Modifier.fillMaxSize(), color = MainColor)
    {
        Box(contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = Constant.logo),
                contentDescription = null,
                modifier = Modifier
                    .size(300.dp, 300.dp)
                    .scale(scale.value)
            )

        }
    }

}
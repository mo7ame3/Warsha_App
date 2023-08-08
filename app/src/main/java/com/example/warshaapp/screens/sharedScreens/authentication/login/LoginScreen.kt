package com.example.warshaapp.screens.sharedScreens.authentication.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.warshaapp.components.LoginButton
import com.example.warshaapp.components.PasswordInput
import com.example.warshaapp.components.TextInput
import com.example.warshaapp.constant.Constant
import com.example.warshaapp.data.WrapperClass
import com.example.warshaapp.model.shared.authentication.Authentication
import com.example.warshaapp.model.shared.getCraftOfWorker.GetCraftOfWorker
import com.example.warshaapp.navigation.AllScreens
import com.example.warshaapp.screens.sharedScreens.authentication.register.RegisterForm
import com.example.warshaapp.sharedpreference.SharedPreference
import com.example.warshaapp.ui.theme.MainColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController, authenticationViewModel: AuthenticationViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sharedPreference = SharedPreference(context)
    //Toggle between login and register
    var toggledTopButton by remember {
        mutableStateOf(true)
    }
    // loading variable
    val loading = remember {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MainColor)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(topEnd = 200.dp, bottomStart = 200.dp)
        ) {

            Column(
                modifier = Modifier.padding(top = 150.dp), horizontalAlignment = Alignment.End
            ) {
                if (!loading.value) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.Top
                    ) {
                        LoginButton(label = "تسجيل الدخول", isLogin = toggledTopButton) {
                            toggledTopButton = true
                        }
                        LoginButton(label = "حساب جديد", isLogin = !toggledTopButton) {
                            toggledTopButton = false
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    //verticalArrangement = Arrangement.SpaceBetween
                ) {
                    if (toggledTopButton) {
                        LoginForm(
                            navController = navController,
                            scope = scope,
                            loading = loading,
                            authenticationViewModel = authenticationViewModel,
                            sharedPreference = sharedPreference
                        )
                    } else {
                        RegisterForm(
                            navController = navController,
                            authenticationViewModel = authenticationViewModel,
                            craftList = if (authenticationViewModel.craftList.value.data?.data?.crafts != null) authenticationViewModel.craftList.value.data?.data?.crafts!! else null,
                            loading = loading,
                            sharedPreference = sharedPreference
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginForm(
    navController: NavController,
    scope: CoroutineScope,
    loading: MutableState<Boolean>,
    authenticationViewModel: AuthenticationViewModel,
    sharedPreference: SharedPreference,
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val email = rememberSaveable {
        mutableStateOf("")
    }
    val password = rememberSaveable {
        mutableStateOf("")
    }
    val emailError = remember {
        mutableStateOf(false)
    }
    val eye = remember {
        mutableStateOf(false)
    }
    val passwordError = remember {
        mutableStateOf(false)
    }
    val valid =
        (!passwordError.value && !emailError.value && email.value.isNotBlank() && password.value.isNotBlank())
    if (!loading.value) {
        Column(
            modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextInput(input = email,
                isError = emailError,
                label = "البريد الالكتروني",
                onAction = KeyboardActions {
                    keyboardController?.hide()
                })
            Spacer(modifier = Modifier.height(10.dp))

            PasswordInput(password = password, eye = eye, onButtonAction = {
                eye.value = !eye.value
            }, onAction = KeyboardActions {
                if (valid) {
                    scope.launch {
                        loading.value = true
                        val login: WrapperClass<Authentication, Boolean, Exception> =
                            authenticationViewModel.login(
                                email = email.value, password = password.value
                            )
                        if (login.data?.status == "success") {
                            sharedPreference.saveName(login.data!!.data?.user!!.name)
                            sharedPreference.saveToken(login.data!!.token.toString())
                            sharedPreference.saveUserId(login.data!!.data?.user!!.id)
                            Constant.token = login.data!!.token.toString()
                            when (login.data!!.data?.user?.role) {
                                "client" -> {
                                    sharedPreference.saveState("client")
                                    navController.navigate(AllScreens.ClientHomeScreen.name + "/{}") {
                                        navController.popBackStack()
                                    }
                                }

                                "worker" -> {
                                    //get Craft Id
                                    val response: WrapperClass<GetCraftOfWorker, Boolean, java.lang.Exception> =
                                        authenticationViewModel.getCraftOfWorker(login.data!!.data?.user!!.id)
                                    if (response.data!!.data != null) {
                                        sharedPreference.saveState("worker")
                                        sharedPreference.saveCraftId(response.data!!.data!!.user[0].myCraft)
                                        navController.navigate(AllScreens.WorkerHomeScreen.name + "/login") {
                                            navController.popBackStack()
                                        }
                                    }
                                }

                                else -> {
                                    sharedPreference.saveState("admin")
                                    navController.navigate(AllScreens.AdminHomeScreen.name) {
                                        navController.popBackStack()
                                    }
                                }
                            }
                        } else if (login.e != null) {
                            loading.value = false
                            Toast.makeText(
                                context, "خطأ في الانترنت", Toast.LENGTH_SHORT
                            ).show()
                        } else if (login.data?.status == "fail" || login.data?.status == "error") {
                            loading.value = false
                            Toast.makeText(
                                context, login.data?.message, Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    keyboardController?.hide()
                } else {
                    keyboardController?.hide()
                }
            }, isError = passwordError
            )
            Spacer(modifier = Modifier.height(2.dp))
            TextButton(onClick = {
                navController.navigate(route = AllScreens.ForgotPasswordScreen.name)
            }) {
                Text(
                    text = "هل نسيت كلمة السر ؟",
                    color = MainColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            LoginButton(
                isLogin = true, enabled = valid, label = "سجل الدخول"
            ) {
                scope.launch {
                    loading.value = true
                    val login: WrapperClass<Authentication, Boolean, Exception> =
                        authenticationViewModel.login(
                            email = email.value, password = password.value
                        )
                    if (login.data?.status == "success") {
                        sharedPreference.saveName(login.data!!.data?.user!!.name)
                        sharedPreference.saveToken(login.data!!.token.toString())
                        sharedPreference.saveUserId(login.data!!.data?.user!!.id)
                        Constant.token = login.data!!.token.toString()
                        when (login.data!!.data?.user?.role) {
                            "client" -> {
                                sharedPreference.saveState("client")
                                navController.navigate(AllScreens.ClientHomeScreen.name + "/{}") {
                                    navController.popBackStack()
                                }
                            }

                            "worker" -> {
                                //get Craft Id
                                val response: WrapperClass<GetCraftOfWorker, Boolean, java.lang.Exception> =
                                    authenticationViewModel.getCraftOfWorker(login.data!!.data?.user!!.id)
                                if (response.data!!.data != null) {
                                    sharedPreference.saveState("worker")
                                    sharedPreference.saveCraftId(response.data!!.data!!.user[0].myCraft)
                                    navController.navigate(AllScreens.WorkerHomeScreen.name + "/login") {
                                        navController.popBackStack()
                                    }
                                }
                            }

                            else -> {
                                sharedPreference.saveState("admin")
                                navController.navigate(AllScreens.AdminHomeScreen.name) {
                                    navController.popBackStack()
                                }
                            }
                        }
                    } else if (login.e != null) {
                        loading.value = false
                        Toast.makeText(
                            context, "خطأ في الانترنت", Toast.LENGTH_SHORT
                        ).show()
                    } else if (login.data?.status == "fail" || login.data?.status == "error") {
                        loading.value = false
                        Toast.makeText(
                            context, login.data?.message, Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    }
}


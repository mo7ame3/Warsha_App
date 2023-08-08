package com.example.warshaapp.screens.sharedScreens.authentication.register

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.warshaapp.components.CircleProgress
import com.example.warshaapp.components.DropList
import com.example.warshaapp.components.LoginButton
import com.example.warshaapp.components.PasswordInput
import com.example.warshaapp.components.TextInput
import com.example.warshaapp.constant.Constant
import com.example.warshaapp.data.WrapperClass
import com.example.warshaapp.model.shared.authentication.Authentication
import com.example.warshaapp.model.shared.authentication.AuthenticationCraft
import com.example.warshaapp.model.shared.craft.Craft
import com.example.warshaapp.navigation.AllScreens
import com.example.warshaapp.screens.sharedScreens.authentication.login.AuthenticationViewModel
import com.example.warshaapp.sharedpreference.SharedPreference
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterForm(
    navController: NavController,
    authenticationViewModel: AuthenticationViewModel,
    loading: MutableState<Boolean>,
    craftList: List<Craft>? = null,
    sharedPreference: SharedPreference
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val addressList = listOf(
        "مركز الفيوم", "مركز يوسف الصديق", "مركز طامية", "مركز سنورس", "مركز إطسا", "مركز إبشواي"
    )
    val listExpanded = remember {
        mutableStateOf(false)
    }
    val name = remember {
        mutableStateOf("")
    }
    val email = remember {
        mutableStateOf("")
    }
    val address = remember {
        mutableStateOf("")
    }
    val role = remember {
        mutableStateOf("")
    }
    val password = remember {
        mutableStateOf("")
    }
    val passwordConfirm = remember {
        mutableStateOf("")
    }
    val craftName = remember {
        mutableStateOf("")
    }
    val craftId = remember {
        mutableStateOf("")
    }
    val workerOrClientList = listOf(
        "عميل", "عامل"
    )
    val workerOrClintExpanded = remember {
        mutableStateOf(false)
    }
    val craftExpanded = remember {
        mutableStateOf(false)
    }
    val eye = remember {
        mutableStateOf(false)
    }
    //Confirm mutable states
    val eyeConfirm = remember {
        mutableStateOf(false)
    }
    val nameError = remember {
        mutableStateOf(false)
    }
    val emailError = remember {
        mutableStateOf(false)
    }
    val passwordError = remember {
        mutableStateOf(false)
    }
    val passwordConfirmError = remember {
        mutableStateOf(false)
    }

    val valid = if (role.value == workerOrClientList[0]) {
        if (role.value.isNotEmpty() && (password.value == passwordConfirm.value)) {
            !passwordError.value && !passwordConfirmError.value && !nameError.value && !emailError.value
        } else {
            false
        }
    } else if (role.value == workerOrClientList[1]) {
        if (role.value.isNotEmpty() && craftId.value.isNotEmpty() && (password.value == passwordConfirm.value)) {
            !passwordError.value && !passwordConfirmError.value && !nameError.value && !emailError.value
        } else {
            false
        }
    } else {
        false
    }
    if (!loading.value) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextInput(
                input = name,
                isError = nameError,
                label = "الاسم",
                onAction = KeyboardActions {
                    keyboardController?.hide()
                })

            TextInput(
                input = email,
                isError = emailError,
                label = "البريد الالكتروني",
                onAction = KeyboardActions {
                    keyboardController?.hide()
                })

            DropList(
                expanded = listExpanded,
                value = address,
                list = addressList,
            )
            DropList(
                expanded = workerOrClintExpanded,
                value = role,
                list = workerOrClientList,
                label = "مستخدم ك"
            )
            if (role.value == workerOrClientList[0]) {
                craftId.value = ""
            }
            if (role.value == workerOrClientList[1]) {
                if (craftList != null) {
                    DropList(
                        expanded = craftExpanded,
                        value = craftName,
                        craftList = craftList,
                        label = "الوظيفة",
                        craftId = craftId
                    )
                }
            }
            PasswordInput(
                password = password,
                isError = passwordError,
                eye = eye,
                onButtonAction = {
                    eye.value = !eye.value
                }, onAction = KeyboardActions {
                    keyboardController?.hide()
                })
            PasswordInput(
                password = passwordConfirm,
                isError = passwordConfirmError,
                label = "تأكيد كلمة السر",
                eye = eyeConfirm,
                onButtonAction = {
                    eyeConfirm.value = !eyeConfirm.value
                },
                onAction = KeyboardActions {
                    if (valid) {
                        scope.launch {
                            loading.value = true
                            val register: WrapperClass<Authentication, Boolean, Exception> =
                                authenticationViewModel.register(
                                    name = name.value,
                                    email = email.value,
                                    address = address.value,
                                    role = if (role.value == "عميل") "client" else "worker",
                                    password = password.value,
                                    passwordConfirm = password.value
                                )
                            if (register.data?.status == "success") {
                                sharedPreference.saveName(register.data!!.data?.user!!.name)
                                sharedPreference.saveToken(register.data!!.token.toString())
                                sharedPreference.saveUserId(register.data!!.data?.user!!.id)
                                Constant.token =
                                    register.data!!.token.toString()
                                if (role.value == "عميل") {
                                    sharedPreference.saveState("client")
                                    navController.navigate(AllScreens.ClientHomeScreen.name + "/{}") {
                                        navController.popBackStack()
                                    }
                                } else {
                                    sharedPreference.saveState("worker")
                                    val myCraft: WrapperClass<AuthenticationCraft, Boolean, Exception> =
                                        authenticationViewModel.workerChooseCraft(
                                            token = "Bearer " + register.data!!.token.toString(),
                                            myCraft = craftId.value,
                                            workerId = register.data!!.data?.user!!.id
                                        )
                                    if (myCraft.data?.status == "success") {
                                        sharedPreference.saveCraftId(craftId.value)
                                        navController.navigate(AllScreens.WorkerHomeScreen.name + "/login") {
                                            navController.popBackStack()
                                        }
                                    } else {
                                        loading.value = false
                                        Toast.makeText(
                                            context,
                                            register.data?.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            } else if (register.e != null) {
                                loading.value = false
                                Toast.makeText(
                                    context,
                                    "خطأ في الانترنت",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else if (register.data?.status == "fail" || register.data?.status == "error") {
                                loading.value = false
                                Toast.makeText(
                                    context,
                                    register.data?.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        keyboardController?.hide()
                    } else {
                        keyboardController?.hide()
                    }
                }
            )
            Spacer(modifier = Modifier.height(10.dp))

            LoginButton(
                isLogin = true, enabled = valid, label = "سجل الدخول", onClick = {
                    scope.launch {
                        loading.value = true
                        val register: WrapperClass<Authentication, Boolean, Exception> =
                            authenticationViewModel.register(
                                name = name.value,
                                email = email.value,
                                address = address.value,
                                role = if (role.value == "عميل") "client" else "worker",
                                password = password.value,
                                passwordConfirm = password.value
                            )
                        if (register.data?.status == "success") {
                            sharedPreference.saveName(register.data!!.data?.user!!.name)
                            sharedPreference.saveToken(register.data!!.token.toString())
                            sharedPreference.saveUserId(register.data!!.data?.user!!.id)
                            Constant.token =
                                register.data!!.token.toString()
                            if (role.value == "عميل") {
                                sharedPreference.saveState("client")
                                navController.navigate(AllScreens.ClientHomeScreen.name + "/{}") {
                                    navController.popBackStack()
                                }
                            } else {
                                sharedPreference.saveState("worker")
                                val myCraft: WrapperClass<AuthenticationCraft, Boolean, Exception> =
                                    authenticationViewModel.workerChooseCraft(
                                        token = "Bearer " + register.data!!.token.toString(),
                                        myCraft = craftId.value,
                                        workerId = register.data!!.data?.user!!.id
                                    )
                                if (myCraft.data?.status == "success") {
                                    sharedPreference.saveCraftId(craftId.value)
                                    navController.navigate(AllScreens.WorkerHomeScreen.name + "/login") {
                                        navController.popBackStack()
                                    }
                                } else {
                                    loading.value = false
                                    Toast.makeText(
                                        context,
                                        register.data?.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } else if (register.e != null) {
                            loading.value = false
                            Toast.makeText(
                                context,
                                "خطأ في الانترنت",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (register.data?.status == "fail" || register.data?.status == "error") {
                            loading.value = false
                            Toast.makeText(
                                context,
                                register.data?.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            )

        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircleProgress()
        }
    }
}
package com.example.warshaapp.screens.client.profile

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.warshaapp.components.CircleProgress
import com.example.warshaapp.components.ClientCompleteProjectRow
import com.example.warshaapp.components.DefaultButton
import com.example.warshaapp.components.GetSmallPhoto
import com.example.warshaapp.components.TopBar
import com.example.warshaapp.constant.Constant
import com.example.warshaapp.data.WrapperClass
import com.example.warshaapp.model.shared.profile.GetProfile
import com.example.warshaapp.model.shared.user.User
import com.example.warshaapp.screens.sharedScreens.report.ReportScreen
import com.example.warshaapp.ui.theme.MainColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@SuppressLint(
    "UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition",
    "CoroutineCreationDuringComposition"
)
@Composable
fun ClientProfileScreen(
    navController: NavController,
    clientId: String,
    clientProfileViewModel: ClientProfileViewModel
) {
    val isSelect = remember {
        mutableStateOf(false)
    }
    val changeCompleteState = remember {
        mutableStateOf(true)
    }
    val report = remember {
        mutableStateOf(false)
    }

    //coroutineScope
    val scope = rememberCoroutineScope()

    //state flow list
    val getClientProfile = MutableStateFlow<List<User>>(emptyList())

    var loading by remember {
        mutableStateOf(true)
    }
    var exception by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    if (Constant.token.isNotEmpty()) {
        val response =
            produceState<WrapperClass<GetProfile, Boolean, Exception>>(initialValue = WrapperClass()) {
                value = clientProfileViewModel.getProfile(
                    authorization = Constant.token,
                    userId = clientId
                )
            }.value
        if (response.data?.status == "fail" || response.data?.status == "error" || response.e != null) {
            exception = true
            Toast.makeText(
                context,
                "خطأ في الانترنت",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            if (response.data != null) {
                scope.launch {
                    getClientProfile.emit(response.data!!.data?.user!!)
                    loading = false
                    exception = false
                }
            }
        }
    }

    Scaffold(topBar = {
        TopBar(title = "") {
            navController.popBackStack()
        }
    }) {
        if (!report.value) {
            if (!loading && !exception) {
                if (changeCompleteState.value) {
                    isSelect.value = true
                    changeCompleteState.value = false
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        GetSmallPhoto(
                            isProfile = true,
                            uri = if (getClientProfile.value[0].avatar != null) getClientProfile.value[0].avatar.toString() else null
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = getClientProfile.value[0].name,
                        style = TextStyle(
                            color = MainColor,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = getClientProfile.value[0].address, style = TextStyle(
                            color = Color.Gray,
                            fontSize = 15.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(15.dp))
                    DefaultButton(label = "مشاريع مكتملة") {
                        isSelect.value = !isSelect.value
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    if (isSelect.value) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp)
                        ) {
                            items(Constant.completeList) {
                                ClientCompleteProjectRow(item = it)
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                        }
                    }
                    if (!isSelect.value) {
                        Spacer(modifier = Modifier.height(400.dp))
                    }
                    DefaultButton(label = "تقديم بلاغ") {
                        Log.d("TAG", "ClientProfileScreen: ")
                        report.value = true
                        // nav to Report
                        // navController.navigate(AllScreens.ReportScreen.name + "/${false}/worker")

                    }
                }
            } else if (loading && !exception) {
                CircleProgress()
            } else if (exception) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(onClick = {
                        exception = false
                        loading = true
                        scope.launch {
                            val getProfile: WrapperClass<GetProfile, Boolean, Exception> =
                                clientProfileViewModel.getProfile(
                                    userId = clientId,
                                    authorization = Constant.token
                                )
                            if (getProfile.data?.status == "fail" || getProfile.data?.status == "error" || getProfile.e != null) {
                                exception = true
                                Toast.makeText(
                                    context,
                                    "خطأ في الانترنت",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                if (getProfile.data != null) {
                                    scope.launch {
                                        getClientProfile.emit(getProfile.data!!.data?.user!!)
                                        loading = false
                                        exception = false
                                    }
                                }
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Refresh, contentDescription = null,
                            modifier = Modifier.size(60.dp)
                        )
                    }
                }
            }
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                ReportScreen(navController = navController, client = false, status = "worker") {
                    report.value = false
                }
            }
        }
    }
}


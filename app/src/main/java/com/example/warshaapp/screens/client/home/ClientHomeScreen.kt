package com.example.warshaapp.screens.client.home

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.warshaapp.components.BottomBar
import com.example.warshaapp.components.CircleProgress
import com.example.warshaapp.components.DrawerBody
import com.example.warshaapp.components.DrawerHeader
import com.example.warshaapp.components.InternetCraftName
import com.example.warshaapp.components.InternetCraftPhoto
import com.example.warshaapp.components.TopMainBar
import com.example.warshaapp.constant.Constant
import com.example.warshaapp.data.WrapperClass
import com.example.warshaapp.model.shared.craft.Craft
import com.example.warshaapp.model.shared.getAllCrafts.GetAllCrafts
import com.example.warshaapp.navigation.AllScreens
import com.example.warshaapp.screens.client.order.ClientOrderScreen
import com.example.warshaapp.screens.client.order.OrderViewModel
import com.example.warshaapp.sharedpreference.SharedPreference
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@SuppressLint(
    "UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition",
    "StateFlowValueCalledInComposition"
)
@ExperimentalMaterial3Api
@Composable
fun ClientHomeScreen(
    navController: NavController,
    clientHomeViewModel: ClientHomeViewModel,
    orderViewModel: OrderViewModel,
    route: String
) {
    val scope = rememberCoroutineScope()
    //Shared preference variables
    val context = LocalContext.current
    val sharedPreference = SharedPreference(context)
    val name = sharedPreference.getName.collectAsState(initial = "")

    //select item in bottomBar
    val bottomSelected = remember {
        mutableStateOf("home")
    }
    //app title
    val title = remember {
        mutableStateOf(Constant.title)
    }
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    var loading by remember {
        mutableStateOf(true)
    }
    var exception by remember {
        mutableStateOf(false)
    }
    // from order offer
    val isFirst = remember {
        mutableStateOf(true)
    }

    //bottomBar
    if (route == "home" || route == "chat" || route == "order") {
        if (isFirst.value) {
            bottomSelected.value = route
            isFirst.value = false
            title.value =
                if (route == "home") Constant.title else if (route == "chat") "المحادثات" else "طلباتي"
        }
    }

    //state flow list
    val craftFromGetAllCraftList = MutableStateFlow<List<Craft>>(emptyList())

    //response
    if (Constant.token.isNotEmpty()) {
        val craftData: WrapperClass<GetAllCrafts, Boolean, Exception> =
            produceState<WrapperClass<GetAllCrafts, Boolean, Exception>>(
                initialValue = WrapperClass(data = null)
            ) {
                value = clientHomeViewModel.getAllCrafts(authorization = Constant.token)
            }.value
        if (craftData.data?.status == "success") {
            if (craftData.data != null) {
                scope.launch {
                    craftFromGetAllCraftList.emit(craftData.data!!.data?.crafts!!)
                    loading = false
                    exception = false
                }
            }
        } else if (craftData.data?.status == "fail" || craftData.data?.status == "error" || craftData.e != null) {
            exception = true
            Toast.makeText(
                context,
                "خطأ في الانترنت",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Swipe Refresh
    var swipeLoading by remember {
        mutableStateOf(false)
    }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = swipeLoading)

    SwipeRefresh(state = swipeRefreshState, onRefresh = {
        if (bottomSelected.value == "home") {
            swipeLoading = true
            loading = false
            scope.launch {
                val craftData: WrapperClass<GetAllCrafts, Boolean, Exception> =
                    clientHomeViewModel.getAllCrafts(authorization = Constant.token)
                if (craftData.data?.status == "success") {
                    if (craftData.data != null) {
                        craftFromGetAllCraftList.emit(craftData.data!!.data?.crafts!!)
                        swipeLoading = false
                    }
                } else {
                    swipeLoading = false
                }
            }
        }
    }) {

        ModalNavigationDrawer(drawerContent = {
            ModalDrawerSheet(modifier = Modifier.fillMaxWidth(0.8f)) {
                DrawerHeader()
                Spacer(modifier = Modifier.height(50.dp))
                DrawerBody(isClient = true, name = name.value.toString()) {
                    if (it.title == "طلباتي") {
                        scope.launch {
                            bottomSelected.value = "order"
                            drawerState.close()
                        }
                    }
                    if (it.title == name.value.toString()) {
                        //Navigate to Profile
                        scope.launch {
                            navController.navigate(route = AllScreens.ClientMyProfileScreen.name + "/${false}")
                            drawerState.close()
                        }
                    }
                    if (it.title == "إعدادات حسابي") {
                        scope.launch {
                            navController.navigate(route = AllScreens.SettingScreen.name + "/${true}")
                            drawerState.close()
                        }
                    }
                    if (it.title == "مكتملة") {
                        // nav to profile
                        scope.launch {
                            navController.navigate(route = AllScreens.ClientMyProfileScreen.name + "/${true}")
                            drawerState.close()
                        }
                    }
                    if (it.title == "تسجيل الخروج") {
                        //Log out and nav to Login
                        scope.launch {
                            sharedPreference.removeUser()
                            navController.navigate(route = AllScreens.LoginScreen.name) {
                                navController.popBackStack()
                            }
                        }
                    }
                }
            }
        }, drawerState = drawerState) {
            Scaffold(
                topBar = {
                    TopMainBar(title = title) {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                },
                bottomBar = {
                    BottomBar(selected = bottomSelected, title = title)
                },
                modifier = Modifier.fillMaxSize()
            )
            {
                if (!loading && !exception) {
                    Column {
                        Spacer(modifier = Modifier.height(50.dp))
                        if (bottomSelected.value == "home") {
                            Home(craftFromGetAllCraftList.value) {
                                //navigate to add Order
                                navController.navigate(route = AllScreens.ClientPostScreen.name + "/${it.id}/${it.name}")
                            }
                        }
                        if (bottomSelected.value == "order") {
                            ClientOrderScreen(navController, orderViewModel)
                        }
                        if (bottomSelected.value == "chat") {
                            //   ChatList(navController)
                            Text(text = "CHat")
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
                                val craftData: WrapperClass<GetAllCrafts, Boolean, Exception> =
                                    clientHomeViewModel.getAllCrafts(authorization = Constant.token)
                                if (craftData.data?.status == "success") {
                                    if (craftData.data != null) {
                                        scope.launch {
                                            craftFromGetAllCraftList.emit(craftData.data!!.data?.crafts!!)
                                            loading = false
                                            exception = false
                                        }
                                    }
                                } else if (craftData.data?.status == "fail" || craftData.e != null) {
                                    exception = true
                                    Toast.makeText(
                                        context,
                                        "خطأ في الانترنت",
                                        Toast.LENGTH_SHORT
                                    ).show()
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
            }
        }
    }
}


@Composable
fun Home(
    craftFromGetAllCraftList: List<Craft>,
    onClick: (Craft) -> Unit,
) {
    LazyColumn {
        items(craftFromGetAllCraftList) {
            JobRow(job = it) { job ->
                onClick.invoke(job)
            }
        }
        item {
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}


@Composable
fun JobRow(
    job: Craft,
    onClick: (Craft) -> Unit
) {
    Column(modifier = Modifier
        .clickable {
            onClick.invoke(job)
        }
        .fillMaxSize()
        .padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box {
            Surface(
                shadowElevation = 10.dp,
                shape = RoundedCornerShape(size = 30.dp),
//                color = Color.Transparent,
                modifier = Modifier.size(350.dp, 230.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    InternetCraftPhoto(uri = job.avatar)
                    InternetCraftName(jobName = job.name)
                }
            }
        }

    }
}
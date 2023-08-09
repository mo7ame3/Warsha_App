package com.example.warshaapp.screens.worker.home

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.warshaapp.components.BottomBar
import com.example.warshaapp.components.CircleProgress
import com.example.warshaapp.components.DrawerBody
import com.example.warshaapp.components.DrawerHeader
import com.example.warshaapp.components.EmptyColumn
import com.example.warshaapp.components.GetSmallPhoto
import com.example.warshaapp.components.TopMainBar
import com.example.warshaapp.constant.Constant
import com.example.warshaapp.data.WrapperClass
import com.example.warshaapp.model.shared.order.Orders
import com.example.warshaapp.model.worker.home.WorkerHome
import com.example.warshaapp.navigation.AllScreens
import com.example.warshaapp.screens.worker.myOffers.MyOffersViewModel
import com.example.warshaapp.sharedpreference.SharedPreference
import com.example.warshaapp.ui.theme.GrayColor
import com.example.warshaapp.ui.theme.MainColor
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WorkerHomeScreen(
    navController: NavController,
    route: String,
    workerHomeViewModel: WorkerHomeViewModel,
    myOffersViewModel: MyOffersViewModel
) {
    //Log out Variables
    val context = LocalContext.current
    val sharedPreference = SharedPreference(context)
    val name = sharedPreference.getName.collectAsState(initial = "")
    val craftId = sharedPreference.getCraftId.collectAsState(initial = "")

    val homeNavBar = remember {
        mutableStateOf("home")
    }
    val title = remember {
        mutableStateOf(Constant.title)
    }
    val scope = rememberCoroutineScope()

    // from order offer
    val isFirst = remember {
        mutableStateOf(true)
    }
    if (route == "home" || route == "chat" || route == "order") {
        if (isFirst.value) {
            homeNavBar.value = route
            isFirst.value = false
            title.value =
                if (route == "home") Constant.title else if (route == "chat") "المحادثات" else "مشاريعي"
        }
    }

    var loading by remember {
        mutableStateOf(true)
    }
    var exception by remember {
        mutableStateOf(false)
    }
    val result = remember {
        mutableStateOf(0)
    }

    val pendingOrder = remember {
        mutableStateOf(0)
    }
    val length = remember {
        mutableStateOf(0)
    }

    val pendingOrderRefresh = remember {
        mutableStateOf(0)
    }
    val lengthRefresh = remember {
        mutableStateOf(0)
    }

    //state flow list
    val homeList = MutableStateFlow<List<Orders>>(emptyList())

    if (Constant.token.isNotEmpty() && craftId.value.toString().isNotEmpty()) {
        val homeData: WrapperClass<WorkerHome, Boolean, Exception> =
            produceState<WrapperClass<WorkerHome, Boolean, Exception>>(
                initialValue = WrapperClass(data = null)
            ) {
                value = workerHomeViewModel.getHome(
                    authorization = Constant.token,
                    craftId = craftId.value.toString()
                )
            }.value

        if (homeData.data?.status == "success") {
            if (homeData.data!!.result != 0) {
                scope.launch {
                    homeList.emit(homeData.data!!.data!!.orders)
                    homeList.value.forEach {
                        length.value = length.value + 1
                        if (it.status == "pending") {
                            pendingOrder.value = pendingOrder.value + 1
                            if (length.value == homeList.value.size) {
                                loading = false
                                exception = false
                            }
                        } else {
                            if (length.value == homeList.value.size) {
                                loading = false
                                exception = false
                            }
                        }
                    }
                    result.value = homeData.data!!.result
                }
            } else {
                loading = false
                exception = false
            }

        } else if (homeData.data?.status == "fail" || homeData.data?.status == "error" || homeData.e != null) {
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

    val drawerState = rememberDrawerState(DrawerValue.Closed)

    SwipeRefresh(state = swipeRefreshState, onRefresh = {
        if (homeNavBar.value == "home") {
            swipeLoading = true
            loading = false
            scope.launch {
                val homeData: WrapperClass<WorkerHome, Boolean, Exception> =
                    workerHomeViewModel.getHome(
                        authorization = Constant.token,
                        craftId = craftId.value.toString()
                    )
                if (homeData.data?.status == "success") {
                    if (homeData.data!!.result > 0) {
                        homeList.emit(homeData.data!!.data!!.orders)
                        lengthRefresh.value = 0
                        pendingOrderRefresh.value = 0
                        homeList.value.forEach {
                            lengthRefresh.value = lengthRefresh.value + 1
                            if (it.status == "pending") {
                                pendingOrderRefresh.value = pendingOrderRefresh.value + 1
                                if (lengthRefresh.value == homeList.value.size) {
                                    pendingOrder.value = pendingOrderRefresh.value
                                    //  pendingOrderRefresh.value = 0
                                    swipeLoading = false
                                }
                            } else {
                                if (lengthRefresh.value == homeList.value.size) {
                                    pendingOrder.value = pendingOrderRefresh.value
                                    //pendingOrderRefresh.value = 0
                                    swipeLoading = false
                                }
                            }
                        }
                    } else {
                        swipeLoading = false
                        pendingOrder.value = 0
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
                DrawerBody(isClient = false, name = name.value.toString()) {
                    if (it.title == "مشاريعي") {
                        scope.launch {
                            homeNavBar.value = "order"
                            drawerState.close()
                        }
                    }
                    if (it.title == name.value.toString()) {
                        //Navigate to Profile
                        scope.launch {
                            navController.navigate(route = AllScreens.WorkerMyProfileScreen.name)
                            drawerState.close()
                        }
                    }
                    if (it.title == "إعدادات حسابي") {
                        scope.launch {
                            navController.navigate(route = AllScreens.SettingScreen.name + "/${false}")
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
            Scaffold(topBar = {
                TopMainBar(title = title) {
                    scope.launch {
                        drawerState.open()
                    }
                }
            }, bottomBar = {
                BottomBar(selected = homeNavBar, title = title, isClient = false)
            }) {
                if (!loading && !exception) {
                    if (homeNavBar.value == "home") {
                        if (result.value != 0) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 50.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = if (pendingOrder.value == 0) Arrangement.Center else Arrangement.Top
                            ) {
                                if (pendingOrder.value > 0) {
                                    items(homeList.value) {
                                        WorkerHomeRow(item = it) { data ->
                                            navController.navigate(AllScreens.WorkerProblemDetails.name + "/${data._id}")
                                        }
                                    }
                                    item {
                                        Spacer(modifier = Modifier.height(50.dp))
                                    }
                                } else {
                                    item {
                                        Text(
                                            text = "لا يوجد طلبات حاليا", style = TextStyle(
                                                color = MainColor,
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 20.sp
                                            )
                                        )
                                    }
                                }
                            }
                        } else {
                            EmptyColumn(text = "لا يوجد طلبات حاليا")
                        }
                    }
                    if (homeNavBar.value == "chat") {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Chat")
                        }
                    }
                    //my projects
                    if (homeNavBar.value == "order") {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Order")
                        }
//                        MyOffersScreen(navController, myOffersViewModel)
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
                                val homeData: WrapperClass<WorkerHome, Boolean, Exception> =
                                    workerHomeViewModel.getHome(
                                        authorization = Constant.token,
                                        craftId = craftId.value.toString()
                                    )
                                if (homeData.data?.status == "success") {
                                    if (homeData.data != null) {
                                        scope.launch {
                                            if (homeData.data!!.result != 0) {
                                                homeList.emit(homeData.data!!.data!!.orders)
                                                homeList.value.forEach {
                                                    length.value = length.value + 1
                                                    if (it.status == "pending") {
                                                        pendingOrder.value = pendingOrder.value + 1
                                                        if (length.value == homeList.value.size) {
                                                            loading = false
                                                            exception = false
                                                        }
                                                    } else {
                                                        if (length.value == homeList.value.size) {
                                                            loading = false
                                                            exception = false
                                                        }
                                                    }
                                                }
                                                result.value = homeData.data!!.result
                                            }
                                        }
                                    }
                                } else if (homeData.data?.status == "fail" || homeData.data?.status == "error" || homeData.e != null) {
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
fun WorkerHomeRow(
    item: Orders,
    onAction: (Orders) -> Unit
) {
    if (item.status == "pending") {
        Column {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(start = 25.dp, end = 25.dp)
                    .clickable { onAction(item) },
                border = BorderStroke(width = 1.dp, color = GrayColor),
                shape = RoundedCornerShape(30.dp)
            )
            {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 25.dp, end = 25.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        GetSmallPhoto(uri = if (item.user.avatar != null) item.user.avatar.toString() else null)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = item.user.name, style = TextStyle(
                                        color = MainColor,
                                        fontSize = 20.sp
                                    )
                                )
                                Row {
//                                    Text(
//                                        text = item.createdDate, style = TextStyle(
//                                            color = GrayColor,
//                                            fontSize = 12.sp
//                                        )
//                                    )
//                                    Spacer(modifier = Modifier.width(5.dp))
//                                    Icon(
//                                        painter = painterResource(id = R.drawable.clock),
//                                        contentDescription = null,
//                                        tint = GrayColor,
//                                        modifier = Modifier.size(20.dp)
//                                    )
                                }
                            }
                            Text(
                                text = item.title + "- " + item.orderDifficulty, style = TextStyle(
                                    color = MainColor,
                                    fontSize = 14.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = item.user.address, style = TextStyle(
                                    color = GrayColor,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}
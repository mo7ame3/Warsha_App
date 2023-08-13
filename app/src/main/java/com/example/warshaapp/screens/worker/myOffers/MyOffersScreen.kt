package com.example.warshaapp.screens.worker.myOffers

import android.annotation.SuppressLint
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.warshaapp.R
import com.example.warshaapp.components.CircleProgress
import com.example.warshaapp.components.EmptyColumn
import com.example.warshaapp.components.GetSmallPhoto
import com.example.warshaapp.components.LoginButton
import com.example.warshaapp.constant.Constant
import com.example.warshaapp.data.WrapperClass
import com.example.warshaapp.model.worker.getMyOffer.Data
import com.example.warshaapp.model.worker.getMyOffer.GetMyOffer
import com.example.warshaapp.navigation.AllScreens
import com.example.warshaapp.sharedpreference.SharedPreference
import com.example.warshaapp.ui.theme.GrayColor
import com.example.warshaapp.ui.theme.MainColor
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@SuppressLint(
    "RememberReturnType",
    "CoroutineCreationDuringComposition",
    "UnusedMaterial3ScaffoldPaddingParameter",
    "StateFlowValueCalledInComposition"
)
@Composable
fun MyOffersScreen(
    navController: NavController,
    myOffersViewModel: MyOffersViewModel,
) {
    val context = LocalContext.current
    val sharedPreference = SharedPreference(context)
    val userId = sharedPreference.getUserId.collectAsState(initial = "")
    val scope = rememberCoroutineScope()
    val toggleButton = remember {
        mutableStateOf(true)
    }
    var loading by remember {
        mutableStateOf(true)
    }
    var exception by remember {
        mutableStateOf(false)
    }
    val length = remember {
        mutableStateOf(0)
    }

    val pendingCount = remember {
        mutableStateOf(0)
    }
    val acceptedCount = remember {
        mutableStateOf(0)
    }
    val lengthRefresh = remember {
        mutableStateOf(0)
    }
    val pendingCountRefresh = remember {
        mutableStateOf(0)
    }
    val acceptedCountRefresh = remember {
        mutableStateOf(0)
    }
    val offerData = MutableStateFlow<List<Data>>(emptyList())

    if (Constant.token.isNotEmpty() && userId.value.toString().isNotEmpty()) {
        val response: WrapperClass<GetMyOffer, Boolean, Exception> =
            produceState<WrapperClass<GetMyOffer, Boolean, Exception>>(initialValue = WrapperClass()) {
                value = myOffersViewModel.getMyOffers(
                    authorization = "Bearer " + Constant.token, userId = userId.value.toString()
                )
            }.value
        if (response.data?.status == "success") {
            if (!response.data?.data.isNullOrEmpty()) {
                scope.launch {
                    offerData.emit(response.data!!.data!!)
                    offerData.value.forEach {
                        length.value = length.value + 1
                        if (it.status == "pending") {
                            pendingCount.value = pendingCount.value + 1
                            if (length.value == offerData.value.size) {
                                loading = false
                            }
                        } else if (it.status == "accepted") {
                            acceptedCount.value = acceptedCount.value + 1
                            if (length.value == offerData.value.size) {
                                loading = false
                            }
                        } else {
                            if (length.value == offerData.value.size) {
                                loading = false
                            }
                        }
                    }
                }
            }
        } else if (response.data?.status == "fail" || response.data?.status == "error" || response.e != null) {
            exception = true
            Toast.makeText(
                context, "خطأ في الانترنت", Toast.LENGTH_SHORT
            ).show()
        }

    }

    Scaffold {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Swipe Refresh
            var swipeLoading by remember {
                mutableStateOf(false)
            }
            val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = swipeLoading)
            SwipeRefresh(state = swipeRefreshState, onRefresh = {
                lengthRefresh.value = 0
                acceptedCountRefresh.value = 0
                pendingCountRefresh.value = 0
                swipeLoading = true
                scope.launch {
                    val response: WrapperClass<GetMyOffer, Boolean, Exception> =
                        myOffersViewModel.getMyOffers(
                            authorization = Constant.token, userId = userId.value.toString()
                        )
                    if (response.data?.status == "success") {
                        if (response.data != null) {
                            offerData.emit(response.data!!.data!!)
                            offerData.value.forEach {
                                lengthRefresh.value = lengthRefresh.value + 1
                                if (it.status == "pending") {
                                    pendingCountRefresh.value = pendingCountRefresh.value + 1
                                    if (lengthRefresh.value == offerData.value.size) {
                                        pendingCount.value = pendingCountRefresh.value
                                        acceptedCount.value = acceptedCountRefresh.value
                                        swipeLoading = false
                                    }
                                } else if (it.status == "accepted") {
                                    acceptedCountRefresh.value = acceptedCountRefresh.value + 1
                                    if (lengthRefresh.value == offerData.value.size) {
                                        acceptedCount.value = acceptedCountRefresh.value
                                        pendingCount.value = pendingCountRefresh.value
                                        swipeLoading = false
                                    }
                                } else {
                                    if (lengthRefresh.value == offerData.value.size) {
                                        swipeLoading = false
                                    }
                                }
                            }
                        }
                    } else {
                        swipeLoading = false
                    }
                }
            }) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(start = 35.dp, end = 35.dp, top = 20.dp)
                ) {
                    if (!loading && !exception) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            LoginButton(isLogin = toggleButton.value, label = "قيد التنفيذ") {
                                toggleButton.value = true
                            }
                            LoginButton(isLogin = !toggleButton.value, label = "مشاريع لم تبدأ") {
                                toggleButton.value = false
                            }
                        }
                        if (toggleButton.value) {
                            if (offerData.value.isNotEmpty()) {
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(top = 20.dp),
                                    verticalArrangement = if (acceptedCount.value == 0) Arrangement.Center else Arrangement.Top,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    if (acceptedCount.value > 0) {
                                        items(offerData.value) {
                                            UnderwayRow(
                                                item = it, navController = navController
                                            ) { data ->
                                                navController.navigate(route = AllScreens.MyProjectProblemDetails.name + "/${data.order._id}")
                                            }
                                        }
                                        item {
                                            Spacer(modifier = Modifier.height(50.dp))
                                        }
                                    } else {
                                        item {
                                            Text(
                                                text = "لا يوجد عروض قيد التنفيذ",
                                                style = TextStyle(
                                                    color = MainColor,
                                                    fontWeight = FontWeight.SemiBold,
                                                    fontSize = 20.sp
                                                )
                                            )
                                        }
                                    }
                                }
                            } else {
                                EmptyColumn(text = "لا يوجد عروض قيد التنفيذ")

                            }
                        } else {
                            if (offerData.value.isNotEmpty()) {
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(top = 20.dp),
                                    verticalArrangement = if (pendingCount.value == 0) Arrangement.Center else Arrangement.Top,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    if (pendingCount.value > 0) {
                                        items(offerData.value) {
                                            OffersDidNotStartYet(item = it) { data ->
                                                navController.navigate(route = AllScreens.MyProjectProblemDetails.name + "/${data.order._id}")
                                            }
                                        }
                                        item {
                                            Spacer(modifier = Modifier.height(50.dp))
                                        }
                                    } else {
                                        item {
                                            Text(
                                                text = "لا يوجد عروض قيد الانتظار",
                                                style = TextStyle(
                                                    color = MainColor,
                                                    fontWeight = FontWeight.SemiBold,
                                                    fontSize = 20.sp
                                                )
                                            )
                                        }
                                    }
                                }
                            } else {
                                EmptyColumn(text = "لا يوجد عروض قيد الانتظار")
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
                                    val response: WrapperClass<GetMyOffer, Boolean, Exception> =
                                        myOffersViewModel.getMyOffers(
                                            authorization = Constant.token,
                                            userId = userId.value.toString()
                                        )
                                    if (response.data?.status == "success") {
                                        if (response.data != null) {
                                            scope.launch {
                                                if (!response.data!!.data.isNullOrEmpty()) {
                                                    offerData.emit(response.data!!.data!!)
                                                    offerData.value.forEach {
                                                        length.value = length.value + 1
                                                        if (it.status == "pending") {
                                                            pendingCount.value =
                                                                pendingCount.value + 1
                                                            if (length.value == offerData.value.size) {
                                                                loading = false
                                                                exception = false
                                                            }
                                                        } else if (it.status == "accepted") {
                                                            acceptedCount.value =
                                                                acceptedCount.value + 1
                                                            if (length.value == offerData.value.size) {
                                                                loading = false
                                                                exception = false
                                                            }
                                                        } else {
                                                            if (length.value == offerData.value.size) {
                                                                loading = false
                                                                exception = false
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    loading = false
                                                    exception = false
                                                }
                                            }
                                        }
                                    } else if (response.data?.status == "fail" || response.data?.status == "error" || response.e != null) {
                                        exception = true
                                        Toast.makeText(
                                            context, "خطأ في الانترنت", Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = null,
                                    modifier = Modifier.size(60.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}


@Composable
fun UnderwayRow(
    item: Data, navController: NavController, onAction: (Data) -> Unit
) {
    if (item.status == "accepted") {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clickable {
                    onAction.invoke(item)
                }, elevation = CardDefaults.cardElevation(5.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 10.dp, end = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                GetSmallPhoto(uri = if (item.order.user.avatar != null) item.order.user.avatar else null)
                Column {
                    Text(
                        text = item.order.user.name, style = TextStyle(
                            fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = MainColor
                        )
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = item.order.title + "- " + item.order.orderDifficulty,
                        style = TextStyle(
                            fontSize = 14.sp, color = MainColor
                        )
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = item.order.user.address, style = TextStyle(
                            fontSize = 12.sp, color = GrayColor
                        )
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                }
                Surface(
                    modifier = Modifier
                        .size(60.dp)
                        .clickable {
                            navController.navigate(route = AllScreens.WorkerHomeScreen.name + "/chat") {
                                navController.popBackStack()
                            }
                        }, color = Color.Transparent
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Icon(
                            painter = painterResource(id = R.drawable.chat),
                            contentDescription = null,
                            modifier = Modifier.size(40.dp),
                            tint = MainColor
                        )
                    }

                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}


@Composable
fun OffersDidNotStartYet(
    item: Data, onAction: (Data) -> Unit
) {
    if (item.status == "pending") {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clickable {
                    onAction.invoke(item)
                }, elevation = CardDefaults.cardElevation(5.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 10.dp, end = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                GetSmallPhoto(uri = if (item.order.user.avatar != null) item.order.user.avatar else null)
                Spacer(modifier = Modifier.width(20.dp))
                Column {
                    Text(
                        text = item.order.user.name, style = TextStyle(
                            fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = MainColor
                        )
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = item.order.title + "- " + item.order.orderDifficulty,
                        style = TextStyle(
                            fontSize = 14.sp, color = MainColor
                        )
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = item.order.user.address, style = TextStyle(
                            fontSize = 12.sp, color = GrayColor
                        )
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

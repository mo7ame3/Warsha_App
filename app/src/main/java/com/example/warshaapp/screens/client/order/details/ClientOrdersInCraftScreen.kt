package com.example.warshaapp.screens.client.order.details

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import com.example.warshaapp.components.TopBar
import com.example.warshaapp.constant.Constant
import com.example.warshaapp.data.WrapperClass
import com.example.warshaapp.model.client.getMyOrder.Data
import com.example.warshaapp.model.client.getMyOrder.GetMyOrder
import com.example.warshaapp.navigation.AllScreens
import com.example.warshaapp.screens.client.order.OrderViewModel
import com.example.warshaapp.ui.theme.MainColor
import com.example.warshaapp.ui.theme.RedColor
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@SuppressLint(
    "CoroutineCreationDuringComposition", "UnusedMaterial3ScaffoldPaddingParameter",
    "StateFlowValueCalledInComposition"
)
@Composable
fun ClientOrdersInCraftScreen(
    navController: NavController,
    craftId: String,
    name: String,
    orderViewModel: OrderViewModel
) {
    //coroutineScope
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    var loading by remember {
        mutableStateOf(true)
    }
    var exception by remember {
        mutableStateOf(false)
    }
    val orderDonCont = remember {
        mutableStateOf(0)
    }
    val length = remember {
        mutableStateOf(0)
    }
    val lengthRefresh = remember {
        mutableStateOf(0)
    }
    val orderDonContRefresh = remember {
        mutableStateOf(0)
    }
    //state flow list
    val orderList = MutableStateFlow<List<Data>>(emptyList())

    if (Constant.token.isNotEmpty()) {
        val response: WrapperClass<GetMyOrder, Boolean, Exception> =
            produceState<WrapperClass<GetMyOrder, Boolean, Exception>>(initialValue = WrapperClass()) {
                value = orderViewModel.getMyOrder(
                    authorization = Constant.token,
                    craftId = craftId
                )
            }.value

        if (response.data?.status == "success") {
            if (!response.data!!.data.isNullOrEmpty()) {
                scope.launch {
                    orderList.emit(response.data!!.data!!)
                    orderList.value.forEach {
                        length.value = length.value + 1
                        if (it.status == "orderDone" && length.value != orderList.value.size) {
                            orderDonCont.value = orderDonCont.value + 1
                        }
                        if (it.status == "orderDone" && length.value == orderList.value.size) {
                            orderDonCont.value = orderDonCont.value + 1
                            loading = false
                        }
                        if (it.status != "orderDone" && length.value == orderList.value.size) {
                            loading = false
                        }
                    }
                }
            } else {
                loading = false
            }
        } else if (response.data?.status == "fail" || response.data?.status == "error" || response.e != null) {
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

    SwipeRefresh(state = swipeRefreshState,
        onRefresh = {
            lengthRefresh.value = 0
            orderDonContRefresh.value = 0
            swipeLoading = true
            loading = false
            scope.launch {
                val orderData: WrapperClass<GetMyOrder, Boolean, Exception> =
                    orderViewModel.getMyOrder(
                        authorization = Constant.token,
                        craftId = craftId
                    )
                if (orderData.data?.status == "success") {
                    if (orderData.data != null) {
                        orderList.emit(orderData.data!!.data!!)
                        orderList.value.forEach {
                            lengthRefresh.value = lengthRefresh.value + 1
                            if (it.status == "orderDone" && lengthRefresh.value != orderList.value.size) {
                                orderDonContRefresh.value = orderDonContRefresh.value + 1
                            }
                            if (it.status == "orderDone" && lengthRefresh.value == orderList.value.size) {
                                orderDonContRefresh.value = orderDonContRefresh.value + 1
                                orderDonCont.value = orderDonContRefresh.value
                                swipeLoading = false
                            }
                            if (it.status != "orderDone" && lengthRefresh.value == orderList.value.size) {
                                swipeLoading = false
                            }
                        }
                    }
                } else {
                    swipeLoading = false
                }
            }
        }) {
        Scaffold(topBar = {
            TopBar(title = name) {
                navController.navigate(AllScreens.ClientHomeScreen.name + "/order") {
                    navController.popBackStack()
                }
            }
        })
        {
            if (!loading && !exception) {
                if (orderList.value.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 50.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = if (orderDonCont.value == orderList.value.size) Arrangement.Center else Arrangement.Top
                    ) {
                        if (orderDonCont.value != orderList.value.size) {
                            items(orderList.value) { itemList ->
                                MyCraftOrdersRow(
                                    item = itemList
                                ) { orderData ->
                                    //nav to Offer Screen
                                    navController.navigate(AllScreens.ClientOrderOffersScreen.name + "/${orderData.title}/${orderData.description}/${orderData._id}/${craftId}")
                                }

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
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
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
                            val reloadResponse: WrapperClass<GetMyOrder, Boolean, Exception> =
                                orderViewModel.getMyOrder(
                                    authorization = Constant.token,
                                    craftId = craftId
                                )
                            if (reloadResponse.data?.status == "success") {
                                if (reloadResponse.data != null) {
                                    scope.launch {
                                        orderList.emit(reloadResponse.data!!.data!!)
                                        loading = false
                                        exception = false
                                    }
                                }
                            } else if (reloadResponse.data?.status == "fail" || reloadResponse.e != null) {
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


@Composable
fun MyCraftOrdersRow(
    item: Data,
    onClick: (Data) -> Unit,
) {
    if (item.status != "orderDone") {
        Column {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(start = 15.dp, end = 15.dp)
                    .clickable { onClick.invoke(item) },
                shape = RoundedCornerShape(25.dp),
                border = BorderStroke(width = 1.dp, color = MainColor)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 15.dp, end = 15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = item.title,
                            style = TextStyle(
                                color = MainColor,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 20.sp
                            )
                        )
                        Text(
                            text = item.orderDifficulty, style = TextStyle(
                                color = Color.Gray,
                                fontSize = 18.sp
                            )
                        )
                        Text(
                            text = if (item.status == "carryingout") "قيد التنفيذ..." else if (item.status == "pending") "قيد الانتظار..." else "",
                            style = TextStyle(
                                color = RedColor,
                                fontSize = 16.sp
                            )
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
        }
    }

}
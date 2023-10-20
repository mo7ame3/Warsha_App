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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.warshaapp.components.CircleProgress
import com.example.warshaapp.components.GetSmallPhoto
import com.example.warshaapp.components.ProblemDescription
import com.example.warshaapp.components.TopBar
import com.example.warshaapp.constant.Constant
import com.example.warshaapp.data.WrapperClass
import com.example.warshaapp.model.shared.order.Orders
import com.example.warshaapp.model.worker.orderDetails.GetOrderDetails
import com.example.warshaapp.navigation.AllScreens
import com.example.warshaapp.sharedpreference.SharedPreference
import com.example.warshaapp.ui.theme.MainColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@SuppressLint(
    "CoroutineCreationDuringComposition", "UnusedMaterial3ScaffoldPaddingParameter",
    "StateFlowValueCalledInComposition"
)
@Composable
fun MyOfferProblemDetails(
    navController: NavController,
    myOffersViewModel: MyOffersViewModel,
    orderId: String
) {
    val context = LocalContext.current
    val sharedPreference = SharedPreference(context)
    val craftId = sharedPreference.getCraftId.collectAsState(initial = "")
    val scope = rememberCoroutineScope()

    var loading by remember {
        mutableStateOf(true)
    }
    var exception by remember {
        mutableStateOf(false)
    }

    val orderDetails = MutableStateFlow<List<Orders>>(emptyList())

    if (Constant.token.isNotEmpty() && craftId.value.toString().isNotEmpty()) {
        val response =
            produceState<WrapperClass<GetOrderDetails, Boolean, Exception>>(initialValue = WrapperClass()) {
                value = myOffersViewModel.getOrderDetails(
                    authorization = Constant.token,
                    orderId = orderId,
                    craftId = craftId.value.toString()
                )
            }.value
        if (response.data?.status == "success") {
            scope.launch {
                loading = false
                exception = false
                orderDetails.emit(response.data!!.data!!.order)
            }
        } else if (response.data?.status == "fail" || response.data?.status == "error" || response.e != null) {
            exception = true
            Toast.makeText(
                context,
                if (response.data?.status == "fail") response.data!!.message else "خطأ في الانترنت",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    Scaffold(topBar = {
        TopBar(title = "تفاصيل الطلب") {
            navController.popBackStack()
        }
    }) {
        if (!loading && !exception) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 25.dp, end = 25.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            //get Client id from database and nav to client profile
                            navController.navigate(route = AllScreens.ClientProfileScreen.name + "/${orderDetails.value[0].user.id}")
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GetSmallPhoto(uri = if (orderDetails.value[0].user.avatar != null) orderDetails.value[0].user.avatar else null)
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = orderDetails.value[0].user.name, style = TextStyle(
                            color = MainColor,
                            fontSize = 18.sp
                        )
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.padding(start = 25.dp),
                ) {
                    Text(
                        text = orderDetails.value[0].title + "- " + orderDetails.value[0].orderDifficulty,
                        style = TextStyle(
                            color = MainColor,
                            fontSize = 18.sp
                        )
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                //ProblemDescription
                ProblemDescription(
                    problemDescription = orderDetails.value[0].description,
                )
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
                        val response: WrapperClass<GetOrderDetails, Boolean, Exception> =
                            myOffersViewModel.getOrderDetails(
                                authorization = Constant.token,
                                orderId = orderId,
                                craftId = craftId.value.toString()
                            )
                        if (response.data?.status == "success") {
                            if (response.data != null) {
                                scope.launch {
                                    loading = false
                                    exception = false
                                    orderDetails.emit(response.data!!.data!!.order)
                                }
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
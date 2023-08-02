package com.example.warshaapp.screens.client.order

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.warshaapp.components.CircleProgress
import com.example.warshaapp.data.WrapperClass
import com.example.warshaapp.model.shared.authentication.AuthenticationCraftList
import com.example.warshaapp.model.shared.craft.Craft
import com.example.warshaapp.navigation.AllScreens
import com.example.warshaapp.ui.theme.MainColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ClientOrderScreen(navController: NavController, orderViewModel: OrderViewModel) {
    //coroutineScope
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var loading by remember {
        mutableStateOf(true)
    }
    var exception by remember {
        mutableStateOf(false)
    }

    //state flow list
    val craftList = MutableStateFlow<List<Craft>>(emptyList())


    val response: WrapperClass<AuthenticationCraftList, Boolean, Exception> =
        produceState<WrapperClass<AuthenticationCraftList, Boolean, Exception>>(
            initialValue = WrapperClass(data = null)
        )
        {
            value = orderViewModel.getCraftList()
        }.value

    if (response.data?.status == "success") {
        if (response.data != null) {
            scope.launch {
                craftList.emit(response.data!!.data?.crafts!!)
                loading = false
                exception = false
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

    Surface(modifier = Modifier.fillMaxSize()) {
        if (!loading && !exception) {
            LazyColumn {
                items(craftList.value) {
                    OrderRow(onClick = { orderData ->
                        //navigate to my order
                        navController.navigate(route = AllScreens.ClientOrdersInCraftScreen.name + "/${orderData.id}/${orderData.name}")
                    }, craft = it)
                    Spacer(modifier = Modifier.height(10.dp))
                }
                item {
                    Spacer(modifier = Modifier.height(50.dp))
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
                        val reloadResponse: WrapperClass<AuthenticationCraftList, Boolean, Exception> =
                            orderViewModel.getCraftList()

                        if (reloadResponse.data?.status == "success") {
                            if (reloadResponse.data != null) {
                                scope.launch {
                                    craftList.emit(reloadResponse.data!!.data?.crafts!!)
                                    loading = false
                                    exception = false
                                }
                            }
                        } else if (reloadResponse.data?.status == "fail" || reloadResponse.data?.status == "error" || reloadResponse.e != null) {
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

@Composable
fun OrderRow(
    onClick: (Craft) -> Unit,
    craft: Craft
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(start = 15.dp, end = 15.dp)
            .clickable { onClick.invoke(craft) },
        border = BorderStroke(width = 1.dp, color = MainColor), shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = craft.name, style = TextStyle(color = MainColor, fontSize = 20.sp))
        }
    }

}
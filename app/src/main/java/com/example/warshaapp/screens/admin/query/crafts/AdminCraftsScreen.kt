package com.example.warshaapp.screens.admin.query.crafts

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.warshaapp.components.CircleProgress
import com.example.warshaapp.components.FloatingAction
import com.example.warshaapp.components.InternetCraftName
import com.example.warshaapp.components.InternetCraftPhoto
import com.example.warshaapp.components.TopBar
import com.example.warshaapp.constant.Constant
import com.example.warshaapp.data.WrapperClass
import com.example.warshaapp.model.shared.craft.Craft
import com.example.warshaapp.model.shared.getAllCrafts.GetAllCrafts
import com.example.warshaapp.navigation.AllScreens
import com.example.warshaapp.ui.theme.AdminSecondaryColor
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AdminCraftsScreen(
    navController: NavController, craftsViewModel: CraftsViewModel
) {
    val context = LocalContext.current
    //coroutineScope
    val scope = rememberCoroutineScope()

    //check home loading
    var loading by remember {
        mutableStateOf(true)
    }
    var exception by remember {
        mutableStateOf(false)
    }

    val craftFromGetAllCraftList = MutableStateFlow<List<Craft>>(emptyList())

    if (Constant.token.isNotEmpty()) {
        val craftData: WrapperClass<GetAllCrafts, Boolean, Exception> =
            produceState<WrapperClass<GetAllCrafts, Boolean, Exception>>(
                initialValue = WrapperClass(
                    null
                )
            ) {
                value = craftsViewModel.getAllCrafts(authorization = Constant.token)
            }.value
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
                context, "خطأ في الانترنت", Toast.LENGTH_SHORT
            ).show()
        }
    }
    // Swipe Refresh
    var swipeLoading by remember {
        mutableStateOf(false)
    }

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = swipeLoading)

    SwipeRefresh(state = swipeRefreshState, onRefresh = {
        swipeLoading = true
        loading = false
        scope.launch {
            val craftData: WrapperClass<GetAllCrafts, Boolean, Exception> =
                craftsViewModel.getAllCrafts(authorization = Constant.token)
            if (craftData.data?.status == "success") {
                if (craftData.data != null) {
                    craftFromGetAllCraftList.emit(craftData.data!!.data?.crafts!!)
                    swipeLoading = false
                }
            } else {
                swipeLoading = false
            }
        }
    }) {
        Scaffold(topBar = {
            TopBar(title = "") {
                navController.popBackStack()
            }
        },
            floatingActionButton = {
                FloatingAction {
                    navController.navigate(route = AllScreens.AdminCreateNewCraft.name)
                }
            }
        ) {
            if (!loading && !exception) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(items = craftFromGetAllCraftList.value) {
                        JobRow(craftFromGetAllCraft = it, onEditAction = { edit ->
                            navController.navigate(AllScreens.AdminEditCraftsScreen.name + "/${edit}")
                        }, onClick = { craftQuery ->
                            navController.navigate(route = AllScreens.AdminAllWorkersInSpecificCraft.name + "/${craftQuery.id}")

                        })
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
                                craftsViewModel.getAllCrafts(authorization = Constant.token)
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


@Composable
fun JobRow(
    craftFromGetAllCraft: Craft, onEditAction: (String) -> Unit, onClick: (Craft) -> Unit
) {
    Column(modifier = Modifier
        .clickable {
            onClick.invoke(craftFromGetAllCraft)
        }
        .fillMaxSize()
        .padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box {
            Surface(
                shadowElevation = 10.dp,
                shape = RoundedCornerShape(size = 30.dp),
                modifier = Modifier.size(350.dp, 230.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box {
                        InternetCraftPhoto(uri = craftFromGetAllCraft.avatar)
                        Row(
                            modifier = Modifier.size(300.dp, 200.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.Top
                        ) {

                            IconButton(onClick = {
                                // nav to edit
                                onEditAction.invoke(craftFromGetAllCraft.id)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null,
                                    tint = AdminSecondaryColor
                                )
                            }

                        }
                    }
                    InternetCraftName(jobName = craftFromGetAllCraft.name)
                }
            }
        }
    }
}